package com.bmstu.process.manager.client;

import org.osgi.service.component.annotations.Component;

import com.bmstu.process.manager.IActiveProcessListener;
import com.bmstu.process.manager.model.ActiveProcessEvent;

/**
 *
 * Instance of this class prints out
 * deactivated prosesses from event.
 *
 * @author Pirchalava
 *
 */
@Component
public class DeactivateProcessListener implements IActiveProcessListener {

	@Override
	public void handleEvent(ActiveProcessEvent event) {
		event.getDeactivatedProcesses().stream().forEach(process -> {
			System.out.println(process.getName() + " deactivated");
		});
	}

}
