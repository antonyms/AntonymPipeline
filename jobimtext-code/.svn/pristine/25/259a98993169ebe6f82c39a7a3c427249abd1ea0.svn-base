/*
Copyright (c) 2012 IBM Corp. and Michael Glass

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.consistency.validate;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SGLog {
	private static Logger logger = Logger.getLogger("consistency");
	private static final String stackIndent() {
		int depth = Thread.currentThread().getStackTrace().length;
		StringBuffer ind = new StringBuffer();
		//5 is the depth of the main function plus the depth of the logging calls - I think...
		for (int i = 0; i < depth-5; ++i) {
			ind.append("  ");
		}
		return ind.toString();
	}
	public static void info(String toLog) {
		logger.info(toLog);
	}
	public static void fine(String toLog) {
		logger.fine(toLog);
	}
	public static void finer(String toLog) {
		logger.finer(toLog);
	}
	public static void finest(String toLog) {
		logger.finest(toLog);
	}
	public static void setLevel(Level l) {
		//System.out.println("Log level at "+l);
		logger.setLevel(l);
		Logger.getLogger("").getHandlers()[0].setLevel(l); 	
	}
	
	static {
		Logger.getLogger("").getHandlers()[0].setFormatter(new Formatter() {
			public String format(LogRecord rec) {
				return stackIndent()+rec.getMessage()+"\n";
			}		
		});
	}
}
