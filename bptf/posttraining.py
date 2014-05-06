#!/usr/bin/python
from pretraining import *

"""
Out-of-vocabulary embedding
"""
rootPath = '../data/'
outPath = 'bptf/roget_mm'
outPath = rootPath + outPath
pmfPath = outPath
matVocPath = rootPath + "bpth/roget_mm-voc"
grePath = rootPath + "test-data/gre_wordlist.txt"
savePath = rootPath + "test-data/gre_oov.txt"

oov.embedding(pmfPath, matVocPath, grePath, savePath)
