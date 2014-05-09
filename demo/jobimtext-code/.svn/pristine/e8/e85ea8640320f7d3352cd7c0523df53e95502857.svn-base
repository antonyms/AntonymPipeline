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

package com.ibm.bluej.consistency.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import com.ibm.bluej.consistency.term.ATerm;


//TODO: unit tests and performance comparison

//Much of this code is taken from AbstractHashedMap licensed under the Apache License
//This code is not licensed under the Apache License. We retain all the attribution information though.
//Out of an abundance of caution the entirity of all comments where any sign of attribution or credit is made are retained.
//Some of these contain documentation that is not accurate with respect to the modified file.

//GenericsNote: Converted -- However, null keys will now be represented in the internal structures, a big change.
/*
 * An abstract implementation of a hash-based map which provides numerous points for
 * subclasses to override.
 * <p/>
 * This class implements all the features necessary for a subclass hash-based map.
 * Key-value entries are stored in instances of the <code>HashEntry</code> class,
 * which can be overridden and replaced. The iterators can similarly be replaced,
 * without the need to replace the KeySet, EntrySet and Values view classes.
 * <p/>
 * Overridable methods are provided to change the default hashing behaviour, and
 * to change how entries are added to and removed from the map. Hopefully, all you
 * need for unusual subclasses is here.
 * <p/>
 * NOTE: From Commons Collections 3.1 this class extends AbstractMap.
 * This is to provide backwards compatibility for ReferenceMap between v3.0 and v3.1.
 * This extends clause will be removed in v4.0.
 *
 * @author java util HashMap
 * @author Matt Hall, John Watkinson, Stephen Colebourne
 * @version $Revision: 1.1 $ $Date: 2005/10/11 17:05:32 $
 * @since Commons Collections 3.0
 */

public final class ApproxRandSet<T> implements java.util.Set<T>, IRandomAccessible<T>, Serializable {
	private static final long serialVersionUID = 1L;

    protected static class Entry<T> {
    	T value;
    	int hashCode;
    	Entry(T value, int hashCode) {
    		this.value = value;
    		this.hashCode = hashCode;
    	}
    }
    
    /**
     * The default capacity to use
     */
    protected static final int DEFAULT_CAPACITY = 16;
    /**
     * The default threshold to use
     */
    protected static final int DEFAULT_THRESHOLD = 12;
    /**
     * The default load factor to use
     */
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * The maximum capacity allowed
     */
    protected static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Load factor, normally 0.75
     */
    protected float loadFactor;
    /**
     * The size of the set
     */
    protected int size;
    /**
     * Map entries
     */
    protected Entry<T>[][] data;
    /**
     * Size at which to rehash
     */
    protected int threshold;
  
    /**
     * Constructor which performs no validation on the passed in parameters.
     *
     * @param initialCapacity the initial capacity, must be a power of two
     * @param loadFactor      the load factor, must be &gt; 0.0f and generally &lt; 1.0f
     * @param threshold       the threshold, must be sensible
     */
    protected ApproxRandSet(int initialCapacity, float loadFactor, int threshold) {
        this.loadFactor = loadFactor;
        this.data = new Entry[initialCapacity][];
        this.threshold = threshold;
    }

    /**
     * Constructs a new, empty map with the specified initial capacity and
     * default load factor.
     *
     * @param initialCapacity the initial capacity
     * @throws IllegalArgumentException if the initial capacity is less than one
     */
    public ApproxRandSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public ApproxRandSet(Collection<T> c) {
    	data = new Entry[0][];
    	loadFactor = DEFAULT_LOAD_FACTOR;
    	int cSize = c.size();
    	int newSize = (int) ((size + cSize) / loadFactor + 1);
        ensureCapacity(calculateNewCapacity(newSize));
        addAll(c);
    }
    
