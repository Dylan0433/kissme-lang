package com.kissme.lang.born;

import java.lang.reflect.Constructor;

import com.kissme.lang.Ghost;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Bornings {

	/**
	 * 
	 * @param ghost
	 * @param args
	 * @return
	 */
	public static <T> Borning<T> eval(Ghost<T> ghost, Object... args) {
		if (null == args || args.length == 0) {
			return evalEmptyArgs(ghost);
		}

		return evalArgs(ghost, args);
	}

	private static <T> Borning<T> evalEmptyArgs(Ghost<T> ghost) {
		try {

			Constructor<T> constructor = ghost.constructor();
			return new EmptyArgsConstructorBorning<T>(constructor);

		} catch (Exception e) {
			try {

				return new EmptyArgsFactoryMethodBorning<T>(ghost.factoryMethod());
			} catch (Exception ee) {
				throw new BorningException("Can't find empty args constructor or empty args factory create method", ee);
			}
		}

	}

	private static <T> Borning<T> evalArgs(Ghost<T> ghost, Object... args) {

		try {

			Constructor<T> constructor = ghost.constructor(args);
			return new ConstructorBorning<T>(constructor, args);

		} catch (Exception e) {

			try {

				return new FactoryMethodBorning<T>(ghost.factoryMethod(args), args);
			} catch (Exception ee) {
				throw new BorningException("Can't find constructor or factory create method with " + args, ee);
			}
		}
	}

	private Bornings() {}
}
