#!/usr/bin/python
from pretraining import *

"""
Out-of-vocabulary embedding
"""
rootPath = '../data/'
grePath = rootPath + "test-data/gre_wordlist.txt"

experimentPath = rootPath + 'combine/'
pmfPath = experimentPath + 'combine_mm'
synPath = experimentPath + "synonym.txt"
savePath = experimentPath + 'gre_oov'
ITER = 500

matVocPath = pmfPath+"-voc"
for i in xrange(ITER):
    oov.embedding(pmfPath, matVocPath, grePath, savePath+'-'+str(i), synPath, i)
