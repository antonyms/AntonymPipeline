package edu.antonym.prototype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.antonym.Util;

public class SubThesaurus implements Thesaurus {

	Thesaurus parent;
	List<Integer> indices;
	int numSynonyms;
	int numAntonyms;

	public SubThesaurus(Thesaurus parent, List<Integer> indices) {
		super();
		this.parent = parent;
		this.indices = indices;
		Collections.sort(this.indices);
		this.numSynonyms=0;
		this.numAntonyms=0;
		for(Integer i:this.indices) {
			if(parent.getEntry(i).isAntonym()) {
				numAntonyms++;
			} else {
				numSynonyms++;
			}
		}
	}

	public void addEntry(int entry) {
		indices.add(entry);
	}

	@Override
	public Vocabulary getVocab() {
		return parent.getVocab();
	}

	@Override
	public int numEntries() {
		return indices.size();
	}

	@Override
	public Entry getEntry(int entryn) {
		return parent.getEntry(indices.get(entryn));
	}

	@Override
	public boolean isAntonym(int word1, int word2) {
		return lookupEntry(word1, word2, true)>=0;
	}

	@Override
	public boolean isSynonym(int word1, int word2) {
		return lookupEntry(word1, word2, false)>=0;
	}

	@Override
	public int lookupEntry(int word1, int word2, boolean isAnt) {
		int ent=parent.lookupEntry(word1, word2, isAnt);
		return Collections.binarySearch(indices, ent);
	}
	
//Samples a subthesaurus with n entries
	public static Thesaurus SampleThesaurus(Thesaurus th, int n) {
		int thsize=th.numEntries();
		List<Integer> sample=new ArrayList<Integer>(n);
		for(int i=0; i<n; i++) {
			sample.add(Util.r.nextInt(thsize));
		}
		return new SubThesaurus(th, sample);
	}
	public static Thesaurus[] evenSplitThesaurus(Thesaurus th, int folds) {
		double[] probs = new double[folds];
		Arrays.fill(probs, 1 / folds);
		return splitThesaurus(th, probs);
	}

	// This just randomly divvies up the entries of the thesaurus, however, it
	// makes sure to put an entry (word1,word2) in the same partition as the
	// entry
	// (word2,word1).
	public static Thesaurus[] splitThesaurus(Thesaurus th,
			double[] probabilities) {
		class Key {

			private final int x;
			private final int y;

			public Key(int x, int y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o)
					return true;
				if (!(o instanceof Key))
					return false;
				Key key = (Key) o;
				return x == key.x && y == key.y;
			}

			@Override
			public int hashCode() {
				int result = x;
				result = 31 * result + y;
				return result;
			}

		}
		Map<Key, Integer> entrymap = new HashMap<>();
		SubThesaurus[] result = new SubThesaurus[probabilities.length];
		for (int i = 0; i < probabilities.length; i++) {
			result[i] = new SubThesaurus(th, new ArrayList<Integer>());
		}
		for (int i = 0; i < th.numEntries(); i++) {
			Entry ent = th.getEntry(i);
			Integer assignment = entrymap
					.get(new Key(ent.word2(), ent.word1()));
			// If we haven't assigned the reversal of this entry yet, we put it
			// in a random partition
			if (assignment == null) {
				double r = Util.r.nextDouble();
				double tot = 0;
				for (int j = 0; j < probabilities.length; j++) {
					tot += probabilities[j];
					if (tot > r) {
						assignment = j;
						break;
					}
				}
				if (assignment == null) {
					assignment = probabilities.length - 1;
				}
			}
			result[assignment].indices.add(i);
			entrymap.put(new Key(ent.word1(), ent.word2()), assignment);
		}
		return result;
	}

	@Override
	public int numSynonyms() {
		return numSynonyms;
	}

	@Override
	public int numAntonyms() {
		return numAntonyms;
	}

}