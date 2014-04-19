package edu.antonym.traindata.prepare;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
public class TrainDataPrepare {

	IRAMDictionary dict;
	public TrainDataPrepare () throws IOException {
		String path = "C:\\Program Files (x86)\\WordNet\\2.1\\dict\\"; //data/WordNet-3.0/dict/
		URL url = new URL("file", null, path);
		IDictionary dic = new Dictionary(url);
		dict = new RAMDictionary(dic, ILoadPolicy.NO_LOAD);
		dict.open();
	}
	/***
	 * test for jwi API
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public void wordNetDataPrepare() throws IOException {
		//set the path of your wordnet's dict directory
		
		WordNetHelper.traverseDictionary(dict);
		WordNetHelper.saveToFile();
		WordNetHelper.readFile();
		
	}
	
	public void rogetsDataPrepare() {
		AutomaticalCrawling ac = new AutomaticalCrawling();
		ac.traverseWords(dict);
	}
	public static void main(String [] args) throws IOException {
		TrainDataPrepare trainDataPrepare = new TrainDataPrepare();
		//trainDataPrepare.wordNetDataPrepare();
		trainDataPrepare.rogetsDataPrepare();
	}
}
