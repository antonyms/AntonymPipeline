/* This file is part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.tudarmstadt.langtech.ChiWhiDisamb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import de.uni_leipzig.asv.coocc.BinFileMultCol;
import de.uni_leipzig.asv.coocc.BinFileMultColFile;
import de.uni_leipzig.asv.coocc.BinFileMultColPreparer;
import de.uni_leipzig.asv.coocc.BinFileStrCol;
import de.uni_leipzig.asv.coocc.BinFileStrColPreparer;
/**
 * 
 * @author biem
 * contains the chinese whispers graph clustering algorithm
 * as described in Biemann C. (2006): Unsupervised Part-of-Speech Tagging Employing Efficient Graph Clustering. Proceedings of the COLING/ACL-06 Student Research Workshop 2006, Sydney, Australia
 */
public class DisambChineseWhispers {

	private final long seed;
	private final Random r;
	public boolean filesAreAlreadySorted = false;
	private int[] wordHasClass;
	private int curr_largestClass;
	private ArrayList active_nodes;
	public int max_wort_nr;
	private int mode;
	private final boolean d = !true; //debugging
	private Hashtable node_degree;
	private Hashtable curr_Classes;
	public boolean isStillActive = false;
	private int graph_size;
	private int iterations;
	private int currentIteration;
	private String nodeWeightScheme;
	private int disamb_from;
	private int disamb_to;
	private int entryTopN;
	private int edgeTopN;
	private int entryThreshT;
	private int edgeThreshT;
	private int file_edgeThresh = 0;
	private String nodes_file,  edges_file;
	//file access for nodes and edges
	private BinFileStrCol nrStr = null;
	private BinFileMultCol edges = null;
	// for disamb
	private Hashtable active_wnrs;
	private Hashtable activeNeighbours;
	private Hashtable small2orig_wnrs;

	// constructor: init rand num gen
	public DisambChineseWhispers() {
		seed = new Date().getTime();
		r = new Random(seed);
	}
	/**
	 * creates binary format files from input files for use with asv.cooc package
	 * @param nodeListFile
	 * @param edgeListFile
	 * @param weightThresh
	 */
	private void makeBinFiles(String nodeListFile, String edgeListFile, int weightThresh) {
		// NODES
		File nodeList_i = new File(nodeListFile + ".idx");
		File nodeList_b = new File(nodeListFile + ".bin");
		if (!(nodeList_i.exists() && nodeList_b.exists())) {

			new File(nodeListFile + ".idx").delete();
			new File(nodeListFile + ".bin").delete();
			BinFileStrColPreparer b1 = new BinFileStrColPreparer(nodeListFile);
		}
		// THIS is now prepared: this.nrStr = new BinFileStrCol(NodeListFile);

		// EDGES
		File edgeList_i = new File(edgeListFile + ".selected" + ".idx");
		File edgeList_b = new File(edgeListFile + ".selected" + ".bin");

		if (!(edgeList_i.exists() && edgeList_b.exists())) {
			//create _edge_thresh with edge weight threshold
			BufferedReader file_in = null;
			Writer file_out;
			String line;
			String[] columns;
			try {
				file_out = new FileWriter(edgeListFile + ".selected");
				file_in = new BufferedReader(new FileReader(edgeListFile));
				while ((line = file_in.readLine()) != null) {
					columns = line.split("\t");
					if (Integer.parseInt(columns[2]) >= weightThresh) {
						file_out.write(line + "\n");
					}
				}
				file_out.close();
				file_in.close();
			} catch (Exception e) {
				System.err.println("Error while reading file: " + edgeListFile + ".selected");
			}

			BinFileMultColPreparer b2 = new BinFileMultColPreparer(edgeListFile + ".selected", 3);

		} // fi files exist
		else {
			System.out.println("Using existing files; probably different edge threshold!!");
		}
		// THIS is now prepared:  this.edges = new BinFileMultColRam(EdgeListFile+".selected",3);
	}


