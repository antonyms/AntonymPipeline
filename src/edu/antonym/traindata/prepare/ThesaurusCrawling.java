package edu.antonym.traindata.prepare;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThesaurusCrawling {
	    /**
	     * read the contents of the webpage
	     */
	    public String getWordHtmlContentFromThesaurus(String word) {
	        URL url;
	        String temp;
	        StringBuffer sb = new StringBuffer();
	        word = word.replaceAll(" ", "%20");
	        try {
	            url = new URL("http://m.dictionary.com/synonym/" + word);
	            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "gbk"));// read the webpage
	            while ((temp = in.readLine()) != null) {
	                sb.append(temp+'\n');
	            }
	            in.close();
	        } catch (final MalformedURLException me) {
	            System.out.println("error with your URL format!");
	            me.getMessage();
	        } catch (final IOException e) {
	            e.printStackTrace();
	        }
	        return sb.toString();
	    }

	    /**
	     *
	     * @param s
	     * @return get the title of the webpage
	     */
	    public String getTitle(String s) {
	        String regex;
	        String title = "";
	        List<String> list = new ArrayList<String>();
	        regex = "<title>.*?</title>";
	        Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
	        Matcher ma = pa.matcher(s);
	        while (ma.find()) {
	            list.add(ma.group());
	        }
	        for (int i = 0; i < list.size(); i++) {
	            title = title + list.get(i);
	        }
	        return outTag(title);
	    }

	    /**
	     *
	     * @param s
	     * @return get the links
	     */
	    public List<String> getLink(String s) {
	        String regex;
	        List<String> list = new ArrayList<String>();
	        regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
	        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
	        Matcher ma = pa.matcher(s);
	        while (ma.find()) {
	            list.add(ma.group());
	        }
	        return list;
	    }

	    /**
	     *
	     * @param s
	     * @return get the script
	     */
	    public List<String> getScript(String s) {
	        String regex;
	        List<String> list = new ArrayList<String>();
	        regex = "<SCRIPT.*?</SCRIPT>";
	        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
	        Matcher ma = pa.matcher(s);
	        while (ma.find()) {
	            list.add(ma.group());
	        }
	        return list;
	    }
	    
	    public List<String> getNews(String s) {
	        String regex = "<a.*?</a>";
	        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
	        Matcher ma = pa.matcher(s);
	        List<String> list = new ArrayList<String>();
	        while (ma.find()) {
	            list.add(ma.group());
	        }
	        return list;
	    }

	    /**
	     *
	     * @param s
	     * @return get the CSS
	     */
	    public List<String> getCSS(String s) {
	        String regex;
	        List<String> list = new ArrayList<String>();
	        regex = "<style.*?</style>";
	        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
	        Matcher ma = pa.matcher(s);
	        while (ma.find()) {
	            list.add(ma.group());
	        }
	        return list;
	    }

	    /**
	     *
	     * @param s
	     * @return remove marks
	     */
	    public String outTag(final String s) {
	        return s.replaceAll("<.*?>", "");
	    }
	    
	    public static void main(String[] args) {
	    	ThesaurusCrawling t = new ThesaurusCrawling();
	        String content = t.getWordHtmlContentFromThesaurus("big");
	        System.out.println(content);
	        System.out.println(t.getTitle(content));
	        List<String> a = t.getNews(content);
	        List<String> news = new ArrayList<String>();
	        for (String s : a) {
	            news.add(s.replaceAll("<.*?>", ""));
	        }
	        System.out.println(news);
	    }

}
