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
if len(sys.argv)<4:
	print "This script removes all rows within a file, where the value in the specified column is below the specified value"
	print "USAGE: input column below_value"
f=sys.argv[1]
col = int(sys.argv[2])
below=float(sys.argv[3])

for line in open(f):
	line=line.strip()
	ls = line.split("\t")
	if(float(ls[col])<below):
		print line
	
