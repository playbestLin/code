package com.nari.rdt.cron.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.nari.rdt.MsgHandler;
import com.nari.rdt.SpringBeanLoader;
import com.nari.rdt.chain.handler.TransshipmentHandler;
import com.nari.rdt.service.ICommonService;
import com.nari.rdt.thrift.Message;
import com.nari.rdt.thrift.Result;
import com.nari.rdt.util.ByteUtil;

public class CommonJob implements Job {
	private static final Logger logger = Logger.getLogger(CommonJob.class
			.getName());

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap dataMap = jobDetail.getJobDataMap();

		Result result = null;
		try {
			
			System.out.println(context.getJobDetail().getName() + "-被执行- "
					+ new Date());

			SpringBeanLoader beanLoader = SpringBeanLoader.getInstance();

			ICommonService commonService = (ICommonService) beanLoader
					.getApplicationContext().getBean("commonServiceImpl");

			Message message = new Message();
			// 数据交付的模式：DB or MQ
			int dataConsumerType = dataMap.getInt(JobInfo.DataConsumerTypeKey);
			message.setDeliveryMode(getDeliveryMode(dataConsumerType));
			message.setAppId("alertservice");
			message.setType("alarm");
			// 数据发送目标地址[Host]:[Port]
			String destination = dataMap
					.getString(JobInfo.DataConsumerSourceKey);
			message.setDestination(destination);
			message = MsgHandler.sealMessage(message);

			// 执行的SQL语句的模板
			String executeSQL = dataMap.getString(JobInfo.DataConsumerSQLKey);
			message.setExecuteSQL(executeSQL);
			// 消息的内容
			List<Map> extractResult = null;

			// 从数据库中抽取数据
			String dataExtractSQL = dataMap
					.getString(JobInfo.DataExtractSQLKey);
			extractResult = commonService.extractFromDB(dataExtractSQL);

			message.setContent(ByteUtil.getByteBuffer(extractResult));

			TransshipmentHandler transshipmentHandler = (TransshipmentHandler) beanLoader
					.getApplicationContext().getBean("transshipmentHandler");

			result = transshipmentHandler.transData(message);

			if (result.getResultValue() == 0) {
				try {
					jobDetail = processException(jobDetail, result,
							message.getMessageID(), null);
					context.getScheduler().addJob(jobDetail, true);
				} catch (SchedulerException schedulerException) {
					logger.error(schedulerException);
				}
			}
			
			if(dataMap.containsKey(JobInfo.HealthStatusKey)) {
				int healthStatus = dataMap.getInt(JobInfo.HealthStatusKey);
				if(healthStatus == 2) {
					dataMap.put(JobInfo.HealthStatusKey, 1);
				}
			}

		} catch (Exception e) {
			try {
				jobDetail = processException(jobDetail, result, null, e);
				context.getScheduler().addJob(jobDetail, true);
			} catch (SchedulerException schedulerException) {
				logger.error(schedulerException);
			}
		}
	}

	private JobDetail processException(JobDetail jobDetail, Result result,
			String messageID, Exception e) {

		String exceptionType = null;
		String errorMessage = null;
		if (result != null) {
			exceptionType = String.valueOf(result.errorCode);
			errorMessage = result.getMessage();
		}

		if (e != null) {
			exceptionType = e.getClass().getSimpleName();
			errorMessage = e.getMessage();
		}

		jobDetail = updateExceptionDetailInfo(jobDetail, exceptionType,
				messageID, errorMessage);

		if (exceptionType != null) {
			updateExceptionFrequency(jobDetail, exceptionType);
		}
		
		jobDetail.getJobDataMap().put(JobInfo.HealthStatusKey, 2);

		return jobDetail;
	}

	/**
	 * 
	 * @param jobDetail
	 * @param exceptionType
	 * @param messageID
	 *            在Message转发前产生的异常此字段可为空
	 * @param errorMessage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JobDetail updateExceptionDetailInfo(JobDetail jobDetail,
			String exceptionType, String messageID, String errorMessage) {
		JobDataMap dataMap = jobDetail.getJobDataMap();
		Map<String, ExceptionDetailInfo> messageExceptionInfoMap = null;
		if (dataMap.containsKey(JobInfo.ExceptionDetailInfosKey)) {
			messageExceptionInfoMap = (Map<String, ExceptionDetailInfo>) dataMap
					.get(JobInfo.ExceptionDetailInfosKey);
		}

		if (messageExceptionInfoMap == null) {
			messageExceptionInfoMap = new HashMap<String, ExceptionDetailInfo>();
		}

		ExceptionDetailInfo messageExceptionInfo = new ExceptionDetailInfo();
		messageExceptionInfo.setExceptionType(exceptionType);
		if (messageID != null) {
			messageExceptionInfo.setMessageID(messageID);
		}
		messageExceptionInfo.setErrorMessage(errorMessage);
		messageExceptionInfo.setOccurDate(new Date());

		String key = messageExceptionInfo.getExceptionType()
				+ messageExceptionInfo.getOccurDate();
		messageExceptionInfoMap.put(key, messageExceptionInfo);

		dataMap.put(JobInfo.ExceptionDetailInfosKey, messageExceptionInfoMap);

		return jobDetail;
	}

	@SuppressWarnings("unchecked")
	private JobDetail updateExceptionFrequency(JobDetail jobDetail,
			String exceptionType) {
		JobDataMap dataMap = jobDetail.getJobDataMap();

		Map<String, ExceptionFrequencyInfo> exceptionFrequencyInfoMap = null;

		if (dataMap.containsKey(JobInfo.ExceptionFrequencyInfosKey)) {
			exceptionFrequencyInfoMap = (Map<String, ExceptionFrequencyInfo>) dataMap
					.get(JobInfo.ExceptionFrequencyInfosKey);
		}

		if (exceptionFrequencyInfoMap == null) {
			exceptionFrequencyInfoMap = new HashMap<String, ExceptionFrequencyInfo>();
		}

		ExceptionFrequencyInfo commonExceptionInfo = null;

		if (exceptionFrequencyInfoMap.containsKey(exceptionType)) {
			commonExceptionInfo = exceptionFrequencyInfoMap.get(exceptionType);
			commonExceptionInfo.setNumOfOccur(commonExceptionInfo
					.getNumOfOccur() + 1);
			commonExceptionInfo.setDate(new Date());
		} else {
			commonExceptionInfo = new ExceptionFrequencyInfo();
			commonExceptionInfo.setExceptionType(exceptionType);
			commonExceptionInfo.setNumOfOccur(1);
			commonExceptionInfo.setDate(new Date());
		}

		exceptionFrequencyInfoMap.put(exceptionType, commonExceptionInfo);

		dataMap.put(JobInfo.ExceptionFrequencyInfosKey,
				exceptionFrequencyInfoMap);
		return jobDetail;
	}

	private String getDeliveryMode(int dataConsumerType) {
		if (dataConsumerType == 1) {
			return "DB";
		}
		if (dataConsumerType == 2) {
			return "MQ";
		}
		return "DB";
	}
}
