package com.bmstu.process.manager;

import com.bmstu.process.manager.model.ActiveProcessEvent;

/**
 *
 * Instance of this class receives events
 * from {@link IActiveProcessManager} and processes them.
 *
 * @author Pirchalava
 *
 */
public interface IActiveProcessListener {

	/**
	 *
	 * Processes given event.
	 *
	 * @param event - event to process. Can't be <code>null</code>.
	 */
	void handleEvent(ActiveProcessEvent event);

}
