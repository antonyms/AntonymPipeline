import pprint
import sys 

def printPig(cls, params,comment=False):
	p = " -param "
	out="pig "+p+p.join(params)+" pig/"+cls+".pig"
	if comment: out="#"+out
	print out
	
def printHadoop(cls,inp, outp, compress, param="",comment=False):
	tmp = " ".join([cls,inp,outp,param, str(compress)])
	out ="hadoop jar lib/org.jobimtext.jar org.jobimtext.hadoop.mapreducer."+tmp
	if comment: out="#"+out 
	print out
def bylength(word1,word2):
    return len(word1)-len(word2)

def removeOutputfolders():
	l = globals()
	folders = []
	for name in l.keys():
		if(name.endswith("_out")):
			folders.append(name+"\t"+ l[name])

	folders.sort(cmp=bylength)
	for f in folders:
		print("#hadoop dfs -rmr "+f)
		#print(f)

if(len(sys.argv)<7):
	print "Parameters needed: dataset s t p significance simsort_count [computer file_prefix]"
	print "dataset: e.g. news10M_ngram_1_3, news10M_maltparser"
	print "w: maximal number of uniq words a feature is allowed to have" 
	print "s: lower threshold for significance score"
	print "t: lower threshold for word count "
	print "p: maximal number of features a word is allowed to have"
	print "significance: LMI, PMI, LL"
	print "simsort count - number of similar terms sorted in the last step" 
	print "optional parameter:"
	print "computer: computer to copy the files to"
	print "file_prefix: prefix, the files (distributional similarity, WordCount, FeatureCount) will be copied to"
	sys.exit()
	

clsUniq="UniqMapper"
clsContext ="WordFeatureCount"
clsContextPruning ="PruneFeaturesPerWord"
clsWordCount = "WordCount"
clsFeatureCount = "FeatureCount"
clsFreqSig = "FreqSig"
clsPrunegraph = "PruneGraph"
clsAggregate="AggrPerFt"
clsSimCount="SimCounts1WithFeatures"
clsSimSort="SimSort"




dataset="news1k_parses"
s = 10
t = 10
p = 500

dataset = sys.argv[1]
wc = sys.argv[2]
s  = sys.argv[3]
t  = sys.argv[4]
p  = sys.argv[5]
significance  = sys.argv[6]
simsort_limit = sys.argv[7]



clsFreqSig = clsFreqSig+significance
fileHandle = open(dataset+"_s"+str(s)+"_t"+str(t)+"_p"+str(p)+"_"+significance+"_simsort"+simsort_limit+".sh", 'w')
sys.stdout = fileHandle

context_out = dataset+"__"+clsContext
dataset_filter= dataset+"__"+clsContextPruning+"_"+wc;
context_filter_out = dataset_filter+"__"+clsContext
context_filter_p=[]
context_filter_p.append("contextout="+context_out)
context_filter_p.append("out="+context_filter_out)
context_filter_p.append("w="+wc)

wordcount_out = dataset+"__"+clsWordCount
featurecount_out = dataset+"__"+clsFeatureCount
freqsig_p=[]
freqsig_p.append("s="+str(s))
freqsig_p.append("t="+str(t))
freqsig_out = dataset_filter+"__"+clsFreqSig+"_"+"_".join(freqsig_p).replace("=","_")
freqsig_p.append("wordcountout="+wordcount_out)
freqsig_p.append("featurecountout="+featurecount_out)
freqsig_p.append("contextout="+context_filter_out)
freqsig_p.append("freqsigout="+freqsig_out)
prunegraph_p=[]
prunegraph_p.append("p="+str(p))
prunegraph_out = freqsig_out+"__"+clsPrunegraph+"_"+"_".join(prunegraph_p).replace("=","_")
prunegraph_p.append("freqsigout="+freqsig_out)
prunegraph_p.append("prunegraphout="+prunegraph_out)
aggregate_out=prunegraph_out+"__"+clsAggregate
simcount_p=[]
simcount_out=aggregate_out+"__"+clsSimCount+"_".join(simcount_p).replace("=","_")

simsort_out=simcount_out+"__"+clsSimSort
simsort_p=[]
simsort_p.append("limit="+simsort_limit)
simsort_out=simcount_out+"__"+clsSimSort+"_".join(simsort_p).replace("=","_")
simsort_p.append("IN="+simcount_out)
simsort_p.append("OUT="+simsort_out)


removeOutputfolders()

#group by commands 
printHadoop(clsUniq,dataset,dataset+"_pre_WC",True,"0,2,3")
printHadoop(clsUniq,dataset,dataset+"_pre_WFC",True,"0,1,2,3,4")
printHadoop(clsUniq,dataset,dataset+"_pre_FC",True,"1,2,4")
printHadoop(clsUniq,dataset+"_pre_WC",wordcount_out,True,"0")
printHadoop(clsUniq,dataset+"_pre_FC",featurecount_out,True,"0")
printHadoop(clsUniq,dataset+"_pre_WFC",context_out,True,"0,1")


#printHadoop(clsContext,dataset,context_out,True)
printPig(clsContextPruning,context_filter_p)

#printHadoop(clsFeatureCount,context_filter_out,featurecount_out,True)
#printHadoop(clsWordCount,context_filter_out,wordcount_out,True)
printPig(clsFreqSig,freqsig_p)
printPig(clsPrunegraph,prunegraph_p)
printHadoop(clsAggregate,prunegraph_out,aggregate_out,True)
printHadoop(clsSimCount,aggregate_out,simcount_out,True)
printPig(clsSimSort,simsort_p)





#create output prefix folder
if(len(sys.argv)>9):
	server = sys.argv[8]
	prefix = sys.argv[9]
	pos = prefix.rfind("/")
	if(pos > -1):
		print " ".join(["ssh", server, " 'mkdir -p "+prefix[0:pos],"'"])	
	print " ".join(["hadoop dfs -text ",wordcount_out+"/p*" ,"| ssh", server, " 'cat -> ",prefix+wordcount_out,"'"])
	print " ".join(["hadoop dfs -text ",freqsig_out+"/p*" ,"| ssh", server, " 'cat -> ",prefix+freqsig_out,"'"])
	print " ".join(["hadoop dfs -text ",featurecount_out+"/p*" ,"| ssh", server, " 'cat -> ",prefix+featurecount_out,"'"])
	print " ".join(["hadoop dfs -text ",simsort_out+"/p*" ,"| ssh", server, " 'cat -> ",prefix+simsort_out,"'"])

