package edu.antonym.traindata.prepare;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	public int getEntry(int entryn) {
		// TODO Auto-generated method stub
		return 0;
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
		String vocabularyFilePath = "vocabulary1.txt";
		String synonymFilePath = "synonym1.txt";
		String antonymFilePath = "antonym1.txt";

		try {
			FileWriter vocabularyFileWriter = new FileWriter(vocabularyFilePath, true);
			PrintWriter vocabularyPrintWriter = new PrintWriter(vocabularyFileWriter);

			FileWriter synonymFileWriter = new FileWriter(synonymFilePath, true);
			PrintWriter synonymPrintWriter = new PrintWriter(synonymFileWriter);

			FileWriter antonymFileWriter = new FileWriter(antonymFilePath, true);
			PrintWriter antonymPrintWriter = new PrintWriter(antonymFileWriter);

			for (POS pos : POS.values()) {
				Iterator<?> iterator = dict.getIndexWordIterator(pos);
				while (iterator.hasNext()) {
					IIndexWord iIndexWord = (IIndexWord) iterator.next();
					if(iIndexWord.getLemma().matches("[a-zA-Z]+")) {
						ThesaurusParser parser = new ThesaurusParser(iIndexWord.getLemma());
						antonymList = parser.antonymsParse();
						synonymList = parser.synonymsParse();

						vocabularyPrintWriter.println(iIndexWord.getLemma());
						for(String a : antonymList) {
							antonymPrintWriter.print(a + " ");
						}
						antonymPrintWriter.println();

						for(String s : synonymList) {
							synonymPrintWriter.print(s + " ");
						}
						synonymPrintWriter.println();
					}
				}
			}
			vocabularyFileWriter.close();
			vocabularyPrintWriter.close();
			synonymFileWriter.close();
			synonymPrintWriter.close();
			antonymFileWriter.close();
			antonymPrintWriter.close();

		} catch(Exception e) {
			e.getMessage();
		}




	}

}
