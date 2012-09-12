package com.kissme.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Lang {

	/**
	 * 
	 * @return
	 */
	public static RuntimeException impossiable() {
		return new RuntimeException("r u kidding me!");
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static RuntimeException oneThrow(String message) {
		return new RuntimeException(message);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static RuntimeException uncheck(Throwable e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}

		if (e instanceof InvocationTargetException) {
			Throwable cause = e.getCause();
			return new RuntimeException(cause);
		}

		return new RuntimeException(e);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static Throwable unwrap(Throwable e) {
		Throwable unwrapp = e;

		while (true) {
			if (unwrapp instanceof InvocationTargetException) {
				unwrapp = ((InvocationTargetException) unwrapp).getTargetException();
			} else if (unwrapp instanceof UndeclaredThrowableException) {
				unwrapp = ((UndeclaredThrowableException) unwrapp).getUndeclaredThrowable();
			} else {
				return unwrapp;
			}
		}
	}

	/**
	 * 
	 * @param throwables
	 * @return
	 */
	public static ComboException comboThrow(Throwable... throwables) {
		Collection<Throwable> throwablesAsCollection = Arrays.asList(throwables);
		return comboThrow(throwablesAsCollection);
	}

	/**
	 * 
	 * @param throwables
	 * @return
	 */
	public static ComboException comboThrow(Collection<Throwable> throwables) {
		return new ComboException(throwables);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	@SuppressWarnings("serial")
	static final class ComboException extends RuntimeException {
		private Collection<Throwable> throwables;

		public ComboException() {
			throwables = new LinkedList<Throwable>();
		}

		public ComboException(Collection<Throwable> throwables) {
			this.throwables = throwables;
		}

		public ComboException push(Throwable throwable) {
			this.throwables.add(throwable);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getMessage()
		 */
		@Override
		public String getMessage() {
			StringBuilder sb = new StringBuilder();
			for (Throwable throwable : this.throwables) {
				sb.append(throwable.getMessage()).append("\n");
			}
			return sb.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getLocalizedMessage()
		 */
		@Override
		public String getLocalizedMessage() {
			StringBuilder sb = new StringBuilder();
			for (Throwable throwable : this.throwables) {
				sb.append(throwable.getLocalizedMessage()).append("\n");
			}
			return sb.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getCause()
		 */
		@Override
		public Throwable getCause() {
			if (this.throwables.isEmpty()) {
				return null;
			}

			Iterator<Throwable> it = this.throwables.iterator();
			return it.next();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#printStackTrace()
		 */
		@Override
		public void printStackTrace() {
			for (Throwable throwable : this.throwables) {
				throwable.printStackTrace();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
		 */
		@Override
		public void printStackTrace(PrintStream s) {
			for (Throwable throwable : this.throwables) {
				throwable.printStackTrace(s);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
		 */
		@Override
		public void printStackTrace(PrintWriter s) {
			for (Throwable throwable : this.throwables) {
				throwable.printStackTrace(s);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getStackTrace()
		 */
		@Override
		public StackTraceElement[] getStackTrace() {
			List<StackTraceElement> result = new LinkedList<StackTraceElement>();
			for (Throwable throwable : this.throwables) {
				result.addAll(Arrays.asList(throwable.getStackTrace()));
			}

			return result.toArray(new StackTraceElement[] {});
		}

	}

	/**
	 * 
	 * @param prototype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T prototype) {

		Ghost<T> me = (Ghost<T>) Ghost.me(prototype.getClass());
		if (me.openEyes().isOf(Cloneable.class)) {
			return (T) me.invoke(prototype, "clone");
		}

		if (me.openEyes().isOf(Serializable.class)) {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			serialize((Serializable) prototype, bao);
			return (T) deserialize(new ByteArrayInputStream(bao.toByteArray()));
		}

		throw new IllegalArgumentException("what can i do ? maybe only you know.");
	}

	/**
	 * 
	 * @param <T>
	 * @param obj
	 * @param out
	 */
	public static <T extends Serializable> void serialize(T obj, OutputStream out) {
		ObjectOutputStream oos = null;

		try {

			oos = new ObjectOutputStream(out);
			oos.writeObject(obj);
			oos.flush();
		} catch (Exception e) {
			throw uncheck(e);
		} finally {
			IOs.freeQuietly(oos, out);
		}
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public static Object deserialize(InputStream in) {
		ObjectInputStream ooi = null;

		try {

			ooi = new ObjectInputStream(in);
			return ooi.readObject();
		} catch (Exception e) {
			throw uncheck(e);
		} finally {
			IOs.freeQuietly(ooi, in);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param safe
	 * @return
	 */
	public static <T> T nullSafe(T actual, T safe) {
		return null == actual ? safe : actual;
	}

	/**
	 * 
	 * @param type
	 * @param length
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<T> type, int length) {
		return (T[]) Array.newInstance(type, length);
	}

	/**
	 * 
	 * @param reference
	 * @param length
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(T[] reference, int length) {
		Class<?> type = reference.getClass().getComponentType();
		return (T[]) Array.newInstance(type, length);
	}

	/**
	 * 
	 * @param first
	 * @param second
	 * @param type
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
		T[] result = newArray(type, first.length + second.length);
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * 
	 * @param element
	 * @param array
	 * @return
	 */
	public static <T> T[] concat(T element, T[] array) {
		T[] result = newArray(array, array.length + 1);
		result[0] = element;
		System.arraycopy(array, 0, result, 1, array.length);
		return result;
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public static Class<?>[] evalToTypes(Object... args) {
		Class<?>[] types = new Class[args.length];
		int i = 0;
		for (Object arg : args) {
			types[i++] = null == arg ? Object.class : arg.getClass();
		}

		return types;
	}

	/**
	 * 
	 * @param obj
	 * @param each
	 */
	@SuppressWarnings("unchecked")
	public static <T> void each(Object obj, Each<T> each) {

		Ghost<?> ghost = (Ghost<T>) Ghost.me(obj.getClass());
		if (ghost.openEyes().isArray()) {
			int length = Array.getLength(obj);
			for (int i = 0; i < length; i++) {
				T item = (T) Array.get(obj, i);
				each.invoke(i, item);
			}

			return;
		}

		// collection is iterable
		if (ghost.openEyes().isIterable()) {
			Iterable<T> iterable = (Iterable<T>) obj;
			doInIterable(iterable, each);
			return;
		}

		if (ghost.openEyes().isIterator()) {
			final Iterator<T> it = (Iterator<T>) obj;
			doInIterable(new Iterable<T>() {

				public Iterator<T> iterator() {
					return it;
				}

			}, each);
			return;
		}

		if (ghost.openEyes().isMap()) {
			Map<?, ?> map = (Map<?, ?>) obj;
			final Set<T> entries = (Set<T>) map.entrySet();
			doInIterable(new Iterable<T>() {

				public Iterator<T> iterator() {
					return entries.iterator();
				}

			}, each);
			return;
		}

		// duck typing
		Class<?> eachType = Ghost.me(each.getClass()).genericsType(Each.class);
		if (ghost.hasReturnTypeMethod("get", eachType, int.class) && ghost.hasReturnTypeMethod("size", int.class)) {

			int length = (Integer) ghost.invoke(obj, "size");
			for (int i = 0; i < length; i++) {
				T item = (T) ghost.invoke(obj, "get", i);
				each.invoke(i, item);
			}
			return;
		}

		if (ghost.hasReturnTypeMethod("get", eachType, int.class) && ghost.hasReturnTypeMethod("length", int.class)) {

			int length = (Integer) ghost.invoke(obj, "length");
			for (int i = 0; i < length; i++) {
				T item = (T) ghost.invoke(obj, "get", i);
				each.invoke(i, item);
			}
			return;
		}

		if (ghost.hasReturnTypeMethod("hasNext", boolean.class) && ghost.hasReturnTypeMethod("next", eachType)) {

			int index = 0;
			while ((Boolean) ghost.invoke(obj, "hasNext")) {
				T item = (T) ghost.invoke(obj, "next");
				each.invoke(index++, item);
			}
			return;
		}

		throw new IllegalArgumentException("what can i do ? maybe only you know.");
	}

	private static <T> void doInIterable(Iterable<T> iterable, Each<T> each) {
		Iterator<T> it = iterable.iterator();
		int index = 0;
		while (it.hasNext()) {
			each.invoke(index++, it.next());
		}

	}

	/**
	 * 
	 * @param unfilter
	 * @param predicate
	 * @return
	 */
	public static <T> Iterator<T> filter(final Iterator<T> unfilter, final Predicate<? super T> predicate) {
		return new AbstractIterator<T>() {

			@Override
			protected T computeNext() {
				while (unfilter.hasNext()) {
					T next = unfilter.next();
					if (predicate.apply(next)) {
						return next;
					}
				}

				return endOfData();
			}
		};
	}

	/**
	 * 
	 * @param fromIterator
	 * @param function
	 * @return
	 */
	public static <Input, Output> Iterator<Output> transform(final Iterator<Input> fromIterator, final Function<? super Input, ? extends Output> function) {
		return new TransformedIterator<Input, Output>(fromIterator) {

			@Override
			Output transform(Input from) {
				return function.apply(from);
			}
		};
	}

	/**
	 * 
	 * @param unfilter
	 * @param predicate
	 * @return
	 */
	public static <T> Iterable<T> filter(final Iterable<T> unfilter, final Predicate<T> predicate) {
		return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				return filter(unfilter.iterator(), predicate);
			}
		};
	}

	/**
	 * 
	 * @param fromIterable
	 * @param function
	 * @return
	 */
	public static <Input, Output> Iterable<Output> transform(final Iterable<Input> fromIterable, final Function<? super Input, ? extends Output> function) {
		return new Iterable<Output>() {

			@Override
			public Iterator<Output> iterator() {
				return transform(fromIterable.iterator(), function);
			}

		};
	}

	/**
	 * 
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static <Input, Output> List<Output> transform(final List<Input> fromList, final Function<? super Input, ? extends Output> function) {
		return fromList instanceof RandomAccess ? new TransformingRandomAccessList<Input, Output>(fromList, function)
				: new TransformingSequentialList<Input, Output>(fromList, function);
	}

	/**
	 * 
	 * @param milliseconds
	 */
	public static void sleepQuietly(long milliseconds) {
		try {

			if (milliseconds > 0) {
				Thread.sleep(milliseconds);
			}
		} catch (Exception ingore) {
		}
	}

	/**
	 * 
	 * @return
	 */
	public static Stopwatch newStopwatch() {
		return new Stopwatch();
	}

	static class Stopwatch {
		private long start;
		private long end;

		public long start() {
			start = System.currentTimeMillis();
			return start;
		}

		public long stop() {
			end = System.currentTimeMillis();
			return end;
		}

		public long getElapse() {
			return end - start;
		}
	}

	private Lang() {
	}
}
