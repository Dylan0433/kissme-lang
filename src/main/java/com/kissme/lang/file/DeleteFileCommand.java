package com.kissme.lang.file;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.kissme.lang.Lang;
import com.kissme.lang.Preconditions;

/**
 * 
 * 
 * @author loudyn.
 */
public class DeleteFileCommand implements FileCommand {
	private final File file;
	
	/**
	 * 
	 * @param file
	 */
	public DeleteFileCommand(final File file) {
		Preconditions.notNull(file);
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.file.FileCommand#execute()
	 */
	public void execute() {
		try {

			if (!file.exists()) {
				return;
			}

			List<Throwable> throwables = new LinkedList<Throwable>();
			File[] files = file.listFiles();
			if (null != files && files.length > 0) {

				for (File file : files) {
					try {

						delete(file);
					} catch (Exception e) {
						throwables.add(e);
					}
				}
			}

			if (!throwables.isEmpty()) {
				throw Lang.comboThrow(throwables);
			}

			if (!file.delete()) {
				throw newIOException(file);
			}

		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	private void delete(File file) throws Exception {

		File[] inners = file.listFiles();
		if (null != inners && inners.length > 0) {
			for (File inner : inners) {
				delete(inner);
			}
		}

		if (!file.delete()) {
			throw newIOException(file);
		}
	}

	private Exception newIOException(File file) {
		return new IOException("can not delete the file[" + file.getName() + "]");
	}

}
