/* This file is distributed as part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.uni_leipzig.asv.coocc;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * This class prepares the two tempFiles for fast access of a large
 * multiple-column table containing only Integers.
 * It takes the a file as input, which first must be dumped
 * from the database via one of the following example commands: <br><br>
 *
 * select wort_nr, group_nr into outfile '/var/roedel/ksim/r_word_group.dump' from r_word_group where group_nr >= 3286 and group_nr <= 2578224 order by wort_nr asc;<br>
 * select group_nr, group_type into outfile '/var/roedel/ksim/r_group_type.dump' from r_group_type where group_nr >= 3286 and group_nr <= 2578224 order by group_nr asc;<br><br>
 *
 * The file must be in the working directory of this program.<br><br>
 *
 * Assumes that wordnumers in the first column are mostly wothout holes.<br>
 * It will fill up useless 4 bytes per missing wordnumber up to the next existing
 * wordnumber<br><br>
 *
 * Format of first file: byte[4] of numbers<br><br>
 *
 * Format of second file: byte[4] of numbers times the columns - 1<br><br>
 *
 * Semantics: The integer at 4*n-1 in the first file gives the begin and the
 * integer at 4*n of the first file gives the end of the area to read in the
 * second file in order to retrieve the stored information for the n-th number<br><br>
 *
 * ASSUMPTIONS:<br>
 * - column1: wordNrs don't have too large 'holes' (index will be too large
 *   otherwise)<br>
 * - All columns in the dump are non-null (will throw Exception otherwise)<br><br>
 *
 * @author Stefan Bordag
 * @author ChW (Christoph Weißenborn)
 */
public class BinFileMultColPreparer
{
	/** for better recognition the data file has this extension */
	public static final String ext1 = ".bin";

	/** The index file gets another extension */
	public static final String ext2 = ".idx";

	private byte[] file1 = null;

	private byte[] file2 = null;

	protected int columns = 2;

	/** Sole constructor. Takes the fileName and columns count. If you have two
	 * columns in the file, give 2 here. If 3, give 3 etc. If you give 2 although
	 * you have 3, the third will be ignored.
	 * @param fileName String - the dump file
	 * @param columnCount int - number of columns in file */
	public BinFileMultColPreparer(final String fileName, int columnCount)
	{
		if (columnCount < 2)
		{
			throw new IllegalArgumentException("A datafile with only one column " +
			"can be stored more effectively as a simple list...");
		}

		this.columns = columnCount;
		createFiles(fileName);
	}

	/**
	 * This method wraps the actual algorithm, taking care of opening and closing
	 * the files, etc.
	 * @param fileName String -
	 */
	protected final void createFiles(final String fileName)
	{
		if (fileName == null)
		{
			return;
		}
		File dmpFile = new File(fileName);
		File datFile = new File(fileName + ext1);
		File idxFile = new File(fileName + ext2);

		if (datFile.exists() && datFile.length() > 0 &&
				idxFile.exists() && idxFile.length() > 0)
		{
			return;
		}
		BufferedReader reader = null;
		BufferedOutputStream datWriter = null;
		BufferedOutputStream idxWriter = null;
		try
		{
			reader = new BufferedReader(new FileReader(dmpFile), 1048576);
			datWriter = new BufferedOutputStream(new FileOutputStream(datFile), 1048576);
			idxWriter = new BufferedOutputStream(new FileOutputStream(idxFile), 1048576);

			writeFiles(reader, datWriter, idxWriter);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (Exception e)
				{}
			}
			if (datWriter != null)
			{
				try
				{
					datWriter.close();
				}
				catch (Exception e)
				{}
			}
			if (idxWriter != null)
			{
				try
				{
					idxWriter.close();
				}
				catch (Exception e)
				{}
			}
		}
	}

	/**
	 * This method actually does the work.
	 * @param reader BufferedReader - Where to read from
	 * @param datWriter OutputStream - Datafile
	 * @param idxWriter OutputStream - Indexfile
	 * @throws Exception
	 */
	protected final void writeFiles(BufferedReader reader, OutputStream datWriter, OutputStream idxWriter) throws Exception
	{
		String line = reader.readLine();

		int count = 0;
		int oldCollNumber = 0;
		int curCollNumber = 1;
		while (line != null)
		{
			int[] values = splitToIntArray(line);

			if ( values[0] < 1 ) {line = reader.readLine(); continue;}
			// write the data to the datafile
			for (int i = 1; i < this.columns && i < values.length; i++)
			{
				datWriter.write(Converter.intToBytes(values[i]), 0, 4);
			}

			// write the indexfile
			if (values[0] != curCollNumber )
			{
				int diff = values[0] - curCollNumber;
				if (diff > 1)
				{
					// System.out.println("WordNR hole with size "+diff+" detected after: "+curCollBlock);
					// a hole was detected, fill the indexfile with the same entries up
					// to the next wordnumber.
					for (int i = diff ; i > 1 ; i--)
					{
						idxWriter.write(Converter.intToBytes(count), 0, 4);
					}
				}
				idxWriter.write(Converter.intToBytes(count), 0, 4);
				oldCollNumber = curCollNumber;
				curCollNumber = values[0];
			}
			line = reader.readLine();
			count++;
		}
		idxWriter.write(Converter.intToBytes(count), 0, 4);
	}

	/**
	 * Splits the line into an array of Integers.
	 * @param line
	 * @return */
	protected final int[] splitToIntArray(final String line)
	{
		String[] split = line.split("\t");
		int[] retVals = new int[split.length];
		for ( int i = 0 ; i < split.length ; i++ )
		{
			retVals[i] = new Integer(split[i]).intValue();
		}
		return retVals;
	}


	/** @param fileName */
	protected final void readFiles(final String fileName)
	{
		File datFile = (new File(fileName + ext1));
		File idxFile = (new File(fileName + ext2));
		if (datFile.exists() && datFile.length() > 0 &&
				idxFile.exists() && idxFile.length() > 0)
		{
			int lenDat = (int) datFile.length();
			int lenIdx = (int) idxFile.length();
			System.out.println("length of data file  = " + lenDat);
			System.out.println("length of index file = " + lenIdx);

			this.file1 = new byte[lenDat];
			this.file2 = new byte[lenIdx];
		}
		else
		{
			return;
		}

		RandomAccessFile datReader = null;
		RandomAccessFile idxReader = null;
		try
		{
			if (fileName != null)
			{
				datReader = new RandomAccessFile(datFile, "r");
				idxReader = new RandomAccessFile(idxFile, "r");
			}

			datReader.read(this.file1);
			idxReader.read(this.file2);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (datReader != null)
			{
				try
				{
					datReader.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			if (idxReader != null)
			{
				try
				{
					idxReader.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Use this main in order to prepare file directly from the command line.
	 *
	 */
	public static final void main(final String[] args)
	{
		if ( args == null || args.length < 2 )
		{
			System.out.println("Please give file to prepare as well as how many columns have to be read (minus 1).");
		}
		else
		{
			Integer columns = null;
			try
			{
				columns = new Integer(args[1]);
			}
			catch(Exception ex)
			{
				System.out.println("The second argument must be a number!");
				ex.printStackTrace();
			}
			BinFileMultColPreparer prep = new BinFileMultColPreparer(args[0], columns.intValue());
		}
	}

}
