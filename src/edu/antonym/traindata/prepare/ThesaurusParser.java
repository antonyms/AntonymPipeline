package edu.antonym.traindata.prepare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThesaurusParser {
	ThesaurusCrawling thesaurusCrwaling;
	String content;
	List<String> antonymsList;
	public ThesaurusParser() {
		this.thesaurusCrwaling = new ThesaurusCrawling();
		this.content = thesaurusCrwaling.getWordHtmlContentFromThesaurus("large");
	}
	/**
	 * parse the crawled web contents, and get the list of the antonym of the word
	 * being searched
	 * @return
	 */
	public List<String> antonymsParse() {
		Document doc = Jsoup.parse(content);
		Elements result = doc.getElementsByClass("result");
		Elements antonymsElements = new Elements();
		ArrayList<String> antonyms = new ArrayList<String>();
		for(Element e : result) {
			if(e.getElementsContainingText("Antonyms:").first() != null) {
				antonymsElements.add(e.getElementsContainingText("Antonyms:").first());
			}
		}
		for(Element e : antonymsElements) {
			String temp= e.text();
			temp = temp.substring(temp.lastIndexOf(':') + 1).trim();
			temp.replaceAll("\\s+", "");
			antonyms.addAll(Arrays.asList(temp.split(",")));
		} 
		HashSet<String> setToRemoveDuplicate = new HashSet<String>();
		setToRemoveDuplicate.addAll(antonyms);
		antonyms.clear();
		antonyms.addAll(setToRemoveDuplicate);
		System.out.println(antonyms);
		return antonyms;
	}
	
	/**
	 * parse the crawled web contents, and get the list of the synonyms of the word
	 * being searched
	 * @return
	 */
	public List<String> synonymsParse() {
		Document doc = Jsoup.parse(content);
		Elements result = doc.getElementsByClass("result");
		Elements synonymsElements = new Elements();
		ArrayList<String> synonyms = new ArrayList<String>();
		for(Element e : result) {
			if(e.getElementsContainingText("Synonyms:").first() != null) {
				synonymsElements.add(e.getElementsContainingText("Synonyms:").first());
			}
		}
		for(Element e : synonymsElements) {
			String temp= e.text();
			temp = temp.substring(temp.lastIndexOf(':') + 1).trim();
			temp.replaceAll("\\s+", "");
			synonyms.addAll(Arrays.asList(temp.split(",")));
		} 
		HashSet<String> setToRemoveDuplicate = new HashSet<String>();
		setToRemoveDuplicate.addAll(synonyms);
		synonyms.clear();
		synonyms.addAll(setToRemoveDuplicate);
		System.out.println(synonyms);
		return synonyms;
	}
	
	public static void main(String [] args) {
		ThesaurusParser t = new ThesaurusParser();
		t.synonymsParse();
		t.antonymsParse();
	}
}
