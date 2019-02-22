package ww.midnite.util.model;

import java.util.LinkedList;


public class Queue<T> {

	private final int max;
	private final LinkedList<T> queue;


	public Queue(final int max) {
		this.max = max;
		queue = new LinkedList<T>();
	}


	public T first() {
		return queue.getFirst();
	}


	public T last() {
		return queue.getLast();
	}


	public synchronized void add(final T t) {
		if (full()) {
			queue.removeFirst();
		}
		queue.add(t);
	}


	public synchronized void clear() {
		queue.clear();
	}


	public boolean full() {
		return queue.size() == max;
	}

}
