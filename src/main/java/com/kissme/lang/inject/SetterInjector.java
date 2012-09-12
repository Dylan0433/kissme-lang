package com.kissme.lang.inject;

import java.lang.reflect.Method;

import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
class SetterInjector implements Injector {

	private final Object me;
	private final Method setter;

	/**
	 * 
	 * @param me
	 * @param setter
	 */
	SetterInjector(Object me, Method setter) {
		this.me = me;
		this.setter = setter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.inject.Injecting#inject(java.lang.Object)
	 */
	public void inject(Object value) {
		try {

			setter.invoke(me, value);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

}
