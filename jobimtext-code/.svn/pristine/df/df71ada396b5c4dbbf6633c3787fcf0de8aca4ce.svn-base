/*******************************************************************************
* Copyright 2013
* Copyright (c) 2013 IBM Corp.
* 

* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/

package com.ibm.sai.semantic_role_annotator.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.*;


public class DateExtractor {

	static String months = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|january|february|march|april|may|june|july|september|october|november|december)";
	static String year = "([0-9]{4}|[0-9]{2})", dayORmonth = "[0-9]{1,2}", year4digit = "[0-9]{4}";
	
	static String space = " ";
	
	public static final String DATE_REGEX = "(" 
			+ months + space + dayORmonth + "," + space + year
			+ "|" + dayORmonth + space + months + "(," + space + "|" + space + "')" + year
			+ "|" + dayORmonth + "[-\\\\/]" + dayORmonth + "[-\\\\/]" + year
			+ "|" + "[0-9]{1,2}" + space + months + space + year
			
			+ "|" + "the" + space + "([a-z]{3,15} )?" + year + "s"
			+ "|" + "the" + space + "([a-z]{3,15} )?" + year4digit + "s?"
			+ "|" + months + " '" + year
			+ "|" + months + "(, | ')" + year
			
			+ "|" + dayORmonth + "[st|rd|nd|th]? " + months
			+ "|" + months + " " + dayORmonth
			
			+ ")";
	
	public static final Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX, Pattern.CASE_INSENSITIVE); // Case insensitive is to match also "mar" and not only "Mar" for March

	public static boolean containsDate(String str)
	{
	    Matcher matcher = DATE_PATTERN.matcher(str);
	    return matcher.matches();
	}
	
	
	public static Map<Integer, DateString> extractDates ( String text ) {
		
		Map<Integer, DateString> mapDates = new TreeMap<Integer, DateString>();
		
		Matcher myMatcher = DATE_PATTERN.matcher(text);
		int space = 0;
		while (myMatcher.find()) {
			DateString ds = new DateString();
			ds.setText(myMatcher.group());
			
			//String tmp = text.substring(si, myMatcher.start());
			//space += tmp.length() - tmp.replaceAll("\\s+", "").length();
			
			ds.setBegIndex(myMatcher.start() - space);
			
			//tmp = text.substring(myMatcher.start(), myMatcher.end());
			//space += tmp.length() - tmp.replaceAll("\\s+", "").length();
			
			//si = myMatcher.end();
			
			ds.setEndIndex(myMatcher.end() - space);
			mapDates.put(ds.getBegIndex(), ds);
		}

		return mapDates;
	}
	
	
	public static String createDateExpressions ( String text ) {
		
		Map<Integer, DateString> mapDates = extractDates(text);
		
		for ( DateString dateInstance :  mapDates.values() ) {
			text = text.replace(dateInstance.getText(), dateInstance.getText().replace(" ", "_"));
		}

		return text;
	}
}