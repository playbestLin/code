package com.nari.rdt.cron.job;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

public class JobInfo {

	public final static String TriggerTypeKey = "TriggerType";
	public final static String JobStatusKey = "JobStatus";
	public final static String HealthStatusKey = "HealthStatus";
	public final static String DataExtractJobTypeKey = "DataExtractJobType";
	public final static String DataExtractSourceKey = "DataExtractSource";
	public final static String DataExtractSQLKey = "DataExtractSource";
	public final static String DataConsumerTypeKey = "DataConsumerType";
	public final static String DataConsumerSourceKey = "DataConsumerSource";
	public final static String DataConsumerSQLKey = "DataConsumerSQL";
	public final static String ErrorLogKey = "ErrorLog";
	public final static String CronTriggerRateKey = "CronTriggerRate";
	public final static String ExceptionFrequencyInfosKey = "ExceptionFrequencyInfos";
	public final static String ExceptionDetailInfosKey = "ExceptionDetailInfos";

	// 计划名称
	private String jobName;
	// 计划分组
	private String jobGroup;
	// 触发方式，1表示定时器(SimpleTrigger)，2表示计划任务(CronTrigger)
	private int triggerType;
	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	// 时间间隔
	private int interval;
	// 数据抽取的Job类型
	private int dataExtractJobType;
	// 数据抽取的数据源
	private String dataExtractSource;
	// 数据抽取的SQL
	private String dataExtractSQL;
	// 数据消费的类型
	private int dataConsumerType;
	// 数据消费的数据源
	private String dataConsumerSource;
	// 插入数据的SQL模板
	private String dataConsumerSQL;
	// 任务状态，1表示启动，2表示停止
	private int jobStatus;
	// 健康状态，1表示正常，2表示异常
	private int healthStatus;
	// 最近一次执行时间
	private Date prevExecuteTime;
	// 错误日志
	private String errorLog;
	// 计划任务的计划描述
	private String cron;
	// 计划任务的执行频率：天、周、月、高级
	private int rate;
	// 异常信息
	private Map<String, ExceptionFrequencyInfo> exceptionFrequencyInfos;
	// 消息执行的异常信息
	private Map<String, ExceptionDetailInfo> exceptionDetailInfos;

	public JobInfo() {

	}

	/**
	 * 获得计划名称
	 * 
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * 设置计划名称
	 * 
	 * @param jobName
	 *            the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * 获得计划分组
	 * 
	 * @return the jobGroup
	 */
	public String getJobGroup() {
		return jobGroup;
	}

	/**
	 * 设置计划分组
	 * 
	 * @param jobGroup
	 *            the jobGroup to set
	 */
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	/**
	 * 获得触发方式
	 * 
	 * @return the triggerType
	 */
	public int getTriggerType() {
		return triggerType;
	}

	/**
	 * 设置触发方式，1表示定时器(SimpleTrigger)，2表示计划任务(CronTrigger)
	 * 
	 * @param triggerType
	 *            the triggerType to set
	 */
	public void setTriggerType(int triggerType) {
		this.triggerType = triggerType;
	}

	/**
	 * 获得计划开始时间
	 * 
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 设置计划开始时间
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获得计划执行间隔（分钟）
	 * 
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * 设置计划执行间隔（分钟）
	 * 
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * 获得数据抽取的计划类型
	 * 
	 * @return the dataExtractJobType
	 */
	public int getDataExtractJobType() {
		return dataExtractJobType;
	}

	/**
	 * 设置数据抽取的计划类型
	 * 
	 * @param dataExtractJobType
	 *            the dataExtractJobType to set
	 */
	public void setDataExtractJobType(int dataExtractJobType) {
		this.dataExtractJobType = dataExtractJobType;
	}

	/**
	 * 获得数据抽取的数据源
	 * 
	 * @return the dataExtractSource
	 */
	public String getDataExtractSource() {
		return dataExtractSource;
	}

