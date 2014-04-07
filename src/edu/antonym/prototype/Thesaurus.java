package edu.antonym.prototype;

import java.util.List;

public interface Thesaurus {
	
	public List<Integer> getAntonyms(int word);
	public List<Integer> getSynonyms(int word);
	
	public boolean isAntonym(int word1, int word2);
	public boolean isSynonym(int word1, int word2);
}
