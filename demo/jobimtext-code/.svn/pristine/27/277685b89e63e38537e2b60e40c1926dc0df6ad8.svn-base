/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.consistency.interactive;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.formula.RemovableFormula;
import com.ibm.bluej.consistency.proposal.Proposal;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.CompositeTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.SparseVectorTerm;
import com.ibm.bluej.consistency.term.TermCollection;
import com.ibm.bluej.consistency.term.Updatable;
import com.ibm.bluej.consistency.util.IRandomAccessible;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.MutableDouble;
import com.ibm.bluej.util.common.MutableDoubleHashMap;
import com.ibm.bluej.util.common.MutableInteger;
import com.ibm.bluej.util.common.RandomUtil;
import com.ibm.bluej.util.common.Warnings;


/*TODO:
  search to map
  show state - using passed posGrnd to html class
    a predicate can be seen as an annotation over a span
    use a highlighting function
    show the stringer of the predicate in a mouse over
    how to show what it is linked to?
    sounds like this recreates UIMA Annotation viewer
      but in HTML
      and for Consistency
  add objective, search to MAP
  allow nowTrue, nowFalse
  beginDelta, endDelta, showDelta
  commit, rollback
  findFormula and showWeight, showImmediateWeight, etc
 */
public class InteractiveControl extends Thread {
	//CONSIDER: read from serialization.properties
	public static String connectionFileDir = "/home/mrglass/debugServer/connectionFiles/";
	
	public static final int DEFAULT_PORT = 4756;
	public static final int CHECK_PERIOD = 100;
	
	static final String MATCHING_TERMS = "Matching terms";
	static final String MATCHING_FORMULAS = "Matching formulas";
	static final String LINK_TOO_OLD = "Link too old";
	static final String DUPLICATE_PAGE_ID = "Duplicate pageId";
	static final String RESUMED = "Resumed";
	static final String EXCEPTION_PREFIX = "EXCEPTION: ";
	
	public static final int MAX_PAGES = 4;
	
	public static void finish() {
		if (control == null) {
			return;
		}
		control.interactivityFinished = true;
		while (control.isAlive()) {
			try {
				if (control.serverSock != null) {
					control.serverSock.close();
				}
				Thread.sleep(100);
			} catch (Exception e) {}	
		}
	}
	
	/*
	private static int periodicCheckCount = 0;
	public static void periodicCheck() {
		++periodicCheckCount;
		 if (periodicCheckCount % CHECK_PERIOD == 0) {
			 checkControl();
			 periodicCheckCount = 0;
		 }
	}
	*/
	
	public static boolean checkContinue() {
		if (control == null) {
			control = new InteractiveControl(DEFAULT_PORT);
			control.start();
		}
		checkControl();
		return control.searchShouldContinue;
	}
	
	private static void checkControl() {
		if (control == null) {
			return;
		}
		synchronized (control) {
			if (control.interactivityRequested) {
				control.interactivityGranted = true;
				control.notify();
				try {
					control.wait();
				} catch (InterruptedException e) {
					System.err.println("Unexpected interupt: ");
					e.printStackTrace();
				}
			}
		}	
	}
	
	private boolean searchShouldContinue = true;
	private boolean interactivityRequested = false;
	private boolean interactivityGranted = false;
	private boolean interactivityFinished = false;
	
	private static InteractiveControl control;
	private InteractiveControl(int port) {
		this.port = port;
		this.setDaemon(true);
	}

