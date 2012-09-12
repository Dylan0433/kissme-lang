package com.kissme.lang.eject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Ejectors {

	/**
	 * 
	 * @param who
	 * @param field
	 * @return
	 */
	public static Ejector newFieldEjector(Object who, Field field) {
		return new FieldEjector(who, field);
	}

	/**
	 * 
	 * @param who
	 * @param getter
	 * @return
	 */
	public static Ejector newGetterEjector(Object who, Method getter) {
		return new GetterEjector(who, getter);
	}

	private Ejectors() {}
}
