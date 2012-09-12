package com.kissme.lang;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * 
 * @author loudyn
 * 
 */
class TransformingRandomAccessList<Input, Output> extends AbstractList<Output> implements RandomAccess, Serializable {

	final List<Input> fromList;
	final Function<? super Input, ? extends Output> function;

	TransformingRandomAccessList(List<Input> fromList, Function<? super Input, ? extends Output> function) {
		this.fromList = fromList;
		this.function = function;
	}

	@Override
	public Output get(int index) {
		return function.apply(fromList.get(index));
	}

	@Override
	public int size() {
		return fromList.size();
	}

	@Override
	public Output remove(int index) {
		return function.apply(fromList.remove(index));
	}

	@Override
	public boolean isEmpty() {
		return fromList.isEmpty();
	}

	@Override
	public void clear() {
		fromList.clear();
	}

	private static final long serialVersionUID = 1L;
}
