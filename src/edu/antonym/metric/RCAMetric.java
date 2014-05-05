package edu.antonym.metric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Jama.Matrix;


import jpaul.DataStructs.UnionFind;


import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Thesaurus.Entry;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

public class RCAMetric implements WordMetric {
VectorEmbedding emb;

	public RCAMetric(VectorEmbedding emb,Thesaurus th) {
		this.emb=emb;
		Map<Integer,Set<Integer>> discriminativesets=new HashMap<Integer,Set<Integer>>();
		
		UnionFind<Integer> chunklets=new UnionFind<Integer>();
		for(int i=0; i<th.numEntries(); i++) {
			Entry ent=th.getEntry(i);
			chunklets.union(ent.word1(), ent.word2());
		}
		
		Map<Integer, Integer> chunkletsizes=new HashMap<>();
		
		for(int i=0; i<th.getVocab().getVocabSize(); i++) {
			Integer chunk=chunklets.find(i);
			Integer find=chunkletsizes.get(chunk);
			if(find==null) {
				find=0;
			}
			chunkletsizes.put(chunk, find+1);
			discriminativesets.put(chunk, new HashSet<Integer>());
		}
		
		for(int i=0; i<th.numEntries(); i++) {
			Entry ent=th.getEntry(i);
			int chunklet1=chunklets.find(ent.word1());
			int chunklet2=chunklets.find(ent.word2());
			discriminativesets.get(chunklet1).add(chunklet2);
			discriminativesets.get(chunklet1).add(chunklet2);
		}
		
		int vecdim=emb.getDimension();
		
		Matrix Cb=new Matrix(vecdim, vecdim);
		Matrix Cw=new Matrix(vecdim, vecdim);
		
	}
	@Override
	public Vocabulary getVocab() {
		return emb.getVocab();
	}

	@Override
	public double similarity(int word1, int word2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
