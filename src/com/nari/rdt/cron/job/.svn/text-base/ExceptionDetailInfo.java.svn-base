package com.nari.rdt.cron.job;

import java.io.Serializable;
import java.util.Date;

public class ExceptionDetailInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String exceptionType;
	private String messageID;
	private String errorMessage;
	private Date occurDate;

	public ExceptionDetailInfo() {

	}

	/**
	 * @return the exceptionType
	 */
	public String getExceptionType() {
		return exceptionType;
	}

	/**
	 * @param exceptionType
	 *            the exceptionType to set
	 */
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	/**
	 * @return the messageID
	 */
	public String getMessageID() {
		return messageID;
	}

	/**
	 * @param messageID
	 *            the messageID to set
	 */
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the occurDate
	 */
	public Date getOccurDate() {
		return occurDate;
	}

	/**
	 * @param occurDate
	 *            the occurDate to set
	 */
	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("----------------- 异常详细：---------------------"  + "\r\n");
		builder.append("{异常类型：" + exceptionType + "\r\n");
		builder.append("消息ID：" + messageID + "\r\n");
		builder.append("异常详情：" + errorMessage + "\r\n");
		builder.append("异常出现时间：" + occurDate +"}\r\n");
		builder.append("-----------------  end   ---------------------"  + "\r\n");
		return builder.toString();
	}

}
