package com.kissme.lang.invoke;

import java.lang.reflect.Method;

import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
class EmptyArgsMethodInvoker implements Invoker {

	private final Object me;
	private final Method method;

	/**
	 * 
	 * @param who
	 * @param method
	 */
	public EmptyArgsMethodInvoker(Object who, Method method) {
		this.me = who;
		this.method = method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.invoke.Invoker#invoke()
	 */
	public Object invoke() {
		try {

			return method.invoke(me);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

}
