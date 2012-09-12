package com.kissme.lang.born;

import java.lang.reflect.Constructor;

/**
 * 
 * @author loudyn
 * 
 */
class ConstructorBorning<T> implements Borning<T> {

	private final Constructor<T> constructor;
	private final Object[] args;

	/**
	 * 
	 * @param constructor
	 */
	ConstructorBorning(Constructor<T> constructor, Object[] args) {
		this.constructor = constructor;
		this.constructor.setAccessible(true);
		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.born.Borning#born()
	 */
	public T born() {
		try {
			
			return constructor.newInstance(args);
		} catch (Exception e) {
			throw new BorningException(e);
		}
	}
}
