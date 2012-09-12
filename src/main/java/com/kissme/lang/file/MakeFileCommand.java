package com.kissme.lang.file;

import java.io.File;

import com.kissme.lang.Preconditions;

/**
 * 
 * 
 * @author loudyn.
 */
public class MakeFileCommand implements FileCommand {
	private final File file;

	public MakeFileCommand(File file) {
		Preconditions.notNull(file);
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.file.FileCommand#execute()
	 */
	public void execute() {
		if (file.exists()) {
			return;
		}

		if (null != file.getParentFile()) {
			file.getParentFile().mkdirs();
			return;
		}

		file.mkdir();
	}

}
