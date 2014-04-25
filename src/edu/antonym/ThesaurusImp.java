package edu.antonym;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Vocabulary;

public class ThesaurusImp implements Thesaurus{

	Map<Integer, List<Integer>> lookUpAntonym;
	Map<Integer, List<Integer>> lookUpSynonym;
	List<Integer> entries;
	Vocabulary vocab;
	/**
	 * 
	 * @param antonymsFile the file of antonyms
	 * @param synonymsFile the file of synonyms
	 * @throws FileNotFoundException
	 */

	public ThesaurusImp(File antonymsFile, File synonymsFile, File vocabFile) throws FileNotFoundException {
		this(new FileInputStream(antonymsFile), new FileInputStream(synonymsFile), vocabFile);
	}
	/**
	 * Creates thesaurus lookup map from newline delimited text stream
	 * @param stream
	 * @throws FileNotFoundException 
	 */
	
	public ThesaurusImp(InputStream antonymStream, InputStream synonymStream, File vocabFile) throws FileNotFoundException {
		Set<Integer> entries = new HashSet<Integer>();
		lookUpAntonym = new HashMap<Integer, List<Integer>>();
		lookUpSynonym = new HashMap<Integer, List<Integer>>();		
		vocab = new SimpleVocab(vocabFile);
		Scanner scanAntonyms=new Scanner(antonymStream);
		while(scanAntonyms.hasNextLine()) {
			String words=scanAntonyms.nextLine();
			List<String> antonyms = Arrays.asList(words.split("\\s"));;
			int target = vocab.lookupWord(antonyms.get(0));
			entries.add(target);
			antonyms = antonyms.subList(1, antonyms.size());
			List<Integer> antonymsId = new ArrayList<Integer>();			
			for(String a : antonyms) {
				if(a != null) {
					antonymsId.add(vocab.lookupWord(a));
				}				
			}			
			lookUpAntonym.put(target, antonymsId);
		}
		scanAntonyms.close();
		
		
		Scanner scanSynonyms=new Scanner(synonymStream);
		while(scanSynonyms.hasNextLine()) {
			String words=scanSynonyms.nextLine();
			List<String> synonyms = Arrays.asList(words.split("\\s"));;
			int target = vocab.lookupWord(synonyms.get(0));
			entries.add(target);
			synonyms = synonyms.subList(1, synonyms.size());
			List<Integer> synonymsId = new ArrayList<Integer>();
			for(String s : synonyms) {
				if(s != null) {
					synonymsId.add(vocab.lookupWord(s));
				}
			}
			lookUpSynonym.put(target, synonymsId);
		}
		scanSynonyms.close();
		
		this.entries = new ArrayList<Integer>(entries);
	}
	
	@Override
	public List<Integer> getAntonyms(int word) {
		List<Integer> res=lookUpAntonym.get(word);
		if(res == null) {
			return new ArrayList<Integer>();
		}
		return res;
	}

	@Override
	public List<Integer> getSynonyms(int word) {
		List<Integer> res=lookUpSynonym.get(word);
		if(res == null) {
			return new ArrayList<Integer>();
		}
		return res;
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
		return vocab;
	}
	@Override
	public int numEntries() {
		return entries.size();
	}
	@Override
	public int getEntry(int entryn) {
		return entries.get(entryn);
	}

}
