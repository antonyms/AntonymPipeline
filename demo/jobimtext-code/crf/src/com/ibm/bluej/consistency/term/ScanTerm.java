/*
Copyright (c) 2012 IBM Corp. and Michael Glass

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

package com.ibm.bluej.consistency.term;

import java.io.Serializable;

import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;




public final class ScanTerm extends CompositeTerm implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	public static ScanTerm make(Object... parts) {
		Term[] ps = new Term[parts.length];
		for (int i = 0; i < parts.length; ++i) {
			Object o = parts[i];
			if (o instanceof String) {
				
			}
		}
		return new ScanTerm(ps);
	}
	*/
	/*
	public ScanTerm(Term pred, Term... args) {
		this.parts = parts;
	}
	*/
	public ScanTerm(ATerm... parts) {
		this.parts = parts;
	}
	
	/**
	 * just a test, binds always unchanged
	 * @param ground
	 * @param binds
	 * @return
	 */
	public boolean matches(ATerm ground, Binds binds) {
		binds.quickCheckPoint();
		boolean doesMatch = matchesInner(ground, binds);
		binds.quickRollback();
		return doesMatch;
	}
	
	protected boolean matchesInner(ATerm ground, Binds binds) {	
		if (!(ground instanceof ScanTerm)) {
			return false;
		}
		ScanTerm g = (ScanTerm)ground;
		if (this.parts.length != g.parts.length) {
			return false;
		}
		for (int i = 0; i < parts.length; ++i) {
			if (!parts[i].matchesInner(g.parts[i], binds)) {
				return false;
			}
		}
		return true;
	}
	
	//binds changed if binding is possible
	/*
	public boolean mayBind(Term ground, Binds binds) {
		binds.quickCheckPoint();
		if (bind(ground, binds)) {
			return true;
		}
		binds.quickRollback();
		return false;
	}
	*/
	
	public void bind(ATerm ground, Binds binds) {
		ScanTerm g = (ScanTerm)ground;
		for (int i = 0; i < parts.length; ++i) {
			if (parts[i] instanceof ValueTerm) {
				continue;
			}
			parts[i].bind(g.parts[i], binds);
		}
	}
	
	//CONSIDER: hash caching
	//private int hash = 0;
	public int hashCode() {
		//if (hash != 0) {
		//	return hash;
		//}
		int hash = ATerm.mdjbFirst();
		for (int i = 0; i < parts.length; ++i) {
			hash = ATerm.mdjbNext(hash, parts[i].hashCode());
		}
		return hash;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof ScanTerm)) {
			return false;
		}
		ScanTerm g = (ScanTerm)o;
		if (this.parts.length != g.parts.length) {
			return false;
		}
		for (int i = 0; i < parts.length; ++i) {
			if (!parts[i].equals(g.parts[i])) {
				return false;
			}
		}
		return true;
	}
	

	
	public ATerm ground(Binds binds, Updatable neededBy) {
		//only need to copy if at least one part is different...
		ATerm[] partsCopy = new ATerm[parts.length];
		for (int i = 0; i < parts.length; ++i) {
			partsCopy[i] = parts[i].ground(binds, neededBy);
		}
		ScanTerm t = new ScanTerm(partsCopy);
		return t;
	}
	
	//basic version that only deals with non-nested ScanTerms
	public static ScanTerm basicFromString(String str) {
		str = str.trim();
		str = str.substring(0, str.length()-1);
		String[] parts = str.split("\\(|\\,");
		ATerm[] pt = new ATerm[parts.length];
		ScanTerm t = new ScanTerm(pt);
		for (int i = 0; i < pt.length; ++i) {
			String p = parts[i].trim();
			pt[i] = ValueTerm.basicFromString(p);
		}
		if (SGDebug.LOGGING) SGLog.finest("fromString: "+t);
		return t;
	}
	
	public String toString() {
		if (parts.length == 0) {
			return "()";
		}
		StringBuffer buf = new StringBuffer();
		int begin = 0;
		if (parts[0] instanceof StringTerm) {
			buf.append(parts[0].toString());
			++begin;
		}
		buf.append("(");
		for (int i = begin; i < parts.length; ++i) {
			if (i > begin) {
				buf.append(", ");
			}
			buf.append(parts[i].toString());
		}
		buf.append(")");
		return buf.toString();
	}
	
	public int valueHash() {
		int hash = ATerm.mdjbFirst();
		for (int i = 0; i < parts.length; ++i) {
			hash = ATerm.mdjbNext(hash, parts[i].valueHash());
		}
		return hash;
	}
	public boolean valueEquals(ATerm t) {
		if (t instanceof Function) {
			t = ((Function)t).getValue();
		}
		if (!(t instanceof ScanTerm)) {
			return false;
		}
		ScanTerm g = (ScanTerm)t;
		if (this.parts.length != g.parts.length) {
			return false;
		}
		for (int i = 0; i < parts.length; ++i) {
			if (!parts[i].valueEquals(g.parts[i])) {
				return false;
			}
		}
		return true;
	}
	public ATerm valueClone() {
		ATerm[] cparts = new ATerm[parts.length];
		for (int i = 0; i < parts.length; ++i) {
			cparts[i] = parts[i].valueClone();
		}
		return new ScanTerm(cparts);
	}
}
