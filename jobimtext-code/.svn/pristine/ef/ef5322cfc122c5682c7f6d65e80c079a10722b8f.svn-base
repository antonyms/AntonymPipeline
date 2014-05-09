/* This file is part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.tudarmstadt.langtech.ChiWhiDisamb;
/**
 * 
 * @author biem
 * Main class: handles command line options and calls the disambiguation clustering process
 */
public class Main
{
	/**
	 * @author biem
	 * @param args The command line options.
	 */
	public static void main(String[] args) {

		boolean readFromFile = false;
		boolean gotError = false;
		//GUI
		if (args.length < 1) {
			System.out.println("Missing parameters. Try -h for help");

		} else {
			boolean algIsGiven = false;
			RunCWD s = new RunCWD();
			for (int i = 0; i < args.length;) {
				//System.out.println(args[i]);
				if (args[i].equals("-F")) {//read from File
					i++;
					readFromFile = true;
				} else if (args[i].equals("-i")) {//files 1. node list 2. edge list
					if (!readFromFile || (i + 2 > args.length) || args[i + 1].startsWith("-") || args[i + 2].startsWith("-")) {
						System.err.println("Input Error!\n\tUsage for fileinput:" +
						"\n\t\tNeed two filenames as input -F -i wordlist-file cooccs-file!\n");
						gotError = true;
					} else {
						s.nodeListFile = args[i + 1];
						s.edgeListFile = args[i + 2];
						s.gotFileInNames = true;
					}
					i += 3;
				} else if (args[i].equals("-a")) {//AlgOpt (top, dist log, dist nolog, vote x.x)
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -a [\"1\"|\"lin\"|\"log\"\n");
						gotError = true;
					} else {
						s.ALGOPT = args[i + 1];
						algIsGiven = true;
					}
					i += 2;
				} else if (args[i].equals("-N")) {//entry_topN parameter
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -N x (with x integer value>0)\n");
						gotError = true;
					} else {
						s.entry_topN = Integer.parseInt(args[i + 1]);
						algIsGiven = true;
					}
					i += 2;
				} else if (args[i].equals("-n")) {//entry_topN parameter
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -n x (with x integer value>0)\n");
						gotError = true;
					} else {
						s.edge_topN = Integer.parseInt(args[i + 1]);
						algIsGiven = true;
					}
					i += 2;
				} else if (args[i].equals("-o")) {//fileoutname
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -o outfilename!\n");
						gotError = true;
					} else {
						s.outFileName = args[i + 1];
						s.gotFileOutNames = true;
					}
					i += 2;
				} else if (args[i].equals("-d")) {//iteration depth
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -d x with x in N for nr of iterations (default 10)!\n");
						gotError = true;
					} else {
						try {
							s.nr_of_iterations = Integer.parseInt(args[i + 1]);
						} catch (Exception e) {
							System.out.println("-d " + args[i + 1] + " is not a number! Now running with -d 10!\n");
						}
					}
					i += 2;
				} else if (args[i].equals("-dt")) {//db edge threshold
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -dt x with x in N for on disk min edge threshold (default 0)!\n");
						gotError = true;
					} else {
						try {
							s.diskThresh = Integer.parseInt(args[i + 1]);
						} catch (Exception e) {
							System.out.println("-dt " + args[i + 1] + " is not a number! Now running with -dt 0!\n");
						}
					}
					i += 2;
				} else if (args[i].equals("-T")) {//db edge threshold
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -T x with x in N for entry node threshold (default 0)!\n");
						gotError = true;
					} else {
						try {
							s.entry_thresh = Integer.parseInt(args[i + 1]);
						} catch (Exception e) {
							System.out.println("-T " + args[i + 1] + " is not a number! Now running with -T 0!\n");
						}
					}
					i += 2;
				} else if (args[i].equals("-t")) {//db edge threshold
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -t x with x in N for subgraph edge threshold (default 0)!\n");
						gotError = true;
					} else {
						try {
							s.edge_thresh = Integer.parseInt(args[i + 1]);
						} catch (Exception e) {
							System.out.println("-t " + args[i + 1] + " is not a number! Now running with -t 0!\n");
						}
					}
					i += 2;
				} else if (args[i].equals("-from")) {// disamb from
					if ((i + 1 > args.length) || (args[i + 1].startsWith("-")) && (!args[i + 1].equals("-1"))) {
						System.err.println("Please use -from x with x =-1 or in node id numbers (default -1 means all!)\n");
						gotError = true;
					} else {
						try {
							s.disamb_from = Integer.parseInt(args[i + 1]);
						} catch (Exception e) {
							System.out.println("-from " + args[i + 1] + " is not a number! Now running with -from -1!\n");
						}
					}
					i += 2;
				} else if (args[i].equals("-to")) {// disamb from
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -to x with x in node id numbers (default 0 means all!)\n");
						gotError = true;
					} else {
						try {
							s.disamb_to = Integer.parseInt(args[i + 1]);
						} catch (Exception e) {
							System.out.println("-to " + args[i + 1] + " is not a number! Now running with -to 0!\n");
						}
					}
					i += 2;
				} else if (args[i].equals("-targets")) {// target file
					if ((i + 1 > args.length) || args[i + 1].startsWith("-")) {
						System.err.println("Please use -targets filename\n");
						gotError = true;
					} else {
						try {
							s.targetFile = args[i + 1];
						} catch (Exception e) {
							System.out.println("-targets " + args[i + 1] + " is something strange\n");
						}
					}
					i += 2;
				} else if (args[i].equals("-H") || args[i].equals("-h") || args[i].equals("--help")) {
					System.out.println(s.help);
					System.exit(1);
				}
				if (gotError) {
					System.err.println(s.help);
					System.exit(0);
				}
			}
			if (!algIsGiven) {
				System.err.println("Please decide for an algorithm option!\n");
				System.err.println(s.help);
				System.exit(0);
			}

			// runs main from console
			s.run();
		}
	}

}


