package com.ibm.bluej.util.common;

import java.util.*;

/**
 * HashMap Utilities
 * 
 * @author partha
 * 
 */
public class HashMapUtil {

  /** adds a Key-Value pair to a HashMap of HashSets
   * @param map
   * @param key
   * @param value
   * @return
   */
  public static <K, V> boolean addHS(Map<K, HashSet<V>> map, K key, V value) {
    HashSet<V> hs = map.get(key);
    if (hs == null) {
      hs = new HashSet<V>();
      hs.add(value);
      map.put(key, hs);
    } else {
      hs.add(value);
    }
    return true;
  }

  /** adds a Key-Value pair to a HashMap of HashSets
   * @param map
   * @param key
   * @param value
   * @return
   */
  public static <K1, K2, V> boolean addHS(Map<K1, HashMap<K2, HashSet<V>>> map, K1 key1, K2 key2,
          V value) {
	HashMap<K2, HashSet<V>> h = map.get(key1);
    if (h == null) {
      h = new HashMap<K2, HashSet<V>>();
      map.put(key1, h);
    }
    return addHS(h, key2, value);
  } 
  
  /**
   * Adds an entry to an ArrayList which is an entry in a HashMap with a key of type K2, which is a value in a Map with a key of type K1
   * @param map
   * @param key1
   * @param key2
   * @param value
   */
  public static <K1, K2, V> void addAL(Map<K1, HashMap<K2, ArrayList<V>>> map, K1 key1, K2 key2,
          V value) {
    HashMap<K2, ArrayList<V>> h = map.get(key1);
    if (h == null) {
      ArrayList<V> al = new ArrayList<V>();
      al.add(value);
      h = new HashMap<K2, ArrayList<V>>();
      h.put(key2, al);
      map.put(key1, h);
    } else {
      ArrayList<V> al = h.get(key2);
      if (al == null) {
        al = new ArrayList<V>();
        al.add(value);
        h.put(key2, al);
      } else {
        al.add(value);
      }
    }
  }
  
  /**
   * Adds an entry to an ArrayList which is an entry in a Map with a key of type K
   * @param map
   * @param key
   * @param value
   */
  public static <K, V> void addAL(Map<K, ArrayList<V>> map, K key, V value) {
    ArrayList<V> al = map.get(key);
    if (al == null) {
      al = new ArrayList<V>();
    }
    al.add(value);
    map.put(key, al);
  }

	public static <K, V> void addAL(Map<K, ArrayList<V>> map, Map<K,V> toAdd) {
		for (Map.Entry<K, V> e : toAdd.entrySet()) {
			addAL(map,e.getKey(),e.getValue());
		}
	}
  public static <K1, K2, V, M extends Map<K2,V>> void addAL2(Map<K1, HashMap<K2, ArrayList<V>>> map, Map<K1, M> toAdd) {
		for (Map.Entry<K1, M> e : toAdd.entrySet()) {
			K1 key1 = e.getKey();
			HashMap<K2, ArrayList<V>> m2 = map.get(key1);
			if (m2 == null) {
				m2 = new HashMap<K2, ArrayList<V>>();
				map.put(key1, m2);
			}
			addAL(m2, e.getValue());
		}
	}
  
  /**
   * Puts a value to a HashMap against an key of type K2, which itself a value of a HashMap with the key of type K1 
   * @param map
   * @param key1
   * @param key2
   * @param value
   */
  public static <K1, K2, V> V put2(Map<K1, HashMap<K2, V>> map, K1 key1, K2 key2, V value){
    HashMap<K2, V> h = map.get(key1);
    if (h == null){
      h = new HashMap<K2, V>();
      map.put(key1, h);
    }
    return h.put(key2, value);
  }

  /**
   * Reverses keys and values of HashMap that has HashSet as values 
   * @param in - a HashMap of HashSet
   * @return out - a HashMap of HashSet with key-values reversed 
   */
  public static <K, V> HashMap<V, HashSet<K>> reverseHS(Map<K, HashSet<V>> in) {
    HashMap<V, HashSet<K>> out = new HashMap<V, HashSet<K>>();
    for (Map.Entry<K, HashSet<V>> e : in.entrySet()) {
      for (V v : e.getValue()) {
        addHS(out, v, e.getKey());
      }
    }
    return out;
  }
  
  
  /**
   * Reverses keys and values of HashMap that has ArrayList as value 
   * @param in - a HashMap of ArrayList
   * @return out - a HashMap of ArrayList with key-values reversed 
   */
  public static <K, V> HashMap<V, ArrayList<K>> reverseAL(Map<K, ArrayList<V>> in) {
    HashMap<V, ArrayList<K>> out = new HashMap<V, ArrayList<K>>();
    for (Map.Entry<K, ArrayList<V>> e : in.entrySet()) {
      ArrayList<V> av = e.getValue();
      for (V v : av) {
        addAL(out, v, e.getKey());
      }
    }
    return out;
  }

