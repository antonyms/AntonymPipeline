package com.ibm.bluej.util.common;

import java.io.Serializable;
import java.util.*;


public class Span implements Cloneable, Serializable, Comparable<Span> {
  private static final long serialVersionUID = 1L;

  public static final char INTERVAL_OPEN_SYMBOL = '(';

  public static final char INTERVAL_CLOSE_SYMBOL = ')';

  public static final char INTERVAL_SEP_SYMBOL = ',';

  public static final String FORMAT = "(\\(\\s*[0-9]+\\s*,\\s*[0-9]+\\s*\\))";

  // start is the first char, end is one past the last char
  public int start, end;

  public Span(int first, int last) {
    this.start = first;
    this.end = last;
  }

  /**
   * returns the String indicated by the Span from text
   * 
   * @param text
   * @return
   */
  public String substring(String text) {
    return text.substring(this.start, this.end);
  }

	public String substringLimit(String whole) {
		return whole.substring(Math.max(start,0), Math.min(whole.length(),end));
	}  
  
  /**
   * true if the spans overlap at all
   * 
   * @param s
   * @return
   */
  public boolean overlaps(Span s) {
    if (s == null) {
      return false;
    } else {
      if ((contains(s)) || (this.start >= s.start && this.start < s.end)
              || (this.end <= s.end && this.end > s.start)) {
        return true;
      } else {
        return false;
      }
    }
  }
  
  /**
   * true if the spans overlap at all
   * 
   * @param s
   * @return
   */
  public boolean overlaps(int s, int e) {

      if ((contains(s,e)) || (this.start >= s && this.start < e)
              || (this.end <= e && this.end > s)) {
        return true;
      } else {
        return false;
      }
    
  }

  /**
   * true if this span contains (or is equal to) s
   * 
   * @param s
   * @return boolean
   */
  public boolean contains(Span s) {
    return (start <= s.start && end >= s.end);
  }

	public boolean contains(int ndx) {
		return ndx >= start && ndx <= end;
	}
	public boolean contains(int otherStart, int otherEnd) {
		return (start <= otherStart && end >= otherEnd);
	}
	public boolean properContains(Span s) {
		return (start <= s.start && end >= s.end && 
				(start < s.start || end > s.end));
	}
	
	/**
	 * neither span contains the other, but they do overlap
	 * @param s
	 * @return
	 */
	public boolean crosses(Span s) {
		return start < s.start && end < s.end && end > s.start ||
				s.start < start && s.end < end && s.end > start;
				
	}
	
  /**
   * the span in interval notation http://en.wikipedia.org/wiki/Interval_(mathematics)
   */
  @Override
  public String toString() {
    return (String.valueOf(INTERVAL_OPEN_SYMBOL) + start + String.valueOf(INTERVAL_SEP_SYMBOL)
            + end + String.valueOf(INTERVAL_CLOSE_SYMBOL));
  }

  /**
   * creates a span from interval notation
   * 
   * @param str
   * @return span
   */
  public static Span fromString(String str) {
    if (!str.matches(FORMAT)) {
      throw new IllegalArgumentException("The string should be of the format: " + FORMAT);
    }
    int open = str.indexOf(INTERVAL_OPEN_SYMBOL);
    int mid = str.indexOf(INTERVAL_SEP_SYMBOL);
    int close = str.indexOf(INTERVAL_CLOSE_SYMBOL);
    // matcher might be better. But worried about performance when we account for thread-safety
    String begin = str.substring(open + 1, mid).trim();
    String end = str.substring(mid + 1, close).trim();
    return new Span(Integer.parseInt(begin), Integer.parseInt(end));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + start;
    result = prime * result + end;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Span other = (Span) obj;
    if (start != other.start)
      return false;
    if (end != other.end)
      return false;

    return true;
  }
  
	public static class LengthComparator implements Comparator<Span> {

		@Override
		public int compare(Span o1, Span o2) {
			return o1.length() - o2.length();
		}
		
	}
	
	public static int sumLength(Span s1, Span s2) {
		int overlapLen = s1.overlapLength(s2);
		return s1.length() + s2.length() - overlapLen;
	}
	
	public int distanceTo(Span s) {
		if (s.start > end)
			return s.start - end;
		if (start > s.end)
			return start - s.end;
		return 0;
	}
	
	public boolean fix(int minStart, int maxEnd) {
		boolean changed = false;
		if (start < minStart) {
			start = minStart; 
			changed = true;
		}
		if (end > maxEnd) {
			end = maxEnd;
			changed = true;
		}
		return changed;
	}
	
