package org.jobimtext.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jobimtext.api.map.DcaLightThesaurusMap;
import org.jobimtext.api.map.IThesaurusMap;


public class JoBimDemoCommandline {
	
	
		IThesaurusMap<String, String> dt;
	public JoBimDemoCommandline(String path1, String path2, String path3) {
	  try {
      dt = new DcaLightThesaurusMap(path1, path2, path3, 200);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

	public Map<String,Double> getSimilarTerms(String term) {
		return dt.getSimilarTerms(term);
	}
	
	public Map<Integer, List<String>> getSenseCluster(String term) {
	  return dt.getSenses(term);
	}
	
	private static void printMap(Map<String,Double> m, String filter) {
	  if (m.size() == 0) {
	    System.out.println("entry not found\n");
	    return;
	  }
	  List<Map.Entry<String, Double>> list = 
	      new ArrayList<Map.Entry<String, Double>>();
	  for (Map.Entry<String, Double> entry : m.entrySet()) {
	    list.add(entry);
	  }
	  Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
      public int compare(Map.Entry<String, Double> o1, 
          Map.Entry<String, Double> o2) {
        Double o1e = o1.getValue(); Double o2e = o2.getValue();
        if (o1e > o2e) return -1; if (o1e < o2e) return 1;
        return 0;
      }	    
	  });
		for (Map.Entry<String, Double> e : list) {
		  if (filter != null && filter.equals(e.getKey())) continue;
		    System.out.println(e.getKey()+"\t"+e.getValue());
		}
		System.out.println(" ");
	}
	
	private static void printMap(Map<Integer, List<String>> map) {
	  if (map.size() == 0) {
	    System.out.println("entry not found\n");
	    return;
	  }
	  int index = 0;
	  StringBuffer sb = new StringBuffer();
	  for (int key : map.keySet()) {
	    List<String> l = map.get(key);
	    sb.append((index++)+": ");
	    for (int j=0; j<l.size()-1; j++) {
	      sb.append(l.get(j)+", ");
	    }	    
	    sb.append(l.get(l.size()-1)+"\n");
	  }
	  System.out.println(sb.toString()+"\n");
	}
	
	private static String CMD_QUIT = "q";
	private static String CMD_SIMTERM = "t";
	private static String CMD_SENSE = "s";
	
	private void runCmdLoop() {
	  System.out.print("\n\ntype a command (t, s or q):\n\n");
	  Scanner scanner = new Scanner(System.in);
	  String cmd = ""; String[] args = null;
	  while(!CMD_QUIT.equals(cmd)) {
	    if (args != null && args.length > 1) {
	      String arg = ""; // stupid! untokenize
	      for (int i=1; i<args.length; i++) {
	        arg += args[i];
	        if (i < args.length-1) 
	          arg+=" ";
	      }
        if (CMD_SIMTERM.equals(cmd)) {
          printMap(getSimilarTerms(arg), arg);  
        }
        else if (CMD_SENSE.equals(cmd)) {
          printMap(getSenseCluster(arg));  
        }
	      else {
	        System.out.println("unrecognized command "+cmd);
	      }
	    }
	    args = scanner.nextLine().toLowerCase().split(" ");
	    cmd = args[0];
	  }
	}
	
	public static void main(String[] args) {
	  String path1 = args[0], path2 = args[1], path3 = args[2];
		JoBimDemoCommandline api = new JoBimDemoCommandline(path1, path2, path3);
		api.runCmdLoop();				
	}
}
