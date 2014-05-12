#!/usr/bin/python
from pretraining import mm
from pretraining.utils import *

rootPath = '../data/'
thPath = rootPath + 'combine/'
antPath = thPath + 'antonym.txt'
synPath = thPath + 'synonym.txt'
vocPath = '../data/bptf/roget_mm-voc'
grePath = rootPath + 'test-data/gre_wordlist.txt'
commonPath = rootPath + 'test-data/test_wordlist.txt'
topPath = '../data/Rogets/top' #TODO

zero_proportion = 3
sim_proportion = 1#TODO

gre = load_dic(grePath)
common = load_dic(commonPath)
top = load_dic(topPath)

top = set(top)
print 'common before: %d' % len(common)
common = [x for x in common if x in top]
print 'common after: %d' % len(common)
gre = set(gre)
print 'gre before: %d' % len(gre)
gre = gre.union(common)
print 'gre after: %d' % len(gre)


voc = get_more_voc_from_ant(antPath, gre)
#voc = get_more_voc_from_synant(antPath, synPath, gre)
#voc = get_voc_from_synant(antPath, synPath, gre)
print 'finally get vocabulary: %d' % len(voc)
save_dic(vocPath, voc)

word2id, N = get_voc_dic(voc)

"""
Calculate pair-wise cosine product of morpho vectors.
"""
savePath = thPath + "sim.txt"
#mm.vec2sim(vocPath, savePath, sample_rate=.05)

"""
Generate MatrixMarket format file for training.
"""

outPath = 'bptf/roget_mm'
outPath = rootPath + outPath

simPath = thPath + 'sim.txt'

mm.sim2mm(antPath, synPath, vocPath, simPath, outPath,
        zero_proportion, sim_proportion)
