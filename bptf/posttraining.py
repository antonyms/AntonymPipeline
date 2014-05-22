#!/usr/bin/python
from pretraining import *

"""
Out-of-vocabulary embedding
"""
rootPath = '../data/'
pmfPath = rootPath + 'bptf/roget_mm'
matVocPath = rootPath + "bptf/roget_mm-voc"
grePath = rootPath + "test-data/gre_wordlist.txt"
savePath = rootPath + "bptf/gre_oov.txt"

oov.embedding(pmfPath, matVocPath, grePath, savePath, 58)
