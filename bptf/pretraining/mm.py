#!/usr/bin/python
import collections
import numpy as np
import cPickle
import sys
from utils import *

def vec2sim(vocPath, savePath, sample_rate=.02):

    matVoc = load_dic(vocPath)
    word2id, N = get_voc_dic(matVoc)

    embed, vec = load_embedding("../data/morpho/hsmnCsmRNN.words", "../data/morpho/hsmnCsmRNN.We")

    voc = list(matVoc)
    with open(savePath, 'w') as f:
        for i in xrange(N-1):
            print i
            if voc[i] in embed:
                for j in xrange(i+1,N):
                    if voc[j] in embed and 1==np.random.binomial(1,sample_rate):
                        a = voc[i]
                        b = voc[j]
                        if a <= b:
                            line = str(a) + ' ' + str(b)
                        else:
                            line = str(b) + ' ' + str(a)
                        line += ' ' + str(cosine_sim(embed[voc[i]], embed[voc[j]])) + '\n'
                        f.write(line)

def sim2mm(antPath, synPath, grePath, commonPath, simPath,
        single, TFIDF, MORE, NO_GRE, zero_proportion, sim_proportion):

    # inner dictionary: gre words list and popular words
    gre = load_dic(grePath)
    common = load_dic(commonPath)
    gre = set(gre)
    gre = gre.union(common)
    #save_dic("Rogets/inner.txt", gre)
    print len(gre)


    if TFIDF:
        voc, doc, entries, items = get_voc_tfidfdoc_from_synant(antPath, synPath, gre)
        entry2id, N = get_voc_dic(entries)
        item2id, N = get_voc_dic(items)
        word2id, N = get_voc_dic(voc)
    else:
        if MORE:
            voc = get_more_voc_from_synant(antPath, synPath, gre)
        else:
            voc = get_voc_from_synant(antPath, synPath, gre)
        print len(voc)
        if NO_GRE:
            gre = load_dic(grePath)
            voc = voc.difference(gre)
        #save_dic("Rogets/outer.txt", voc)
        print len(voc)
        word2id, N = get_voc_dic(voc)
        entries = voc
        items = voc
        entry2id = word2id
        item2id = word2id

    not_th = a_not_in_b(gre, voc)
    #save_dic('Rogets/not_th.txt', not_th)
    print N

    G = np.zeros((N, N), dtype=bool)

    output = []
    d = PairDict()

    ant, word = load_th(antPath)
    for target in ant:
        if target in entries:
            for w in ant[target]:
                if w in items:
                    a = word2id[target]
                    ai = entry2id[target]
                    b = word2id[w]
                    bi = item2id[w]
                    p = (a, b)
                    if p in d:
                        continue
                    d[p] = 1
                    G[a-1,b-1] = True
                    G[b-1,a-1] = True
                    line = str(a) + ' ' + str(b)
                    affix = ''
                    if not single:
                        affix += ' 1'
                    affix += ' '
                    if TFIDF:
                        affix += str(-doc[ai-1,bi-1])
                    else:
                        affix += '-4.0'
                    output.append(line+affix)
                    line = str(b) + ' ' + str(a)
                    output.append(line+affix)
    X = len(output)/2
    print 'x' + str(X)

    syn, word = load_th(synPath)
    for target in syn:
        if target in entries:
            for w in syn[target]:
                if w in items:
                    a = word2id[target]
                    ai = entry2id[target]
                    b = word2id[w]
                    bi = item2id[w]
                    p = (a, b)
                    if p in d:
                        continue
                    d[p] = 1
                    G[a-1,b-1] = True
                    G[b-1,a-1] = True
                    line = str(a) + ' ' + str(b)
                    affix = ''
                    if not single:
                        affix += ' 1'
                    affix += ' '
                    if TFIDF:
                        affix += str(doc[ai-1,bi-1])
                    else:
                        affix += '6.0'
                    output.append(line+affix)
                    line = str(b) + ' ' + str(a)
                    output.append(line+affix)
    X = len(output)/2 - X
    print 'x' + str(X)

    neiG = np.zeros(G.shape, dtype=bool)
    for i, row in enumerate(G):
        print i
        neigh = set()
        for j in np.where(row==True)[0]:
            neigh = neigh.union(np.where(G[j]==True)[0])
            neiG[i] = row
            neiG[i, list(neigh)] = True
    NN = G.sum() * zero_proportion
    G = neiG

    G[range(N), range(N)] = True
    count = 0
    while count < NN:
        i = np.random.randint(N)
        j = np.random.randint(N)
        if G[i,j] == False:
            G[i,j] = True
            count += 1
            line = str(i+1) + ' ' + str(j+1)
            if not single:
                line += ' 1'
            line += ' 5.0'
            output.append(line)
            line = str(j+1) + ' ' + str(i+1)
            if not single:
                line += ' 1'
            line += ' 5.0'
            output.append(line)
    N = len(output)/2 * sim_proportion
    print N

    if not single:
        sim = load_sim(simPath, voc, True, N)
        print len(sim)
        for row in sim:
            line = str(word2id[row[0]]) + ' ' + str(word2id[row[1]])
            psim = sim[row]/10 + 5
            line += ' 2 ' + str(psim)
            output.append(line)
            line = str(word2id[row[1]]) + ' ' + str(word2id[row[0]])
            line += ' 2 ' + str(psim)
            output.append(line)

    with open(outPath, 'w') as f:
        f.write('%%MatrixMarket matrix coordinate real general\n% Generated 27-Apr-2014\n'+str(len(voc))+' '+str(len(voc))+' '+str(len(output))+'\n')
        f.write('\n'.join(output))
    save_dic(outPath+'-voc', voc)

if __name__ == "__main__":

    single = False
    TFIDF = False
    MORE = True
    NO_GRE = True
    zero_proportion = 3
    sim_proportion = 1

    rootPath = '../../data/'
    thPath = rootPath + 'Rogets/'
    antPath = thPath + 'antonym.txt'
    synPath = thPath + 'synonym.txt'
    grePath = rootPath + 'test-data/gre_wordlist.txt'
    commonPath = rootPath + 'test-data/test_wordlist.txt'

    if not single:
        if MORE:
            outPath = '../../pmf/roget_no_mm'
        else:
            outPath = '../../pmf/roget_gre_mm'
    else:
        outPath = '../../pmf/roget_single_mm'
    simPath = rootPath + 'morpho/sim.txt'
    sim2mm(antPath, synPath, grePath, commonPath, simPath,
        single, TFIDF, MORE, NO_GRE, zero_proportion, sim_proportion)
