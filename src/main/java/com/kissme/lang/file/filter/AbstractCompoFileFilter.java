package com.kissme.lang.file.filter;

import java.io.File;

import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class AbstractCompoFileFilter implements CompoFileFilter {

	@Override
	public final CompoFileFilter and(CompoFileFilter another) {
		return new AndCompoFileFilter(this, another);
	}

	@Override
	public final CompoFileFilter or(CompoFileFilter another) {
		return new OrCompoFileFilter(this, another);
	}

	@Override
	public final CompoFileFilter not(CompoFileFilter another) {
		return new NotCompoFileFilter(another);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	class AndCompoFileFilter extends AbstractCompoFileFilter {
		private final CompoFileFilter one;
		private final CompoFileFilter another;

		public AndCompoFileFilter(CompoFileFilter one, CompoFileFilter another) {

			Preconditions.notNull(one);
			Preconditions.notNull(another);
			this.one = one;
			this.another = another;
		}

		@Override
		public boolean accept(File pathname) {
			// make sure one.accept() call before another.accept()
			// we also can write like this : one.accept(pathname) && another.accept(pathname)
			if (!one.accept(pathname)) {
				return false;
			}

			return another.accept(pathname);
		}
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	class OrCompoFileFilter extends AbstractCompoFileFilter {
		private final CompoFileFilter one;
		private final CompoFileFilter another;

		public OrCompoFileFilter(CompoFileFilter one, CompoFileFilter another) {

			Preconditions.notNull(one);
			Preconditions.notNull(another);
			this.one = one;
			this.another = another;
		}

		@Override
		public boolean accept(File pathname) {
			// make sure one.accept() call before another.accept()
			// we also can write like this : one.accept(pathname) || another.accept(pathname)
			if (!one.accept(pathname)) {
				return another.accept(pathname);
			}

			return true;
		}
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	class NotCompoFileFilter extends AbstractCompoFileFilter {
		private final CompoFileFilter another;

		public NotCompoFileFilter(CompoFileFilter another) {

			Preconditions.notNull(another);
			this.another = another;
		}

		@Override
		public boolean accept(File pathname) {
			return !another.accept(pathname);
		}
	}

}
