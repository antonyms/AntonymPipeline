package com.ibm.sai.distributional_similarity.api.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.sorting.IndirectComparator;
import com.carrotsearch.hppc.sorting.IndirectSort;

public class DSPreProcessor {

  public static void main(String args[]) {
    
    String thePath = args[0], conPath = args[1], outPath = args[3];
    int max = Integer.parseInt(args[2]);
    
    DSDictionary dict = new DSDictionary();   
    
    IntObjectOpenHashMap<IntArrayList> thesaurus = 
        new IntObjectOpenHashMap<IntArrayList>();
    
    IntObjectOpenHashMap<IntArrayList> context = 
        new IntObjectOpenHashMap<IntArrayList>();
    
    TableReader simReader = new SimTableReader();
    simReader.loadMapFromFileOrDir(thePath, dict, thesaurus, -1, "*");
    
    TableReader conReader = new SimTableReader();
    conReader.loadMapFromFileOrDir(conPath, dict, context, -1, "*");
    
    try {
      writeIntStringMap(dict.intToTerm, outPath+"/dictionary.dca");
      writeIntIntArray(thesaurus, outPath+"/thesaurus.dca", max);
      writeIntIntArray(context, outPath+"/context.dca", max); 
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  private static void writeIntStringMap(IntObjectOpenHashMap<String> map, 
      String outPath) throws IOException {
    
    BufferedWriter out = new BufferedWriter(new FileWriter(outPath));    
    int [] keys = map.keys;
    Object [] values = map.values;
    boolean [] states = map.allocated;
    for (int i = 0; i < states.length; i++) {
      if (states[i]) {
        String term = (String)values[i];
        out.write(keys[i] + "\t" + term + "\n");
      }
    }
    out.close();
  }
  
  private static void writeIntIntArray(IntObjectOpenHashMap<IntArrayList> map, 
      String outPath, int max) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(outPath));
    int keys[] = map.keys;
    Object[] values = map.values;
    boolean [] states = map.allocated;
    for (int i = 0; i < states.length; i++) {
      if (states[i]) {
        out.write(Integer.toString(keys[i]));
        IntArrayList list = (IntArrayList)values[i];
        int [] buffer = list.buffer;
        int size = list.size();        
        if (size <= max*2) {
          for (int j = 0; j < size; j+=2) {
            int term = buffer[j];
            int score = buffer[j+1];
            out.write("\t"+term+"\t"+((double)score));
          }
        }
        else { // sort and prune          
          IntArrayList terms = new IntArrayList();
          IntArrayList scores = new IntArrayList();
          for (int j = 0; j < size; j+=2) {
            terms.add(buffer[j]);
            scores.add(buffer[j+1]);       
          }          
          int[] si = IndirectSort.mergesort(0, scores.size(), 
              new IndirectComparator.DescendingIntComparator(scores.buffer));            
          for (int j = 0; j < max; j++) {
            int term = terms.buffer[si[j]];
            int score = scores.buffer[si[j]];
            out.write("\t"+term+"\t"+((double)score));
          }          
        }
        out.write("\n");
    
      }
    }
    out.close();
  }
}
