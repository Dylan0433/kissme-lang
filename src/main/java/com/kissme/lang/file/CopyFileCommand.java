package com.kissme.lang.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.kissme.lang.IOs;
import com.kissme.lang.Lang;
import com.kissme.lang.Preconditions;

/**
 * 
 * 
 * @author loudyn.
 */
public class CopyFileCommand implements FileCommand {
	private final File source;
	private final File target;

	/**
	 * 
	 * @param source
	 * @param target
	 */
	public CopyFileCommand(File source, File target) {
		Preconditions.notNull(source);
		Preconditions.notNull(target);

		this.source = source;
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.file.FileCommand#execute()
	 */
	public void execute() {

		if (!source.exists()) {
			throw Lang.uncheck(new FileNotFoundException("the source file didn't exist!"));
		}

		InputStream in = null;
		OutputStream out = null;

		try {

			in = new FileInputStream(source);
			out = new FileOutputStream(target);
			IOs.piping(in, out);

		} catch (Exception e) {
			throw Lang.uncheck(e);
		} finally {
			IOs.freeQuietly(in, out);
		}
	}
}
