package com.kissme.lang.born;

import java.lang.reflect.Method;

/**
 * 
 * @author loudyn
 * 
 */
class EmptyArgsFactoryMethodBorning<T> implements Borning<T> {
	private final Method method;

	/**
	 * 
	 * @param method
	 */
	EmptyArgsFactoryMethodBorning(Method method) {
		this.method = method;
		this.method.setAccessible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.born.Borning#born(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public T born() {
		try {

			return (T) method.invoke(null);
		} catch (Exception e) {
			throw new BorningException(e);
		}
	}

}
