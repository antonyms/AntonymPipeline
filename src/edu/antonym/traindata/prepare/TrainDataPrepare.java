package edu.antonym.traindata.prepare;
import java.io.IOException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
public class TrainDataPrepare {

	public TrainDataPrepare () {
		
	}
	/***
	 * test for jwi API
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public void wordNetDataPrepare() throws IOException {
		//set the path of your wordnet's dict directory
		String path = "data/WordNet-3.0/dict/";
		URL url = new URL("file", null, path);
		IDictionary dic = new Dictionary(url);
		IRAMDictionary dict = new RAMDictionary(dic, ILoadPolicy.NO_LOAD);
		dict.open();
		
		WordNetHelper.traverseDictionary(dict);
		WordNetHelper.saveToFile();
		WordNetHelper.readFile();
		
	}
	
	public static void main(String [] args) throws IOException {
		TrainDataPrepare trainDataPrepare = new TrainDataPrepare();
		trainDataPrepare.wordNetDataPrepare();
	}
}
