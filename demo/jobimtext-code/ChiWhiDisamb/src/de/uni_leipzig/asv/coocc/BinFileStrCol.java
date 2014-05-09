/* This file is distributed as part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.uni_leipzig.asv.coocc;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reads the stored files for wordstrings.
 *
 * @author Stefan Bordag
 */
public class BinFileStrCol
{
	protected byte[] indexFile = null;
	protected RandomAccessFile dataFileReader = null;

	protected String fileName = null;

	public BinFileStrCol(String fileName)
	{
		this.fileName = fileName;
		if ( existsFileDump(fileName) )
		{
			this.indexFile = new byte[(int)(new File(fileName+BinFileStrColPreparer.ext2)).length()];
		}
		else
		{
			throw new IllegalArgumentException("Can't read IndexFile "+fileName+" since it doesn't exist or is zero-length.");
		}

		try
		{
			RandomAccessFile reader1 = null;
			if ( fileName != null )
			{
				reader1 = new RandomAccessFile(  fileName + BinFileStrColPreparer.ext2, "r" );
				this.dataFileReader = new RandomAccessFile(  fileName + BinFileStrColPreparer.ext1, "r" );
			}

			reader1.read(this.indexFile);
			reader1.close();
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Look up in file2 what's at position n-1 and n and returns the wort_bin
	 * from n-1 to n from file1 as Object[2] with Object[0]=String and Object[1]=Integer
	 * @param wordNr
	 * @return
	 */
	/*  public Object[] getWordAndAnzahl(Integer wordNr)
  {
    Object[] retVal = new Object[2];
    retVal[0] = "";
    retVal[1] = new Integer(0);
    if ( (wordNr.intValue()-1)*4 >= this.indexFile.length )
    {
      return retVal;
    }
    try
    {
      int fromPos = 0;
      if ( wordNr.intValue() > 1 )
      {
        fromPos = Converter.bytesToInteger(this.indexFile, (wordNr.intValue()-2)*4).intValue();
      }
      // -1 here and -2 above, because we began to count with 0, though wordNrs begin with 1
      int toPos = Converter.bytesToInteger(this.indexFile, (wordNr.intValue()-1)*4) .intValue();
      if ( (toPos - fromPos) < 1 )
      {
        return retVal;
      }

      this.dataFileReader.seek(fromPos);

      Integer[] curVals = null;
      byte[] byteBuf = new byte[(toPos-fromPos)-4];
      byte[] byteBuf2 = new byte[4];
      this.dataFileReader.read(byteBuf);
      retVal[0] = new String(byteBuf);
      this.dataFileReader.read(byteBuf2);
      retVal[1] = Converter.bytesToInteger(byteBuf2);
    }
    catch ( Exception ex )
    {
      ex.printStackTrace();
    }
    return retVal;
  }*/

	public int getMaxWordNr()
	{
		return this.indexFile.length/4;
	}

	/**
	 * Lineare Suche zum auffinden von Wortnummern
	 * @param word
	 * @return
	 */
	public Integer getNumber(String word)
	{
		int count = 1;
		while ( count < this.getMaxWordNr() )
		{
			if ((getWord( new Integer( count ) ) !=null)&&( getWord( new Integer( count ) ).equals(word) ))
			{
				return new Integer(count);
			}
			count++;
		}
		return new Integer(0);
	}
	//  }

	/**
	 * Look up in file2 what's at position n-1 and n and returns the wort_bin
	 * from n-1 to n from file1 as Object[2] with Object[0]=String and Object[1]=Integer
	 * @param wordNr
	 * @return
	 */
	public String getWord(Integer wordNr)
	{
		//either there is a word 0 or we don't want it. If there should be a word 0,
		// then it should always be existant. This is the workaround for the case that
		// this word doesn't exist.
		if ( wordNr.intValue() == 0 )
		{
			return null;
		}
		if ( (wordNr.intValue())*4 > this.indexFile.length || wordNr.intValue() < 1 )
		{
			return null;
		}
		String retVal = "";
		try
		{
			int fromPos = 0;
			if ( wordNr.intValue() > 1 )
			{
				// word 1 will be 0 here
				// fromPos for word 2 gives 2-2=0 at which an address is found, say 3 times 4(bytes)
				// 0:0,0:16 : 1
				// 0:16,1:1000 : 2
				// 1:1000,2:... : 3
				// 2:...,3:... : 4
				// seek goes then at that position and reads from the to toPos
				fromPos = Converter.bytesToInt(this.indexFile, (wordNr.intValue()-2)*4);
			}
			// -1 here, because we began to count with 0, though wordNrs begin with 1
			int toPos = Converter.bytesToInt( this.indexFile, ( wordNr.intValue()-1 ) * 4 );
			if ( (toPos - fromPos) < 1 )
			{
				return null;
			}

			this.dataFileReader.seek(fromPos);

			byte[] byteBuf = new byte[(toPos-fromPos)];//-4];
			this.dataFileReader.read(byteBuf);
			retVal = new String(byteBuf);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
		if ( retVal.length() < 1 )
		{
			return null;
		}
		return retVal;
	}

	public List getWordsForNumbersSameOrder(List numbers)
	{
		ArrayList retVec = new ArrayList( numbers.size() );
		for ( Iterator it = numbers.iterator(); it.hasNext(); )
		{
			Integer number = (Integer)it.next();
			retVec.add(getWord(number));
		}
		return retVec;
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
		if ( (new File(fileName+BinFileStrColPreparer.ext1)).exists() &&
				(new File(fileName+BinFileStrColPreparer.ext1)).length() > 0 &&
				(new File(fileName+BinFileStrColPreparer.ext2)).exists() &&
				(new File(fileName+BinFileStrColPreparer.ext2)).length() > 0 )
		{
			return true;
		}
		return false;
	}

	public void testFiles(String fileName)
	{
		for ( int i = 50001 ; i > 49989 ; i-- )
		{
			System.out.println(i+" = "+getWord(new Integer(i)));
		}
	}

	public static void main(String[] args)
	{
		if ( args == null || args.length < 1 )
		{
			System.out.println("Please give file which should be tested.");
		}
		else
		{
			BinFileStrCol str = new BinFileStrCol(args[0]);
			//      System.out.println("Testing word "+2+" : "+str.getWord(new Integer(2)));
			for ( int i = -1 ; i < 10 ; i++ )
			{
				System.out.println("Testing word "+i+" : "+str.getWord(new Integer(i)));
			}
		}
	}


}

