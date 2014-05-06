#!/usr/bin/python
import numpy as np
from utils import *
from nltk.corpus import wordnet as wn

def readArray(path):
    with open(path, 'r') as f:
        data = f.readlines()
    out = []
    for row in data[3:]:
        row = row.strip().split()
        row = [float(x) for x in row]
        out.append(row)
    out = np.array(out)
    return out

def readParam(pmfPath, itr, k=1):
    path = pmfPath + '-' + str(itr) + "_"
    U = readArray(path+"U.mm")
    T = readArray(path+"V.mm")[k]
    A = readArray(path+"A_U.mm")
    mu = readArray(path+"mu_U.mm")
    alpha = readArray(path+"alpha.mm")[0][0]
    return U, T, A, mu, alpha

def embedding(pmfPath, matVocPath, grePath, savePath):
    # load Gibbs sampling parameters
    itr = 0
    U, T, A, mu, alpha = readParam(pmfPath, itr)
    D = U.shape[1]
    print U.shape, mu.shape, A.shape

    # get out-of-vocabulary word
    matVoc = load_dic(matVocPath)
    word2id, N = get_voc_dic(matVoc)
    voc = matVoc
    gre = load_dic(grePath)
    not_th_in_vec = a_not_in_b(gre, voc)
    print len(not_th_in_vec)
    print len(voc)

    # use this by default
    embed, vec = load_embedding("../data/morpho/hsmnCsmRNN.words", "../data/morpho/hsmnCsmRNN.We")

    def OOVembed(target):
        assert target in embed
        target_vec = embed[target]
        Q = []
        R = []
        for w in voc:
            if w != target and w in embed:# and np.random.binomial(2, .02)==1:
                sim = cosine_sim(target_vec, embed[w])
                sim = sim/10+5
                Q.append(np.reshape(U[word2id[w]-1] * T, (D,1)))
                R.append(sim)

        XtX = np.zeros(A.shape, dtype=float)
        Xty = np.zeros(mu.shape, dtype=float)
        #th = min(max(abs(np.array(R)))/2, .3)
        for q, r in zip(Q, R):
            if True:# or abs(r) >= th:
                Xty += r * q
                XtX += q * q.T

        A_ = A + alpha * XtX
        mu_ = np.dot(np.linalg.inv(A_), np.dot(A, mu) + alpha * Xty)
        return mu_.T.tolist()[0]

    output = []
    word = []
    i = 1
    for w in not_th_in_vec:
        v = wn.morphy(w)
        if True and w in embed: # oov embedding
            vector = OOVembed(w)
            vector = '\t'.join([str(x) for x in vector])
            word.append(w)
            output.append(vector)
            print i
            i += 1
        elif False and v in voc: # morphy
            vector = U[word2id[v]-1]
            vector = '\t'.join([str(x) for x in vector])
            word.append(w)
            output.append(vector)
            print 2

    save_dic(savePath+'.voc', word)
    with open(savePath, 'w') as f:
        f.write('\n'.join(output))

if __name__ == "__main__":

    pmfPath = "../../pmf/roget_no_mm-"
    matVocPath = "../../pmf/roget_no_mm-voc"
    grePath = "test-data/gre_wordlist.txt"
    savePath = "test-data/gre_oov.txt"

    embedding(pmfPath, matVocPath, grePath, savePath)
