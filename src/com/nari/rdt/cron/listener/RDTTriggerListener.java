package com.nari.rdt.cron.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

public class RDTTriggerListener implements TriggerListener {

	private final static String TRIGGER_LISTENER_NAME="commonTrigger";
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return TRIGGER_LISTENER_NAME;
	}

	@Override
	public void triggerComplete(Trigger arg0, JobExecutionContext arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void triggerFired(Trigger arg0, JobExecutionContext arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void triggerMisfired(Trigger arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean vetoJobExecution(Trigger arg0, JobExecutionContext arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
