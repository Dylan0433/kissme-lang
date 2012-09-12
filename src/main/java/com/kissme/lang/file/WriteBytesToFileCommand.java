package com.kissme.lang.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.kissme.lang.IOs;
import com.kissme.lang.Lang;
import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public class WriteBytesToFileCommand implements FileCommand {
	private final File target;
	private final byte[] content;

	public WriteBytesToFileCommand(File target, byte[] content) {
		Preconditions.notNull(target);
		Preconditions.notNull(content);

		this.target = target;
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.file.FileCommand#execute()
	 */
	public void execute() {

		OutputStream out = null;
		try {

			out = new FileOutputStream(target);
			out.write(content);

		} catch (Exception e) {
			throw Lang.uncheck(e);
		} finally {
			IOs.freeQuietly(out);
		}
	}

}
