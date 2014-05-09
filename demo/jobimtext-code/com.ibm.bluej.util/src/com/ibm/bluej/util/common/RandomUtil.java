package com.ibm.bluej.util.common;

import java.util.*;

public class RandomUtil {

  /**
   * generates a random int in the range [fromInclusive-toExclusive)
   * 
   * @param fromInclusive
   * @param toExclusive
   * @return
   */
  public static int randomInt(int fromInclusive, int toExclusive) {
    if (fromInclusive > toExclusive) {
      throw new IllegalArgumentException("fromInclusive can not be > toExclusive ");
    }
    int num;
    do {
      num = (int) (fromInclusive + Math.floor(Math.random() * (toExclusive - fromInclusive)));
    } while (num >= toExclusive);
    return num;
  }

  /**
   * return a random entry in a collection
   * 
   * @param col
   * @return
   */
  public static <T> T randomMember(Collection<T> col) {
    if (col == null || col.size() == 0) {
      return null;
    }
    int index = randomInt(0, col.size());
    if (col instanceof List)
    	return ((List<T>)col).get(index);
    int counter = 0;
    T ret = null;
    for (T entry : col) {
      if (counter == index) {
        ret = entry;
        break;
      }
      counter++;
    }
    return ret;
  }

  /**
   * remove and return a random member of the passed Collection
   * 
   * @param col
   * @return
   */
  public static <T> T removeRandom(Collection<T> col) {
    T entry = randomMember(col);
    col.remove(entry);
    return entry;
  }

  /**
   * return a random entry in a map
   * 
   * @param map
   * @return
   */
  public static <K, V> Map.Entry<K, V> randomEntry(Map<K, V> map) {
    if (map == null || map.size() == 0) {
      return null;
    }
    int index = randomInt(0, map.size());
    int counter = 0;
    Map.Entry<K, V> ret = null;
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (counter == index) {
        ret = entry;
      }
      counter++;
    }
    return ret;
  }

  /**
   * A sample of n elements. It is a Collection. uses weighted reservoir sampling
   * http://arxiv.org/pdf/1012.0256.pdf (A-Chao)
   * http://blog.cloudera.com/blog/2013/04/hadoop-stratified-randosampling-algorithm/
   * 
   * @author partha
   * 
   * @param <T>
   */
  public static class Sample<T> extends ArrayList<T> {
    private static final long serialVersionUID = 1L;

    int sampleSize;

    double accumulatedWeight;

    public Sample(int sampleSize) {
      this.sampleSize = sampleSize;
    }

    public void maybeSave(T item) {
    	maybeSave(item, 1.0);
    }
    
    /**
     * maybeSave(T item, double weight) behaves like two functions: shouldSave and save
     * 
     * @param item
     * @param weight
     */
    public void maybeSave(T item, double weight) {
      if (!isFull()) {
        add(item);
      } else if (shouldSave(weight)) {
        save(item);
      }
    }

    public boolean shouldSave() {
    	return shouldSave(1.0);
    }
    
    /**
     * determines if the item should be stored in the reservoir
     * 
     * @param weight
     * @return boolean
     */
    public boolean shouldSave(double weight) {
      accumulatedWeight += weight;
      if (!isFull()) {
        return true;
      } else if (Math.random() < calculateProb(weight)) {
        return true;
      }
      return false;
    }

    /**
     * stores the item in the reservoir, ejecting another one if needed
     * @param item
     */
    public void save(T item) {
      if (!isFull()) {
        this.add(item);
      } else {
        set(randomInt(0, sampleSize), item); // randomly replace an entry
      }
    }

    private boolean isFull() {
      return this.size() >= sampleSize;
    }

    private double calculateProb(double weight) {
      return (weight * sampleSize) / accumulatedWeight;
    }
  }

	public static <T> T randomFromIterator(Iterator<T> iter) {
		if (!iter.hasNext()) {
			return null;
		}
		T element = iter.next();
		double seen = 1;
		while(iter.hasNext()) {
			seen += 1;
			T posReplace = iter.next();
			if (Math.random() < 1.0/seen) {
				element = posReplace;
			}
		}
		return element;
	}  
	public static <T> T removeRandomFast(ArrayList<T> list, Random rand) {
		int ndx = rand.nextInt(list.size());
		if (ndx == list.size()-1)
			return list.remove(ndx);
		T r = list.get(ndx);
		list.set(ndx, list.remove(list.size()-1));
		return r;
	}
	public static <T> ArrayList<T> getSample(Iterable<T> from, int size) {
		Sample<T> s = new Sample<T>(size);
		for (T i : from) {
			s.maybeSave(i);
		}
		return s;
	}
	
	public static int[] toShuffledIntArray(Collection<Integer> c, Random rand) {
		int[] s = new int[c.size()];
		int ndx = 0;
		for (Integer ci : c) {
			if (ndx == 0) {
				s[ndx] = ci;
			} else {
				int j = rand.nextInt(ndx+1);
				s[ndx] = s[j];
				s[j] = ci;
			}
			++ndx;
		}
		return s;
	}
	
  public static void main(String[] args) {
    RandomUtil.Sample<Integer> sample = new RandomUtil.Sample<Integer>(10);
    for (int i=0; i < 1000; i++ ){
      sample.maybeSave(new Integer(i),345433+Math.random());
    }
    for (Integer i: sample){
      System.out.println(i);
    }
  }
}
