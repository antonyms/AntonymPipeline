package org.jobimtext.hadoop.mapreducer;


public class StringUtil {
	public static String trim(String word){
		return word.replace((char)65533, ' ').trim().replaceAll("^[\\s\\xA0]+", "").replaceAll("[\\s\\xA0]+$", "");
	}
	public static void main(String[] args) {
		String word =" test ";
		System.out.println("^"+word.trim()+"$");
		word =" test \\xA0";
		
		System.out.println("^"+word.trim()+"$");
	}
}