	private ServerSocket serverSock;
	private int port; // created by constructor
	//copy paste essentials from RPCListener
	//  basically start this as a thread, listen on a port
	//  get requests in and spit responses out
	//  but we won't create a new thread to process each request
	public void run() {
		while (true) {
			try {
				serverSock = new ServerSocket(port);
				break;
			} catch (IOException e1) {
				++port;
			}
		}
		String hostname = null;
		try {
			hostname = java.net.InetAddress.getLocalHost().getHostName();
			File connectionFile = new File(connectionFileDir+hostname+"."+port);
			FileUtil.writeFileAsString(connectionFile, hostname+":"+port);
			connectionFile.deleteOnExit();
		} catch (Throwable e1) {
			//System.err.println("Unknown host when trying to write connection file.");
			if (Warnings.limitWarn("consistency.InteractiveControl", 20, "Could not start interactive control"))
				e1.printStackTrace();
			return;
		}
		
		System.out.println("Interactive control available on "+hostname+":"+port);
		while (!interactivityFinished) {
			try {
				Socket client = serverSock.accept();
				// System.out.println("Accepted a connection from: "+client.getInetAddress());
				// System.out.println("Trying to get streams");
				ObjectOutputStream oos = new ObjectOutputStream(
						client.getOutputStream());
				// System.out.println("Got one");
				ObjectInputStream ois = new ObjectInputStream(
						client.getInputStream());

				// System.out.println("Trying to read obj");
				SGIRequest in = (SGIRequest) ois.readObject();

				oos.writeObject(process(in));

				oos.flush();
				// client.shutdownInput();
				// client.shutdownOutput();
				// close streams and connections
				ois.close();
				oos.close();
				client.close();
			} catch (Exception e) {
				if (!interactivityFinished) {
					System.err.println("Exception in consistency interactivity: ");
					e.printStackTrace();
				}
			}
		}
	}
	
	private Random rand = new Random();
	private LinkedHashMap<Integer, Object> links = new LinkedHashMap<Integer, Object>();
	private MutableInteger linkIdGen = new MutableInteger(0);
	
	
	//process requests from the debugging remote application (probably webserver)
	public SGIResponse process(SGIRequest request) {	
		if (request instanceof ResumeSGSearch) {
			synchronized (control) {
				control.interactivityRequested = false;
				control.interactivityGranted = false;
				if (((ResumeSGSearch)request).requestStop) {
					control.searchShouldContinue = false;
				}
				control.notify();
			}
			links.clear();
			return new SGIResponse(RESUMED);
		}
		synchronized (control) {
			if (!control.interactivityGranted) {
				control.interactivityRequested = true;
				try {
					this.wait();
				} catch (InterruptedException e) {
					System.err.println("Unexpected interupt: ");
					e.printStackTrace();
				}
			}
		}

		SGIResponse response = null;
		//Link following requests
		if (request instanceof FollowLink) {
			FollowLink req = (FollowLink)request;
			response = followLink(req);
		} else if (request instanceof QueryTerm) {
			QueryTerm req = (QueryTerm)request;
			if (req.showFormulas) {
				response = queryFormulas(new ScanTerm(req.parts), req.maxRequested, req.isPositive);
			} else {
				response = queryTerm(new ScanTerm(req.parts), req.maxRequested);
			}
		} else if (request instanceof SimpleRequest) {
			//CONSIDER: many requests should auto-resume, some requests shouldn't even pause
			SimpleRequest req = (SimpleRequest)request;
			response = new SGIResponse(req.request);
			if (req.request.equals("formulas")) {
				Iterator<Formula> fit = SGSearch.getCRFState().formFinder.getFormulas();
				RandomUtil.Sample<Formula> formSample = new RandomUtil.Sample<Formula>(100);
				int remFormulaCount = 0;
				while (fit.hasNext()) {
					Formula f = fit.next();
					if (f instanceof RemovableFormula) {
						++remFormulaCount;
						formSample.maybeSave(f);
					} else {
						response.addSimpleRow(f.toString());
					}
				}
				if (remFormulaCount > 100) {
					response.addSimpleRow("Sample of 100 dynamic formulas:");
				} else {
					response.addSimpleRow("All dynamic formulas:");
				}
				for (Formula f : formSample) {
					response.addSimpleRow(f.toString());
				}
			} else if (req.request.equals("constants")) {
				for (String con : SGSearch.getCRFDescription().constants.keySet()) {
					response.addSimpleRow(con);
				}
			} else if (req.request.equals("groundCount")) {
				response.addSimpleRow("Total positive ground", ""+SGSearch.getCRFState().posGrnd.size());
				//per predicate counts
				HashMap<String, MutableDouble> predicateCounts = new HashMap<String,MutableDouble>();
				Iterator<IndexEntry> pos = SGSearch.getCRFState().posGrnd.getGroundTerms();
				while (pos.hasNext()) {
					ScanTerm t = pos.next().term;
					if (t.parts.length > 0) {
						MutableDoubleHashMap.increase(predicateCounts, t.parts[0].toString(), 1.0);
					} else {
						MutableDoubleHashMap.increase(predicateCounts, "*ZERO_LENGTH_TERMS*", 1.0);
					}
				}
				for (Map.Entry<String, MutableDouble> e : predicateCounts.entrySet()) {
					response.addSimpleRow(e.getKey(), e.getValue().toString());
				}
				response.addSimpleRow("Total negated ground", ""+SGSearch.getCRFState().negGrnd.size());
				
			} else if (req.request.equals("proposals")) {
				if (SGSearch.getCRFDescription().proposals == null) {
					response.addSimpleRow("Proposals not declaratively specified.");
				} else {
					for (Proposal p : SGSearch.getCRFDescription().proposals.getProposals()) {
						response.addSimpleRow(p.toString());
					}
				}
			}
		}

		if (links.size() > 10000) {
			ArrayList<Integer> toRemove = new ArrayList<Integer>();
			int remove = links.size()/2;
			for (Integer oldest : links.keySet()) {
				toRemove.add(oldest);
				if (--remove <= 0) {
					break;
				}
			}
			for (Integer old : toRemove) {
				links.remove(old);
			}
		}
		
		return response;
	}
	
