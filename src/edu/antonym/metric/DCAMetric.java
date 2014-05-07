package edu.antonym.metric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import jpaul.DataStructs.UnionFind;

import edu.antonym.NormalizedTextFileEmbedding;
import edu.antonym.SimpleThesaurus;
import edu.antonym.SimpleVocab;
import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Thesaurus.Entry;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;
import edu.antonym.test.RandomizedTestCase;
import edu.antonym.test.TestCase1;

/*
 * Metric learning according to
 * 
 * 
 * Learning Distance Metrics with Contextual Constraints for Image Retrieval
 * Steven C. H. Hoi† , Wei Liu† , Michael R. Lyu† and Wei-Ying Ma‡
 * †
 * Chinese University of Hong Kong, Hong Kong
 * Microsoft Research Asia, Beijing, P.R. China
 * 
 */
public class DCAMetric implements WordMetric {
	VectorEmbedding emb;
	Matrix M;

	public DCAMetric(VectorEmbedding emb, Thesaurus th) {

		/*
		 * Step 1. Compute Cb and Cw
		 */
		this.emb = emb;
		Map<Integer, Set<Integer>> discriminativesets = new HashMap<Integer, Set<Integer>>();

		UnionFind<Integer> chunklets = new UnionFind<Integer>();
		for (int i = 0; i < th.numEntries(); i++) {
			Entry ent = th.getEntry(i);
			if (!ent.isAntonym()) {
				chunklets.union(ent.word1(), ent.word2());
			}
		}

		for (int i = 0; i < th.getVocab().getVocabSize(); i++) {
			Integer chunk = chunklets.find(i);
			discriminativesets.put(chunk, new HashSet<Integer>());
		}

		for (int i = 0; i < th.numEntries(); i++) {
			Entry ent = th.getEntry(i);
			if (ent.isAntonym()) {
				int chunklet1 = chunklets.find(ent.word1());
				int chunklet2 = chunklets.find(ent.word2());
				discriminativesets.get(chunklet1).add(chunklet2);
				discriminativesets.get(chunklet2).add(chunklet1);

			}
		}

		int vecdim = emb.getDimension();

		Map<Integer, Jama.Matrix> m = new HashMap<>();
		Matrix C_w = new Matrix(vecdim, vecdim);

		// calculate m_j and Cw
		int numchunklets = 0;
		for (Set<Integer> e : chunklets.allNonTrivialEquivalenceClasses()) {
			int representative = chunklets.find(e.iterator().next());

			Jama.Matrix chunkletAverage = new Matrix(vecdim, 1);
			for (Integer i : e) {
				double[] rep = emb.getVectorRep(i);
				chunkletAverage.plusEquals(new Matrix(rep, vecdim));
			}
			chunkletAverage.timesEquals(1.0d / ((double) e.size()));
			m.put(representative, chunkletAverage);

			Matrix Cwpartialsum = new Matrix(vecdim, vecdim);
			for (Integer i : e) {
				double[] rep = emb.getVectorRep(i);
				Matrix x_ji = new Matrix(rep, vecdim);
				Matrix d = x_ji.minus(chunkletAverage);
				Cwpartialsum.plusEquals(d.times(d.transpose()));
			}
			Cwpartialsum.timesEquals(1.0d / ((double) e.size()));
			C_w.plusEquals(Cwpartialsum);
			numchunklets++;
		}
		C_w.timesEquals(1.0d / ((double) numchunklets));

		// Calculate n_b and Cb
		int nb = 0;
		Matrix C_b = new Matrix(vecdim, vecdim);
		for (java.util.Map.Entry<Integer, Set<Integer>> e : discriminativesets
				.entrySet()) {
			for (Integer i : e.getValue()) {
				int j = e.getKey();
				// If our chunklet has only one entry, the average is just that
				// entry
				Matrix m_j = m.get(j);
				if (m_j == null) {
					m_j = new Matrix(emb.getVectorRep(j), vecdim);
				}
				Matrix m_i = m.get(i);
				if (m_i == null) {
					m_i = new Matrix(emb.getVectorRep(i), vecdim);
				}
				Matrix dif = m_j.minus(m_i);

				C_b.plusEquals(dif.times(dif.transpose()));
			}

			nb += e.getValue().size();
		}
		C_b.timesEquals(1.0d / ((double) nb));

		C_b.print(4, 4);
		/*
		 * Step 2: Diagonalize Cb by eigenanalysis
		 */
		/*
		 * 2.1. Find U to satisfy U Cb U = Λb and U U = I, here Λb is a diagonal
		 * matrix sorted in increasing order ;
		 */
		EigenvalueDecomposition eigCb = C_b.eig();
		/*
		 * 2.2. Form a matrix U by the last k column vectors of U with nonzero
		 * eigenvalues
		 */
		double[] values = eigCb.getRealEigenvalues();

		int firstnonzero;
		for (firstnonzero = 0; firstnonzero < vecdim; firstnonzero++) {
			if (values[firstnonzero] != 0.0d) {
				break;
			}
		}
		int k = vecdim - firstnonzero;

		Matrix Uhat = eigCb.getV().getMatrix(0, vecdim - 1, firstnonzero,
				vecdim - 1);

		/*
		 * 2.3. Let Db = U Cb U be the k ∗ k submatrix of Λb
		 */
		Matrix D_b = eigCb.getD().getMatrix(firstnonzero, vecdim - 1,
				firstnonzero, vecdim - 1);

		/*
		 * 2.4
		 */
		Matrix D_b_inv_sqrt = D_b.copy();
		for (int i = 0; i < k; i++) {
			D_b_inv_sqrt.set(i, i, Math.pow(D_b_inv_sqrt.get(i, i), -0.5d));
		}
		D_b_inv_sqrt.print(4, 4);
		Matrix Z = Uhat.times(D_b_inv_sqrt);
		Matrix C_z = Z.transpose().times(C_w).times(Z);

		/*
		 * Step 3: Diagonalize Cz by eigenanalysi
		 */
		/*
		 * 3.1. Find V to satisfy V Cz V = Λw and V V = I, here Λw is a diagonal
		 * matrix sorted in decreasing order
		 */
		EigenvalueDecomposition eigCz = C_z.eig();
		Matrix D_w = eigCz.getD();
		Matrix V = eigCz.getV();
		/*
		 * Step 4. Final Outputs A = Z V Dwˆ −1/2 and M = A A .
		 */
		Matrix D_w_inv_sqrt = D_b.copy();
		for (int i = 0; i < k; i++) {
			D_w_inv_sqrt.set(i, i, Math.pow(D_w_inv_sqrt.get(i, i), -0.5d));
		}
		Matrix A = Z.times(V).times(D_w_inv_sqrt);
		A.print(4, 4);
		M = A.transpose().times(A);
		M.print(4, 4);

	}