	List wordNewAndOldAndString;//mapping old and new ID for node_string
	/**
	 * Creates renumbered word and significance lists for gapless storage
	 * @param currentWordlist The filename from un-renumbered wordlist-file.
	 * @param currentSignificanceList The filename from un-renumbered significancelist-file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void reNumber(String currentWordList, String currentSignificanceList) throws FileNotFoundException, IOException {

		System.out.println("*\tRenumbering and sorting labels and edges:");

		Hashtable wortNrsForSort = new Hashtable();
		wordNewAndOldAndString = new ArrayList();

		BufferedReader fileToNewNumerateWL = new BufferedReader(new FileReader(currentWordList));
		String lineFromFileToNewNumerateNodeList;

		int linecount = 0;
		while ((lineFromFileToNewNumerateNodeList = fileToNewNumerateWL.readLine()) != null) {
			linecount++;
			String[] cols = lineFromFileToNewNumerateNodeList.split("\t");
			//System.out.println(cols[0]+"\t"+cols[1]);

			//wortlsistenElemente bekommen neue Nummern
			Integer oldWordNumber = new Integer(Integer.parseInt(cols[0]));
			String word = cols[1];
			Integer newWordNumber = new Integer(linecount);

			wordNewAndOldAndString.add(new NodeRenumberer(newWordNumber.intValue(), oldWordNumber.intValue(), word));

			wortNrsForSort.put(oldWordNumber, new NodeRenumberer(newWordNumber.intValue(), oldWordNumber.intValue(), word));
		}
		fileToNewNumerateWL.close();
		Collections.sort(wordNewAndOldAndString);

		//writing renumbered wordlistfile

		Writer wlOut = new FileWriter(currentWordList + ".renumbered");
		Iterator iter = wordNewAndOldAndString.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			wlOut.write(((NodeRenumberer) o).getNewNodeID() + "\t" + ((NodeRenumberer) o).getWord() + "\n");
		}
		wlOut.close();
		System.out.println("\t--\tWordlist renumbered:\n\t\t-\t" + currentWordList + ".renumbered");

		List newEdgeList = new ArrayList();
		BufferedReader fileToNewNumerateWWL = new BufferedReader(new FileReader(currentSignificanceList));
		String zeileFromFileToNewNumerateWWL;

		int countErrors = 0;
		while ((zeileFromFileToNewNumerateWWL = fileToNewNumerateWWL.readLine()) != null) {
			try {
				String cols[] = zeileFromFileToNewNumerateWWL.split("\t");
				int wordNumberNew1 = ((NodeRenumberer) wortNrsForSort.get(new Integer(Integer.parseInt(cols[0])))).getNewNodeID();
				int wordNumberNew2 = ((NodeRenumberer) wortNrsForSort.get(new Integer(Integer.parseInt(cols[1])))).getNewNodeID();
				int significance = Integer.parseInt(cols[2]);

				newEdgeList.add(new EdgeRenumberer(wordNumberNew1, wordNumberNew2, significance));
				//System.out.println(wordNumberNew1+"\t"+wordNumberNew2+"\t"+significance);
			} catch (Exception e) {
				//mapping funktioniert nicht, wenn wor tnummern aus kollok_sig nicht in wortliste
				//System.err.println("Word number not found in line: "+zeileFromFileToNewNumerateWWL);
				countErrors++;
			}
		}
		if (countErrors > 0) {
			System.err.println("Found " + countErrors + " lines containing wordnumbers that could not be mapped between node list and edge list!");
		}
		fileToNewNumerateWWL.close();

		Collections.sort(newEdgeList);

		//writing renumbered significancelistfile
		Writer wwlOut = new FileWriter(currentSignificanceList + ".renumbered");
		iter = newEdgeList.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			wwlOut.write(((EdgeRenumberer) o).getNewNodeID1() + "\t" + ((EdgeRenumberer) o).getNewNodeID2() + "\t" + ((EdgeRenumberer) o).getEdgeWeight() + "\n");
		}
		wwlOut.close();
		System.out.println("\t--\tSignificancelist renumbered:\n\t\t-\t" + currentSignificanceList + ".renumbered\n*\tdone.");
	}

	/**
	 * initialize process: get graph for target word number
	 * @param goal_wnr
	 * this is also parametrized by class level variables entryTopN, entryThreshT, edgeTopN, edgeThreshT
	 * @return
	 */
	private boolean disamb_init(int goal_wnr) {
		// prepare subgraph for goal_wnr
		ArrayList list = new ArrayList();
		activeNeighbours = new Hashtable();
		active_wnrs = new Hashtable();
		small2orig_wnrs = new Hashtable();
		node_degree = new Hashtable();

		//boolean d=true;
		list = (ArrayList) edges.getData(new Integer(goal_wnr), this.entryTopN);
		graph_size = list.size();
		if (graph_size == 0) {
			return false;
		} else {
			if (d) {
				System.out.println("Constructing subgraph for " + goal_wnr);
			}
			// list of word numbers
			Integer newNumber = new Integer(1);
			for (Iterator it = list.iterator(); it.hasNext();) {
				Integer[] actnode = (Integer[]) it.next();
				if (actnode[1] >= this.entryThreshT) {
					active_wnrs.put(actnode[0], newNumber);
					small2orig_wnrs.put(newNumber, actnode[0]);
					if (d) {
						System.out.println("Adding node nr. " + actnode[0] + " with nr " + newNumber);
					}
					newNumber = newNumber + 1;
				}
			} // rof
			if (d) {
				System.out.println("Processing " + active_wnrs.size() + " active nodes");
			}

			// subgraph: get edges
			for (Enumeration e = active_wnrs.keys(); e.hasMoreElements();) {
				Integer act = (Integer) e.nextElement();
				ArrayList neighbours = new ArrayList();
				if (d) {
					System.out.println("Searching for edges: " + act);
				}
				// get neighbours
				list = (ArrayList) edges.getData(act, this.edgeTopN); // ,limit);
				for (Iterator it = list.iterator(); it.hasNext();) {
					Integer[] actneighbour = (Integer[]) it.next();
					if (d) {
						System.out.println("checking " + actneighbour[0] + " with weight " + actneighbour[1]);
					}
					if ((active_wnrs.containsKey(actneighbour[0])) && (actneighbour[1] >= this.edgeThreshT)) {
						actneighbour[0] = (Integer) active_wnrs.get(actneighbour[0]);
						neighbours.add(actneighbour);
						if (d) {
							System.out.println("Adding edges from " + act + " to " + actneighbour[0]);
						}

					} // fi containskey
				} // rof
				if (neighbours.size() > 0) {
					act = (Integer) active_wnrs.get(act);
					activeNeighbours.put(act, neighbours);
					if (mode == 0) {
						node_degree.put(act, new Double(1.0));
					}
					if (mode == 2) {
						node_degree.put(act, new Double(neighbours.size()));
					}
					if (mode == 1) {
						node_degree.put(act, new Double(Math.log(neighbours.size())));
					}

					if (d) {
						System.out.println("Inverse weight for " + act + " is " + node_degree.get(act));
					}
				} // fi size >0
				else { // delete singletons  from avtive wnrs
					active_wnrs.remove(act);
				} // esle fi


			} // rof enum e
		} // fi list.size>0


		if (d) {
			System.out.println("Graph size: " + graph_size);
		}
		// make wordHasClass arrays
		wordHasClass = new int[graph_size + 1];

		for (int i = 1; i <= graph_size; i++) {
			if (d) {
				System.out.println("Processing nr " + i);
			}
			wordHasClass[i] = i;
		}
		curr_largestClass = graph_size + 1;

		active_nodes = new ArrayList();
		for (Enumeration e = active_wnrs.keys(); e.hasMoreElements();) {
			Integer mapNr = (Integer) (active_wnrs.get(e.nextElement()));
			active_nodes.add(mapNr);
		} // rof active_wnrs.keys


		return true;
	} // end disamb_init

