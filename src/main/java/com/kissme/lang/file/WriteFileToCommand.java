package com.kissme.lang.file;

import java.io.File;
import java.io.FileInputStream;
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
public class WriteFileToCommand implements FileCommand {
	private final File source;
	private final OutputStream out;
	private final boolean close;

	public WriteFileToCommand(File source, OutputStream out, boolean close) {
		Preconditions.notNull(source);
		Preconditions.notNull(out);
		
		this.source = source;
		this.out = out;
		this.close = close;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.file.FileCommand#execute()
	 */
	public void execute() {
		InputStream in = null;

		try {

			in = new FileInputStream(source);
			IOs.piping(in, out);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		} finally {
			IOs.freeQuietly(in);
			if (close) {
				IOs.freeQuietly(out);
			}
		}
	}
}
