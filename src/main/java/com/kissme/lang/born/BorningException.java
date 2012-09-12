package com.kissme.lang.born;

/**
 * 
 * @author loudyn
 * 
 */
class BorningException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param e
	 */
	public BorningException(Exception e) {
		super(e);
	}

	/**
	 * 
	 * @param message
	 * @param e
	 */
	public BorningException(String message, Exception e) {
		super(message, e);
	}
}
