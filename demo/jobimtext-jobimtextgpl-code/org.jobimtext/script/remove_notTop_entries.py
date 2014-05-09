import sys;
if(len(sys.argv)<3):
	print "This script keeps only the top N entries for each term"
	print "python remove_notTop_entries.py input topN output"
	print "IMPORTANT: input file should be sorted according word1 (col 1) and reverse score (col 3)"
	sys.exit()
input=sys.argv[1]
output=sys.argv[3]
top=int(sys.argv[2])
f = open(input,'r')
out = open(output,"w")
pre = ""
pre=""
i=0
prel=""
for line in f:
	
	k=line.split("\t")
	if(len(k)>2):
		if(pre==k[0]):
			i+=1
			if(i<top):
				if i==1:
					out.write(prel)
				out.write(line)
		else:
			i=0
			prel=line
                	#out.write(line);
		pre=k[0]
	else:
		print "to short: "+line

f.close()
out.close()
