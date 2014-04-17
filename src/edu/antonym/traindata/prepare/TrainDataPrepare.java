package edu.antonym.traindata.prepare;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
public class TrainDataPrepare {
	NounSynset nounSynset;
	NounSynset[] hyponyms;
	public TrainDataPrepare () {
		
	}
	/***
	 * test for jwi API
	 * @throws IOException
	 */
	public void jwiTest() throws IOException {
		//set the path of your wordnet's dict directory
		String path = "C:\\Program Files (x86)\\WordNet\\2.1\\dict\\";
		URL url = new URL("file", null, path);
		IDictionary dic = new Dictionary(url);
		IRAMDictionary dict = new RAMDictionary(dic, ILoadPolicy.NO_LOAD);
		dict.open();
		
		System.out.println(WordNetHelper.getSynonyms(dict, "small"));
		WordNetHelper.traverseDictionary(dict);
		System.out.println(WordNetHelper.wordNetAntonymsList);
		System.out.println(WordNetHelper.wordNetSynonymsList);
		IIndexWord iIndexWord = dict.getIndexWord("dog", POS.NOUN);
		IWordID wordID = iIndexWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		
		Set<IPointer> set = iIndexWord.getPointers();
		
		for(IPointer ip :set){
			if(ip.getSymbol().equals(Pointer.ANTONYM.getSymbol())){
				List<IWordID> wordIDs = word.getRelatedWords(ip);
				for(IWordID wordId : wordIDs)
				word = dict.getWord(wordId);
				System.out.println(word);
			}
			//System.out.println(ip.toString());
		}
	}
	/***
	 * test for jaws api
	 */
	public void jawsTest() {
		System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict\\");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets("fly", SynsetType.NOUN);
		for (int i = 0; i < synsets.length; i++) {
			nounSynset = (NounSynset) (synsets[i]);
			hyponyms = nounSynset.getHypernyms();
			System.err.println(nounSynset.getWordForms()[0] + ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + "hyponyms");
		}
		
	}
	public static void main(String [] args) throws IOException {
		TrainDataPrepare trainDataPrepare = new TrainDataPrepare();
		trainDataPrepare.jwiTest();
	}
}
