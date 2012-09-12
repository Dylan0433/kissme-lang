package com.kissme.lang.eject;

import java.lang.reflect.Field;

import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
class FieldEjector implements Ejector {

	private final Object me;
	private final Field field;

	/**
	 * 
	 * @param me
	 * @param field
	 */
	FieldEjector(Object me, Field field) {
		this.me = me;
		this.field = field;
		this.field.setAccessible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.eject.Ejecting#eject()
	 */
	public Object eject() {
		try {

			return field.get(me);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

}
