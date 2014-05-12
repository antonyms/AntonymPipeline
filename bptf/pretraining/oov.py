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

def readParam(pmfPath, itr, k=1, all_slice=False):
    path = pmfPath + '-' + str(itr) + "_"
    U = readArray(path+"U.mm")
    if all_slice:
        T = readArray(path+"V.mm")
    else:
        T = readArray(path+"V.mm")[k]
    A = readArray(path+"A_U.mm")
    mu = readArray(path+"mu_U.mm")
    alpha = readArray(path+"alpha.mm")[0][0]
    return U, T, A, mu, alpha

def embedding(pmfPath, matVocPath, grePath, savePath):
    # load Gibbs sampling parameters
    synPath = "../data/Rogets/synonym.txt"
    synPath = "../data/WordNet-3.0/synonym.txt"
    synPath = "../data/combine/synonym.txt"
    syns, word = load_th(synPath)
    itr = 0
    U, T, A, mu, alpha = readParam(pmfPath, itr, all_slice=True)
    T0 = T[0]
    T = T[1]
    D = U.shape[1]
    print U.shape, mu.shape, A.shape
    print T0
    print T

    # get out-of-vocabulary word
    matVoc = load_dic(matVocPath)
    word2id, N = get_voc_dic(matVoc)
    voc = matVoc
    gre = load_dic(grePath)
    not_th_in_vec = a_not_in_b(gre, voc)
    print len(not_th_in_vec)
    print len(voc)

    # use this by default
    #embed, vec = load_embedding("../data/morpho/hsmnCsmRNN.words", "../data/morpho/hsmnCsmRNN.We")
    embed, vec = load_embedding("../data/morpho/cwCsmRNN.words", "../data/morpho/cwCsmRNN.We")

    def OOVembed(target):
        Q = []
        R = []
        for w in voc:
            if w != target and (target in syns and w in syns[target] or w in syns and target in syns[w]):
                Q.append(np.reshape(U[word2id[w]-1] * T0, (D,1)))
                R.append(1.0)

        if R and target in embed:
            target_vec = embed[target]
            n = len(voc)
            i = len(R)*2
            while i > 0:
                rand = np.random.randint(n)
                w = voc[rand]
                if w != target and w in embed:
                    sim = cosine_sim(target_vec, embed[w])
                    sim = preprocess_sim(sim)
                    Q.append(np.reshape(U[word2id[w]-1] * T, (D,1)))
                    R.append(sim)
                    i = i - 1

        if not R and target in embed:
            target_vec = embed[target]
            for w in voc:
                if w != target and w in embed:# and np.random.binomial(2, .02)==1:
                    sim = cosine_sim(target_vec, embed[w])
                    sim = preprocess_sim(sim)
                    Q.append(np.reshape(U[word2id[w]-1] * T, (D,1)))
                    R.append(sim)

        if not R:
            return None

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
    for i, w in enumerate(not_th_in_vec):
        v = wn.morphy(w)
        if True and v in voc: # morphy
            vector = U[word2id[v]-1]
            vector = '\t'.join([str(x) for x in vector])
            word.append(w)
            output.append(vector)
            print "%d, morphy" % i
            continue
        if True: # oov embedding
            vector = OOVembed(w)
        if vector == None:
            vector = list(set([x.name.split('.')[0] for x in wn.synsets(w)]))
            if vector:
                vector = OOVembed(vector[0])
            else:
                vector = None
        if vector != None:
            vector = '\t'.join([str(x) for x in vector])
            word.append(w)
            output.append(vector)
            print "%d, oov" % i
            continue
        print "%d" % i

    save_dic(savePath+'.voc', word)
    with open(savePath, 'w') as f:
        f.write('\n'.join(output))

def get_name(string):
    return string.split('#')[0]

if __name__ == "__main__":

    pmfPath = "../../pmf/roget_no_mm-"
    matVocPath = "../../pmf/roget_no_mm-voc"
    grePath = "test-data/gre_wordlist.txt"
    savePath = "test-data/gre_oov.txt"

    embedding(pmfPath, matVocPath, grePath, savePath)
