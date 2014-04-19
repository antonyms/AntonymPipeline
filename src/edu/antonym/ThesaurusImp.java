package edu.antonym;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Vocabulary;

public class ThesaurusImp implements Thesaurus{

	List<String> antonyms;
	List<String> synonyms;
	static List<Integer> antonymsId;
	static List<Integer> synonymsId;
	static Map<Integer, List<Integer>> lookUpAntonym;
	static Map<Integer, List<Integer>> lookUpSynonym;
	SimpleVocab vocab;
	/**
	 * 
	 * @param antonymsFile the file of antonyms
	 * @param synonymsFile the file of synonyms
	 * @throws FileNotFoundException
	 */

	public ThesaurusImp(File antonymsFile, File synonymsFile ) throws FileNotFoundException {
		this(new FileInputStream(antonymsFile), new FileInputStream(synonymsFile));
	}
	/**
	 * Creates thesaurus lookup map from newline delimited text stream
	 * @param stream
	 * @throws FileNotFoundException 
	 */
	
	public ThesaurusImp(InputStream antonymStream, InputStream synonymStream) throws FileNotFoundException {
		antonyms = new ArrayList<String>();
		synonyms = new ArrayList<String>();
		antonymsId = new ArrayList<Integer>();
		synonymsId = new ArrayList<Integer>();
		lookUpAntonym = new HashMap<Integer, List<Integer>>();
		lookUpSynonym = new HashMap<Integer, List<Integer>>();
		File file = new File("vocabulary.txt");
		vocab = new SimpleVocab(file);
		Scanner scanAntonyms=new Scanner(antonymStream);
		int antonymsIndex=0;
		while(scanAntonyms.hasNextLine()) {
			String words=scanAntonyms.nextLine();
			antonyms.addAll(Arrays.asList(words.split(" ")));
			for(String a : antonyms) {
				if(a != null) {
					antonymsId.add(vocab.lookupWord(a));
				}
				
			}
			
			lookUpAntonym.put(antonymsIndex, antonymsId);
			antonymsIndex ++;
			antonyms.clear();
		}
		scanAntonyms.close();
		
		
		Scanner scanSynonyms=new Scanner(synonymStream);
		int synonymIndex=0;
		while(scanSynonyms.hasNextLine()) {
			String words=scanSynonyms.nextLine();
			synonyms.addAll(Arrays.asList(words.split(" ")));
			for(String s : antonyms) {
				if(s != null) {
					antonymsId.add(vocab.lookupWord(s));
				}
			}
			lookUpSynonym.put(synonymIndex, synonymsId);
			synonymIndex ++;
			synonyms.clear();
		}
		scanSynonyms.close();
		
	}
	
	@Override
	public List<Integer> getAntonyms(int word) {
		// TODO Auto-generated method stub
		return antonymsId;
	}

	@Override
	public List<Integer> getSynonyms(int word) {
		// TODO Auto-generated method stub
		return synonymsId;
	}

	@Override
	public boolean isAntonym(int word1, int word2) {
		// TODO Auto-generated method stub
		if(lookUpAntonym.containsKey(word1)) {
			for(Integer word : lookUpAntonym.get(word1)) {
				if(word == word2) {
					return true;
				}
			}
		}
		if(lookUpAntonym.containsKey(word2)) {
			for(Integer word : lookUpAntonym.get(word2)) {
				if(word == word1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isSynonym(int word1, int word2) {
		// TODO Auto-generated method stub
		if(lookUpSynonym.containsKey(word1)) {
			for(Integer word : lookUpSynonym.get(word1)) {
				if(word == word2) {
					return true;
				}
			}
		}
		if(lookUpSynonym.containsKey(word2)) {
			for(Integer word : lookUpSynonym.get(word2)) {
				if(word == word1) {
					return true;
				}
			}
		}
		return false;
	}
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

}
