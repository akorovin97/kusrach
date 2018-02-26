package com.bmstu.process.manager;

import java.util.Collection;

import com.bmstu.process.manager.model.Process;

/**
 *
 * Active process manager.
 * Monitores active processes and sends events to clients.
 *
 * @author Pirchalava
 *
 */
public interface IActiveProcessManager {

	/**
	 *
	 * Returns active processes.
	 *
	 * @return active processes. Can't be <code>null</code>.
	 */
	Collection<Process> getActiveProcesses();

}
