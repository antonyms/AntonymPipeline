package org.jobimtext.holing.parser;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jobimtext.holing.type.Dependency;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;
import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.options.OptionManager;
import org.maltparser.core.symbol.SymbolTable;
import org.maltparser.core.syntaxgraph.DependencyStructure;
import org.maltparser.core.syntaxgraph.edge.Edge;
import org.maltparser.core.syntaxgraph.node.TokenNode;


public class MaltParserAnnotator extends JCasAnnotator_ImplBase {
  
  private Logger logger;
  private MaltParserService parser;
  private SymbolTable symbolTable;
  private String symbolTableName = "DEPREL";
  
  private String modelDirPath = "lib";
  private String modelFileName = "maltparser-en-linear.mco";
  
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);    
    logger = aContext.getLogger();        
    try {
      if (OptionManager.instance().getOptionContainerIndices().size() == 0) {
        OptionManager.instance().loadOptionDescriptionFile(
            MaltParserService.class.getResource("/appdata/options.xml"));
        OptionManager.instance().generateMaps();
      }
      parser = new MaltParserService();
      parser.initializeParserModel("-u " + "file:///"+modelDirPath+'/'+modelFileName + " -m parse");
    }
    catch (MaltChainedException e) {
      logger.log(Level.SEVERE,
          "MaltParser exception while initializing parser model: " + e.getMessage());
    }
    
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    // Iterate over all sentences
    for (Sentence curSentence : select(aJCas, Sentence.class)) {

      // Generate list of tokens for current sentence
      List<Token> tokens = selectCovered(Token.class, curSentence);

      // Generate input format required by parser
      String[] parserInput = new String[tokens.size()];
      for (int i = 0; i < parserInput.length; i++) {
        Token t = tokens.get(i);
        // This only works for the English model. Other models have different input
        // formats. See http://www.maltparser.org/mco/mco.html
        parserInput[i] = String.format("%1$d\t%2$s\t_\t%3$s\t%3$s\t_", i + 1,
            t.getCoveredText(), t.getPOS().getPosValue());
      }
      
      // Parse sentence
      DependencyStructure graph = null;
      try {
        // Parses the sentence
        graph = parser.parse(parserInput);
        symbolTable = graph.getSymbolTables().getSymbolTable(symbolTableName);
      }
      catch (MaltChainedException e) {
        logger.log(Level.WARNING,
            "MaltParser exception while parsing sentence: " + e.getMessage(), e);
        // don't pass on exception - go on with next sentence
        continue;
      }

      /*
       * Generate annotations: NOTE: Index of token in tokenList corresponds to node in
       * DependencyGraph with NodeIndex+1
       */
      try {
        // iterate over all tokens in current sentence
        for (int i = 0; i < tokens.size(); i++) {
          // Start with Node 1 - we omit ROOT-dependencies,
          // because we don't have a ROOT-token.
          TokenNode curNode = graph.getTokenNode(i + 1);

          // iterate over all dependencies for current token
          for (Edge edge : curNode.getHeadEdges()) {
            int sourceIdx = edge.getSource().getIndex();
            int targetIdx = edge.getTarget().getIndex();

            // get corresponding token for node in DependencyGraph
            Token sourceToken = sourceIdx > 0 ? tokens.get(sourceIdx - 1) : null;
            Token targetToken = targetIdx > 0 ? tokens.get(targetIdx - 1) : null;

            // create dep-annotation for current edge
            if (sourceToken != null && targetToken != null) {
              Dependency dep = new Dependency(aJCas, curSentence.getBegin(),
                  curSentence.getEnd());
              dep.setGovernor(sourceToken); // TODO check if source=Governor
              dep.setDependent(targetToken); // TODO check if target=Dependent
              dep.setDependencyType(edge.getLabelSymbol(symbolTable));
              dep.addToIndexes();
            }
          }
        }
      }
      catch (MaltChainedException e) {
        logger.log(Level.WARNING, "MaltParser exception creating dependency annotations: "
            + e.getMessage(), e);
        // don't pass on exception - go on with next sentence
        continue;
      }
    }
    
  }
  
}
