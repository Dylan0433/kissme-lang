package com.kissme.lang;

/**
 * 
 * @author loudyn
 * 
 */
public interface Predicate<T> {

	/**
	 * 
	 * @param input
	 * @return
	 */
	boolean apply(T input);
}
