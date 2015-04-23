package com.nari.rdt.chain.handler;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nari.rdt.InitializeLocale;
import com.nari.rdt.chain.Request;
import com.nari.rdt.jms.IMSMessageProducer;
import com.nari.rdt.thrift.Result;
import com.nari.rdt.util.ByteUtil;

public class Deliver2MQHandler extends Handler {
	private static Logger loger = Logger.getLogger(Deliver2DBHandler.class);
	private IMSMessageProducer producer;
	
	public IMSMessageProducer getProducer() {
		return producer;
	}
	public void setProducer(IMSMessageProducer producer) {
		this.producer = producer;
	}
	@Override
	public void handlerRequest(Request request) throws Exception {
		// TODO Auto-generated method stub
		loger.debug("[Debug] Deliver2MQHandler running");
		if(3 == request.getLevel())
		{
			//将数据存储至消息总线
			Map map = (Map)ByteUtil.getObject(request.getMessage().getContent());
			try{
			producer.send(map);
			}
			catch(Exception e)
			{
				Result result = request.getResult();
				result.setResultValue(0);
				result.setErrorCode(6001);
				InitializeLocale locale = new InitializeLocale();
				String meg = locale.getMessage("6001");
		        result.setMessage(meg);
				request.setResult(result);
				throw e;
			}
			finally
			{
				this.deal.put(request, true);
			}
		}
	}

}
