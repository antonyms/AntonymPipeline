#!/usr/bin/python
import collections
import numpy as np
import cPickle
import sys
from utils import *

def vec2sim(vocPath, savePath, sample_rate=.02):

    matVoc = load_dic(vocPath)
    word2id, N = get_voc_dic(matVoc)

    #embed, vec = load_embedding("../data/morpho/hsmnCsmRNN.words", "../data/morpho/hsmnCsmRNN.We")
    embed, vec = load_embedding("../data/morpho/cwCsmRNN.words", "../data/morpho/cwCsmRNN.We")

    voc = list(matVoc)
    count = 1
    with open(savePath, 'w') as f:
        for i in xrange(N-1):
            print_status(i)
            if voc[i] in embed:
                for j in xrange(i+1,N):
                    if voc[j] in embed and 1==np.random.binomial(1,sample_rate):
                        a = voc[i]
                        b = voc[j]
                        count += 1
                        line = str(a) + ' ' + str(b)
                        line += ' ' + str(cosine_sim(embed[voc[i]], embed[voc[j]])) + '\n'
                        f.write(line)
                        line = str(b) + ' ' + str(a)
                        line += ' ' + str(cosine_sim(embed[voc[i]], embed[voc[j]])) + '\n'
                        f.write(line)
    print "\nGenerated %d pairs of second slice" % count

def sim2mm(antPath, synPath, vocPath, simPath, outPath,
        zero_proportion, sim_proportion):


    voc = load_dic(vocPath)
    word2id, N = get_voc_dic(voc)
    print "Vocabulary size: %d" % N

    G = np.zeros((N, N), dtype=bool)

    output = []

    ant, word = load_th(antPath)
    for target in ant:
        if target in word2id:
            a = word2id[target]
            for w in ant[target]:
                if w in word2id:
                    b = word2id[w]
                    if G[a-1,b-1] == True:
                        continue
                    G[a-1,b-1] = True
                    G[b-1,a-1] = True
                    line = str(a) + ' ' + str(b)
                    affix = ' 1 -1.0'
                    output.append(line+affix)
                    line = str(b) + ' ' + str(a)
                    output.append(line+affix)
    X = len(output)/2
    print 'Antonym pairs: %d' % X

    syn, word = load_th(synPath)
    for target in syn:
        if target in word2id:
            a = word2id[target]
            for w in syn[target]:
                if w in word2id:
                    b = word2id[w]
                    if G[a-1,b-1] == True:
                        continue
                    G[a-1,b-1] = True
                    G[b-1,a-1] = True
                    line = str(a) + ' ' + str(b)
                    affix = ' 1 1.0'
                    output.append(line+affix)
                    line = str(b) + ' ' + str(a)
                    output.append(line+affix)
    X = len(output)/2 - X
    print 'Synonym pairs: %d' % X

    neiG = np.zeros(G.shape, dtype=bool)
    for i, row in enumerate(G):
        print_status(i)
        neigh = []
        for j in np.where(row==True)[0]:
            neigh += np.where(G[j]==True)[0].tolist()
        neiG[i] = row
        neigh.append(i)
        neiG[i, neigh] = True
    NN = G.sum()/2
    print '\nGot first slice: %d' % NN
    NN = NN * zero_proportion
    G = neiG

    count = 0
    while count < NN:
        i = np.random.randint(N)
        j = np.random.randint(N)
        if G[i,j] == False:
            G[i,j] = True
            G[j,i] = True
            count += 1
            line = str(i+1) + ' ' + str(j+1)
            line += ' 1 0.0'
            output.append(line)
            line = str(j+1) + ' ' + str(i+1)
            line += ' 1 0.0'
            output.append(line)
    N = len(output)/2
    print 'Finished first slice: %d' % N
    N_ = N * sim_proportion
    print 'Desired second slice: %d' % N_

    prop = N_*2
    with open(simPath, 'r') as f:
        count = 0
        for row in f:
            count += 1
    prop = float(prop) / count
    print 'Sample probability: %f' % prop
    voc = set(voc)
    with open(simPath, 'r') as f:
        for row in f:
            if np.random.binomial(1, prop)==1:
                row = row.strip().split()
                a = row[0]
                b = row[1]
                if a in voc and b in voc:
                    line = str(word2id[a]) + ' ' + str(word2id[b])
                    psim = preprocess_sim(float(row[2]))
                    line += ' 2 ' + str(psim)
                    output.append(line)

    print 'Finished second slice: %d' % (len(output)/2 - N)

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
