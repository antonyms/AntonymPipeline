/* This file is part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */


package de.tudarmstadt.langtech.ChiWhiDisamb.test;
import de.tudarmstadt.langtech.ChiWhiDisamb.RunCWD;
/**
 * 
 * @author biem
 * test of ChiWhiDisamb with demo files
 * this is not a unit test
 */
public class Test
{

	public static void main(String args[]) {

		RunCWD p = new RunCWD();
		System.out.println("Testing with demo files\n\n");

		p.ALGOPT="1";
		p.diskThresh=0;
		p.nodeListFile="testdata/demo.nodes";
		p.edgeListFile="testdata/demo.sort.edges";
		p.outFileName="testdata/demo.result.cwd";
		p.run();

		System.out.println("DONEs\n\n");

	}


}
