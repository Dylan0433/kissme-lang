package com.kissme.lang;

import java.util.Iterator;

/**
 * 
 * @author loudyn
 *
 */
abstract class TransformedIterator<F, E> implements Iterator<E> {
	final Iterator<? extends F> delegate;

	TransformedIterator(Iterator<? extends F> iterator) {
		this.delegate = iterator;
	}

	abstract E transform(F from);

	@Override
	public final boolean hasNext() {
		return delegate.hasNext();
	}

	@Override
	public final E next() {
		return transform(delegate.next());
	}

	@Override
	public final void remove() {
		delegate.remove();
	}

}
