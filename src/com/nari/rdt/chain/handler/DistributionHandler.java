package com.nari.rdt.chain.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.nari.rdt.InitializeCache;
import com.nari.rdt.chain.Request;

public class DistributionHandler extends Handler {
	private static Logger loger = Logger.getLogger(DistributionHandler.class);
	@Override
	public void handlerRequest(Request request) {
		// TODO Auto-generated method stub
		if(1 == request.getLevel())
		{
			if(isDeliveryToMe(request.getDestination().get("host")))
			{
				//数据传送目的地是自己
				
				//根据数据类型判断是消息总线交付模式(3)还是数据库持久化交付模式(4)
				String mode = request.getMessage().getDeliveryMode();
				if ("DB".equalsIgnoreCase(mode))
				{
					request.setLevel(4);
				}
				else if ("MQ".equalsIgnoreCase(mode))
				{
					request.setLevel(3);
				}
				else if ("H2".equalsIgnoreCase(mode))
				{
					request.setLevel(9);
				}
				else
				{
					this.deal.put(request, true);
				}
				
			}else
			{
				//数据传送目的地不是自己，则进入转发流程责任链
				request.setLevel(2);
			}
		}
	}
	
	private boolean isDeliveryToMe(String destination)
	{
		List<String> ipList = InitializeCache.getHostAddress();
		for(String ip : ipList)
		{
			if(destination.equals(ip))
			{
				return true;
			}
		}
		return false;
	}

}
