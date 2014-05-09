/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/*
 * Created on May 8, 2008
 * 
 */
package com.ibm.bluej.util.common;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;


public abstract class Lang {
	public static String backQuote(String cmd) {
		try {
			return FileUtil.readProcessAsString(Runtime.getRuntime().exec(cmd));
		} catch (Exception ie) {
			throw new Error(ie);
		}
	}
	
	public static boolean strToBool(String s) {
		if (s == null) {
			return false;
		}
		s = s.toLowerCase();
		if (s.startsWith("y")) {
			return true;
		}
		return false;
	}
	
	public static double log2(double x) {
		return Math.log(x)/Math.log(2);
	}
	private static DecimalFormat shortFormat = new DecimalFormat("0.000");
	public static String dblStr(double x) {
		return shortFormat.format(x);
	}
	
	public static String initCap(String s) {
		return Character.toUpperCase(s.charAt(0))+s.substring(1);
	}
	
	
	public static <T> String stringerList(T[] l, FunST<T,String> stringer, String sep) {
		return stringerList(Arrays.asList(l), stringer, sep);
	}
	
	public static <T> String stringerList(Iterable<T> l, FunST<T,String> stringer, String sep) {
		if (l == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (T o : l) {
			buf.append(stringer.f(o)).append(sep);
		}
		if (buf.length() > 0) {
			buf.setLength(buf.length() - sep.length());
		}
		return buf.toString();
	}
	
	public static String stringList(Object[] l, String sep) {
		return stringList(Arrays.asList(l), sep);
	}
	public static String stringList(Iterable<?> l, String sep) {
		if (l == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (Object o : l) {
			if (o instanceof Double) {
				o = Lang.dblStr((Double)o);
			}
			buf.append(o).append(sep);
		}
		if (buf.length() > 0) {
			buf.setLength(buf.length() - sep.length());
		}
		return buf.toString();
	}
	
	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {				
		}
		return false;
	}
	
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {				
		}
		return false;
	}
	
	public static <T> T NVL(T value, T ifNull) {
		return value == null ? ifNull : value;
	}
	
	public static String forceAlphaNum(String str) {
		int last = str.length();

		Character ch = new Character(str.charAt(0));
		String temp = "";

		for (int i = 0; i < last; i++) {
			if (ch.isLetterOrDigit(str.charAt(i)))
				temp += str.charAt(i);
		}

		return temp;
	} // clean

	public static String truncate(String str, int length) {
		if (str.length() > length) {
			str = str.substring(0, length);
		}
		return str;
	}
	
	public static String indent(String str, int length) {
		char[] spaces = new char[length];
		Arrays.fill(spaces, ' ');
		return new String(spaces) + str;
	}
	
	public static String LPAD(String str, int length) {
		return LPAD(str, ' ', length);
	}
	
	public static String LPAD(String str, char c, int length) {
		String s = str;
		if (s.length() >= length) {
			s = s.substring(0, length);
		}
		int padding = length - s.length();
		char[] spaces = new char[padding];
		Arrays.fill(spaces, c);
		s = new String(spaces) + s;
		return s;
	}
	public static String RPAD(String str, int length) {
		return RPAD(str, ' ', length);
	}
	public static String RPAD(String str, char c, int length) {
		String s = str;
		if (s.length() >= length) {
			s = s.substring(0, length);
		}
		int padding = length - s.length();
		char[] spaces = new char[padding];
		Arrays.fill(spaces, c);
		s =  s + new String(spaces);
		return s;
	}
	
	public static void userWait() {
		try { System.in.read(); } catch (Exception e) {}
	}
	
	private static BufferedReader inReader = null;
	public static String readln() {
		if (inReader == null) {
			inReader = new BufferedReader(new InputStreamReader(System.in));
		}
		try {
			return inReader.readLine();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	public static int linearSearch(Object[] arr, Object obj) {
		int ndx = 0;
		for (; ndx < arr.length; ++ndx) {
			if ((arr[ndx] == null && obj == null) || (arr[ndx] != null && arr[ndx].equals(obj))) {
				return ndx;
			}
		}
		return -1;
		
	}
	
	public static String getClasspath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
	    URL[] urls = ((URLClassLoader)cl).getURLs();
	    StringBuffer classpath = new StringBuffer();
	    for(URL url: urls){
	    	String f = url.getFile();
	    	int wksp = f.indexOf("workspace");
	    	String add = f.substring(wksp+"workspace".length());
	    	if (classpath.length() != 0) {
	    		classpath.append(":");
	    	}
	    	classpath.append("$workspace").append(add);
	    	//System.out.println(url.getFile());
	    }
	    return classpath.toString();
	}
	
	/**
	 * The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain the same.
	 * The special characters ".", "-", "*", and "_" remain the same.
	 * The space character " " is converted into a plus sign "+".
	 * All other characters converted into '%XY'
	 * @param preEncode
	 * @return
	 */
	public static String urlEncode(String preEncode) {
		try {
			return URLEncoder.encode(preEncode, "UTF-8");
		} catch (Exception e) {throw new Error(e);}
	}
	public static String fullURLEncode(String preEncode) {
		return urlEncode(preEncode).replace(".", "2E").replace("-", "%2D").replace("*", "2A").replace("_", "%5F").replace("+", "%20");
	}
	public static String urlDecode(String encoded) {
		try {
			return URLDecoder.decode(encoded, "UTF-8");
		} catch (Exception e) {throw new Error(e);}
	}
}