  /**
   * Reverses keys and values of a HashMap
   * @param in - a HashMap
   * @return out - a HashMap with key-values reversed
   */
  public static <K, V> HashMap<V, K> reverse(Map<K, V> in) {
    HashMap<V, K> hm = new HashMap<V, K>();
    for (Map.Entry<K, V> e : in.entrySet()) {
      hm.put(e.getValue(), e.getKey());
    }
    return hm;
  }
  
	public static <K, V> HashMap<V, ArrayList<K>> reverseDuplicateValues(Map<K,V> in) {
		HashMap<V, ArrayList<K>> rev = new HashMap<V, ArrayList<K>>();
		for (Map.Entry<K, V> entry : in.entrySet()) {
			addAL(rev, entry.getValue(), entry.getKey());
		}
		return rev;
	}
	public static <K1, K2, V, M extends Map<K2,V>> HashMap<K2, HashMap<K1, V>> reverseDouble(Map<K1, M> dblMap) {
		HashMap<K2, HashMap<K1, V>> revDblMap = new HashMap<K2, HashMap<K1, V>>();
		for (Map.Entry<K1, M> e1 : dblMap.entrySet()) {
			for (Map.Entry<K2, V> e2 : e1.getValue().entrySet()) {
				HashMapUtil.put2(revDblMap, e2.getKey(), e1.getKey(), e2.getValue());
			}
		}
		return revDblMap;
	}  
	public static <K, V> HashMap<K,V> fromPairs(Pair<K,V>[] pairs) {
		HashMap<K,V> map = new HashMap<K,V>();
		for (Pair<K,V> p : pairs) {
			map.put(p.first, p.second);
		}
		return map;
	}

	public static <K, V> HashMap<K,V> fromPairs(Iterable<Pair<K,V>> pairs) {
		HashMap<K,V> map = new HashMap<K,V>();
		for (Pair<K,V> p : pairs) {
			map.put(p.first, p.second);
		}
		return map;
	}
	
	public static <K,V> String toString(Map<K,V> map) {
		if (map == null)
			return "null";
		StringBuilder buf = new StringBuilder();
		int longestKey = 0;
		ArrayList<Pair<String,String>> pairs = new ArrayList<Pair<String,String>>();
		for (Map.Entry<K, V> e : map.entrySet()) {
			String kstr = e.getKey().toString();
			pairs.add(Pair.of(kstr, e.getValue().toString()));
			longestKey = Math.max(longestKey, kstr.length());
		}
		Collections.sort(pairs, new FirstPairComparator());
		for (Pair<String,String> p : pairs) {
			buf.append(Lang.LPAD(p.first, longestKey+1)+" -> "+p.second+"\n");
		}
		return buf.toString();
	}
	
	public static <K> int getIndex(Map<K,Integer> k2ndx, K key) {
		Integer ndx = k2ndx.get(key);
		if (ndx == null) {
			ndx = k2ndx.size();
			k2ndx.put(key, ndx);
		}
		return ndx;
	}
	
	public static <K> void retainAll(Map<K,?> map, Collection<K> set) {
		ArrayList<K> toRemove = new ArrayList<K>();
		for (K k : map.keySet()) {
			if (!set.contains(k)) {
				toRemove.add(k);
			}
		}
		for (K r : toRemove) {
			map.remove(r);
		}
	}
	
	public static <K> void removeAll(Map<K,?> map, Collection<K> set) {
		for (K r : set) {
			map.remove(r);
		}
	}
	
	public static <K,V> ArrayList<Pair<K,V>> toPairs(Map<K,V> map) {
		ArrayList<Pair<K,V>> list = new ArrayList<Pair<K,V>>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			list.add(Pair.of(entry.getKey(), entry.getValue()));
		}
		return list;
	}	
}


