package edu.antonym;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.antonym.prototype.Vocabulary;

public class SimpleVocab implements Vocabulary {

	List<String> words;
	Map<String, Integer> ids;
	
	int OOVindex;
	

	public SimpleVocab(File f) throws FileNotFoundException {
		this(f,0);
	}
	public SimpleVocab(InputStream stream) {
		this(stream,0);
	}
	public SimpleVocab(File f,int OOVindex) throws FileNotFoundException {
		this(new FileInputStream(f),OOVindex);
	}
	/**
	 * Creates vocabulary from newline delimited text stream
	 * @param stream
	 */
	
	public SimpleVocab(InputStream stream, int OOVindex) {
		Scanner scan=new Scanner(stream);
		int index=0;
		while(scan.hasNextLine()) {
			String word=scan.nextLine();
			words.add(word);
			ids.put(word,index++);
		}
		scan.close();
		this.OOVindex=OOVindex;
	}
	
	@Override
	public int size() {
		return words.size();
	}
/**
 * Returns the index in the dictionary for out-of-vocabulary words.
 */
	@Override
	public int OOVindex() {
		return OOVindex;
	}

	@Override
	public String lookupIndex(int index) {
		return words.get(index);
	}

	@Override
	public int lookupWord(String word) {
		return ids.get(word);
	}

}