	public static String replaceAll(String source, Map<Span,String> replacements) {
		ArrayList<Pair<Span,String>> rep = HashMapUtil.toPairs(replacements);
		Collections.sort(rep, new FirstPairComparator());
		StringBuffer buf = new StringBuffer();
		int leftOff = 0;
		for (Pair<Span,String> rp : rep) {
			if (rp.first.start >= leftOff) {
				try {
					buf.append(source.substring(leftOff,rp.first.start));
				} catch (Exception e) {
					System.out.println(source+" "+leftOff+", "+rp.first.start);
				}
				buf.append(rp.second);
				leftOff = rp.first.end;
			}
		}
		buf.append(source.substring(leftOff));
		return buf.toString();
	}
	
	public void addOffset(int offset) {
		start += offset;
		end += offset;
	}
	
	public static int firstStart(Iterable<Span> spans) {
		int start = Integer.MAX_VALUE;
		for (Span s : spans)
			start = Math.min(s.start, start);
		return start;
	}
	
	public static int lastEnd(Iterable<Span> spans) {
		int end = Integer.MIN_VALUE;
		for (Span s : spans)
			end = Math.max(s.end, end);
		return end;
	}
	/**
	 * @param source
	 * @param spans
	 * @return
	 */
	public static String compressSpace(String source, Collection<Span> spans) {
		StringBuffer buf = new StringBuffer();
		boolean lastWasSpace = false;
		for (int i = 0; i < source.length(); ++i) {
			if (Character.isWhitespace(source.charAt(i))) {
				if (lastWasSpace) {
					//decrease all offsets after current position by one
					for (Span s : spans) {
						if (s.end >= buf.length()) {
							--s.end;
							if (s.start >= buf.length()) {
								--s.start;
							}
						}
					}
				}
				lastWasSpace = true;
			} else {
				if (lastWasSpace) {
					buf.append(' ');
				}
				buf.append(source.charAt(i));
				lastWasSpace = false;
			}
		}
		return buf.toString();
	}
	
	public static String highlight(String source, Span span, String begin, String end) {
		return source.substring(0,span.start) + begin + span.substring(source) + end + source.substring(span.end);
	}
	
	public static String highlightAll(String source, List<Span> spans, String begin, String end) {
		Collections.sort(spans);
		StringBuffer buf = new StringBuffer();
		int leftOff = 0;
		for (Span span : spans) {
			if (span.start < 0 || span.end >= source.length()) {
				continue;
			}
			if (span.start >= leftOff) {
				try {
					buf.append(source.substring(leftOff,span.start));
				} catch (Exception e) {
					System.out.println(source+" "+leftOff+", "+span.start);
				}
				buf.append(begin);
				buf.append(span.substring(source));
				buf.append(end);
				leftOff = span.end;
			}
		}
		buf.append(source.substring(leftOff));
		return buf.toString();
	}
	
	public static String highlightAll(String source, List<Span> spans, FunST<Span,Pair<String,String>> beginEndMaker) {
		Collections.sort(spans);
		StringBuffer buf = new StringBuffer();
		int leftOff = 0;
		for (Span span : spans) {
			if (span.start < 0 || span.end >= source.length()) {
				continue;
			}
			if (span.start >= leftOff) {
				try {
					buf.append(source.substring(leftOff,span.start));
				} catch (Exception e) {
					System.out.println(source+" "+leftOff+", "+span.start);
				}
				Pair<String,String> be = beginEndMaker.f(span);
				buf.append(be.first);
				buf.append(span.substring(source));
				buf.append(be.second);
				leftOff = span.end;
			}
		}
		buf.append(source.substring(leftOff));
		return buf.toString();
	}
	
	public boolean isValid(String str) {
		return start <= end && start >= 0 && end <= str.length();
	}	
	
	public Span clone() {
		try {
		return (Span)super.clone();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	public int overlapLength(Span other) {
		return Math.max(0, 
				Math.min(this.end, other.end) - Math.max(this.start, other.start));
	}
	
	public double overlapPercent(int otherStart, int otherEnd) {
		return Math.max(0.0, 
				Math.min(this.end, otherEnd) - Math.max(this.start, otherStart)) / 
				(Math.max(this.end, otherEnd) - Math.min(this.start, otherStart));
	}
	
	public double overlapPercent(Span other) {
			return (double)overlapLength(other) / 
					(Math.max(this.end, other.end) - Math.min(this.start, other.start));
	}
	
	public int length() {
		return end - start;
	}	
	
	public int compareTo(Span s) {
		if (start == s.start) {
			return s.end - end; //longer spans first
		}
		return start - s.start;
	}
}
