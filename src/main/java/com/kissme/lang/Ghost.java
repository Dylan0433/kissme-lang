package com.kissme.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.kissme.lang.born.Bornings;
import com.kissme.lang.eject.Ejector;
import com.kissme.lang.eject.Ejectors;
import com.kissme.lang.inject.Injector;
import com.kissme.lang.inject.Injectors;
import com.kissme.lang.invoke.Invokers;

/**
 * 
 * @author loudyn
 * 
 */
public class Ghost<T> {

	private Ghost(Class<T> clazz) {
		this.clazz = clazz;
		this.ghostEyes = new GhostEyes<T>(clazz);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Ghost<T> me(T obj) {
		return (Ghost<T>) me(obj.getClass());
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Ghost<T> me(Class<T> clazz) {
		assert null != clazz;
		return new Ghost<T>(clazz);
	}

	/**
	 * 
	 * @param argTypes
	 * @param paramTypes
	 * @return
	 */
	public static boolean isMatchArgTypes(Class<?>[] argTypes, Class<?>[] paramTypes) {
		if (argTypes.length != paramTypes.length) {
			return false;
		}

		for (int i = 0; i < argTypes.length; i++) {
			if (!Ghost.me(argTypes[i]).openEyes().canCastDirectly(paramTypes[i])) {
				return false;
			}
		}

		return true;
	}

	private Class<T> clazz;
	private GhostEyes<T> ghostEyes;

	/**
	 * 
	 * @return
	 */
	public Class<?> type() {
		return clazz;
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public T born(Object... args) {
		return Bornings.eval(this, args).born();
	}

	/**
	 * 
	 * @param who
	 * @param methodName
	 * @param args
	 * @return
	 */
	public Object invoke(Object who, String methodName, Object... args) {

		Preconditions.notNull(who);
		Preconditions.hasText(methodName);

		try {

			if (null == args || args.length == 0) {
				return Invokers.newEmptyArgsMethodInvoking(who, method(methodName)).invoke();
			}

			Class<?>[] argTypes = Lang.evalToTypes(args);
			Method method = method(methodName, argTypes);
			return Invokers.newMethodInvoking(who, method, args).invoke();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param who
	 * @param fieldName
	 * @return
	 */
	public Ejector ejector(Object who, String fieldName) {

		Preconditions.notNull(who);
		Preconditions.hasText(fieldName);

		try {

			return Ejectors.newGetterEjector(who, getter(fieldName));
		} catch (Exception e) {
			try {

				return Ejectors.newFieldEjector(who, field(fieldName));
			} catch (Exception ee) {
				throw Lang.comboThrow(e, ee);
			}
		}
	}

	/**
	 * 
	 * @param who
	 * @param fieldName
	 * @return
	 */
	public Injector injector(Object who, String fieldName) {

		Preconditions.notNull(who);
		Preconditions.hasText(fieldName);

		Field field = null;
		boolean hasField = false;
		try {

			Method[] setters = setters(fieldName);
			if (setters.length == 1) {
				return Injectors.newSetterInjector(who, setters[0]);
			}

			field = field(fieldName);
			hasField = true;
			return Injectors.newSetterInjector(who, setter(field));
		} catch (Exception e) {
			try {

				if (hasField) {
					return Injectors.newFieldInjector(who, field);
				}

			} catch (Exception ee) {
				throw Lang.comboThrow(e, ee);
			}

			throw Lang.uncheck(e);
		}

	}

	private Method[] setters(String fieldName) {

		String set = "set" + Strings.capitalize(fieldName);
		List<Method> setters = new LinkedList<Method>();
		for (Method m : clazz.getMethods()) {
			if (!Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 1 && m.getName().equals(set)) {
				setters.add(m);
			}
		}
		return setters.toArray(new Method[setters.size()]);
	}

	private Method setter(Field field) {
		return setter(field.getName(), field.getType());
	}

	private Method setter(String name, Class<?> argType) {
		try {

			String set = "set".concat(Strings.capitalize(name));
			return method(set, argType);
		} catch (Exception e) {
			try {

				return method(name, argType);
			} catch (Exception ee) {
				throw Lang.comboThrow(e, ee);
			}
		}
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	private Field field(String fieldName) {
		return field(fieldName, Object.class);
	}

	/**
	 * 
	 * @param fieldName
	 * @param top
	 * @return
	 */
	private Field field(String fieldName, Class<Object> top) {

		Class<?> cc = clazz;
		while (null != cc && cc != top) {
			for (Field field : cc.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {
					return field;
				}
			}

			cc = cc.getSuperclass();
		}

		throw Lang.uncheck(new NoSuchFieldException());
	}

	/**
	 * 
	 * @param anno
	 * @return
	 */
	public <A extends Annotation> boolean hasAnnotation(Class<A> anno) {
		return hasAnnotation(anno, Object.class);
	}

	/**
	 * 
	 * @param anno
	 * @param top
	 * @return
	 */
	public <A extends Annotation> boolean hasAnnotation(Class<A> anno, Class<?> top) {
		if (clazz.isAnnotationPresent(anno)) {
			return true;
		}

		Class<?> superClazz = clazz.getSuperclass();
		while (null != superClazz && superClazz != top) {
			if (superClazz.isAnnotationPresent(anno)) {
				return true;
			}

			superClazz = superClazz.getSuperclass();
		}

		return false;

	}

	/**
	 * 
	 * @param anno
	 * @return
	 */
	public <A extends Annotation> A annotation(Class<A> anno) {
		return annotation(anno, Object.class);
	}

	/**
	 * 
	 * @param anno
	 * @param top
	 * @return
	 */
	public <A extends Annotation> A annotation(Class<A> anno, Class<?> top) {
		return annotation(clazz, anno, top);
	}

	private <A extends Annotation> A annotation(Class<?> clazz, Class<A> anno, Class<?> top) {
		A result = clazz.getAnnotation(anno);
		if (null != result) {
			return result;
		}

		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != top) {
			return annotation(superClazz, anno, top);
		}

		return null;
	}

	/**
	 * 
	 * @param anno
	 * @return
	 */
	public <A extends Annotation> Field[] annotationFields(Class<A> anno) {
		return annotationFields(anno, Object.class);
	}

	/**
	 * 
	 * @param anno
	 * @param top
	 * @return
	 */
	public <A extends Annotation> Field[] annotationFields(Class<A> anno, Class<?> top) {

		Preconditions.notNull(anno);
		Preconditions.notNull(top);

		List<Field> fields = new LinkedList<Field>();
		Class<?> cc = clazz;
		while (null != cc && cc != top) {
			for (Field field : cc.getDeclaredFields()) {
				if (field.isAnnotationPresent(anno)) {
					fields.add(field);
				}
			}

			cc = cc.getSuperclass();
		}

		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	private Method getter(String fieldName) {

		String fn = Strings.capitalize(fieldName);
		String get = "get".concat(fn);
		String is = "is".concat(fn);
		for (Method method : clazz.getMethods()) {
			if (method.getParameterTypes().length != 0) {
				continue;
			}

			if (method.getName().equals(get)) {
				return method;
			}

			if (method.getName().equals(is) && Ghost.me(method.getReturnType()).openEyes().isBoolean()) {
				return method;
			}

			if (method.getName().equals(fieldName)) {
				return method;
			}
		}

		throw Lang.uncheck(new NoSuchMethodException());
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Constructor<T> constructor(Object... args) {
		try {

			if (null == args || args.length == 0) {

				Constructor<T> c = clazz.getDeclaredConstructor();
				if (!Modifier.isPrivate(c.getModifiers())) {
					return c;
				}
			}

			Class<?>[] argTypes = Lang.evalToTypes(args);
			for (Constructor<?> c : constructors()) {

				Class<?>[] paramTypes = c.getParameterTypes();
				if (isMatchArgTypes(argTypes, paramTypes)) {
					return (Constructor<T>) c;
				}
			}

			throw new NoSuchMethodException();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Constructor<T>[] constructors() {
		try {

			List<Constructor<T>> ccs = new LinkedList<Constructor<T>>();

			for (Constructor<?> c : clazz.getDeclaredConstructors()) {
				if (!Modifier.isPrivate(c.getModifiers())) {
					ccs.add((Constructor<T>) c);
				}
			}

			return ccs.toArray(new Constructor[ccs.size()]);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param name
	 * @param argTypes
	 * @return
	 */
	private Method method(String name, Class<?>... argTypes) {

		try {

			if (null == argTypes || argTypes.length == 0) {
				return clazz.getMethod(name);
			}

			for (Method method : clazz.getMethods()) {
				if (!method.getName().equals(name)) {
					continue;
				}

				Class<?>[] paramsTypes = method.getParameterTypes();
				if (isMatchArgTypes(argTypes, paramsTypes)) {
					return method;
				}
			}

			throw new NoSuchMethodException();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	private Method returnTypeMethod(String name, Class<?> returnType, Class<?>... argTypes) {
		try {

			Method method = method(name, argTypes);
			Ghost<?> ghost = Ghost.me(method.getReturnType());
			if (ghost.openEyes().isPrimitive()) {
				if (Ghost.me(returnType).openEyes().wrapperClass() == ghost.openEyes().wrapperClass()) {
					return method;
				}
			}

			if (ghost.openEyes().isOf(returnType)) {
				return method;
			}

			throw new NoSuchMethodException();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param name
	 * @param paramsTypes
	 * @return
	 */
	public boolean hasMethod(String name, Class<?>... argTypes) {

		Preconditions.hasText(name);

		try {

			return null != method(name, argTypes);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @param name
	 * @param returnType
	 * @param argTypes
	 * @return
	 */
	public boolean hasReturnTypeMethod(String name, Class<?> returnType, Class<?>... argTypes) {

		Preconditions.hasText(name);
		Preconditions.notNull(returnType);

		try {

			return null != returnTypeMethod(name, returnType, argTypes);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Method[] factoryMethods() {
		Set<Method> methods = new HashSet<Method>();

		for (Method m : clazz.getMethods()) {
			if (Modifier.isPrivate(m.getModifiers())) {
				continue;
			}
			if (!Modifier.isStatic(m.getModifiers())) {
				continue;
			}

			if (m.getReturnType() != clazz) {
				continue;
			}
			methods.add(m);
		}

		return methods.toArray(new Method[methods.size()]);
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public Method factoryMethod(Object... args) {

		for (Method m : factoryMethods()) {
			Class<?>[] argTypes = Lang.evalToTypes(args);
			if (isMatchArgTypes(argTypes, m.getParameterTypes())) {
				return m;
			}

		}

		throw Lang.uncheck(new NoSuchMethodException());
	}

	/**
	 * 
	 * @return
	 */
	public Class<?> genericsType(Class<?> superClazz) {
		if (genericsTypes(superClazz).length == 0) {
			return Object.class;
		}

		return genericsTypes(superClazz)[0];
	}

	/**
	 * 
	 * @return
	 */
	public Class<?>[] genericsTypes(Class<?> superClazz) {

		Type type = getGenericSupertype(clazz, superClazz);

		List<Class<?>> types = new LinkedList<Class<?>>();
		if (type instanceof ParameterizedType) {
			Type[] paramTypes = ((ParameterizedType) type).getActualTypeArguments();
			types.addAll(evalTypesAsClasses(paramTypes));
		} else if (type instanceof GenericArrayType) {
			Type paramType = ((GenericArrayType) type).getGenericComponentType();
			types.add(evalTypesAsClasses(paramType).get(0));
		}

		return types.toArray(new Class<?>[types.size()]);
	}

	private Type getGenericSupertype(Class<?> raw, Class<?> toResolve) {

		if (toResolve.isInterface()) {
			Class<?>[] interfaces = raw.getInterfaces();
			for (int i = 0, length = interfaces.length; i < length; i++) {
				if (interfaces[i] == toResolve) {
					return clazz.getGenericInterfaces()[i];
				} else if (toResolve.isAssignableFrom(interfaces[i])) {
					return getGenericSupertype(interfaces[i], toResolve);
				}
			}
		}

		// check our supertypes
		if (!raw.isInterface()) {
			while (raw != Object.class) {
				Class<?> rawSupertype = raw.getSuperclass();
				if (rawSupertype == toResolve) {
					return raw.getGenericSuperclass();
				} else if (toResolve.isAssignableFrom(rawSupertype)) {
					return getGenericSupertype(rawSupertype, toResolve);
				}
				raw = rawSupertype;
			}
		}

		// we can't resolve this further
		return toResolve;
	}

	private List<Class<?>> evalTypesAsClasses(Type... types) {

		List<Class<?>> clazzes = new LinkedList<Class<?>>();
		for (Type type : types) {
			if (type instanceof Class<?>) {
				clazzes.add((Class<?>) type);
				continue;
			}

			if (type instanceof ParameterizedType) {
				clazzes.add((Class<?>) ((ParameterizedType) type).getRawType());
				continue;
			}

			if (type instanceof TypeVariable<?>) {
				TypeVariable<?> tv = (TypeVariable<?>) type;
				clazzes.add((Class<?>) tv.getBounds()[0]);
			}
		}
		return clazzes;
	}

	/**
	 * 
	 * @return
	 */
	public GhostEyes<T> openEyes() {
		return ghostEyes;
	}
}
