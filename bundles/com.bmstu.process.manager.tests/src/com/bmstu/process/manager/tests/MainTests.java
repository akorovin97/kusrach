package com.bmstu.process.manager.tests;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.bmstu.process.manager.IActiveProcessListener;
import com.bmstu.process.manager.IActiveProcessManager;
import com.bmstu.process.manager.model.ActiveProcessEvent;
import com.bmstu.process.manager.model.Process;

/**
 *
 * Active process manager tests.
 *
 * @author Pirchalava
 *
 */
public class MainTests {

	@Test
	public void managerTest() {
		IActiveProcessManager manager = getService(IActiveProcessManager.class);
		assertNotNull("Active process manager not found", manager);

		Collection<Process> processes = manager.getActiveProcesses();
		assertNotNull("Processes is null", processes);
	}

	@Test
	public void clientTest() {
		IActiveProcessListener listener = getService(IActiveProcessListener.class);
		assertNotNull("Listener is null", listener);
		listener.handleEvent(new ActiveProcessEvent(Collections.emptyList(), Collections.emptyList()));
	}

	static <T> T getService(Class<T> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(MainTests.class);
		if (bundle != null) {
			ServiceTracker<T, T> st = new ServiceTracker<T, T>(bundle.getBundleContext(), clazz, null);
			st.open();

			try {
				// give the runtime some time to startup
				return st.waitForService(500);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