	@Override
	public Vocabulary getVocab() {
		return emb.getVocab();
	}

	@Override
	public double similarity(int word1, int word2) {
		Matrix w1 = new Matrix(emb.getVectorRep(word1), emb.getDimension());
		Matrix w2 = new Matrix(emb.getVectorRep(word2), emb.getDimension());
		double w1norm = Math.sqrt(w1.transpose().times(M).times(w1).get(0, 0));
		double w2norm = Math.sqrt(w2.transpose().times(M).times(w2).get(0, 0));

		return w1.transpose().times(M).times(w2).get(0, 0) / (w1norm * w2norm);
	}

	public static void main(String[] args) throws IOException {
		Vocabulary vocab = new SimpleVocab(new File("data/huangvocab.txt"));
		Thesaurus th = new SimpleThesaurus(vocab, new File(
				"data/Rogets/synonym.txt"), new File("data/Rogets/antonym.txt"));
		NormalizedVectorEmbedding origEmbed = new NormalizedTextFileEmbedding(
				new File("data/huangvectors.txt"), vocab);
		DCAMetric metric = new DCAMetric(origEmbed, th);

		MetricEvaluator me = new RandomizedTestCase(th);
		double score = me.score(metric);
		System.out.println("Newscore " + score);
		score = me.score(origEmbed);
		System.out.println("Oldscore " + score);
	}

}
