package com.nari.rdt.cron.job;

import java.io.Serializable;
import java.util.Date;

public class ExceptionFrequencyInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String exceptionType;
	private int numOfOccur;
	private Date date;
	@Deprecated
	private String errorMessage;

	public ExceptionFrequencyInfo() {

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
	 * @return the numOfOccur
	 */
	public int getNumOfOccur() {
		return numOfOccur;
	}

	/**
	 * @param numOfOccur
	 *            the numOfOccur to set
	 */
	public void setNumOfOccur(int numOfOccur) {
		this.numOfOccur = numOfOccur;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the errorMessage
	 * @deprecated
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 * @deprecated
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("----------------- 异常详细：---------------------"  + "\r\n");
		builder.append("{异常类型：" + exceptionType + "\r\n");
		builder.append("出现频率：" + numOfOccur + "\r\n");
		builder.append("最近出现时间：" + date + "}\r\n");
		builder.append("-----------------  end   ---------------------"  + "\r\n");
		return builder.toString();
	}
}
