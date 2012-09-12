package com.kissme.lang;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author loudyn
 * 
 */
public final class GhostEyes<T> {

	private Class<T> clazz;

	/**
	 * 
	 * @param clazz
	 */
	GhostEyes(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean is(Class<?> type) {
		return null != type && type == clazz;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean isOf(Class<?> type) {
		return null != type && type.isAssignableFrom(clazz);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean isUpper(Class<?> type) {
		return null != type && clazz.isAssignableFrom(type);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPojo() {
		if (isPrimitive()) {
			return false;
		}

		if (isLikeString()) {
			return false;
		}

		if (isDatetime()) {
			return false;
		}

		if (isArray() || isIterable() || isIterator() || isMap()) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMap() {
		return isOf(Map.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCollection() {
		return isOf(Collection.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isIterator() {
		return isOf(Iterator.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isIterable() {
		return isOf(Iterable.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEnum() {
		return clazz.isEnum();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isArray() {
		return clazz.isArray();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isString() {
		return isOf(String.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLikeString() {
		return isOf(CharSequence.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDatetime() {
		return isOf(Date.class) || isOf(Calendar.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPrimitive() {
		if (clazz.isPrimitive()) {
			return true;
		}

		return is(Boolean.class) || is(Character.class) || is(Byte.class)
				|| is(Short.class) || is(Integer.class) || is(Long.class)
				|| is(Float.class) || is(Double.class) || is(Void.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNumber() {
		return isOf(Number.class) || isPrimitiveNumber();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPrimitiveNumber() {
		return (clazz.isPrimitive() && !is(boolean.class) && !is(char.class) && !is(void.class));
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBoolean() {
		return is(boolean.class) || is(Boolean.class);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSerializable() {
		return isOf(Serializable.class);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean canCastDirectly(Class<?> type) {
		if (isOf(type)) {
			return true;
		}

		if (type.isPrimitive() && clazz.isPrimitive()) {
			return Ghost.me(type).openEyes().isPrimitiveNumber() && isPrimitiveNumber();
		}
		try {

			return Ghost.me(type).openEyes().wrapperClass() == wrapperClass();
		} catch (Exception ingore) {}

		return false;
	}

	/**
	 * 
	 * @return
	 */
	public Class<?> wrapperClass() {
		if (clazz.isPrimitive()) {
			return primitiveMapping.get(clazz);
		}

		return clazz;
	}

	private static final Map<Class<?>, Class<?>> primitiveMapping = new HashMap<Class<?>, Class<?>>();
	static {

		primitiveMapping.put(boolean.class, Boolean.class);
		primitiveMapping.put(char.class, Character.class);
		primitiveMapping.put(byte.class, Byte.class);
		primitiveMapping.put(int.class, Integer.class);
		primitiveMapping.put(long.class, Long.class);
		primitiveMapping.put(float.class, Float.class);
		primitiveMapping.put(double.class, Double.class);
		primitiveMapping.put(void.class, Void.class);
	}

}
