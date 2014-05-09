# Copyright 2012
# FG Language Technologie
# Technische Universitaet Darmstadt
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


import sys;
if len(sys.argv)<4:
	print "needs parameters: inputfile" 
f = open(sys.argv[1],'r')
score=2
pre = ""
for line in f:
	k=line.split("\t")
	if(len(k)>2):
		if(k[1]!=k[0]):
			
			count = float(k[2])
        		if(count >= score):
                		out.write(pre.replace("\n","")+line);
	else:
		print "to short: "+line

f.close()
out.close()
