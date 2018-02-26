package com.bmstu.process.manager.model;

import java.util.List;
import java.util.Objects;

/**
 *
 * Instance of this class represents process.
 * Each process has name and arguments.
 *
 * @author Pirchalava
 *
 */
public class Process {

	private String name;
	private List<String> args;

	/**
	 *
	 * Constructor.
	 *
	 * @param name - process name. Can't be <code>null</code>.
	 * @param args - process args. Can't be <code>null</code>.
	 */
	public Process(String name, List<String> args) {
		this.name = name;
		this.args = args;
	}

	/**
	 *
	 * Returns process name.
	 *
	 * @return process name. Can't return <code>null</code>.
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * Returns process arguments.
	 *
	 * @return process arguments. Can't return <code>null</code>.
	 */
	public List<String> getArgs() {
		return args;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, args);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}

		if (object instanceof Process) {
			Process otherProcess = (Process)object;

			return getName().equals(otherProcess.getName()) && getArgs().equals(otherProcess.getArgs());
		}

		return false;
	}
}
