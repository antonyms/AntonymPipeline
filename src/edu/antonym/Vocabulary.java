package edu.antonym;

public interface Vocabulary {
	public int OOVindex();
	public String lookupIndex(int index);
	public int lookupWord(String word);
}