	/**
	 * 设置数据抽取的数据源
	 * 
	 * @param dataExtractSource
	 *            the dataExtractSource to set
	 */
	public void setDataExtractSource(String dataExtractSource) {
		this.dataExtractSource = dataExtractSource;
	}

	/**
	 * 获得数据抽取的SQL
	 * 
	 * @return the dataExtractSQL
	 */
	public String getDataExtractSQL() {
		return dataExtractSQL;
	}

	/**
	 * 设置数据抽取的SQL
	 * 
	 * @param dataExtractSQL
	 *            the dataExtractSQL to set
	 */
	public void setDataExtractSQL(String dataExtractSQL) {
		this.dataExtractSQL = dataExtractSQL.trim();
	}

	/**
	 * 获得数据消费的消费类型
	 * 
	 * @return the dataConsumerType
	 */
	public int getDataConsumerType() {
		return dataConsumerType;
	}

	/**
	 * 设置数据消费的消费类型
	 * 
	 * @param dataConsumerType
	 *            the dataConsumerType to set
	 */
	public void setDataConsumerType(int dataConsumerType) {
		this.dataConsumerType = dataConsumerType;
	}

	/**
	 * 获得数据消费的数据源
	 * 
	 * @return the dataConsumerSource
	 */
	public String getDataConsumerSource() {
		return dataConsumerSource;
	}

	/**
	 * 设置数据消费的数据源
	 * 
	 * @param dataConsumerSource
	 *            the dataConsumerSource to set
	 */
	public void setDataConsumerSource(String dataConsumerSource) {
		this.dataConsumerSource = dataConsumerSource;
	}

	/**
	 * 获得数据消费的SQL
	 * 
	 * @return the dataConsumerSQL
	 */
	public String getDataConsumerSQL() {
		return dataConsumerSQL;
	}

	/**
	 * 设置数据消费的SQL
	 * 
	 * @param dataConsumerSQL
	 *            the dataConsumerSQL to set
	 */
	public void setDataConsumerSQL(String dataConsumerSQL) {
		this.dataConsumerSQL = dataConsumerSQL.trim().toUpperCase();
	}

	/**
	 * 获得任务运行状态
	 * 
	 * @return the jobStatus
	 */
	public int getJobStatus() {
		return jobStatus;
	}

