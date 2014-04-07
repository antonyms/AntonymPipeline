package edu.antonym.prototype;

public interface Vocabulary {
	public int size();
	public int OOVindex();
	public String lookupIndex(int index);
	public int lookupWord(String word);
}
