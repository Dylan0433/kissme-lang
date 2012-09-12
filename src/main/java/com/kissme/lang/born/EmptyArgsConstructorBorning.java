package com.kissme.lang.born;

import java.lang.reflect.Constructor;

/**
 * 
 * @author loudyn
 * 
 */
class EmptyArgsConstructorBorning<T> implements Borning<T> {

	private final Constructor<T> constructor;

	/**
	 * 
	 * @param constructor
	 */
	EmptyArgsConstructorBorning(Constructor<T> constructor) {
		this.constructor = constructor;
		this.constructor.setAccessible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.born.Borning#born()
	 */
	public T born() {
		try {

			return constructor.newInstance();
		} catch (Exception e) {
			throw new BorningException(e);
		}
	}

}
