package edu.antonym.prototype;

import java.util.List;

public interface Thesaurus {
	public interface Entry {
		public int word1();
		public int word2();
		public boolean isAntonym();
	}
	public Vocabulary getVocab();
	
	//Returns the words included in the Thesaurus
	public int numEntries();
	public Entry getEntry(int entryn);
	
	public List<Integer> getAntonyms(int word);
	public List<Integer> getSynonyms(int word);
	
	public boolean isAntonym(int word1, int word2);
	public boolean isSynonym(int word1, int word2);
}
