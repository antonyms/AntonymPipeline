/* This file is distributed as part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.uni_leipzig.asv.coocc;

/**
 * This class converts bytes to Integers and vice versa, the same for chars.
 */
public class Converter
{
	public static final int MASK = 0xff;

	protected Converter()
	{
	}

	public static byte[] intToBytes(int a)
	{
		byte[] c = new byte[4];
		c[0] = (byte) ( (a >> 24) & MASK);
		c[1] = (byte) ( (a >> 16) & MASK);
		c[2] = (byte) ( (a >> 8) & MASK);
		c[3] = (byte) (a & MASK);
		return c;
	}

	public static byte[] integerToBytes(Integer a)
	{
		byte[] c = new byte[4];
		c[0] = (byte) ( (a.intValue() >> 24) & MASK);
		c[1] = (byte) ( (a.intValue() >> 16) & MASK);
		c[2] = (byte) ( (a.intValue() >> 8) & MASK);
		c[3] = (byte) (a.intValue() & MASK);
		return c;
	}

	/**
	 * @todo: implement directly, without creating an extra charfield.
	 * @todo: implement variable size
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int bytesToInt(byte[] b, int offset)
	{
		int mask = MASK;

		int val = 0;
		int shift = 0;
		int j = offset + 4;
		for (int i = offset + 4; i > offset; i--)
		{
			val |= b[i - 1] << shift & mask;
			shift += 8;
			mask <<= 8;
		}
		return val;
	}

	public static Integer bytesToInteger(byte[] b, int offset)
	{
		return new Integer(bytesToInt(b, offset));
	}

	public static int bytesToInt(byte[] b)
	{
		int mask = MASK;

		if (b.length < 5)
		{
			int val = 0;
			int shift = 0;
			int j = b.length;
			for (int i = b.length; i > 0; i--)
			{
				val |= b[i - 1] << shift & mask;
				shift += 8;
				mask <<= 8;
			}
			return val;
		}
		throw new IllegalArgumentException("bytesToInteger requires <5 byte arrays");
	}

	public static Integer bytesToInteger(byte[] b)
	{
		return new Integer(bytesToInt(b));
	}

}
