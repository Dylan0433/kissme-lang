package com.kissme.lang.invoke;

import java.lang.reflect.Method;

import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
class MethodInvoker implements Invoker {

	private final Object me;
	private final Method method;
	private final Object[] args;

	/**
	 * 
	 * @param who
	 * @param method
	 * @param args
	 */
	public MethodInvoker(Object who, Method method, Object[] args) {
		this.me = who;
		this.method = method;
		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.invoke.Invoker#invoke()
	 */
	public Object invoke() {
		try {

			return method.invoke(me, args);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

}
