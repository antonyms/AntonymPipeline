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
	public int numSynonyms();
	public int numAntonyms();
	public Entry getEntry(int entryn);
	
	public int lookupEntry(int word1, int word2, boolean isAnt);

	
	public boolean isAntonym(int word1, int word2);
	public boolean isSynonym(int word1, int word2);
}
