package com.ibm.bluej.util.common;

import java.util.*;

import com.ibm.bluej.util.common.*;

public class NestedIterable<S,T> implements Iterable<T> {
	Iterable<S> outerIterable;
	FunST<S,Iterator<T>> outer2Inner;
	
	public NestedIterable(Iterable<S> outer, FunST<S,Iterator<T>> outer2Inner) {
		outerIterable = outer;
		this.outer2Inner = outer2Inner;
	}

	@Override
	public Iterator<T> iterator() {
		return new NestedIterator();
	}
	
	class NestedIterator implements Iterator<T> {
		NestedIterator() {
			outer = outerIterable.iterator();
			nextInnerIt();
		}
		
		private void nextInnerIt() {
			if (outer.hasNext()) {
				inner = outer2Inner.f(outer.next());
			} else {
				inner = null;
			}
		}
		
		Iterator<S> outer;
		Iterator<T> inner;
		@Override
		public boolean hasNext() {
			return inner != null && inner.hasNext();
		}

		@Override
		public T next() {
			T ret = inner.next();
			while (inner != null && !inner.hasNext())
				nextInnerIt();
			return ret;
		}

		@Override
		public void remove() {
			inner.remove();
		}
		
	}
}
