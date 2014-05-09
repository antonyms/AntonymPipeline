package com.ibm.bluej.util.common;

import java.util.*;

public abstract class NextOnlyIterator<T> implements Iterator<T> {
	private T next;
	private boolean done = false;
	
	abstract protected T getNext();

	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasNext() {
		if (done)
			return false;
		if (next == null)
			next = getNext();
		done = next == null;
		return !done;
	}

	@Override
	public T next() {
		if (done)
			return null;
		if (next != null) {
			T toRet = next;
			next = null;
			return toRet;
		}
		return getNext();
	}
}
