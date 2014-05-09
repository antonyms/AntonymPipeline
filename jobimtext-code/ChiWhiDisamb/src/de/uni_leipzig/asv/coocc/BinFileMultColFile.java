/* This file is distributed as part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */
package de.uni_leipzig.asv.coocc;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class gives access to collocations which is slower but less
 * memory-intense. It loads up only the index file and operates on the file
 * per random accesses.
 *
 * @author Stefan Bordag
 */
public class BinFileMultColFile implements BinFileMultCol
{
	/**
	 * This is the indexfile which is loaded into memory
	 */
	protected byte[] indexFile = null;

	/**
	 * This represents the datafile
	 */
	protected RandomAccessFile dataFileReader = null;

	/**
	 * The number of columns to assume
	 */
	protected int columns = 2;

	/**
	 * Sole constructor. Reads the index after which it is ready to deliver data.
	 * @param fileName
	 * @param columns How many columns the original file had (must match exactly!)
	 */
	public BinFileMultColFile(String fileName, int columns)
	{
		if (columns < 2)
		{
			throw new IllegalArgumentException("A datafile with only one column can be stored more effectively as a simple list...");
		}
		this.columns = columns;
		if (existsFileDump(fileName))
		{
			this.indexFile = new byte[ (int) (new File(fileName + BinFileMultColPreparer.ext2)).length()];
		}
		else
		{
			throw new IllegalArgumentException("Prepared binary files for " + fileName + " do not exist.");
		}

		try
		{
			RandomAccessFile reader1 = null;
			if (fileName != null)
			{
				reader1 = new RandomAccessFile(fileName + BinFileMultColPreparer.ext2, "r");
				this.dataFileReader = new RandomAccessFile(fileName + BinFileMultColPreparer.ext1, "r");
			}

			reader1.read(this.indexFile);
			reader1.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Tests, whether the required files for this class, given with the fileName
	 * exist and are not of null-length (like after a failed start of FilePreparer
	 *
	 * @param fileName relative or absolute filename without index or data extensions
	 * @return
	 */
	public static boolean existsFileDump(String fileName)
	{
		if ( (new File(fileName + BinFileMultColPreparer.ext1)).exists() &&
				(new File(fileName + BinFileMultColPreparer.ext1)).length() > 0 &&
				(new File(fileName + BinFileMultColPreparer.ext2)).exists() &&
				(new File(fileName + BinFileMultColPreparer.ext2)).length() > 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * For a given number returns the row
	 * @param wordNr Integer
	 * @return List::Integer[]
	 */
	@Override
	public List getData(Integer wordNr)
	{
		return getData(wordNr, Integer.MAX_VALUE);
	}

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param wordNr
	 * @param maxItems
	 * @return
	 */
	@Override
	public List getData(Integer number, int maxItems)
	{
		return getData(number, this.columns, maxItems);
	}

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param number = the first column
	 * @param myColumns = how many columns to return
	 * @param maxItems = at max this count of items
	 * @return
	 */
	@Override
	public List getData(Integer number, int myColumns, int maxItems)
	{
		if ( ( (number.intValue() ) * 4) > this.indexFile.length || number.intValue() < 1)
		{
			//System.err.println("Used number "+number+" is out of range, returning empty ArrayList.");
			//      throw new IllegalArgumentException("Number used to get data was too small or too large: " + number + " against index length of " +(this.indexFile.length / 4));
			return new ArrayList();
		}
		List retVec = new ArrayList();
		try
		{
			int fromPos = 0;
			if (number.intValue() > 1)
			{
				fromPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 2) * 4);
			}
			// -1 here, because we began to count with 0, though wordNrs begin with 1
			int toPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 1) * 4);

			this.dataFileReader.seek(fromPos * 4 * (this.columns - 1));

			// create the vector of the needed size, which is either the maxColls or
			// maximum of collocations possible, whichever is smaller
			retVec = new ArrayList(Math.min(maxItems, toPos - fromPos));

			byte[] byteBuf = new byte[4];
			int realCount = 0;
			// read all rows
			Integer[] values = null;
			for (int i = fromPos; i < toPos && realCount < maxItems; i++)
			{
				values = new Integer[myColumns - 1];
				// read all values of a row ( assumedly there are this.columns columns,
				// we need to read this.columns - 1
				for (int j = 0; j < (this.columns - 1); j++)
				{
					if ( j < myColumns-1 )
					{
						this.dataFileReader.read(byteBuf);
						values[j] = new Integer(Converter.bytesToInt(byteBuf));
					}
					else
					{
						this.dataFileReader.skipBytes(4);
					}
				}
				retVec.add(values);
				realCount++;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		if (retVec == null)
		{
			return new ArrayList();
		}
		return retVec;
	}

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param number = the first column
	 * @param myColumns = how many columns to return
	 * @param maxItems = at max this count of items
	 * @param minCols = for each column each value minimum is specified here
	 * @param maxCols = for each column each value maximum is specified here
	 * @return
	 */
	@Override
	public List getData(Integer number, int myColumns, int maxItems, int[] minCols, int[] maxCols)
	{
		if ( ( (number.intValue() ) * 4) > this.indexFile.length || number.intValue() < 1)
		{
			//System.err.println("Used number "+number+" is out of range, returning empty ArrayList.");
			//      throw new IllegalArgumentException("Number used to get data was too small or too large: " + number + " against index length of " +(this.indexFile.length / 4));
			return new ArrayList();
		}
		List retVec = new ArrayList();
		try
		{
			int fromPos = 0;
			if (number.intValue() > 1)
			{
				fromPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 2) * 4);
			}
			// -1 here, because we began to count with 0, though wordNrs begin with 1
			int toPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 1) * 4);

			this.dataFileReader.seek(fromPos * 4 * (this.columns - 1));

			// create the vector of the needed size, which is either the maxColls or
			// maximum of collocations possible, whichever is smaller
			retVec = new ArrayList(Math.min(maxItems, toPos - fromPos));

			byte[] byteBuf = new byte[4];
			int realCount = 0;
			// read all rows
			Integer[] values = null;
			Integer curVal = null;
			for (int i = fromPos; i < toPos && realCount < maxItems; i++)
			{
				values = new Integer[myColumns - 1];
				// read all values of a row ( assumedly there are this.columns columns,
				// we need to read this.columns - 1
				boolean add = true;
				for (int j = 0; j < (this.columns - 1); j++)
				{
					if ( j < myColumns-1 )
					{
						this.dataFileReader.read(byteBuf);
						curVal = new Integer(Converter.bytesToInt(byteBuf));
						values[j] = curVal;
						if (curVal.intValue() < minCols[j+1] || curVal.intValue() > maxCols[j+1])
						{
							add = false;
						}
					}
					else
					{
						this.dataFileReader.skipBytes(4);
					}
				}
				if ( add )
				{
					retVec.add(values);
					realCount++;
				}

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		if (retVec == null)
		{
			return new ArrayList();
		}
		return retVec;
	}

	/**
	 * Returns the data for the given number up to the given maximum in a list.
	 * @param wordNr The number of the item of which to return the columns
	 * @param maxItems How many items to return at max
	 * @param minCols for each column the minimum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @param maxCols for each column the maximum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @return List::Integer[]
	 */
	@Override
	public List getData(Integer number, int maxItems, int[] minCols, int[] maxCols)
	{
		return getData(number, this.columns, maxItems, minCols, maxCols);
	}

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @return Map::Integer->Integer[]
	 */
	@Override
	public Map getDataAsMap(Integer wordNr)
	{
		return getDataAsMap(wordNr, Integer.MAX_VALUE);
	}

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param wordNr The number of the item of which to return the columns
	 * @param myColumns How many columns to return
	 * @param maxItems How many items to return at max
	 * @return Map::Integer->Integer[]
	 */
	@Override
	public Map getDataAsMap(Integer number, int myColumns, int maxItems)
	{
		if ( ( (number.intValue() ) * 4) > this.indexFile.length || number.intValue() < 1)
		{
			//System.err.println("Used number "+number+" is out of range, returning empty ArrayList.");
			//      throw new IllegalArgumentException("Number used to get data was too small or too large: " + number + " against index length of " +(this.indexFile.length / 4));
			return new HashMap();
		}
		Map retMap = new HashMap();
		try
		{
			int fromPos = 0;
			if (number.intValue() > 1)
			{
				fromPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 2) * 4);
			}
			// -1 here, because we began to count with 0, though wordNrs begin with 1
			int toPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 1) * 4);

			this.dataFileReader.seek(fromPos * 4 * (this.columns - 1));

			// create the vector of the needed size, which is either the maxColls or
			// maximum of collocations possible, whichever is smaller
			retMap = new HashMap(Math.min(maxItems, toPos - fromPos));

			byte[] byteBuf = new byte[4];
			int realCount = 0;
			// read all rows
			for (int i = fromPos; i < toPos && realCount < maxItems; i++)
			{
				// read all values of a row ( assumedly there are this.columns columns,
				// we need to read this.columns - 1
				this.dataFileReader.read(byteBuf);
				Integer key = new Integer(Converter.bytesToInt(byteBuf));
				Integer[] values = new Integer[myColumns - 2];
				for (int j = 1; j < (this.columns - 1); j++)
				{
					if ( j < myColumns-1 )
					{
						this.dataFileReader.read(byteBuf);
						values[j-1] = new Integer(Converter.bytesToInt(byteBuf));
					}
					else
					{
						this.dataFileReader.skipBytes(4);
					}
				}
				retMap.put(key, values);
				realCount++;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		if (retMap == null)
		{
			return new HashMap();
		}
		return retMap;
	}

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @param maxItems How many items to return at max
	 * @return Map::Integer->Integer[]
	 */
	@Override
	public Map getDataAsMap(Integer number, int maxItems)
	{
		return getDataAsMap(number, this.columns, maxItems);
	}

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @param myColumns How many columns to return
	 * @param maxItems How many items to return at max
	 * @param minCols for each column the minimum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @param maxCols for each column the maximum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @return Map::Integer->Integer[]
	 */
	@Override
	public Map getDataAsMap(Integer number, int myColumns, int maxItems, int[] minCols, int[] maxCols)
	{
		if ( ( (number.intValue() ) * 4) > this.indexFile.length || number.intValue() < 1)
		{
			//System.err.println("Used number "+number+" is out of range, returning empty ArrayList.");
			//      throw new IllegalArgumentException("Number used to get data was too small or too large: " + number + " against index length of " +(this.indexFile.length / 4));
			return new HashMap();
		}
		Map retMap = new HashMap();
		try
		{
			int fromPos = 0;
			if (number.intValue() > 1)
			{
				fromPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 2) * 4);
			}
			// -1 here, because we began to count with 0, though wordNrs begin with 1
			int toPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 1) * 4);

			this.dataFileReader.seek(fromPos * 4 * (this.columns - 1));

			// create the vector of the needed size, which is either the maxColls or
			// maximum of collocations possible, whichever is smaller
			retMap = new HashMap(Math.min(maxItems, toPos - fromPos));

			byte[] byteBuf = new byte[4];
			int realCount = 0;
			// read all rows
			for (int i = fromPos; i < toPos && realCount < maxItems; i++)
			{
				// read all values of a row ( assumedly there are this.columns columns,
				// we need to read this.columns - 1
				this.dataFileReader.read(byteBuf);
				boolean add = true;
				Integer key = new Integer(Converter.bytesToInt(byteBuf));
				if ( key.intValue() < minCols[0] || key.intValue() > maxCols[0] )
				{
					add = false;
				}
				Integer[] values = new Integer[myColumns - 2];
				for (int j = 1; j < (this.columns - 1); j++)
				{
					if ( j < myColumns-1)
					{
						this.dataFileReader.read(byteBuf);
						Integer curVal = new Integer(Converter.bytesToInt(byteBuf));
						values[j-1] = curVal;
						if (curVal.intValue() < minCols[j+1] || curVal.intValue() > maxCols[j+1])
						{
							add = false;
						}
					}
					else
					{
						this.dataFileReader.skipBytes(4);
					}
				}
				if ( add )
				{
					retMap.put(key, values);
					realCount++;
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		if (retMap == null)
		{
			return new HashMap();
		}
		return retMap;
	}

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @param myColumns How many columns to return
	 * @param maxItems How many items to return at max
	 * otherwise be omitted)
	 * @param maxCols for each column the maximum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @return Map::Integer->Integer[]
	 */
	@Override
	public Map getDataAsMap(Integer number, int maxItems, int[] minCols, int[] maxCols)
	{
		return getDataAsMap(number, this.columns, maxItems, minCols, maxCols);
	}

	/**
	 * Column here is the column in the initial datafile, count begins with 1.
	 * Return the specified column completely.
	 * @param number
	 * @param col
	 * @return List::Integer
	 */
	@Override
	public List getSingleColumn(Integer number, int col)
	{
		return getSingleColumn(number, Integer.MAX_VALUE, col);
	}

	@Override
	public List getSingleColumn(Integer number, int maxItems, int col)
	{
		if ( ( (number.intValue() ) * 4) > this.indexFile.length || number.intValue() < 1)
		{
			throw new IllegalArgumentException("Number used to get data was too small or too large: " + number + " against index length of " +
					(this.indexFile.length / 4));
		}
		if (col > this.columns || col <= 0)
		{
			throw new IllegalArgumentException("Selected column " + col + " can not be retrieved: file has " + this.columns + " columns.");
		}
		col--; // since the first column is used for the index file
		col--; // since count begins with 0 in the program
		List retVec = new ArrayList();
		try
		{
			int fromPos = 0;
			if (number.intValue() > 1)
			{
				fromPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 2) * 4);
			}
			// -1 here, because we began to count with 0, though wordNrs begin with 1
			int toPos = Converter.bytesToInt(this.indexFile, (number.intValue() - 1) * 4);

			this.dataFileReader.seek(fromPos * 4 * (this.columns - 1));

			// create the vector of the needed size, which is either the maxColls or
			// maximum of collocations possible, whichever is smaller
			retVec = new ArrayList(Math.min(maxItems, toPos - fromPos));

			byte[] byteBuf = new byte[4];
			int realCount = 0;
			// read all rows
			for (int i = fromPos; i < toPos && realCount < maxItems; i++)
			{
				Integer[] values = new Integer[this.columns - 1];
				// read all values of a row ( assumedly there are this.columns columns,
				// we need to read this.columns - 1
				// FIXME: improve this - there is no real need to read them all only to take one then
				for (int j = 0; j < (this.columns - 1); j++)
				{
					this.dataFileReader.read(byteBuf);
					values[j] = new Integer(Converter.bytesToInt(byteBuf));
				}
				retVec.add(values[col]);
				realCount++;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		if (retVec == null)
		{
			return new ArrayList();
		}
		return retVec;
	}

	/**
	 * With this main method it's possible to test a prepared file.
	 * It will print some testcases.
	 * Takes two parameters, first is the filename, the second is the number of
	 * columns the has. The third is the number of columns to return.
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		if (args == null || args.length < 3)
		{
			System.out.println("Please give file to test as well as how many columns the file had and how many it has to read.");
		}
		else
		{
			Integer columns = null;
			Integer myColumns = null;
			try
			{
				columns = new Integer(args[1]);
				myColumns = new Integer(args[2]);
			}
			catch (Exception ex)
			{
				System.out.println("The second and third argument must be a number!");
				ex.printStackTrace();
			}
			BinFileMultCol prep = new BinFileMultColFile(args[0], columns.intValue());
			System.out.print("Testing getData() : ");
			for ( int i = -1 ; i < 10 ; i++ )
			{
				System.out.print("Testing "+i+" : ");
				List list = prep.getData(new Integer(i),myColumns.intValue(), new Integer(40).intValue());
				for ( int j = 0 ; j < list.size() ; j++ )
				{
					if ( myColumns.intValue() < 3 )
					{
						System.out.print(" " + ( (Integer[]) list.get(j))[0]);
					}
					else if ( myColumns.intValue() < 4 )
					{
						System.out.print(" " + ( (Integer[]) list.get(j))[0]+":"+((Integer[])list.get(j))[1]);
					}
					else if ( myColumns.intValue() < 5 )
					{
						System.out.print(" " + ( (Integer[]) list.get(j))[0]+":"+((Integer[])list.get(j))[1]+":"+((Integer[])list.get(j))[2]);
					}
					else if ( myColumns.intValue() < 6 )
					{
						System.out.print(" " + ( (Integer[]) list.get(j))[0]+":"+((Integer[])list.get(j))[1]+":"+((Integer[])list.get(j))[2]+":"+((Integer[])list.get(j))[3]);
					}

				}
				System.out.println();
			}
			System.out.print("Testing getDataAsMap() : ");
			int[] minCols = new int[3];
			minCols[0] = 0;
			minCols[1] = 0;
			minCols[2] = 4;
			int[] maxCols = new int[3];
			maxCols[0] = Integer.MAX_VALUE;
			maxCols[1] = 10;
			maxCols[2] = Integer.MAX_VALUE;

			for ( int i = -1 ; i < 10 ; i++ )
			{
				System.out.print("Testing " + i + " : <");
				Map map = prep.getDataAsMap(new Integer(i), myColumns.intValue(), new Integer(40).intValue(), minCols, maxCols);
				//        Map map = prep.getDataAsMap(new Integer(i), myColumns.intValue(), new Integer(40).intValue());
				for (Iterator it = map.keySet().iterator(); it.hasNext(); )
				{
					Integer curKey = (Integer)it.next();
					Integer[] curVals = (Integer[])map.get(curKey);
					System.out.print(curKey+":");
					for (Integer curVal : curVals) {
						System.out.print(curVal+" ");
					}
				}
				System.out.println(">");
			}
		}
	}

}
