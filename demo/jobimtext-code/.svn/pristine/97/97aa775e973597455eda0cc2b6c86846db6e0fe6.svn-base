package com.ibm.sai.distributional_similarity.api.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;

public class DSDictionary {

  IntObjectOpenHashMap<String> intToTerm;
  ObjectIntOpenHashMap<String> termToInt;
  int size = 1;
  
  public DSDictionary() {
    intToTerm = new IntObjectOpenHashMap<String>();
    termToInt = new ObjectIntOpenHashMap<String>();
  }

  public int getIndex(String term) {
    return termToInt.get(term);
  }
  
  public String getTerm(int index) {
    return intToTerm.get(index);
  }
  
  public int addTerm(String term) {
    if (!termToInt.containsKey(term)) {
      termToInt.put(term, size);
      intToTerm.put(size, term);
      size++;
      if (size == Integer.MAX_VALUE) {
        throw new IllegalStateException();
      }
    }
    return termToInt.get(term);
  }
  
  public void putTerm(int index, String term) {
    termToInt.put(term, index);
    intToTerm.put(index, term);
  }

  public int getSize() {
    return size;
  }
  
  public static DSDictionary create(String file) {
    DSDictionary dict = new DSDictionary();
    try {
      String line;
      BufferedReader reader = new BufferedReader(new FileReader(file));
      int num = 0;
      while ((line = reader.readLine()) != null) {
    	  ++num;
        if (dict.size % 1000000 == 0) {
          System.out.println("Dictionary read "+dict.intToTerm.size()+" terms");
        }
        String[] tokens = line.split("\t");
        if ( 2 <= tokens.length ) { 
        	dict.putTerm(Integer.parseInt(tokens[0]), tokens[1]);
        } else { 
        	System.out.println(file + " line " + num + ": two tab-separated tokens not found in \"" + line + "\"");
        }
      }
      reader.close();
      System.out.println("Dictionary read "+dict.intToTerm.size()+" terms");
    } catch (IOException e) {
      e.printStackTrace();
    }
    dict.size = dict.termToInt.size();
    
    return dict;
  }
}
