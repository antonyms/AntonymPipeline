package edu.antonym.prototype;

import java.io.File;
import java.io.FileNotFoundException;

public interface Vocabulary {
	public int size();
	public int OOVindex();
	public String lookupIndex(int index);
	public int lookupWord(String word);
	public int getVocabSize();
}
