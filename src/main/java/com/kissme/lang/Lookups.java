package com.kissme.lang;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Lookups {

	/**
	 * 
	 * @param clz
	 * @return
	 */
	public static <T> T lookup(Class<T> clz) {
		ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();
		return lookup(clz, clzLoader);
	}

	/**
	 * 
	 * @param clz
	 * @param clzLoader
	 * @return
	 */
	public static <T> T lookup(Class<T> clz, ClassLoader clzLoader) {

		ServiceLoader<T> loader = ServiceLoader.load(clz, clzLoader);
		Iterator<T> it = loader.iterator();
		if (it.hasNext()) {
			return it.next();
		}

		return null;
	}

	/**
	 * 
	 * @param clz
	 * @return
	 */
	public static <T> Iterator<T> lookupMultiple(Class<T> clz) {
		ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();
		return lookupMultiple(clz, clzLoader);
	}

	/**
	 * 
	 * @param clz
	 * @param clzLoader
	 * @return
	 */
	public static <T> Iterator<T> lookupMultiple(Class<T> clz, ClassLoader clzLoader) {
		ServiceLoader<T> loader = ServiceLoader.load(clz, clzLoader);
		return loader.iterator();
	}

	/**
	 * 
	 * @param packageName
	 * @return
	 */
	public static Class<?>[] lookupMultiple(String packageName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return lookupMultiple(packageName, loader);
	}

	/**
	 * 
	 * @param packageName
	 * @param loader
	 * @return
	 */
	public static Class<?>[] lookupMultiple(String packageName, ClassLoader loader) {

		String packageAsPath = Files.asPath(packageName);
		URL packageURL = loader.getResource(packageAsPath);

		String resourcePath = null != packageURL ? packageURL.getFile() : null;
		if (Strings.isBlank(resourcePath)) {
			return new Class<?>[] {};
		}

		File[] clazzFiles = Files.list(new File(resourcePath), new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}
				
				return pathname.getName().endsWith(".class");
			}
		});

		List<Class<?>> clazzes = new LinkedList<Class<?>>();
		for (File file : clazzFiles) {
			String filepath = Files.asUnix(Files.canonical(file));
			int index = filepath.indexOf(Files.asUnix(packageAsPath));
			if (index == -1) {
				throw Lang.impossiable();
			}

			int end = filepath.lastIndexOf(".");
			if (end == -1) {
				throw Lang.impossiable();
			}

			String clazzName = Files.asPackage(filepath.substring(index, end));
			try {

				clazzes.add(Class.forName(clazzName));
			} catch (Exception e) {
				throw Lang.uncheck(e);
			}
		}

		return clazzes.toArray(new Class<?>[clazzes.size()]);

	}

	private Lookups() {}
}