	private SGIResponse followLink(FollowLink request) {
		Object linked = links.get(request.linkId);
		if (linked == null) {
			return new SGIResponse(EXCEPTION_PREFIX+LINK_TOO_OLD);
		}
		SGIResponse response = new SGIResponse(request.linkType);
		//none of these will have attributes or links
		if (linked instanceof IndexEntry) {		
			for (FormulaCreate c : ((IndexEntry)linked).getCreates()) {
				if (!c.isGarbage()) {
					response.addSimpleRow(c.toString());
				}
			}
		} else if (linked instanceof TermCollection && request.linkType.equals("TermCollection")) {
			for (ATerm t : ((TermCollection)linked)) {
				response.addSimpleRow(displayString(t));
			}
		} else if (linked instanceof SparseVectorTerm) {
			for (Map.Entry<Object, MutableDouble> e : ((SparseVectorTerm)linked).value.entrySet()) {
				response.addSimpleRow(displayString(e.getKey()), e.getValue().toString());
			}
		} else if (linked instanceof Function) {
			for (Updatable u : ((Function)linked).getUsedBy()) {
				response.addSimpleRow(displayString(u));
			}
		}
		return response;
	}
	
	private void makeQueryTermRow(SGIResponse response, IndexEntry e) {
		if (e == null) return;
		 
		SGIResponse.Row row = new SGIResponse.Row();
		//show the term, links include any in-term links (from SparseVectors or Sets)
		row.parts = new String[e.term.parts.length+1];
		for (int i = 0; i < e.term.parts.length; ++i) {
			row.parts[i] = displayString(e.term.parts[i]);
		}
		//properties should be Evid/Def/Reg
		row.attributes = new String[1];
		row.attributes[0] = e.getClass().getSimpleName();
	
		//link to the createList
		row.parts[row.parts.length-1] = link("Creates", "Creates", e);
		response.rows.add(row);
	}
	
