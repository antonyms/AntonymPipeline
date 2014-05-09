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

/**
 * This class prepares the two tempFiles which are needed to access word_bins
 * faster.
 * It takes the wordlist file as input, which first must be dumped
 * from the database via the following command:
 *
 * select w.wort_nr, w.wort_bin into outfile '/var/roedel/ksim/wortliste.dump' from wortliste w order by w.wort_nr asc;
 *
 * The file must be in the working directory of this program under data/ksim/ or
 * as specified.
 *
 * Assumes that wordnumers in the first column are mostly wothout holes.
 * It will fill up useless 4 bytes per missing wordnumber up to the next existing
 * wordnumber in the indexfile
 *
 * Format of first file: char[?] of words
 *
 * Format of second file: char[4]
 * Semantics: nth 4-byte-number gives location of end of collocationsnumbers of
 * the wordnumber n. Begin is stored at n-1
 *
 * ASSUMPTIONS:
 * column1: wordNrs don't have too large 'holes'
 *
 * @author Stefan Bordag
 * @author ChW (Christoph Weißenborn)
 */
public class BinFileStrColPreparer
{
	/** for better recognition the data file has this extension */
	public static final String ext1 = ".bin";

	/** The index file gets another extension */
	public static final String ext2 = ".idx";

	/** @param fileName String - */
	public BinFileStrColPreparer(String fileName)
	{
		if (fileName == null || fileName.length() < 1)
		{
			throw new IllegalArgumentException("BinFileStrColPreparer: Can't create bin or indexfile from nothing!");
		}

		createFiles(fileName);
	}

	protected final void createFiles(String fileName)
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

	protected final void writeFiles(BufferedReader reader, OutputStream datWriter, OutputStream idxWriter) throws Exception
	{
		String line = reader.readLine();
		int count = 0;
		int oldWordNr = 0;
		String[] split = null;
		int curWordNr = 0;
		byte[] wort_bin_bytes = null;
		int diff = 0;
		while (line != null)
		{
			split = line.split("\t");
			try {
				curWordNr = new Integer(split[0].trim()).intValue();
				if ( curWordNr < 1 || split.length <= 1 || split[1] == null || split[1].length() < 1 )
				{line = reader.readLine(); continue;}

				String wort_bin = split[1].trim();

				wort_bin_bytes = wort_bin.getBytes();
				datWriter.write(wort_bin_bytes, 0, wort_bin_bytes.length);

				diff = curWordNr - oldWordNr;
				if (diff > 1)
				{
					for (int i = diff; i > 1; i--)
					{
						idxWriter.write(Converter.intToBytes(count), 0, 4);
					}
				}
				count += wort_bin_bytes.length; //+4;
				idxWriter.write(Converter.intToBytes(count), 0, 4);
				oldWordNr = curWordNr;
			} catch (NumberFormatException e) {System.err.println("Could not process line " + line + ":" +  e.getMessage());}
			line = reader.readLine();
		}
	}

	/** */
	public final void testFiles()
	{
		// TODO: fill test
	}

	public static final void main(String[] args)
	{
		if (args == null || args.length < 1)
		{
			System.out.println("Please give file which should be converted.");
		}
		else
		{
			BinFileStrColPreparer prep = new BinFileStrColPreparer(args[0]);
			//            prep.testFiles();
		}
	}
}
