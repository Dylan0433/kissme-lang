package com.kissme.lang.born;

import java.lang.reflect.Method;

/**
 * 
 * @author loudyn
 * 
 */
class FactoryMethodBorning<T> implements Borning<T> {

	private final Method method;
	private final Object[] args;

	/**
	 * 
	 * @param method
	 * @param args
	 */
	FactoryMethodBorning(Method method,Object[] args) {
		this.method = method;
		this.method.setAccessible(true);
		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.born.Borning#born()
	 */
	@SuppressWarnings("unchecked")
	public T born() {
		try {

			return (T) method.invoke(null, args);
		} catch (Exception e) {
			throw new BorningException(e);
		}
	}

}
