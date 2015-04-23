package com.nari.rdt.cron.job;

import java.util.*;

public interface IJobHandler {

	/**
	 * 添加新的计划，计划名称，计划分组，时间间隔必填，开始时间若不填将默认为立即开始
	 * 
	 * @param jobInfo
	 * @return
	 */
	public Result addJob(JobInfo jobInfo);

	/**
	 * 查询当前调度器中的任务，JobInfo中可以给出查询的约束条件（任务名称、 执行频率、任务状态、健康状态），若未设置相应的约束条件则默认全部查询。
	 * 
	 * @param jobInfo
	 * @return
	 */
	public List<JobInfo> queryJob(JobInfo jobInfo);

	/**
	 * 删除当前调度器中的任务，需给出待删除的任务的名称和分组（jobName，jobGroup）
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public Result deleteJob(String jobName, String jobGroup);

	/**
	 * 启动任务，需给出待启动的任务的名称和分组
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public Result startJob(String jobName, String jobGroup);

	/**
	 * 暂停任务，需给出待暂停的任务的名称和分组
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public Result pauseJob(String jobName, String jobGroup);

	/**
	 * 更新任务
	 * 
	 * @param jobInfo
	 * @return
	 */
	public Result updateJob(JobInfo jobInfo);
}
