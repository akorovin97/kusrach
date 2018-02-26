package com.bmstu.process.manager.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.bmstu.process.manager.IActiveProcessListener;
import com.bmstu.process.manager.IActiveProcessManager;
import com.bmstu.process.manager.model.ActiveProcessEvent;
import com.bmstu.process.manager.model.Process;


@Component(immediate = true)
public class ActiveProcessManager implements IActiveProcessManager {

	private static final int AWAIT_TERMINATION_TIME = 10;

	private Collection<IActiveProcessListener> listeners;
	private Collection<Process> processes;
	private ExecutorService executorService;
	private boolean isActive;

	/**
	 * Constuctor.
	 */
	public ActiveProcessManager() {
		listeners = new HashSet<>();
		processes = new HashSet<>();
	}

	@Override
	public Collection<Process> getActiveProcesses() {
		return processes;
	}

	@Activate
	public void activate(Map<String, Object> properties) {
		isActive = true;
		initProcesses();

		executorService = Executors.newFixedThreadPool(1);
		executorService.execute(new ProcessesListener());
	}

	@Deactivate
	public void deactivate() {
		try {
			isActive = false;
			executorService.awaitTermination(AWAIT_TERMINATION_TIME, TimeUnit.SECONDS);
			listeners.clear();
			processes.clear();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void bindListener(IActiveProcessListener listener) {
		listeners.add(listener);
	}

	public void unbindListener(IActiveProcessListener listener) {
		listeners.remove(listener);
	}

	private void throwEvent(ActiveProcessEvent event) {
		listeners.stream().forEach(listener -> {
			listener.handleEvent(event);
		});
	}

	private void initProcesses() {
		processes.addAll(getActiveProcessesInternal());
	}

	private Collection<Process> getActiveProcessesInternal() {
		Collection<Process> activeProcesses = new HashSet<>();
		Sigar sigar = new Sigar();
		try {
			for (long pid : sigar.getProcList()) {
				Process process = createProcess(sigar, pid);
				if (process != null) {
					activeProcesses.add(process);
				}
			}
		}
		catch (SigarException e) {
			e.printStackTrace();
		}

		return activeProcesses;
	}

	private Process createProcess(Sigar sigar, long pid) {
		try {
			String procName = sigar.getProcExe(pid).getName();
			List<String> procArgs = Arrays.asList(sigar.getProcArgs(pid));

			return new Process(procName, procArgs);
		}
		catch (SigarException e) {
			// nothing
		}

		return null;
	}

	private class ProcessesListener implements Runnable {
		private static final int SLEEP_TIME = 1000;

		@Override
		public void run() {
			while (isActive) {
				try {
					Collection<Process> activeProcesses = getActiveProcessesInternal();

					Collection<Process> activatedProcesses = findActivatedProcesses(activeProcesses, processes);
					Collection<Process> deactivatedProcesses = findDeactivatedProcesses(activeProcesses, processes);

					if (!activatedProcesses.isEmpty() || !deactivatedProcesses.isEmpty()) {
						throwEvent(new ActiveProcessEvent(activatedProcesses, deactivatedProcesses));
					}

					processes = activeProcesses;
					Thread.sleep(SLEEP_TIME);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private Collection<Process> findActivatedProcesses(Collection<Process> newProcesses, Collection<Process> oldProcesses) {
			Collection<Process> activatedProcesses = new ArrayList<>();

			newProcesses.stream().forEach(newProcess -> {
				if (!oldProcesses.contains(newProcess)) {
					activatedProcesses.add(newProcess);
				}
			});

			return activatedProcesses;
		}

		private Collection<Process> findDeactivatedProcesses(Collection<Process> newProcesses, Collection<Process> oldProcesses) {
			Collection<Process> deactivatedProcesses = new ArrayList<>();

			oldProcesses.stream().forEach(oldProcess -> {
				if (!newProcesses.contains(oldProcess)) {
					deactivatedProcesses.add(oldProcess);
				}
			});

			return deactivatedProcesses;
		}
	}
}
