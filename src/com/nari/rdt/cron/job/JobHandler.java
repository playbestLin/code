package com.nari.rdt.cron.job;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class JobHandler implements IJobHandler {
	private static Logger logger = Logger.getLogger(JobHandler.class);

	private Scheduler scheduler = null;

	public JobHandler() {

		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			scheduler = schedulerFactory.getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error(e);
		}
	}

	@Override
	public Result addJob(JobInfo jobInfo) {
		Result result = new Result();

		JobDetail jobDetail = createJobDetail(jobInfo);
		int triggerType = jobInfo.getTriggerType();

		if (triggerType != 1 && triggerType != 2) {
			result.setResultOK(false);
			result.setErrorMsg("输入的任务触发方式不正确：1表示定时器(SimpleTrigger)，2表示计划任务(CronTrigger)");
			return result;
		}

		Trigger trigger = null;
		try {
			trigger = createTrigger(jobInfo);
		} catch (ParseException e) {
			result.setResultOK(false);
			result.setErrorMsg(e.getMessage());
			logger.error(e);
		}

		try {
			scheduler.scheduleJob(jobDetail, trigger);
			jobDetail.getJobDataMap().put(JobInfo.JobStatusKey, 1);
			scheduler.addJob(jobDetail, true);
			result.setResultOK(true);
		} catch (SchedulerException e) {
			result.setResultOK(false);
			result.setErrorMsg(e.getMessage());
			logger.error(e);
		}

		return result;
	}

	@Override
	public List<JobInfo> queryJob(JobInfo jobInfo) {

		List<JobInfo> jobInfoList = new LinkedList<JobInfo>();
		try {
			String[] jobGroupNames = scheduler.getJobGroupNames();
			if (jobGroupNames != null && jobGroupNames.length > 0) {
				int groupNamesLength = jobGroupNames.length;
				for (int i = 0; i < groupNamesLength; i++) {
					String groupName = jobGroupNames[i];
					String[] jobNames = scheduler.getJobNames(groupName);
					if (jobNames != null && jobNames.length > 0) {
						int jobNamesLength = jobNames.length;
						for (int j = 0; j < jobNamesLength; j++) {
							String jobName = jobNames[j];
							JobDetail jobDetail = scheduler.getJobDetail(
									jobName, groupName);
							JobInfo info = createJobInfo(jobDetail);
							if (info == null) {
								continue;
							}
							if (jobInfo == null) {
								jobInfoList.add(info);
							} else if (filterJobInfo(jobInfo, info)) {
								jobInfoList.add(info);
							}
						}
					}
				}
			}
		} catch (SchedulerException e) {
			logger.error(e);
		}
		return jobInfoList;
	}

	@Override
	public Result deleteJob(String jobName, String jobGroup) {

		Result result = new Result();

		if (jobName == null || "".equals(jobName)) {
			result.setResultOK(false);
			result.setErrorMsg("待删除的任务名称不能为空");
			return result;
		}
		if (jobGroup == null || "".equals(jobGroup)) {
			result.setResultOK(false);
			result.setErrorMsg("待删除的任务分组名称不能为空");
			return result;
		}

		try {
			scheduler.pauseAll();
			scheduler.deleteJob(jobName, jobGroup);
			scheduler.resumeAll();
			result.setResultOK(true);
		} catch (SchedulerException e) {
			result.setResultOK(false);
			result.setErrorMsg(e.getMessage());
			logger.error(e);
		}
		return result;
	}

	@Override
	public Result startJob(String jobName, String jobGroup) {

		Result result = new Result();

		if (jobName == null || "".equals(jobName)) {
			result.setResultOK(false);
			result.setErrorMsg("待启动的任务名称不能为空");
			return result;
		}
		if (jobGroup == null || "".equals(jobGroup)) {
			result.setResultOK(false);
			result.setErrorMsg("待启动的任务分组名称不能为空");
			return result;
		}

		try {
			scheduler.resumeJob(jobName, jobGroup);
			result.setResultOK(true);
			JobDetail jobDetail = scheduler.getJobDetail(jobName, jobGroup);
			jobDetail.getJobDataMap().put(JobInfo.JobStatusKey, 1);
			scheduler.addJob(jobDetail, true);
		} catch (SchedulerException e) {
			result.setResultOK(false);
			result.setErrorMsg(e.getMessage());
			logger.error(e);
		}
		return result;
	}

	@Override
	public Result pauseJob(String jobName, String jobGroup) {
		Result result = new Result();

		if (jobName == null || "".equals(jobName)) {
			result.setResultOK(false);
			result.setErrorMsg("待暂停的任务名称不能为空");
			return result;
		}
		if (jobGroup == null || "".equals(jobGroup)) {
			result.setResultOK(false);
			result.setErrorMsg("待暂停的任务分组名称不能为空");
			return result;
		}

		try {
			scheduler.pauseJob(jobName, jobGroup);
			JobDetail jobDetail = scheduler.getJobDetail(jobName, jobGroup);
			jobDetail.getJobDataMap().put(JobInfo.JobStatusKey, 2);
			scheduler.addJob(jobDetail, true);
			result.setResultOK(true);
		} catch (SchedulerException e) {
			result.setResultOK(false);
			result.setErrorMsg(e.getMessage());
			logger.error(e);
		}
		return result;
	}

	@Override
	public Result updateJob(JobInfo jobInfo) {
		Result result = new Result();

		String jobName = jobInfo.getJobName();
		String jobGroup = jobInfo.getJobGroup();

		if (jobName == null || "".equals(jobName)) {
			result.setResultOK(false);
			result.setErrorMsg("待更新的任务名称不能为空");
			return result;
		}
		if (jobGroup == null || "".equals(jobGroup)) {
			result.setResultOK(false);
			result.setErrorMsg("待更新的任务分组名称不能为空");
			return result;
		}

		try {
			// 执行任务的更新动作:先将任务删除，再重新添加此任务
			scheduler.pauseAll();
			scheduler.deleteJob(jobName, jobGroup);

			Result addResult = addJob(jobInfo);

			if (addResult.isResultOK()) {
				result.setResultOK(true);
			} else {
				result.setResultOK(false);
				result.setErrorMsg(addResult.getErrorMsg());
			}

			scheduler.resumeAll();
		} catch (SchedulerException e) {
			result.setResultOK(false);
			result.setErrorMsg(e.toString());
			logger.error(e);
		}
		return result;
	}

	public boolean shutDown() {
		try {
			scheduler.shutdown(true);
			logger.info("调度器已关闭");
			return true;
		} catch (SchedulerException e) {
			logger.error(e);
			return false;
		}
	}

	private JobDetail createJobDetail(JobInfo jobInfo) {

		JobDetail jobDetail = new JobDetail();
		jobDetail.setJobClass(CommonJob.class);
		jobDetail.setName(jobInfo.getJobName());
		jobDetail.setGroup(jobInfo.getJobGroup());

		JobDataMap dataMap = jobDetail.getJobDataMap();
		if (jobInfo.getTriggerType() > 0) {
			dataMap.put(JobInfo.TriggerTypeKey, jobInfo.getTriggerType());
		}
		if (jobInfo.getDataExtractJobType() > 0) {
			dataMap.put(JobInfo.DataExtractJobTypeKey,
					jobInfo.getDataExtractJobType());
		}
		if (jobInfo.getDataExtractSource() != null
				&& !"".equals(jobInfo.getDataExtractSource())) {
			dataMap.put(JobInfo.DataExtractSourceKey,
					jobInfo.getDataExtractSource());
		}
		if (jobInfo.getDataExtractSQL() != null
				&& !"".equals(jobInfo.getDataExtractSQL())) {
			dataMap.put(JobInfo.DataExtractSQLKey, jobInfo.getDataExtractSQL());
		}
		if (jobInfo.getDataConsumerType() > 0) {
			dataMap.put(JobInfo.DataConsumerTypeKey,
					jobInfo.getDataConsumerType());
		}
		if (jobInfo.getDataConsumerSource() != null
				&& !"".equals(jobInfo.getDataConsumerSource())) {
			dataMap.put(JobInfo.DataConsumerSourceKey,
					jobInfo.getDataConsumerSource());
		}
		if (jobInfo.getDataConsumerSQL() != null
				&& !"".equals(jobInfo.getDataConsumerSQL())) {
			dataMap.put(JobInfo.DataConsumerSQLKey,
					jobInfo.getDataConsumerSQL());
		}
		if (jobInfo.getRate() > 0) {
			dataMap.put(JobInfo.CronTriggerRateKey, jobInfo.getRate());
		}
		dataMap.put(JobInfo.HealthStatusKey, 1);

		return jobDetail;
	}

	private Trigger createTrigger(JobInfo jobInfo) throws ParseException {
		int triggerType = jobInfo.getTriggerType();

		if (triggerType == 1) {
			return createSimpleTrigger(jobInfo);
		}

		if (triggerType == 2) {
			return createCronTrigger(jobInfo);
		}

		return null;

	}

	private SimpleTrigger createSimpleTrigger(JobInfo jobInfo) {

		SimpleTrigger simpleTrigger = new SimpleTrigger();
		simpleTrigger.setName(getTriggerName(jobInfo.getJobName()));
		simpleTrigger.setGroup(jobInfo.getJobGroup());
		simpleTrigger.setStartTime(jobInfo.getStartTime());
		simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		simpleTrigger.setRepeatInterval(jobInfo.getInterval() * 60 * 1000);
		simpleTrigger
				.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);

		return simpleTrigger;
	}

	private CronTrigger createCronTrigger(JobInfo jobInfo)
			throws ParseException {
		CronTrigger cronTrigger = new CronTrigger();
		cronTrigger.setName(getTriggerName(jobInfo.getJobName()));
		cronTrigger.setGroup(jobInfo.getJobGroup());
		cronTrigger.setCronExpression(jobInfo.getCron());
		cronTrigger
				.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
		return cronTrigger;
	}

	private JobInfo createJobInfo(JobDetail jobDetail) {
		JobInfo jobInfo = new JobInfo();

		JobDataMap jobDataMap = jobDetail.getJobDataMap();

		jobInfo.setJobName(jobDetail.getName());
		jobInfo.setJobGroup(jobDetail.getGroup());
		if (jobDataMap != null && !jobDataMap.isEmpty()) {
			if (jobDataMap.get(JobInfo.TriggerTypeKey) != null) {
				int triggerTypeKey = (Integer) jobDataMap
						.get(JobInfo.TriggerTypeKey);
				jobInfo.setTriggerType(triggerTypeKey);
			}
			if (jobDataMap.get(JobInfo.JobStatusKey) != null) {
				int jobStatus = (Integer) jobDataMap.get(JobInfo.JobStatusKey);
				jobInfo.setJobStatus(jobStatus);
			}
			if (jobDataMap.get(JobInfo.HealthStatusKey) != null) {
				int healthStatus = (Integer) jobDataMap
						.get(JobInfo.HealthStatusKey);
				jobInfo.setHealthStatus(healthStatus);
			}
			if (jobDataMap.get(JobInfo.DataExtractJobTypeKey) != null) {
				int dataExtractJobType = (Integer) jobDataMap
						.get(JobInfo.DataExtractJobTypeKey);
				jobInfo.setDataExtractJobType(dataExtractJobType);
			}
			if (jobDataMap.get(JobInfo.DataExtractSourceKey) != null) {
				String dataExtractSource = (String) jobDataMap
						.get(JobInfo.DataExtractSourceKey);
				jobInfo.setDataExtractSource(dataExtractSource);
			}
			if (jobDataMap.get(JobInfo.DataExtractSQLKey) != null) {
				String dataExtractSQL = (String) jobDataMap
						.get(JobInfo.DataExtractSQLKey);
				jobInfo.setDataExtractSQL(dataExtractSQL);
			}

			if (jobDataMap.get(JobInfo.DataConsumerTypeKey) != null) {
				int dataConsumerType = (Integer) jobDataMap
						.get(JobInfo.DataConsumerTypeKey);
				jobInfo.setDataConsumerType(dataConsumerType);
			}
			if (jobDataMap.get(JobInfo.DataConsumerSourceKey) != null) {
				String dataConsumerSource = (String) jobDataMap
						.get(JobInfo.DataConsumerSourceKey);
				jobInfo.setDataConsumerSource(dataConsumerSource);
			}
			if (jobDataMap.get(JobInfo.DataConsumerSQLKey) != null) {
				String dataConsumerSQL = (String) jobDataMap
						.get(JobInfo.DataConsumerSQLKey);
				jobInfo.setDataConsumerSQL(dataConsumerSQL);
			}
			if (jobDataMap.get(JobInfo.ErrorLogKey) != null) {
				String errorLog = (String) jobDataMap.get(JobInfo.ErrorLogKey);
				jobInfo.setErrorLog(errorLog);
			}
			if (jobDataMap.get(JobInfo.CronTriggerRateKey) != null) {
				int cronTriggerRate = (Integer) jobDataMap
						.get(JobInfo.CronTriggerRateKey);
				jobInfo.setRate(cronTriggerRate);
			}
			if (jobDataMap.get(JobInfo.ExceptionFrequencyInfosKey) != null) {
				Map<String, ExceptionFrequencyInfo> exceptionFrequencyInfos = (Map<String, ExceptionFrequencyInfo>) jobDataMap
						.get(JobInfo.ExceptionFrequencyInfosKey);
				jobInfo.setExceptionFrequencyInfos(exceptionFrequencyInfos);
			}
			if (jobDataMap.get(JobInfo.ExceptionDetailInfosKey) != null) {
				Map<String, ExceptionDetailInfo> exceptionDetailInfos = (Map<String, ExceptionDetailInfo>) jobDataMap
						.get(JobInfo.ExceptionDetailInfosKey);
				jobInfo.setExceptionDetailInfos(exceptionDetailInfos);
			}

			try {
				Trigger trigger = scheduler.getTrigger(
						getTriggerName(jobDetail.getName()),
						jobDetail.getGroup());

				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
					jobInfo.setInterval((int) simpleTrigger.getRepeatInterval()
							/ (60 * 1000));
					jobInfo.setPrevExecuteTime(simpleTrigger
							.getPreviousFireTime());
					jobInfo.setStartTime(simpleTrigger.getStartTime());
				} else if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					jobInfo.setPrevExecuteTime(cronTrigger
							.getPreviousFireTime());
					jobInfo.setStartTime(cronTrigger.getStartTime());
					jobInfo.setCron(cronTrigger.getCronExpression());
				}
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}

		return jobInfo;
	}

	private String getTriggerName(String jobName) {
		// 触发器的名称前端没有传来参数，系统自定设置为任务名称 + Trigger
		return jobName + "Trigger";
	}

	private boolean filterJobInfo(JobInfo condition, JobInfo target) {
		// 根据查询的条件（任务名称、执行频率、任务状态、健康状态），对数据库中的任务进行过滤

		String jobName = condition.getJobName();
		int interval = condition.getInterval();
		int jobStatus = condition.getJobStatus();
		int healthStatus = condition.getHealthStatus();

		if (jobName != null && !"".equals(jobName)) {
			if (!target.getJobName().equals(jobName)) {
				return false;
			}
		}

		if (interval > 0) {
			if (target.getInterval() != interval) {
				return false;
			}
		}

		if (jobStatus > 0) {
			if (jobStatus != target.getJobStatus()) {
				return false;
			}
		}

		if (healthStatus > 0) {
			if (healthStatus != target.getHealthStatus()) {
				return false;
			}
		}

		return true;

	}

}
