package com.kissme.lang;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author loudyn
 * 
 */
class TransformingSequentialList<Input, Output> extends AbstractSequentialList<Output> implements Serializable {


	final List<Input> fromList;
	final Function<? super Input, ? extends Output> function;

	TransformingSequentialList(List<Input> fromList, Function<? super Input, ? extends Output> function) {
		this.fromList = fromList;
		this.function = function;
	}

	@Override
	public ListIterator<Output> listIterator(int index) {
		final ListIterator<Input> delegate = fromList.listIterator(index);
		return new ListIterator<Output>() {

			@Override
			public boolean hasNext() {
				return delegate.hasNext();
			}

			@Override
			public Output next() {
				return function.apply(delegate.next());
			}

			@Override
			public boolean hasPrevious() {
				return delegate.hasPrevious();
			}

			@Override
			public Output previous() {
				return function.apply(delegate.previous());
			}

			@Override
			public int nextIndex() {
				return delegate.nextIndex();
			}

			@Override
			public int previousIndex() {
				return delegate.previousIndex();
			}

			@Override
			public void remove() {
				delegate.remove();
			}

			@Override
			public void set(Output e) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void add(Output e) {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public int size() {
		return fromList.size();
	}

	@Override
	public void clear() {
		fromList.clear();
	}

	private static final long serialVersionUID = 1L;
}
