package com.kissme.lang.file.filter;

import java.io.FileFilter;

/**
 * 
 * @author loudyn
 * 
 */
public interface CompoFileFilter extends FileFilter {
	/**
	 * 
	 * @param another
	 * @return
	 */
	public CompoFileFilter and(CompoFileFilter another);

	/**
	 * 
	 * @param another
	 * @return
	 */
	public CompoFileFilter or(CompoFileFilter another);

	/**
	 * 
	 * @param another
	 * @return
	 */
	public CompoFileFilter not(CompoFileFilter another);
}
