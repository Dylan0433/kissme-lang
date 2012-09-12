package com.kissme.lang.file;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author loudyn
 * 
 */
public class FileCommandInvoker {
	private List<FileCommand> commands = new ArrayList<FileCommand>();

	/**
	 * 
	 * @param command
	 * @return
	 */
	public FileCommandInvoker command(final FileCommand command) {
		this.commands.add(command);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public FileCommandInvoker invoke() {
		for (FileCommand command : this.commands) {
			command.execute();
		}

		return this;
	}
}
