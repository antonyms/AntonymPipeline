package edu.antonym.traindata.prepare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

/**
 * Helper class for wordnet, provide static method to query word's antonyms and synonyms
 * include traversal method can traverse the whole wordnet dictionary and get each word's 
 * antonyms and synonyms.
 * @author Terrence
 *
 */
public class WordNetHelper {
/**
 * Pass in the dictionary variable, and the word being searched, 
 * return the list of the synonyms of the word
 * @param dict
 * @param word
 * @return
 */
	public static List<Map<String, List<String>>> wordNetSynonymsList;
	public static List<Map<String, List<String>>> wordNetAntonymsList;
	public static List<String> getSynonyms(IDictionary dict, String word) {
		List<String> synonymList = new ArrayList<String>();
		//emulate all the pos types, noun, adj, etc.
		for(POS pos : POS.values()){		
			IIndexWord iIndexWord = dict.getIndexWord(word, pos);
			if(iIndexWord != null) {
				//iterate all the words has the meaning of the word passed in
				for(int i = 0; i < iIndexWord.getWordIDs().size(); i++) {
					IWordID wordID = iIndexWord.getWordIDs().get(i);
					IWord w = dict.getWord(wordID);
					ISynset synset = w.getSynset();
					
					for(IWord iWord : synset.getWords()) {
						if(!synonymList.contains(iWord.getLemma())) {
							synonymList.add(iWord.getLemma());
						}
					}	
				}
			}
		}
		return synonymList;
	}
	/**
	 * Pass in the dictionary objct and the word want to search
	 * it will traverse all the pos of the word form, query all the 
	 * synonyms for the words, save to a list, save the lists to the
	 * hashmap and return.
	 * @param dict
	 * @param word
	 * @return
	 */
	public static Map<String, List<String>> getSynonymsMap(IDictionary dict, String word) {
		Map<String, List<String>> synonymsMap = new HashMap<String, List<String>>();
		//enumerate all the pos types, such as noun, adj, etc.
		for(POS pos : POS.values()){		
			IIndexWord iIndexWord = dict.getIndexWord(word, pos);
			if(iIndexWord != null) {
				//iterate all the words has the meaning of the word passed in
				for(int i = 0; i < iIndexWord.getWordIDs().size(); i++) {
					IWordID wordID = iIndexWord.getWordIDs().get(i);
					IWord w = dict.getWord(wordID);
					ISynset synset = w.getSynset();
					List<String> synonymList = new ArrayList<String>();
					
					for(IWord iWord : synset.getWords()) {
						//save the word into list as long as it doesnt exist in the list 
						//and does not equal to the word being searched
						if(!synonymList.contains(iWord.getLemma()) && !iWord.getLemma().equalsIgnoreCase(w.getLemma())) {
							synonymList.add(iWord.getLemma());
						}
					}
					if(!synonymList.isEmpty()) {
						synonymsMap.put(w.getLemma(), synonymList);
					}
				}
			}
		}
		return synonymsMap;
	}
	
	public static List<String> getAntonymsList(IDictionary dict, String word) {
		List<String> antonymList = new ArrayList<String>();
		for(POS pos : POS.values()) {
			IIndexWord iIndexWord = dict.getIndexWord(word, pos);
			if(iIndexWord != null) {
				
				for(int i = 0; i < iIndexWord.getWordIDs().size(); i++) {
					IWordID wordID = iIndexWord.getWordIDs().get(i);
					IWord w = dict.getWord(wordID);
					
					for(IWordID antonym : w.getRelatedWords(Pointer.ANTONYM)) {
						if(!antonymList.contains(dict.getWord(antonym).getLemma())) {
							antonymList.add(dict.getWord(antonym).getLemma());
						}
					}
				}
			}
		}
		return antonymList;
	}
	/**
	 * Pass in the dictionary objct and the word want to search
	 * it will traverse all the pos of the word form, query all the 
	 * antonyms for the words, save to a list, save the lists to the
	 * hashmap and return.
	 * @param dict
	 * @param word
	 * @return
	 */
	public static Map<String, List<String>> getAntonymsMap(IDictionary dict, String word) {
		Map<String,List<String>> antonymMap = new HashMap<String, List<String>>();
		for(POS pos : POS.values()) {
			IIndexWord iIndexWord = dict.getIndexWord(word, pos);
			if(iIndexWord != null) {
				
				for(int i = 0; i < iIndexWord.getWordIDs().size(); i++) {
					IWordID wordID = iIndexWord.getWordIDs().get(i);
					IWord w = dict.getWord(wordID);
					List<String> antonymList = new ArrayList<String>();
					
					for(IWordID antonym : w.getRelatedWords(Pointer.ANTONYM)) {
						if(!antonymList.contains(dict.getWord(antonym).getLemma()) && !dict.getWord(antonym).getLemma().equalsIgnoreCase(w.getLemma())) {
							antonymList.add(dict.getWord(antonym).getLemma());
						}
					}
					if(!antonymList.isEmpty()) {
						antonymMap.put(w.getLemma(), antonymList);
					}
				}
			}
	
		}
		return antonymMap;
	}
	/**
	 * traverse the dictionary, search each word's synonyms and antonyms
	 * save the maps to a list.
	 * @param dict
	 */
	public static void traverseDictionary(IDictionary dict) {
		wordNetSynonymsList = new ArrayList<Map<String, List<String>>>();
		wordNetAntonymsList = new ArrayList<Map<String, List<String>>>();
		for(POS pos : POS.values()) {
			Iterator<?> iterator = dict.getIndexWordIterator(pos);
			while(iterator.hasNext()) {
				IIndexWord iIndexWord = (IIndexWord) iterator.next();
				Map<String, List<String>> antonymsMap = new HashMap<String, List<String>>();
				Map<String, List<String>> synonymsMap = new HashMap<String, List<String>>();
				antonymsMap = getAntonymsMap(dict, iIndexWord.getLemma());
				synonymsMap = getSynonymsMap(dict, iIndexWord.getLemma());
				if(!antonymsMap.isEmpty()) {
					wordNetAntonymsList.add(antonymsMap);
				}
				if(!synonymsMap.isEmpty()) {
					wordNetSynonymsList.add(synonymsMap);
				}	
			}
		}
	}
}