    /**
     * Constructs a new, empty map with the specified initial capacity and
     * load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is less than one
     * @throws IllegalArgumentException if the load factor is less than or equal to zero
     */
    protected ApproxRandSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0");
        }
        if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be greater than 0");
        }
        this.loadFactor = loadFactor;
        this.threshold = calculateThreshold(initialCapacity, loadFactor);
        initialCapacity = calculateNewCapacity(initialCapacity);
        this.data = new Entry[initialCapacity][];
    }
    
    /**
     * Gets the size of the map.
     *
     * @return the size
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the map is currently empty.
     *
     * @return true if the map is currently size zero
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the map contains the specified key.
     *
     * @param key the key to search for
     * @return true if the map contains the key
     */
    public boolean contains(Object key) {
        int hashCode = hash(key);
        Entry[] entry = data[hashIndex(hashCode, data.length)]; // no local for hash index
        if (entry == null) {
        	return false;
        }
        for (int i = 0; i < entry.length && entry[i] != null; ++i) {
            if (entry[i].hashCode == hashCode && isEqualKey(key, entry[i].value)) {
                return true;
            }
        }
        return false;
    }

 
    //-----------------------------------------------------------------------
    /**
     * Puts a key-value mapping into this map.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return the value previously mapped to this key, null if none
     */
    public boolean add(T key) {
        int hashCode = hash(key);
        int index = hashIndex(hashCode, data.length);
        Entry<T>[] entry = data[index];
        if (entry == null) {
        	internalAdd(data, index, 0, new Entry<T>(key, hashCode));
        } else {
	        int i = 0;
	        for (; i < entry.length && entry[i] != null; ++i) {
	            if (entry[i].hashCode == hashCode && isEqualKey(key, entry[i].value)) {
	                return false;
	            }
	        }
	        internalAdd(data, index, i, new Entry<T>(key, hashCode));
        }
        checkCapacity();
        return true;
    }

    /**
     * Puts all the values from the specified map into this map.
     * <p/>
     * This implementation iterates around the specified map and
     * uses {@link #put(Object, Object)}.
     *
     * @param map the map to add
     * @throws NullPointerException if the map is null
     */
    public boolean addAll(Collection<? extends T> c) {
        int cSize = c.size();
        if (cSize == 0) {
            return false;
        }
        int newSize = (int) ((size + cSize) / loadFactor + 1);
        ensureCapacity(calculateNewCapacity(newSize));
        boolean addedOne = false;
        // Have to cast here because of compiler inference problems.
        for (Iterator<? extends T> it = c.iterator(); it.hasNext();) {
            T v = it.next();
            addedOne = add(v) || addedOne;
        }
        return addedOne;
    }

    /**
     * Removes the specified mapping from this map.
     *
     * @param key the mapping to remove
     * @return the value mapped to the removed key, null if key not in map
     */
    public boolean remove(Object key) {
        int hashCode = hash(key);
        int index = hashIndex(hashCode, data.length);
        Entry<T>[] entry = data[index];
        if (entry == null) {
        	return false;
        }
        for (int i = 0; i < entry.length && entry[i] != null; ++i) {
            if (entry[i].hashCode == hashCode && isEqualKey(key, entry[i].value)) {
            	--size;
            	for (int c = i+1; c < entry.length; ++c) {
            		entry[c-1] = entry[c];
            	}
            	//System.arraycopy(entry, i+1, entry, i, entry.length-i-1);
            	entry[entry.length-1] = null;
            	return true;
            }
        }
        return false;
    }

    /**
     * Clears the map, resetting the size to zero and nullifying references
     * to avoid garbage collection issues.
     */
    public void clear() {
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] = null;
        }
        size = 0;
    }

    /**
     * Gets the hash code for the key specified.
     * This implementation uses the additional hashing routine from JDK1.4.
     * Subclasses can override this to return alternate hash codes.
     *
     * @param key the key to get a hash code for
     * @return the hash code
     */
    protected int hash(Object key) {
        // same as JDK 1.4
        int h = key.hashCode();
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        h ^= (h >>> 10);
        return h;
    }

    /**
     * Compares two keys, in internal converted form, to see if they are equal.
     * This implementation uses the equals method.
     * Subclasses can override this to match differently.
     *
     * @param key1 the first key to compare passed in from outside
     * @param key2 the second key extracted from the entry via <code>entry.key</code>
     * @return true if equal
     */
    protected boolean isEqualKey(Object key1, Object key2) {
        return (key1 == key2 || ((key1 != null) && key1.equals(key2)));
    }

    /**
     * Gets the index into the data storage for the hashCode specified.
     * This implementation uses the least significant bits of the hashCode.
     * Subclasses can override this to return alternate bucketing.
     *
     * @param hashCode the hash code to use
     * @param dataSize the size of the data to pick a bucket from
     * @return the bucket index
     */
    protected int hashIndex(int hashCode, int dataSize) {
        return hashCode & (dataSize - 1);
    }

    
    //-----------------------------------------------------------------------
    /**
     * Checks the capacity of the map and enlarges it if necessary.
     * <p/>
     * This implementation uses the threshold to check if the map needs enlarging
     */
    private void checkCapacity() {
        if (size >= threshold) {
            int newCapacity = data.length * 2;
            if (newCapacity <= MAXIMUM_CAPACITY) {
                ensureCapacity(newCapacity);
            }
        }
    }

    private void internalAdd(Entry<T>[][] d, int index, int tryFrom, Entry e) {
    	if (d[index] == null) {
    		d[index] = new Entry[1];
    		d[index][0] = e;
    	} else {
	    	for (; tryFrom < d[index].length && d[index][tryFrom] != null; ++tryFrom);
	    	if (d[index].length > tryFrom) {
	    		d[index][tryFrom] = e;
	    	} else {
	        	Entry<T>[] morespace = new Entry[d[index].length<<1];
	        	System.arraycopy(d[index], 0, morespace, 0, d[index].length);
	        	morespace[d[index].length] = e;
	        	d[index] = morespace;	
	    	}
    	}
    	++size;
    }
    
    /**
     * Changes the size of the data structure to the capacity proposed.
     *
     * @param newCapacity the new capacity of the array (a power of two, less or equal to max)
     */
    private void ensureCapacity(int newCapacity) {
        int oldCapacity = data.length;
        if (newCapacity <= oldCapacity) {
            return;
        }
        if (size == 0) {
            threshold = calculateThreshold(newCapacity, loadFactor);
            data = new Entry[newCapacity][];
        } else {
            Entry<T>[][] oldEntries = data;
            Entry<T>[][] newEntries = new Entry[newCapacity][];
            for (int i = oldCapacity - 1; i >= 0; i--) {
                Entry<T>[] entry = oldEntries[i];
                if (entry != null) {
                    oldEntries[i] = null;  // gc
                    for (int j = 0; j < entry.length && entry[j] != null; ++j) {
                        int index = hashIndex(entry[j].hashCode, newCapacity);
                        internalAdd(newEntries, index, 0, entry[j]); //this changes the size
                        --size;
                    }
                }
            }
            threshold = calculateThreshold(newCapacity, loadFactor);
            data = newEntries;
        }
    }

    /**
     * Calculates the new capacity of the map.
     * This implementation normalizes the capacity to a power of two.
     *
     * @param proposedCapacity the proposed capacity
     * @return the normalized new capacity
     */
    protected int calculateNewCapacity(int proposedCapacity) {
        int newCapacity = 1;
        if (proposedCapacity > MAXIMUM_CAPACITY) {
            newCapacity = MAXIMUM_CAPACITY;
        } else {
            while (newCapacity < proposedCapacity) {
                newCapacity <<= 1;  // multiply by two
            }
            if (newCapacity > MAXIMUM_CAPACITY) {
                newCapacity = MAXIMUM_CAPACITY;
            }
        }
        return newCapacity;
    }

    /**
     * Calculates the new threshold of the map, where it will be resized.
     * This implementation uses the load factor.
     *
     * @param newCapacity the new capacity
     * @param factor      the load factor
     * @return the new resize threshold
     */
    protected int calculateThreshold(int newCapacity, float factor) {
        return (int) (newCapacity * factor);
    }
    
 

    //-----------------------------------------------------------------------
    /**
     * Clones the map without cloning the keys or values.
     * <p/>
     * To implement <code>clone()</code>, a subclass must implement the
     * <code>Cloneable</code> interface and make this method public.
     *
     * @return a shallow clone
     */
    protected Object clone() {
        try {
            ApproxRandSet<T> cloned = (ApproxRandSet<T>) super.clone();
            cloned.data = new Entry[data.length][];
            cloned.size = 0;
            cloned.addAll(this);
            return cloned;

        } catch (CloneNotSupportedException ex) {
            return null;  // should never happen
        }
    }

    //TODO: not random enough, too little weight placed on the collisions
	public T getRandom(Random r) {
		if (size == 0) {
			return null;
		}
		int index = r.nextInt(data.length);
		while(data[index] == null || data[index][0] == null) {
			index = (index + 1) % data.length;
		}
		
		int ei = data[index].length;
		do {
			ei = r.nextInt(ei);
		} while (data[index][ei] == null);
		
		return data[index][ei].value;
	}

	public Iterator<T> iterator() {
		return new RSIterator<T>(data);
	}

	protected static class RSIterator<T> implements Iterator<T> {
		RSIterator(Entry<T>[][] data) {
			this.data = data;
			index = 0;
			ei = 0;
			moveNext();
		}
		
		Entry<T>[][] data;
		int index;
		int ei;
		
		Entry<T> current;
		
		public boolean hasNext() {
			return current != null;
		}

		protected void moveNext() {
			for (; index < data.length; ++index) {
				if (data[index] != null && ei < data[index].length && data[index][ei] != null) {
					break;
				}
				ei = 0;
			}
			if (index < data.length) {
				current = data[index][ei];
			} else {
				current = null;
			}
		}
		
		public T next() {
			T v = current.value;
			++ei;
			moveNext();
			return v;
		}

		public void remove() {
			//TODO:
			throw new UnsupportedOperationException();
		}
		
	}
	
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a) {
		//TODO:
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	public boolean retainAll(Collection<?> c) {
		//TODO:
		throw new UnsupportedOperationException();
	}


	public boolean removeAll(Collection<?> c) {
		boolean oneRemoved = false;
		for (Object o : c) {
			oneRemoved = remove(o) || oneRemoved;
		}
		return oneRemoved;
	}
	

	private static class BigSlowObj {
		int[] data;
		BigSlowObj(int v) {
			data = new int[100];
			Arrays.fill(data, v);
		}
		public int hashCode() {
			int h = ATerm.mdjbFirst();
			for (int d : data) {
				h = ATerm.mdjbNext(h, d);
			}
			return h;
		}
		public boolean equals(Object o) {
			if (!(o instanceof BigSlowObj)) {
				return false;
			}
			BigSlowObj bso = (BigSlowObj)o;
			if (data.length != bso.data.length) {
				return false;
			}
			for (int i = 0; i < data.length; ++i) {
				if (data[i] != bso.data[i]) {
					return false;
				}
			}
			return true;
		}
	}
	
	//slower, but not too bad
	public static void main(String[] args) {
		HashSet<Integer> baseline = new HashSet<Integer>();
		ApproxRandSet<Integer> testme = new ApproxRandSet<Integer>(8);
		Random r = new Random();
		
		for (int i = 0; i < 10000; ++i) {
			int v = r.nextInt(1000);
			if (r.nextBoolean()) {
				if (baseline.add(v) != testme.add(v)) {
					throw new Error("add failed");
				}
			} else {
				if (baseline.remove(v) != testme.remove(v)) {
					throw new Error("remove failed");
				}
			}
			if (baseline.size() != testme.size()) {
				throw new Error("size failed");
			}
			if (!testme.containsAll(baseline)) {
				throw new Error("my contains");
			}
			if (!baseline.containsAll(testme)) {
				throw new Error("my containment");
			}
			if (r.nextInt(100) == 0) {
				testme.clear(); baseline.clear();
			}
		}
		
		ApproxRandSet<BigSlowObj> stestme = new ApproxRandSet<BigSlowObj>(8);
		int loops = 1000000;
		long n = System.nanoTime();
		for (int i = 0; i < loops; ++i) {
			int v = r.nextInt(10000);
			if (r.nextBoolean()) {
				stestme.add(new BigSlowObj(v));
			} else {
				stestme.remove(new BigSlowObj(v));
			}
		}
		System.out.println((System.nanoTime()-n)/1000 + " micros elapsed for RandSet");
		
		HashSet<BigSlowObj> sbaseline = new HashSet<BigSlowObj>();
		n = System.nanoTime();
		for (int i = 0; i < loops; ++i) {
			int v = r.nextInt(10000);
			if (r.nextBoolean()) {
				sbaseline.add(new BigSlowObj(v));
			} else {
				sbaseline.remove(new BigSlowObj(v));
			}
		}
		System.out.println((System.nanoTime()-n)/1000 + " micros elapsed for HashSet");
	}

}
