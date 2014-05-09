package com.ibm.bluej.util.common;

import java.util.*;
import java.util.concurrent.*;


public abstract class ThreadedLoopIterator<T> extends NextOnlyIterator<T> {

	private BlockingQueue<SillyHolder<T>> items = new ArrayBlockingQueue<SillyHolder<T>>(5);
	private LoopThread t;
	
	protected void setQueueSize(int size) {
		items = new ArrayBlockingQueue<SillyHolder<T>>(size);
	}
	
	//needed because the BlockingQueues can't hold nulls
	private static class SillyHolder<T> {
		SillyHolder(T obj) {
			this.obj = obj;
		}
		T obj;
	}
	private static SillyHolder END = new SillyHolder(null); //marker for end of iterator
	private static SillyHolder ERROR = new SillyHolder(null); //marker for error
	private volatile Error error = new Error("unset");
	
	public ThreadedLoopIterator() {
		t = new LoopThread();
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public T getNext() {
		try {
			SillyHolder<T> h = items.take();
			if (h == ERROR) {
				synchronized (ERROR) {
					throw error;
				}
			}
			return h.obj;
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	protected class LoopThread extends Thread {	
		@Override
		public void run() {
			try {
				loop();
				items.put(END);
			} catch (Throwable t) {
				synchronized (ERROR) {
					if (t instanceof Error)
						error = (Error)t;
					else
						error = new Error(t);
					try {items.put(ERROR);}catch (Throwable ign){}
				}			
			}
		}
	}
	
	/**
	 * loop just goes through and puts each thing using add
	 * null is reserved as a terminator
	 */
	protected abstract void loop();
	
	protected void add(T item) {
		try {items.put(new SillyHolder<T>(item));}catch (Exception e) {throw new Error(e);}
	}
}