	/**
	 *  perform a step of the chinese whispers iteration
	 */
	private void disamb_iteration_step() {
		Hashtable currentNeighbourhood;
		Hashtable adj_nodes;
		List list;
		List list2;
		Integer nodeID_neighs, edgeWeight_neighs;
		Integer actillness;
		Double dummy, actvalue;
		int maxillness;
		double maxvalue;
		double addval;
		int wnr;

		//boolean d=true;

		// randomize order
		Collections.shuffle(active_nodes, r);

		if (d) {
			System.out.println(" -- new iteration -- ");
		}

		// System.out.println("infect step: r="+r.nextDouble());

		for (Iterator nodes = active_nodes.listIterator(); nodes.hasNext();) {
			wnr = ((Integer) nodes.next()).intValue();


			if (d) {
				System.out.println("Treating word nr" + wnr);
			}



			currentNeighbourhood = new Hashtable();

			if (d) {
				System.out.println("updating node");
			}
			if (activeNeighbours.get(new Integer(wnr)) == null) {
				list = new ArrayList();
			} else {
				list = (ArrayList) activeNeighbours.get(new Integer(wnr));
			}
			if (d) {
				System.out.println("list collected:" + list.toString());
			}

			// Formal proving ... randomize order of list
			adj_nodes = new Hashtable();
			Collections.shuffle(list, r);

			for (Iterator it = list.iterator(); it.hasNext();) {
				adj_nodes.put(it.next(), "dummy");
			} // rof

			for (Enumeration e = adj_nodes.keys(); e.hasMoreElements();) {
				Integer[] actVals = (Integer[]) e.nextElement();
				nodeID_neighs = actVals[0];
				edgeWeight_neighs = actVals[1];
				addval = edgeWeight_neighs.doubleValue();

				if (node_degree.containsKey(new Integer(nodeID_neighs))) {
					addval = addval / ((Double) (node_degree.get(new Integer(nodeID_neighs)))).doubleValue();
					if (d) {
						System.out.println("neigh " + nodeID_neighs + " has degree: " + node_degree.get(new Integer(nodeID_neighs)));
					}
					if (d) {
						System.out.println("Entering " + wnr + " - " + nodeID_neighs + " with weight " + addval);
					}
				} else {
					if (d) {
						System.err.println("neigh " + nodeID_neighs + " has no degree!");
					}
					addval = 0;
				}

				actillness = new Integer(wordHasClass[nodeID_neighs.intValue()]);
				if (currentNeighbourhood.containsKey(actillness)) { //bekannte krankheit
					dummy = ((Double) currentNeighbourhood.get(actillness));
					dummy = new Double(dummy.doubleValue() + addval);
					currentNeighbourhood.put(actillness, dummy);

				} else { // neue krankheit
					currentNeighbourhood.put(actillness, new Double(addval));
				} // esle
			} // rof Iterator it

			if (d) {
				System.out.println("Neighbourhood of ID " + wnr + ":" + currentNeighbourhood.toString());
			}
			//find dominant label in neighbourhood
			maxillness = 0;
			maxvalue = 0.0;
			int actillint = 0;

			// randomize order: put it in list, shuffle list
			list2 = new ArrayList();

			for (Enumeration e = currentNeighbourhood.keys(); e.hasMoreElements();) {
				list2.add(e.nextElement());
			}  // rof Enum e

			Collections.shuffle(list2, r);


			for (Iterator it = list2.iterator(); it.hasNext();) {
				actillint = ((Integer) it.next()).intValue();
				actvalue = (Double) currentNeighbourhood.get(new Integer(actillint));

				if (d) {
					System.out.println(wnr + ": testing for " + actillint + " with value " + actvalue);
				}

				if ((actillint > 0) && (actvalue.doubleValue() > maxvalue)) {
					maxvalue = actvalue.doubleValue();
					maxillness = actillint;
				}
			} // rof enum e currentNeighbourhood

			// endlich anstecken
			if (maxillness == 0) {
				maxillness = wordHasClass[wnr];
			}  // niemand wird gesund.
			wordHasClass[wnr] = maxillness;
			if (d) {
				System.out.println("Infected " + wnr + " with " + maxillness);
			}


		} // rof enum wordnumbers

	}

