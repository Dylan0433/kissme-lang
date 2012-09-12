package com.kissme.lang.eject;

import java.lang.reflect.Method;

import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
class GetterEjector implements Ejector {

	private final Object me;
	private final Method getter;

	/**
	 * 
	 * @param me
	 * @param getter
	 */
	GetterEjector(Object me, Method getter) {
		this.me = me;
		this.getter = getter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.eject.Ejecting#eject()
	 */
	public Object eject() {
		try {

			return getter.invoke(me);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}
}
