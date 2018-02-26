package com.bmstu.process.manager.model;

import java.util.Collection;

/**
 *
 * Instance of this class represents processes
 * change event. Contains activated/deactivated processes.
 *
 * @author Pirchalava
 *
 */
public class ActiveProcessEvent {
	private Collection<Process> activatedProcesses;
	private Collection<Process> deactivatedProcesses;


	/**
	 *
	 * Constructor.
	 *
	 * @param activatedProcesses - activated processes. Can't be <code>null</code>.
	 * @param deactivatedProcesses - deactivated processes. Can't be <code>null</code>.
	 */
	public ActiveProcessEvent(Collection<Process> activatedProcesses, Collection<Process> deactivatedProcesses) {
		this.activatedProcesses = activatedProcesses;
		this.deactivatedProcesses = deactivatedProcesses;
	}

	/**
	 *
	 * Returns activated processes.
	 *
	 * @return activated processes. Can't return <code>null</code>.
	 */
	public Collection<Process> getActivatedProcesses() {
		return activatedProcesses;
	}

	/**
	 *
	 * Returns deactivated processes.
	 *
	 * @return deactivated processes. Can't return <code>null</code>.
	 */
	public Collection<Process> getDeactivatedProcesses() {
		return deactivatedProcesses;
	}
}
