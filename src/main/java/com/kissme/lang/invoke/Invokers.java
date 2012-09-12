package com.kissme.lang.invoke;

import java.lang.reflect.Method;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Invokers {

	/**
	 * 
	 * @param who
	 * @param method
	 * @return
	 */
	public static Invoker newEmptyArgsMethodInvoking(Object who, Method method) {
		return new EmptyArgsMethodInvoker(who, method);
	}

	/**
	 * 
	 * @param who
	 * @param method
	 * @param args
	 * @return
	 */
	public static Invoker newMethodInvoking(Object who, Method method, Object[] args) {
		return new MethodInvoker(who, method, args);
	}

	private Invokers() {}

}
