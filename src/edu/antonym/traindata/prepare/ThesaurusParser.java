package edu.antonym.traindata.prepare;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThesaurusParser {
	ThesaurusCrawling thesaurusCrwaling;
	String content;
	String word;
	List<String> antonymsList;
	public ThesaurusParser() {
		this.thesaurusCrwaling = new ThesaurusCrawling();
		word = "big deal";
		this.content = thesaurusCrwaling.getWordHtmlContentFromThesaurus(word);
	}
	public ThesaurusParser(String word) {
		this.thesaurusCrwaling = new ThesaurusCrawling();
		this.word = word;
		this.content = thesaurusCrwaling.getWordHtmlContentFromThesaurus(word);
	}
	/**
	 * parse the crawled web contents, and get the list of the antonym of the word
	 * being searched
	 * @return
	 */
	public List<String> antonymsParse() {
		Document doc = Jsoup.parse(content);
		Elements result = doc.getElementsByClass("result");
		Set<String> antonyms = new HashSet<String>();
		int k = 0;
		int flag = -1;
		while (k < result.size()) {
			Element entry = result.get(k).getElementsContainingText("Main Entry:").first();
			if (entry != null) {
				flag = -1;
				String temp = entry.text();
				temp = temp.substring(temp.lastIndexOf(':') + 1).trim();
				if (temp.equals(word)) {
					flag = 0;
				}				
				k++;
				continue;
			}
			if (flag == 0) {				
				Element ant = result.get(k).getElementsContainingText("Antonyms:").first();
				if (ant != null) {
					String temp = ant.text();
					temp = temp.substring(temp.lastIndexOf(':') + 1).trim();
					List<String> words = Arrays.asList(temp.split(","));
					for (String w : words) {
						antonyms.add(w.trim().replaceAll(" ", "_").replaceAll("\\*", ""));
					}
					flag = -1;
				}
			}
			k++;
		}
		return new ArrayList<String>(antonyms);
	}
	
	/**
	 * parse the crawled web contents, and get the list of the synonyms of the word
	 * being searched
	 * @return
	 */
	public List<String> synonymsParse() {
		Document doc = Jsoup.parse(content);
		Elements result = doc.getElementsByClass("result");
		Set<String> synonyms = new HashSet<String>();
		int k = 0;
		int flag = -1;
		while (k < result.size()) {
			Element entry = result.get(k).getElementsContainingText("Main Entry:").first();
			if (entry != null) {
				flag = -1;
				String temp = entry.text();
				temp = temp.substring(temp.lastIndexOf(':') + 1).trim();
				if (temp.equals(word)) {
					flag = 0;
				}				
				k++;
				continue;
			}
			if (flag == 0) {				
				Element ant = result.get(k).getElementsContainingText("Synonyms:").first();
				if (ant != null) {
					String temp = ant.text();
					temp = temp.substring(temp.lastIndexOf(':') + 1).trim();
					List<String> words = Arrays.asList(temp.split(","));
					for (String w : words) {
						synonyms.add(w.trim().replaceAll(" ", "_").replaceAll("\\*", ""));
					}
					flag = -1;
				}
			}
			k++;
		}
		return new ArrayList<String>(synonyms);
	}
	
	
	public static void main(String [] args) {
		ThesaurusParser t = new ThesaurusParser("a la carte");
		System.out.println(t.synonymsParse());
		System.out.println(t.antonymsParse());
	}
}
