import os
import sys
if len(sys.argv)<3:
	print "This script removes all rows where the term in the first column (separated by tab) is not contained in the file_with_entries"
	print "USAGE: file_with_words_to_remove file_with_entries"
words=[]
for line in open(sys.argv[1]).readlines():
	words.append(line.strip())
words=set(words)
f  =  open(sys.argv[2])
for line in f:
	if line.strip().split("\t")[0] in words:
		print line.strip()
