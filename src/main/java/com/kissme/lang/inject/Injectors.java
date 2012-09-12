package com.kissme.lang.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Injectors {

	/**
	 * 
	 * @param who
	 * @param setter
	 * @return
	 */
	public static Injector newSetterInjector(Object who, Method setter) {
		return new SetterInjector(who, setter);
	}

	/**
	 * 
	 * @param who
	 * @param field
	 * @return
	 */
	public static Injector newFieldInjector(Object who, Field field) {
		return new FieldInjector(who, field);
	}

	private Injectors() {}
}
