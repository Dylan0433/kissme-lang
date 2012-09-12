package com.kissme.lang;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Strings {

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String capitalize(String s) {
		if (null == s) {
			return null;
		}
		if (s.length() == 0) {
			return "";
		}

		char char0 = s.charAt(0);
		if (Character.isUpperCase(char0)) {
			return s.toString();
		}

		return new StringBuilder(s.length()).append(Character.toUpperCase(char0)).append(s.subSequence(1, s.length())).toString();
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(String text) {
		if (null == text) {
			return true;
		}

		return text.length() <= 0;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isBlank(String text) {
		if (isEmpty(text)) {
			return true;
		}

		int length = text.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param text
	 * @param prefix
	 * @return
	 */
	public boolean startsWith(String text, String prefix) {
		if (isBlank(text)) {
			return false;
		}

		return text.startsWith(prefix);
	}

	/**
	 * 
	 * @param text
	 * @param suffix
	 * @return
	 */
	public boolean endsWith(String text, String suffix) {
		if (isBlank(text)) {
			return false;
		}

		return text.endsWith(suffix);
	}

	/**
	 * 
	 * @param text
	 * @param delimiter
	 * @return
	 */
	public static String[] split(String text, String delimiter) {
		if (isEmpty(text)) {
			return new String[] {};
		}

		return text.split(delimiter);
	}

	private Strings() {}

}
