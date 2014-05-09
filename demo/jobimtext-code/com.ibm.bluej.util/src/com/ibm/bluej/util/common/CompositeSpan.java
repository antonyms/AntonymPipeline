package com.ibm.bluej.util.common;

import java.io.*;
import java.util.*;

//TODO: guava has very nice implementation
public class CompositeSpan implements Serializable, Comparable<CompositeSpan> {
	
	private static final long serialVersionUID = 1L;
	private boolean[] compSpan; //TODO: BitSet makes more sense
	
	public CompositeSpan(int length) {
		compSpan = new boolean[length];
	}
	public CompositeSpan() {
		compSpan = new boolean[0];
	}
	
	public CompositeSpan(Iterable<Span> s) {
		int largestEnd = 0;
		for (Span si : s) {
			if (si.end > largestEnd) {
				largestEnd = si.end;
			}
		}
		compSpan = new boolean[largestEnd];
		for (Span si : s) {
			for (int i = si.start; i < si.end; ++i) {
				compSpan[i] = true;
			}
		}
	}
	
	public String substring(String str, String insertInGaps) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < compSpan.length; ++i)
			if (compSpan[i])
				buf.append(str.charAt(i));
			else if (i > 0 && compSpan[i-1] && insertInGaps != null)
				buf.append(insertInGaps);
		return buf.toString();
	}
	
	public void add(Span s) {
		if (s.end > compSpan.length) {
			compSpan = Arrays.copyOf(compSpan, s.end);
		}
		for (int p = s.start; p < s.end; ++p)
			compSpan[p] = true;
	}
	
	public int firstNdx() {
		for (int i = 0; i < compSpan.length; ++i)
			if (compSpan[i])
				return i;
		return -1;
	}
	public int lastNdx() {
		for (int i = compSpan.length-1; i >= 0; --i)
			if (compSpan[i])
				return i;
		return -1;
	}
	
	public String toString() {
		ArrayList<String> spanParts = new ArrayList<String>();
		int startSpan = -1;
		for (int i = 0; i < compSpan.length; ++i) {
			if (compSpan[i]) {
				if (startSpan == -1) {
					startSpan = i;
				}
			} else if (startSpan != -1) {
				spanParts.add("("+startSpan+" - "+i+")");
				startSpan = -1;
			}
		}
		if (startSpan != -1) {
			spanParts.add("("+startSpan+" - "+compSpan.length+")");
		}
		return Lang.stringList(spanParts, " ^ ");
	}
	
	public int overlap(CompositeSpan s) {
		int total = 0;
		for (int i = 0; i < compSpan.length && i < s.compSpan.length; ++i)
			if (compSpan[i] && s.compSpan[i]) 
				++total;
		return total;
	}
	public int union(CompositeSpan s) {
		int total = 0;
		for (int i = 0; i < compSpan.length || i < s.compSpan.length; ++i)
			if ((compSpan.length > i && compSpan[i]) || (s.compSpan.length > i && s.compSpan[i])) 
				++total;
		return total;
	}
	
	public int totalInSpan() {
		int total = 0;
		for (int i = 0; i < compSpan.length; ++i)
			if (compSpan[i]) 
				++total;
		return total;
	}
	
	public boolean contains(int ndx) {
		if (ndx >= compSpan.length)
			return false;
		return compSpan[ndx];
	}
	
	public boolean partiallyContains(Span s) {
		return partiallyContains(s.start, s.end);
	}
	
	public boolean partiallyContains(int start, int end) {
		if (end > compSpan.length) {
			end = compSpan.length;
		}
		for (int i = start; i < end; ++i) {
			if (compSpan[i]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean fullyContains(int start, int end) {
		if (end > compSpan.length) {
			return false;
		}
		for (int i = start; i < end; ++i) {
			if (!compSpan[i]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean fullyContains(Span span) {
		return fullyContains(span.start, span.end);
	}
	
	public int distanceBefore(int start) {
		if (start >= compSpan.length) {
			return -1;
		}
		int d = 0;
		for (; !compSpan[start-d]; ++d) {
			if (start - d == 0)
				return -1;
		}
		return d;
	}
	
	public int distanceAfter(int end) {
		if (end >= compSpan.length)
			return -1;
		int d = 0;
		for (; !compSpan[end+d]; ++d) {
			if (end + d + 1 >= compSpan.length)
				return -1;
		}
		return d;
	}
	
	public int minDistance(int start, int end) {
		int db = distanceBefore(start);
		int da = distanceAfter(end);
		if (db == -1)
			return da;
		if (da == -1)
			return db;
		return Math.min(da, db);
	}

	@Override
	public int compareTo(CompositeSpan o) {
		for (int i = 0; i < compSpan.length && i < o.compSpan.length; ++i) {
			if (compSpan[i] != o.compSpan[i])
				return compSpan[i] ? -1 : 1;
		}
		int ln = lastNdx();
		int oln = o.lastNdx();
		if (ln != oln)
			return ln > oln ? -1 : 1;
		return 0;
	}
}
