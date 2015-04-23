package com.nari.rdt.chain;

import java.util.concurrent.ConcurrentHashMap;

import com.nari.rdt.thrift.Message;
import com.nari.rdt.thrift.Result;

public class Request {
	private long requestId;
	private int level; // 请求等级
	private Message message; //消息
	private ConcurrentHashMap<String, String> destination;
	private Result result; //返回结果

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Request(long requestId, int level, Message message) {
		this.requestId = requestId;
		this.level = level;
		this.message = message;
	}
	
	public Request(long requestId,int level, Message message, ConcurrentHashMap<String, String> destination) {
		this.requestId = requestId;
		this.level = level;
		this.message = message;
		this.destination = destination;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public ConcurrentHashMap<String, String> getDestination() {
		return destination;
	}

	public void setDestination(ConcurrentHashMap<String, String> destination) {
		this.destination = destination;
	}

}