	/**
	 * Called from outside: sets parameters, starts algorithm, collects and writes results -- main control if DisambChineseWhispers class
	 * @param NodeListFile
	 * @param EdgeListFile
	 * @param _file_edge_thresh
	 * @param _nodeWeightScheme
	 * @param _iterations
	 * @param filename
	 * @param from
	 * @param to
	 * @param targetlistFilename
	 * @param _entry_topN
	 * @param _entry_threshT
	 * @param _edge_topN
	 * @param _edge_threshT
	 */
	public void disamb_start(String NodeListFile, String EdgeListFile, int _file_edge_thresh, String _nodeWeightScheme, int _iterations, String filename, int from, int to, String targetlistFilename, int _entry_topN, int _entry_threshT, int _edge_topN, int _edge_threshT) {

		this.nodes_file = NodeListFile;
		this.edges_file = EdgeListFile;
		this.file_edgeThresh = _file_edge_thresh;
		this.nodeWeightScheme = _nodeWeightScheme;
		this.iterations = _iterations;

		// subgraph parameters
		this.entryTopN = _entry_topN;
		this.entryThreshT = _entry_threshT;
		this.edgeTopN = _edge_topN;
		this.edgeThreshT = _edge_threshT;

		// what to perform disamb on
		Vector targetIDs = new Vector();
		this.node_degree = new Hashtable();
		this.curr_Classes = new Hashtable();

		// defaults
		this.mode = 0;
		disamb_from = -1;
		disamb_to = 0;

		// node weight schemes
		if (_nodeWeightScheme.equals("1")) {
			mode = 0;
		}
		if (_nodeWeightScheme.equals("log")) {
			mode = 1;
		}
		if (_nodeWeightScheme.equals("lin")) {
			mode = 2;
		}

		if (d) {
			System.out.println("Mode: " + mode);
		}

		this.entryTopN = _entry_topN;
		this.disamb_from = from;
		this.disamb_to = to;


		System.out.println("Warning: Files must be sorted! ");
		System.out.println("Input files: " + NodeListFile + " and " + EdgeListFile);
		System.out.println("Node Weight Scheme:" + _nodeWeightScheme);
		System.out.println("Top N selection:\tentry:" + entryTopN + " edge: " + edgeTopN);
		System.out.println("Edge Weight threshold:\tentry:" + entryThreshT + " edge: " + edgeThreshT);




		// computation-intensive processes start here

		makeBinFiles(NodeListFile, EdgeListFile, this.file_edgeThresh);

		this.nrStr = new BinFileStrCol(NodeListFile);
		this.edges = new BinFileMultColFile(EdgeListFile + ".selected", 3);


		// read in the target list and find word numbers
		String line;

		// targe tfilename given: process it. otherwise: fill target with from..to values

		if (!((targetlistFilename == null) || targetlistFilename.length() == 0)) {
			System.out.println("Reading from target list: " + targetlistFilename);
			try {
				int countIn = 0;
				BufferedReader targetsFile = new BufferedReader(new FileReader(targetlistFilename));
				while ((line = targetsFile.readLine()) != null) {
					int curr_num = nrStr.getNumber(line);
					if (curr_num > 0) {
						targetIDs.add(new Integer(curr_num));
						// System.out.print(".");
						countIn++;
					} else {
						System.out.println("Target: " + line + " skipped: not in word list.");
					}
					if ((countIn % 1000) == 0) {
						System.out.println("[" + targetlistFilename + "] processed " + countIn + " lines");
					}
				} // elihw line
				System.out.println();
			} catch (FileNotFoundException e) {
				System.err.println("Error while reading file: " + targetlistFilename + e.getMessage());
			} catch (IOException e) {
				System.err.println("Error while reading file: " + targetlistFilename + e.getMessage());
			}
		} else {
			System.out.println("Preparing target interval: " + targetlistFilename);
			if ((disamb_from == -1) || (disamb_from > nrStr.getMaxWordNr())) {
				disamb_from = nrStr.getMaxWordNr();
			}
			if (disamb_to < 0) {
				disamb_to = 0;
			}
			for (int i = disamb_to; i <= disamb_from; i++) {
				targetIDs.add(new Integer(i));
			} // rof i

		}
		// open file for output
		try {
			Writer writer = new FileWriter(filename);

			System.out.println("Using " + iterations + " iterations per subgraph");
			System.out.println("disambiguating node-wise for " + targetIDs.size() + " targets");
			// System.out.print("\n ["+disamb_from+"] ");

			int countTargets = 0;
			for (Enumeration tIDs = targetIDs.elements(); tIDs.hasMoreElements();) {
				countTargets++;
				if ((countTargets % 1000) == 0) {
					System.out.print("\n [" + countTargets + "] ");
				}
				if ((countTargets % 100) == 0) {
					System.out.print(".");
				}

				int currentID = ((Integer) tIDs.nextElement()).intValue();

				if (disamb_init(currentID)) { // if there is a non-empty subgraph

					for (int it = 1; it < iterations; it++) {  //iteration

						if (!(currentIteration > it)) {
							currentIteration++;
						}


						disamb_iteration_step();


					} // end iteration

					disamb_iteration_step();  //collect random noised

					// output in file stream
					// format:
					// word  <sense-nr>  word1, word2, ...

					Hashtable illnesses = new Hashtable();
					Hashtable words_per_illness = new Hashtable();
					Integer actillness;

					for (Iterator it = active_nodes.listIterator(); it.hasNext();) {
						int i = ((Integer) (it.next())).intValue();
						actillness = new Integer(wordHasClass[i]);
						if (illnesses.containsKey(actillness)) { //bekannte krankheit
							illnesses.put(actillness, new Integer(((Integer) illnesses.get(actillness)).intValue() + 1));
							words_per_illness.put(actillness, new String(((String) words_per_illness.get(actillness)) + ", " + nrStr.getWord((Integer) small2orig_wnrs.get(new Integer(i)))));

						} else { // neue krankheit
							illnesses.put(actillness, new Integer(1));
							words_per_illness.put(actillness, nrStr.getWord((Integer) small2orig_wnrs.get(new Integer(i))));
						} // esle
					} // rof for all labels


					int sensenr = 0;
					for (Enumeration e = words_per_illness.keys(); e.hasMoreElements();) {
						String actVec = (String) words_per_illness.get(e.nextElement());
						String actWord = nrStr.getWord(currentID);
						if (d) {
							System.out.println(actWord + "\t" + sensenr + "\t" + actVec);
						}
						if (!(actWord == null) || (actVec == null)) {   // whysoever.
							writer.write(actWord + "\t" + sensenr + "\t" + actVec + "\n");
							// System.out.println(actWord+"\t"+sensenr+"\t"+actVec+"\n");
						}
						sensenr++;
					} // rof illnesses.keys


				} // fi disamb_init
			} // rof actWnr
			writer.close();
		} catch (Exception e) {
			System.err.println("Write error:" + e.getMessage());
		}
	} // end disamb_start

} // ssalc DisambChineseWhispers