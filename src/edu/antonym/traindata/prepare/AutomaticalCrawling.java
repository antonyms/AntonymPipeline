package edu.antonym.traindata.prepare;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.antonym.prototype.Vocabulary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;

public class AutomaticalCrawling extends WordNetHelper{

	
	
	
	@Override
	public Vocabulary getVocab() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numEntries() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Entry getEntry(int entryn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAntonyms(int word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getSynonyms(int word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAntonym(int word1, int word2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSynonym(int word1, int word2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void traverseWords(IDictionary dict) {
		// TODO Auto-generated method stub
		List<String> antonymList = new ArrayList<String>();
		List<String> synonymList = new ArrayList<String>();
		String vocabularyFilePath = "data/Rogets/vocabulary.txt";
		String synonymFilePath = "data/Rogets/synonym.txt";
		String antonymFilePath = "data/Rogets/antonym.txt";
		String wordnetVocab = "data/Rogets/WordNetVocab.txt";

		try {
			FileWriter vocabularyFileWriter = new FileWriter(vocabularyFilePath, false);
			PrintWriter vocabularyPrintWriter = new PrintWriter(vocabularyFileWriter);
			
			FileWriter wordNetWordsFileWriter = new FileWriter(wordnetVocab, false);
			PrintWriter wordNetWordsPrintWriter = new PrintWriter(wordNetWordsFileWriter);

			FileWriter synonymFileWriter = new FileWriter(synonymFilePath, false);
			PrintWriter synonymPrintWriter = new PrintWriter(synonymFileWriter);

			FileWriter antonymFileWriter = new FileWriter(antonymFilePath, false);
			PrintWriter antonymPrintWriter = new PrintWriter(antonymFileWriter);

			Set<String> words = new HashSet<String>();
			Set<String> wordNetWords = new HashSet<String>();
			int count = 1;
			for (POS pos : POS.values()) {						
				Iterator<?> iterator = dict.getIndexWordIterator(pos);
				while (iterator.hasNext()) {
					//System.out.print('.');
					if (count++ % 100 == 0) {
						synonymPrintWriter.flush();		
						antonymPrintWriter.flush();
					}
					IIndexWord iIndexWord = (IIndexWord) iterator.next();
					String target = iIndexWord.getLemma();
					wordNetWords.add(target);
					if(target.matches("[^/]+") && !words.contains(target)) {						
						words.add(target);	
						System.out.println(target);
						ThesaurusParser parser = new ThesaurusParser(target.replaceAll("_", " "));
						antonymList = parser.antonymsParse();
						synonymList = parser.synonymsParse();
						
						if (!antonymList.isEmpty()) {
							antonymPrintWriter.print(target + "\t");
							for(String a : antonymList) {
								words.add(a.replaceAll(" ", "_"));
								antonymPrintWriter.print(a + "\t");
							}
							antonymPrintWriter.println();
						}
						if (!synonymList.isEmpty()) {
							synonymPrintWriter.print(target + "\t");
							for(String s : synonymList) {
								words.add(s.replaceAll(" ", "_"));
								synonymPrintWriter.print(s + "\t");
							}
							synonymPrintWriter.println();
						}
					}
				}
			}
			for (String w : words) {
				vocabularyPrintWriter.println(w);
			}
			for (String w : wordNetWords) {
				wordNetWordsPrintWriter.println(w);
			}
			vocabularyFileWriter.close();
			vocabularyPrintWriter.close();
			wordNetWordsFileWriter.close();
			wordNetWordsPrintWriter.close();
			synonymFileWriter.close();
			synonymPrintWriter.close();
			antonymFileWriter.close();
			antonymPrintWriter.close();

		} catch(Exception e) {
			e.getMessage();
		}

	}

}
