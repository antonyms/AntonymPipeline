package org.jobimtext.holing.segmenter;

import java.io.Reader;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class WatsonAnalyzer extends StopwordAnalyzerBase {

  protected WatsonAnalyzer(Version version) {
    super(version);
  }

  @Override
  protected TokenStreamComponents createComponents(String arg0, Reader reader) {
    final Tokenizer source = new StandardTokenizer(matchVersion, reader);
    TokenStream result = new StandardFilter(matchVersion, source);
    result = new EnglishPossessiveFilter(result);
    result = new LowerCaseFilter(this.matchVersion, result);
    result = new StopFilter(this.matchVersion, result, stopwords);
    result = new PorterStemFilter(result);
    return new TokenStreamComponents(source, result);
  }

}
