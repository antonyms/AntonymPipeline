package edu.antonym;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.event.ListSelectionEvent;

import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Vocabulary;

public class SimpleThesaurus implements Thesaurus {
	
	//entries are sorted by word1, then by isAntonym, then by w2. This makes all the antonyms of a word contiguous, and all the synonyms of a word continuous
	static class ThesaurusEntry implements Comparable<ThesaurusEntry>, Entry {
		public int word1;
		public int word2;

		boolean isAntonym;

		public ThesaurusEntry() {
		}
		

		public ThesaurusEntry(int word1, int word2, boolean isAntonym) {
			super();
			this.word1 = word1;
			this.word2 = word2;
			this.isAntonym = isAntonym;
		}


		@Override
		public int compareTo(ThesaurusEntry o) {
			int compare = Integer.compare(this.word1, o.word1);
			if (compare == 0) {
				compare = Boolean.compare(this.isAntonym, o.isAntonym);
			}
			if (compare == 0) {
				compare = Integer.compare(this.word2, o.word2);
			}
			return compare;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ThesaurusEntry) {
				ThesaurusEntry other = ((ThesaurusEntry) obj);
				return word1 == other.word1 && isAntonym == other.isAntonym
						&& this.word2 == other.word2;
			}
			return false;
		}

		@Override
		public int word1() {
			return word1;
		}

		@Override
		public int word2() {
			return word2;
		}

		@Override
		public boolean isAntonym() {
			return isAntonym;
		}

	}

	Vocabulary vocab;

	ThesaurusEntry[] entries;

	int numSynonyms;
	public SimpleThesaurus(Vocabulary vocab, File synonyms, File antonyms)
			throws FileNotFoundException {
		this.vocab = vocab;
		this.numSynonyms=0;
		List<ThesaurusEntry> ent =new ArrayList<ThesaurusEntry>();
		Scanner s = new Scanner(synonyms);
		while (s.hasNextLine()) {
			String[] wordz = s.nextLine().split("\\s");
			int w1 = vocab.lookupWord(wordz[0]);
			if (w1 == vocab.OOVindex()) {
				continue;
			}

			for (int i = 1; i < wordz.length; i++) {
				int w2 = vocab.lookupWord(wordz[i]);
				if (w2 == vocab.OOVindex()) {
					continue;
				}
				ent.add(new ThesaurusEntry(w1,w2,false));
				numSynonyms++;
			}

		}

		s = new Scanner(antonyms);
		while (s.hasNext()) {
			String[] wordz = s.nextLine().split("\\s");
			int w1 = vocab.lookupWord(wordz[0]);
			if (w1 == vocab.OOVindex()) {
				continue;
			}

			for (int i = 1; i < wordz.length; i++) {
				int w2 = vocab.lookupWord(wordz[i]);
				if (w2 == vocab.OOVindex()) {
					continue;
				}
				ent.add(new ThesaurusEntry(w1,w2,true));
			}
		}
		entries = ent.toArray(new ThesaurusEntry[ent.size()]);
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
	public int numSynonyms() {
		return numSynonyms;
	}
	public int numAntonyms() {
		return numEntries()-numSynonyms();
	}

	@Override
	public Entry getEntry(int entryn) {
		return entries[entryn];
	}
	
	public int entryStart(int word1, int word2, boolean isant) {
		int ind=lookupEntry(word1, word2, isant);
		if (ind < 0) {
			return -(ind+1); //the insertion point according to the binary search api
		} else {
			return ind;
		}
	}
	
	public int lookupEntry(int word1, int word2, boolean isant) {
		ThesaurusEntry key = new ThesaurusEntry();
		key.word1 = word1;
		key.word2 = word2;
		key.isAntonym = isant;
		return Arrays.binarySearch(entries, key);
	}
	
	List<Integer> lookupentrylist(int word, boolean isant) {
		int first = entryStart(word, 0, isant);
		int last;
		for (last = first; last < entries.length; last++) {
			ThesaurusEntry e = entries[last];
			if (e.word1 != word || e.isAntonym != isant) {
				break;
			}
		}
		List<Integer> result = new ArrayList<Integer>(last - first);
		for (int i = first; i < last; i++) {
			result.add(entries[i].word2);
		}
		return result;
	}

	public List<Integer> getAntonyms(int word) {
		return lookupentrylist(word, true);
	}

	public List<Integer> getSynonyms(int word) {
		return lookupentrylist(word, false);
	}


	@Override
	public boolean isAntonym(int word1, int word2) {
		return lookupEntry(word1, word2, true)>=0;
	}

	@Override
	public boolean isSynonym(int word1, int word2) {
		return lookupEntry(word1, word2, false)>=0;
	}

}