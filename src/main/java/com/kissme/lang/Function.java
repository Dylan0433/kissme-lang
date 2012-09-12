package com.kissme.lang;

/**
 * 
 * @author loudyn
 * 
 */
public interface Function<Input, Output> {
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	Output apply(Input input);
}
