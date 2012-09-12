package com.kissme.lang;

/**
 * 
 * @author loudyn
 * 
 */
public interface Each<Which> {

	/**
	 * 
	 * @param which
	 */
	public void invoke(int index, Which which);
}
