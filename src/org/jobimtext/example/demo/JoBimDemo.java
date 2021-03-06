package org.jobimtext.example.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.CAS;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.XMLInputSource;
import org.jobimtext.api.map.DCAThesaurusMap;
import org.jobimtext.api.map.DatabaseThesaurusMap;
import org.jobimtext.api.map.DcaLightThesaurusMap;
import org.jobimtext.api.map.IThesaurusMap;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;
import org.jobimtext.holing.extractor.JobimExtractorConfiguration;
import org.jobimtext.holing.type.JoBim;
import org.apache.uima.fit.factory.AnalysisEngineFactory;

import com.ibm.bluej.util.common.*;
import com.ibm.bluej.util.common.visualization.*;

public class JoBimDemo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static boolean INCLUDE_SENSES = true;
	
	public JoBimDemo() {
	}

	// arguments
	protected static String annotatorDescriptor;
	static IThesaurusMap<String, String> dt;

	private static JobimAnnotationExtractor extractor;

	static AnalysisEngine ae;

	private JTextArea sentence;

	/*
	 * Map<Annotation, HashMap<String, Double>> priors; Map<Annotation,
	 * HashMap<String, Double>> context; Map<Annotation, Map<Integer,
	 * List<String>>> senses;
	 */
	public JSplitPane detailSplit;
	public final static int MAX_EXPANSIONS = 20;
	BasicJTree detailView = new BasicJTree("None Selected");
	BasicJTree sensesView = new BasicJTree("None Selected");
	BasicJTree sensesrankingView = new BasicJTree("None Selected");
	BasicJTree jobimView = new BasicJTree(
			new BasicJTree.GroupingLabel("JoBims"));
	static List<CustomViewManager> customViews = new ArrayList<CustomViewManager>();
	
	JButton processSent = new JButton("Process");
	JButton clearSent = new JButton("Clear");

	public JoBimDemo(boolean withUI) throws Exception {
		/* distSim = new SimpleDistSimProb(dsApi); */
		

		sentence = new JTextArea();
		if (withUI) {
			setBackground(Color.WHITE);
			setLayout(new BorderLayout());
			createComponents();
		}
	}

	private void processSentence() {
		sentence.setEditable(false);
		long time = System.currentTimeMillis();
		// load through annotator gateway
		// AnnotatorGateway gateway =
		// AnnotatorGateway.instance(annotatorDescriptor);
		// CAS cas = gateway.process("This is a test text.", "en", null);
		Collection<JoBim> jobims = getJoBims(ae, sentence.getText());

		System.out.println("Total jobims : " + jobims.size());
				
		System.out.println("parsing took: "
				+ (System.currentTimeMillis() - time) / 1000.0);

		time = System.currentTimeMillis();
		
		

		
		/*
		 * System.out.println("  dca took: " + distSim.getNanoTimeTaken() /
		 * 1000000000.0); System.out.println("  jobim prob took: " +
		 * ContextualizationExample.FuncJoBimProb.nanoTime / 1000000000.0);
		 */

		// populate the JoBim view
		Map<Annotation, ArrayList<JoBim>> joToBims = new IdentityHashMap<Annotation, ArrayList<JoBim>>();
		for (JoBim jb : jobims) {
			String joString = extractor.extract(jb.getKey());
	//		if (dt.getSimilarTerms(joString, MAX_EXPANSIONS).size() < 2)
		//		continue;
			HashMapUtil.addAL(joToBims, jb.getKey(), jb);
			System.out.println("Adding : " + jb.getKey());
		}
		for (CustomViewManager cvm : customViews)
			cvm.setJoBims(joToBims);
		ArrayList<Annotation> inOrderAnnos = new ArrayList<Annotation>(
				joToBims.keySet());
		Collections.sort(inOrderAnnos, new Comparator<Annotation>() {
			public int compare(Annotation o1, Annotation o2) {
				if (o1.getBegin() != o2.getBegin()) {
					return o1.getBegin() - o2.getBegin();
				}
				return o1.getEnd() - o2.getEnd();
			}
		});
		for (Annotation jo : inOrderAnnos) {
			System.out.println("Jo : " + jo.getCoveredText());
			DefaultMutableTreeNode jNode = new DefaultMutableTreeNode(
					new AnnoLabel(extractor.extract(jo), jo));
			// DefaultMutableTreeNode bimList = new
			// DefaultMutableTreeNode("Bims");
			// jNode.add(bimList);
			for (JoBim bim : joToBims.get(jo)) {
				DefaultMutableTreeNode bNode = new DefaultMutableTreeNode(
						bim.getRelation());
				jNode.add(bNode);
				for (int i = 0; i < bim.getValues().size(); ++i) {
					DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(
							new AnnoLabel(extractor.extract(bim.getValues(i)),
									bim.getValues(i)));
					bNode.add(aNode);
				}
			}

			jobimView.top.add(jNode);
		}
		System.out.println("expansion took: "
				+ (System.currentTimeMillis() - time) / 1000.0);
		jobimView.refresh(new FunST<DefaultMutableTreeNode, Boolean>() {
			public Boolean f(DefaultMutableTreeNode n) {
				return n.getLevel() < 2;
			}
		});

		processSent.setEnabled(false);
		clearSent.setEnabled(true);
	}

	private void clearSentence() {
		sentence.setText("");
		sentence.setEditable(true);

		jobimView.top.removeAllChildren();
		jobimView.refresh(null);

		detailView.top.removeAllChildren();
		detailView.top.setUserObject("");
		detailView.refresh(null);

		sensesView.top.removeAllChildren();
		sensesView.top.setUserObject("");
		sensesView.refresh(null);
		
		sensesrankingView.top.removeAllChildren();
		sensesrankingView.top.setUserObject("");
		sensesrankingView.refresh(null);
		
		for (CustomViewManager cvm : customViews)
			cvm.clear();

		processSent.setEnabled(true);
		clearSent.setEnabled(false);
	}

	public static class AnnoLabel {
		public String label;
		public Annotation anno;

		public AnnoLabel(String label, Annotation anno) {
			this.label = label;
			this.anno = anno;
		}

		public String toString() {
			return label;
		}
	}

	private void createComponents() throws IOException {

		Toolkit.getDefaultToolkit().getSystemEventQueue()
				.push(new TCPopupEventQueue());

		sentence.setEditable(true);
		sentence.setLineWrap(true);
		sentence.setWrapStyleWord(true);

		JPanel top = new JPanel(new BorderLayout());
		top.add(new JScrollPane(sentence), BorderLayout.CENTER);

		processSent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processSentence();
			}
		});

		clearSent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearSentence();
			}
		});

		clearSent.setEnabled(false);
		JPanel buttons = new JPanel();
		buttons.add(processSent);
		buttons.add(clearSent);
		top.add(buttons, BorderLayout.EAST);

		JPanel bottom = new JPanel(new BorderLayout());

		jobimView.addTreeSelectionListener(new ExpansionsViewManager(jobimView,
				detailView, dt, extractor));
		
		if (INCLUDE_SENSES) {
			jobimView.addTreeSelectionListener(new SensesRankingViewManager(jobimView,
				sensesrankingView, dt, extractor));
			jobimView.addTreeSelectionListener(new SensesViewManager(jobimView,
					sensesView, dt, extractor));
			
		}

		jobimView.addTreeSelectionListener(new TreeSelectionListener() {
			private DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(
					Color.yellow);

			public void valueChanged(TreeSelectionEvent e) {
				// remove highlights from sentence
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) jobimView
						.getLastSelectedPathComponent();
				if (node != null && node.getUserObject() instanceof AnnoLabel) {
					Annotation jo = ((AnnoLabel) node.getUserObject()).anno;
					sentence.getHighlighter().removeAllHighlights();
					try {
						sentence.getHighlighter().addHighlight(jo.getBegin(),
								jo.getEnd(), painter);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		jobimView.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Expansions", new JScrollPane(detailView));
		
		if (INCLUDE_SENSES) {
			tabs.addTab("Senses", new JScrollPane(sensesView));
			tabs.addTab("Sense Ranking", new JScrollPane(sensesrankingView));
		}
		//iterate over the CustomViewManagers
		for (CustomViewManager cvm : customViews) {
			BasicJTree cView = new BasicJTree("None Selected");
			cvm.initialize(jobimView, cView, extractor, dt);
			tabs.addTab(cvm.getTabName(), new JScrollPane(cView));
			jobimView.addTreeSelectionListener(cvm);
		}

		detailSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(jobimView), tabs);

		bottom.add(detailSplit, BorderLayout.CENTER);

		this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom),
				BorderLayout.CENTER);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static AnalysisEngine getAnalysisEngine(String descriptorName) {
		System.out.println("Loading Analysis Engine: " + descriptorName);
		try {
			if (descriptorName.endsWith(".xml")) {
				XMLInputSource input = null;
				if (descriptorName.startsWith(File.separator)
						|| descriptorName.startsWith("..")) {
					input = new XMLInputSource(descriptorName);
				} else {
					InputStream stream = ClassLoader.getSystemClassLoader()
							.getResourceAsStream(descriptorName);
					input = new XMLInputSource(stream, null);
				}
				ResourceSpecifier desc = UIMAFramework.getXMLParser()
						.parseResourceSpecifier(input);
				return UIMAFramework.produceAnalysisEngine(desc,
						UIMAFramework.newDefaultResourceManager(), null);
			} else {
				Class annoClass = Class.forName(descriptorName);
				return AnalysisEngineFactory.createPrimitive(annoClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e);
		}
	}

	private static Collection<JoBim> getJoBims(AnalysisEngine ae, String text) {
		try {
			CAS cas = ae.newCAS();
			cas.setDocumentText(text);
			cas.setDocumentLanguage("en");
			ae.process(cas);

			// get all JoBims from JCas
			Class clazz = JoBim.class;
			Iterator<Annotation> it = cas.getJCas()
					.getAnnotationIndex(clazz.getField("type").getInt(clazz))
					.iterator();
			ArrayList<JoBim> jobims = new ArrayList<JoBim>();
			while (it.hasNext()) {
				JoBim jb = (JoBim) it.next();
				// System.out.println(jb.getKey().getCoveredText()+" stringer "+joStringer.f(jb.getKey())+" relation "+jb.getRelation());
				jobims.add(jb);
			}
			

			return jobims;
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	
	
	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption("annotator", true,
				"Descriptor of the annotator to use for the holing operation");
		options.addOption("model", true, "Configuration file for the model");
		options.addOption(
				"toString",
				true,
				"Class name of a function to convert a Jo Annotation to a String as stored in the model");
		options.addOption("database", false,
				"the model is stored in a database");
		options.addOption("keyvalue", false,
				"the model is stored in a remote in-memory server");
		options.addOption("local", false,
				"the model should be loaded locally in memory");
		options.addOption("class", false, 
				"the model is the name of a class that implements IThesaurusMap<String,String> with a default constructor");
		//options.addOption("customViewClass", true, "class name of implementation of CustomViewManager");
		
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);
		extractor = JobimExtractorConfiguration.getExtractorFromXmlFile(cmd
				.getOptionValue("toString"));
		
		String modelConfig = cmd.getOptionValue("model");
		if (cmd.hasOption("local")) {
			dt = new DcaLightThesaurusMap(modelConfig);
		} else if (cmd.hasOption("keyvalue")) {
			dt = new DCAThesaurusMap(modelConfig);
		} else if (cmd.hasOption("database")) {
			dt = new DatabaseThesaurusMap(modelConfig);
		} else if (cmd.hasOption("class")) {
			dt = (IThesaurusMap<String,String>)Class.forName(modelConfig).newInstance();
		}else{
			System.err.println(Lang.stringList(args, "; "));
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("JoBimDemo", options);
			System.exit(1);
		}
		dt.connect();

		annotatorDescriptor = cmd.getOptionValue("annotator");
		ae = getAnalysisEngine(annotatorDescriptor);
		
		for (String arg : cmd.getArgs()) {
			try {
				customViews.add((CustomViewManager)Class.forName(arg).newInstance());
			} catch (Throwable t) {
				System.err.println("can not load custom view class: "+arg);
			}
		}
		//if (cmd.hasOption("customViewClass")) {
		//	customViews.add((CustomViewManager)Class.forName(cmd.getOptionValue("customViewClass")).newInstance());
		//}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("JoBimText Semantic Processing Demo");

		long t = System.currentTimeMillis();
		app = new JoBimDemo(true);
		t = System.currentTimeMillis() - t;
		System.out.println("Initialize took: " + t);

		frame.getContentPane().add(app);
		frame.pack();
		frame.setPreferredSize(appSize);
		frame.setSize(appSize);
		frame.setVisible(true);
		app.detailSplit.setDividerLocation(0.5);

	}

	protected static final Dimension appSize = new Dimension(800, 600);
	protected static JFrame frame = new JFrame();
	protected static JoBimDemo app;

}
