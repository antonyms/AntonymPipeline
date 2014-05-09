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

import os
import sys
import subprocess
import re

def getFolders(dir):
	list = set()
	number_regex = re.compile('.*part-(\d+).*')
	cat = subprocess.Popen(["hadoop", "dfs", "-ls",dir], stdout=subprocess.PIPE)
	for line in cat.stdout:
		ls = line.split()
		if len(ls)==8:
			if ls[len(ls)-1][0]=='.':
				continue
			if line[0]=='d':
				getFolders(ls[len(ls)-1])
			else:
				file = ls[len(ls)-1]	
				res= number_regex.search(file)
				if res is not None:
					list.add(int(res.group(1)))
	missing = 0
	num = len(list)	
	mmax = max (list)
	for i in range(0,mmax+1):
		if i not in list:
			print "Folder: " + dir +" is missing: "+ str(i)
			missing +=1
		else:
			list.remove(i)
	if len(list)>0:
		print"Folder: " +dir +" contains additional files: "+str(list)		


getFolders(sys.argv[1])