	/**
	 * 设置任务运行状态，1表示启动，2表示停止
	 * 
	 * @param jobStatus
	 *            the jobStatus to set
	 */
	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}

	/**
	 * 获得任务健康状态
	 * 
	 * @return the healthStatus
	 */
	public int getHealthStatus() {
		return healthStatus;
	}

	/**
	 * 设置任务健康状态，1表示正常，2表示异常
	 * 
	 * @param healthStatus
	 *            the healthStatus to set
	 */
	public void setHealthStatus(int healthStatus) {
		this.healthStatus = healthStatus;
	}

	/**
	 * 获得最近一次执行时间
	 * 
	 * @return the prevExecuteTime
	 */
	public Date getPrevExecuteTime() {
		return prevExecuteTime;
	}

	/**
	 * 设置最近一次执行时间
	 * 
	 * @param prevExecuteTime
	 *            the prevExecuteTime to set
	 */
	public void setPrevExecuteTime(Date prevExecuteTime) {
		this.prevExecuteTime = prevExecuteTime;
	}

	/**
	 * 获得错误日志
	 * 
	 * @return the errorLog
	 */
	public String getErrorLog() {
		return errorLog;
	}

	/**
	 * 设置错误日志
	 * 
	 * @param errorLog
	 *            the errorLog to set
	 */
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	/**
	 * 获得计划任务的计划描述
	 * 
	 * @return the cron
	 */
	public String getCron() {
		return cron;
	}

	/**
	 * 设置计划任务的计划描述
	 * 
	 * @param cron
	 *            the cron to set
	 */
	public void setCron(String cron) {
		this.cron = cron;
	}

	/**
	 * @return the rate
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * @return the exceptionFrequencyInfos
	 */
	public Map<String, ExceptionFrequencyInfo> getExceptionFrequencyInfos() {
		return exceptionFrequencyInfos;
	}

	public List<ExceptionFrequencyInfo> getCommonExceptionInfoList() {
		List<ExceptionFrequencyInfo> list = new LinkedList<ExceptionFrequencyInfo>();
		if (exceptionFrequencyInfos != null
				&& !exceptionFrequencyInfos.isEmpty()) {
			for (Iterator<String> it = exceptionFrequencyInfos.keySet()
					.iterator(); it.hasNext();) {
				String key = it.next();
				ExceptionFrequencyInfo frequencyInfo = exceptionFrequencyInfos
						.get(key);
				list.add(frequencyInfo);
			}
		}
		return list;
	}

	/**
	 * @param exceptionFrequencyInfos
	 *            the exceptionFrequencyInfos to set
	 */
	public void setExceptionFrequencyInfos(
			Map<String, ExceptionFrequencyInfo> exceptionFrequencyInfos) {
		this.exceptionFrequencyInfos = exceptionFrequencyInfos;
	}

	/**
	 * @return the exceptionDetailInfos
	 */
	public Map<String, ExceptionDetailInfo> getExceptionDetailInfos() {
		return exceptionDetailInfos;
	}
	
	public List<ExceptionDetailInfo> getExceptionDetailInfoList() {
		List<ExceptionDetailInfo> list = new LinkedList<ExceptionDetailInfo>();
		if (exceptionDetailInfos != null
				&& !exceptionDetailInfos.isEmpty()) {
			for (Iterator<String> it = exceptionDetailInfos.keySet()
					.iterator(); it.hasNext();) {
				String key = it.next();
				ExceptionDetailInfo detailInfo = exceptionDetailInfos
						.get(key);
				list.add(detailInfo);
			}
		}
		return list;
	}

	/**
	 * @param exceptionDetailInfos
	 *            the exceptionDetailInfos to set
	 */
	public void setExceptionDetailInfos(
			Map<String, ExceptionDetailInfo> exceptionDetailInfos) {
		this.exceptionDetailInfos = exceptionDetailInfos;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("计划名称：" + jobName + "\r\n");
		builder.append("计划分组：" + jobGroup + "\r\n");
		builder.append("触发方式：" + triggerType + "\r\n");
		builder.append("开始时间：" + startTime + "\r\n");
		builder.append("时间间隔（分钟）：" + interval + "\r\n");
		builder.append("任务状态：" + jobStatus + "\r\n");
		builder.append("健康状态：" + healthStatus + "\r\n");
		builder.append("最近一次执行时间：" + prevExecuteTime + "\r\n");
		builder.append("数据抽取的Job类型：" + dataExtractJobType + "\r\n");
		builder.append("数据抽取的数据源：" + dataExtractSource + "\r\n");
		builder.append("数据抽取的SQL：" + dataExtractSQL + "\r\n");
		builder.append("数据消费的类型：" + dataConsumerType + "\r\n");
		builder.append("数据消费的数据源：" + dataConsumerSource + "\r\n");
		builder.append("插入数据的SQL模板：" + dataConsumerSQL + "\r\n");
		builder.append("计划任务的计划描述：" + cron + "\r\n");
		if(exceptionFrequencyInfos != null && !exceptionFrequencyInfos.isEmpty()) {
			for (Iterator<String> it = exceptionFrequencyInfos.keySet()
					.iterator(); it.hasNext();) {
				String key = it.next();
				ExceptionFrequencyInfo frequencyInfo = exceptionFrequencyInfos
						.get(key);
				builder.append(frequencyInfo);
			}
		}
		
		if(exceptionDetailInfos != null && !exceptionDetailInfos.isEmpty()) {
			for (Iterator<String> it = exceptionDetailInfos.keySet()
					.iterator(); it.hasNext();) {
				String key = it.next();
				ExceptionDetailInfo detailInfo = exceptionDetailInfos
						.get(key);
				builder.append(detailInfo);
			}
		}
		return builder.toString();
	}

}
