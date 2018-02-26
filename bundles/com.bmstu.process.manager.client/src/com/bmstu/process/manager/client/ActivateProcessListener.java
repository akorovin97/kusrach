package com.bmstu.process.manager.client;

import org.osgi.service.component.annotations.Component;

import com.bmstu.process.manager.IActiveProcessListener;
import com.bmstu.process.manager.model.ActiveProcessEvent;

/**
 *
 * Instance of this class prints out
 * activated prosesses from event.
 *
 * @author Pirchalava
 *
 */
@Component
public class ActivateProcessListener implements IActiveProcessListener {

	@Override
	public void handleEvent(ActiveProcessEvent event) {
		event.getActivatedProcesses().stream().forEach(process -> {
			System.out.println(process.getName() + " activated with args: " + process.getArgs());
		});
	}

}
