package com.kissme.lang;

import java.util.Iterator;

/**
 * 
 * @author loudyn
 * 
 */
abstract class UnmodifiableIterator<E> implements Iterator<E> {
	protected UnmodifiableIterator() {}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
