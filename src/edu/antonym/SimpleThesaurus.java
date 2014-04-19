package edu.antonym;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Vocabulary;

public class SimpleThesaurus implements Thesaurus {
	class ThesaurusEntry implements Comparable<ThesaurusEntry> {
		public int word;
		public List<Integer> synonyms;
		public List<Integer> antonyms;

		public ThesaurusEntry() {
			synonyms = new ArrayList<Integer>(1);
			antonyms = new ArrayList<>(1);
		}

		@Override
		public int compareTo(ThesaurusEntry o) {
			return Integer.compare(this.word, o.word);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ThesaurusEntry) {
				return word == ((ThesaurusEntry) obj).word;
			}
			return false;
		}

	}

	Vocabulary vocab;

	ThesaurusEntry[] entries;

	public SimpleThesaurus(Vocabulary vocab, File synonyms, File antonyms) throws FileNotFoundException {
		this.vocab=vocab;

		Map<Integer, ThesaurusEntry> map = new HashMap<Integer, SimpleThesaurus.ThesaurusEntry>();
		Scanner s=new Scanner(synonyms);
		while(s.hasNextLine()) {
			String[] wordz=s.nextLine().split("\\s");
			int w1=vocab.lookupWord(wordz[0]);
			ThesaurusEntry e=map.get(w1);
			if(e==null) {
				e=new ThesaurusEntry();
				e.word=w1;
				map.put(w1, e);
			}
			for(int i=1; i<wordz.length; i++) {
				int w2=vocab.lookupWord(wordz[i]);
				e.synonyms.add(w2);
				e=map.get(w2);
				if(e==null) {
					e=new ThesaurusEntry();
					e.word=w2;
					map.put(w2,e);
				}
				e.synonyms.add(w1);
			}
			
		}

		s=new Scanner(antonyms);
		while(s.hasNext()) {
			String[] wordz=s.nextLine().split("\\s");
			int w1=vocab.lookupWord(wordz[0]);
			ThesaurusEntry e=map.get(w1);
			if(e==null) {
				e=new ThesaurusEntry();
				e.word=w1;
				map.put(w1, e);
			}
			for(int i=1; i<wordz.length; i++) {
				int w2=vocab.lookupWord(wordz[i]);
				e.antonyms.add(w2);
				e=map.get(w2);
				if(e==null) {
					e=new ThesaurusEntry();
					e.word=w2;
					map.put(w2,e);
				}
				e.antonyms.add(w1);
			}
		}
		entries=map.values().toArray(new ThesaurusEntry[map.size()]);
		Arrays.sort(entries);
	}

	@Override
	public Vocabulary getVocab() {
		return vocab;
	}

	@Override
	public int numEntries() {
		return entries.length;
	}

	@Override
	public int getEntry(int entryn) {
		return entries[entryn].word;
	}

	@Override
	public List<Integer> getAntonyms(int word) {
		ThesaurusEntry key=new ThesaurusEntry();
		key.word=word;
		int ind=Arrays.binarySearch(entries, key);
		return entries[ind].antonyms;
	}

	@Override
	public List<Integer> getSynonyms(int word) {
		ThesaurusEntry key=new ThesaurusEntry();
		key.word=word;
		int ind=Arrays.binarySearch(entries, key);
		return entries[ind].synonyms;
	}

	@Override
	public boolean isAntonym(int word1, int word2) {
		ThesaurusEntry key=new ThesaurusEntry();
		key.word=word1;
		int ind=Arrays.binarySearch(entries, key);
		return entries[ind].antonyms.contains(word2);
	}

	@Override
	public boolean isSynonym(int word1, int word2) {
		ThesaurusEntry key=new ThesaurusEntry();
		key.word=word1;
		int ind=Arrays.binarySearch(entries, key);
		return entries[ind].synonyms.contains(word2);
	}

}