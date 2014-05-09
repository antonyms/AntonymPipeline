/* This file is part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.tudarmstadt.langtech.ChiWhiDisamb;

/**
 * 
 * @author biem
 * Class to run ChiWhiDis on the command line: gets called from main class
 */
public class RunCWD {

	public final boolean d=!true;
	public String targetFile = "";
	public int diskThresh = 0;
	public String nodeListFile;
	public String edgeListFile;
	public String ALGOPT = "";
	public String outFileName;
	boolean isFileOut = false;
	boolean isDBOut = false;
	boolean filesAreStillSorted = false;
	boolean gotFileInNames = false;
	boolean gotFileOutNames = false;
	int nr_of_iterations = 10;
	int disamb_from = -1;
	int disamb_to = 0;
	int entry_topN = Integer.MAX_VALUE;
	int edge_topN = Integer.MAX_VALUE;
	int entry_thresh = 0;
	int edge_thresh = 0;

	static String help = "Help for Chinese Whispers Disambiguation Version" +
	"\n\n\tjava -jar -Xmx512M CW.jar [-H -h --help [-F [-i filename1 filename2]] " +
	"-a algorithmoption " +
	"[-d iterationdepth] [-o filename] [-O] [-S] [-from x -to y]|[-targets filename]]" +
	"\n\n\t" +
	"-H | -h | --help\tWrites out this Help.\n\t" +
	"-F\tUse files specified by -i as input.\n\t" +
	"-i\tUse files as input.\n\t\t\t" +
	"filename1\tThe wordlist.\n\t\t\tfilename2\tThe significance list.\n\t" +
	"-a\tSets the algorithmoptions\n\t\t\t" +
	"\"1\"\n\t\t\t" +
	"\"lin\"\n\t\t\t" +
	"\"log\"\n\t" +
	"-d\tnr of iterations x>0 (default x=10).\n\t" +
	"-dt\ton-disk edge threshold x>=0 (default x=0).\n\t" +
	"-t\tedge threshold within entryx>=0 (default x=0).\n\t" +
	"-T\tedge threshold for entry creation x>=0 (default x=0).\n\t" +
	"-n\tedge top N witin entry x>=0 (default x=MAX).\n\t" +
	"-N\tentry creation top N filter x>=0 (default x=MAX).\n\t" +
	"-o\tWrites solutions out into given filename.\n\t\t\t" +
	"filename\t The filename for output.\n\t-S\tDo NOT sort and renumber input.\n" +
	"\t-from\tnumber\tthe word number to start from (largest). -1 means larges available\n" +
	"\t-to\tnumber\tthe word number to end with (smallest)\n" +
	"\t-targets\ttextfile\tfile containing target words. Overrides -from and -to\n\n";



	public void run() {


		boolean gotAlgParam = false;

		String nodeWeightScheme = "";


		DisambChineseWhispers cw = new DisambChineseWhispers();

		if (ALGOPT.equals("1")) {
			nodeWeightScheme = "1";
			gotAlgParam = true;
		}
		if (ALGOPT.equals("log")) {
			nodeWeightScheme = "log";
			gotAlgParam = true;
		}
		if (ALGOPT.equals("lin")) {
			nodeWeightScheme = "lin";
			gotAlgParam = true;
		}

		if (!gotAlgParam) {
			System.err.println("Error!\nNeed Algorithm parameters:\tPlease use -a [\"1\"|\"log\"|\"lin\"]");
			System.exit(0);
		}


		if (d) {System.out.println("calling CW_disamb");}
		cw.disamb_start(nodeListFile, edgeListFile, diskThresh, nodeWeightScheme, nr_of_iterations, outFileName, disamb_from, disamb_to, targetFile, entry_topN,entry_thresh,edge_topN,edge_thresh);


		// end
		System.out.println("\nDONE.\n");
	}

}

