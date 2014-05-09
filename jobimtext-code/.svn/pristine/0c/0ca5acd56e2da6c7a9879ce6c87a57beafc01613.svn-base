package org.jobimtext.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class DtEntryIterator implements Iterable<List<String>> {
	private List<String> list;
	private String prev = "";
	private BufferedReader reader;
	private int maxEntries;
	private boolean addFeatures=false;
	private Map<String, Double> wordCount;
	String encoding = "UTF-8";
	private boolean balanced;
	
	public DtEntryIterator(String dtFilename, int maxEntries,Map<String, Double> wordsToUse, boolean gzip,
			boolean addFeatures, boolean balanced) throws IOException {
		this.wordCount = wordsToUse;
		this.maxEntries = maxEntries;
		this.addFeatures=addFeatures;
		this.balanced = balanced;
		Reader decoder;
		if (gzip) {
			InputStream fileStream = new FileInputStream(new File(dtFilename));
			InputStream gzipStream = new GZIPInputStream(fileStream);
			decoder = new InputStreamReader(gzipStream, encoding);
		} else {
			decoder = new FileReader(new File(dtFilename));
		}
		reader = new BufferedReader(decoder);
	}

	@Override
	public Iterator<List<String>> iterator() {
		return new Iterator<List<String>>() {

			@Override
			public boolean hasNext() {

				if (reader == null)
					return false;

				try {
					return fillList();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;

			}
			private void addToList(String[] line){
				list.add(line[1]);
				if(addFeatures){
					try{
					String[] features = line[3].split(":::");
					for(String f :features){
						if(balanced)list.add(line[1]);
						list.add(f);
					}
					}catch(NullPointerException e){
						System.err.println("The file does not contain 4 columns to extract features ("+Arrays.toString(line)+") format: word1 word2 score feat1 feat2 feat...");
					}
				}
			}
			private boolean fillList() throws IOException {
				String line;
				if (list != null && list.size() > 0) {
					return true;
				}
				while ((line = reader.readLine()) != null) {
					String[] ls = line.split("\t");
					if (ls.length < 3)
						continue;
					
					String word = ls[0];

					if (!prev.equals(word)) {

						if (wordCount.containsKey(word)) {
							list = new ArrayList<String>();
							addToList(ls);
							prev = word;
							return true;
						}
					}
					prev = word;
				}
				return false;
			}

			@Override
			public List<String> next() {

				String line = "";

				try {
					while ((line = reader.readLine()) != null) {
						String[] ls = line.split("\t");
						if (ls.length < 3)
							continue;
						
						String word = ls[0];
						if (!prev.equals(word)) {
							List<String> prevList = list;
							list = new ArrayList<String>();
							if (wordCount.containsKey(word)) {
								addToList(ls);
							}
							if (prevList != null && prevList.size() > 0) {
								prev = word;
								return prevList;
							}
						} else {
							if (list == null)
								continue;
							if (list.size() < (maxEntries)) {
								addToList(ls);

							} else {
								wordCount.remove(word);
								List<String> prevList = list;
								list = null;
								return prevList;
							}
						}
						prev = word;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				List<String> prevList = list;
				list = null;
				return prevList;
			}

			@Override
			public void remove() {
			}
		};
	}

}