	private SGIResponse queryFormulas(ScanTerm t, int maxRequested, boolean isPositive) {
		SGIResponse response = new SGIResponse(MATCHING_FORMULAS);
		IRandomAccessible<Formula> found = SGSearch.getCRFState().formFinder.findMatching(isPositive, t);
		if (maxRequested == 1) {
			response.addSimpleRow(found.getRandom(rand).toString());
		} else if (maxRequested <= 0 || maxRequested >= found.size()) {
			for (Formula e : found) {
				response.addSimpleRow(e.toString());
			}
		} else {
			//take a series of samples
			for (int i = 0; i < maxRequested; ++i) {
				response.addSimpleRow(found.getRandom(rand).toString());
			}
		}
		return response;
	}
	
	private SGIResponse queryTerm(ScanTerm t, int maxRequested) {
		SGIResponse response = new SGIResponse(MATCHING_TERMS);
		IRandomAccessible<IndexEntry> found = SGSearch.getCRFState().posGrnd.find(t, NOBINDS);
		if (SGDebug.INTERACTIVE_CONTROL) {
			System.out.println("found "+found.size()+" matching "+t);
		}
		if (maxRequested == 1) {
			makeQueryTermRow(response, found.getRandom(rand));
		} else if (maxRequested <= 0 || maxRequested >= found.size()) {
			for (IndexEntry e : found) {
				makeQueryTermRow(response, e);
			}
		} else {
			//take a series of samples
			for (int i = 0; i < maxRequested; ++i) {
				makeQueryTermRow(response, found.getRandom(rand));
			}
		}
		return response;
	}
	

	
	
	private Binds NOBINDS = new Binds(null);
	
	String displayString(Object t) {
		StringBuffer buf = new StringBuffer();
		toDisplayString(t, buf);
		return buf.toString();
	}
	
	//adds links when needed
	private void toDisplayString(Object t, StringBuffer buf)
	{
		if (t instanceof TermCollection) {
			TermCollection tc = (TermCollection)t;
			//String noDependTxt = null;
			if (tc.size() > 5) {
				//show first 5 and make link for rest
				buf.append("{");
				int count = 0;
				for (ATerm tci : tc) {
					if (count != 0) {
						buf.append(" ");
					}
					buf.append(tci.toString());
					if (++count >= 5) {
						break;
					}
				}
				
				buf.append(link("...", "TermCollection", tc));
				buf.append("}");
			} else {
				buf.append(tc.toString());
			}
			buf.append(" ");
			buf.append(link("depends", "Depends", t));
		} else if (t instanceof CompositeTerm) {
			if (t instanceof Function) {
				buf.append(t.getClass().getSimpleName());
			}
			if (((CompositeTerm)t).parts != null) {
				buf.append("(");
				for (int i = 0; i < ((CompositeTerm)t).parts.length; ++i) {
					if (i != 0) {
						buf.append(" ");
					}
					toDisplayString(((CompositeTerm)t).parts[i], buf);
				}
				buf.append(")");
			}
			if (t instanceof Function) {
				buf.append("=");
				ATerm v = ((Function)t).getValue();
				if (v == null) {
					buf.append("NULL");
				} else {
					toDisplayString(v, buf);
				}
				buf.append(" ");
				//add link for depends
				buf.append(link("depends", "Depends", t));
			}
		} else if (t instanceof SparseVectorTerm) {
			//link always - I never have small sparse vectors
			SparseVectorTerm sv = (SparseVectorTerm)t;
			buf.append(link("SparseVector ("+sv.value.size()+")", "SparseVector", sv));
		} else {
			buf.append(t.toString());
		}
	}
	
	private String link(String text, String linkType, Object object) {
		String linkText = 
				"<a href=details.con?SGI=true&linkId="+linkIdGen.value+"&linkType="+linkType+">"+
						text+
				"</a>";	
		links.put(linkIdGen.value, object);
		linkIdGen.value++;
		return linkText;
	}
}
