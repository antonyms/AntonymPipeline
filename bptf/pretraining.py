#!/usr/bin/python
from pretraining import mm

"""
Calculate pair-wise cosine product of morpho vectors.
"""
vocPath = "../data/bptf/roget_mm-voc"
savePath = "../data/morpho/sim.txt"
mm.vec2sim(vocPath, savePath, sample_rate=.02)

"""
Generate MatrixMarket format file for training.
Currently use a subset of all the words.
"""
single = False
TFIDF = False
MORE = True
NO_GRE = False
zero_proportion = 3
sim_proportion = 1

rootPath = '../data/'
thPath = rootPath + 'Rogets/'
antPath = thPath + 'antonym.txt'
synPath = thPath + 'synonym.txt'
grePath = rootPath + 'test-data/gre_wordlist.txt'
commonPath = rootPath + 'test-data/test_wordlist.txt'

if not single:
    if MORE:
        outPath = 'bptf/roget_mm'
    else:
        outPath = 'bptf/roget_gre_mm'
else:
    outPath = 'bptf/roget_single_mm'
outPath = rootPath + outPath

simPath = rootPath + 'morpho/sim.txt'

mm.sim2mm(antPath, synPath, grePath, commonPath, simPath,
    single, TFIDF, MORE, NO_GRE, zero_proportion, sim_proportion)
