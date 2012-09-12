package com.kissme.lang;

import java.util.NoSuchElementException;

/**
 * 
 * @author loudyn
 * 
 */
abstract class AbstractIterator<E> extends UnmodifiableIterator<E> {
	private State state = State.NOT_READY;

	/** Constructor for use by subclasses. */
	protected AbstractIterator() {}

	private enum State {
		/** We have computed the next element and haven't returned it yet. */
		READY,

		/** We haven't yet computed or have already returned the element. */
		NOT_READY,

		/** We have reached the end of the data and are finished. */
		DONE,

		/** We've suffered an exception and are kaput. */
		FAILED,
	}

	private E next;

	protected abstract E computeNext();

	/**
	 * 
	 * @return
	 */
	protected final E endOfData() {
		state = State.DONE;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public final boolean hasNext() {
		assert state != State.FAILED;
		switch (state) {
		case DONE:
			return false;
		case READY:
			return true;
		default:
		}
		return tryToComputeNext();
	}

	private boolean tryToComputeNext() {
		state = State.FAILED; // temporary pessimism
		next = computeNext();
		if (state != State.DONE) {
			state = State.READY;
			return true;
		}
		return false;
	}

	@Override
	public final E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		state = State.NOT_READY;
		return next;
	}
}
