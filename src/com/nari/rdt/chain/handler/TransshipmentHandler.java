package com.nari.rdt.chain.handler;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.nari.rdt.InitializeLocale;
import com.nari.rdt.PropertiesParse;
import com.nari.rdt.chain.Request;
import com.nari.rdt.thrift.Message;
import com.nari.rdt.thrift.RDTService;
import com.nari.rdt.thrift.Result;

public class TransshipmentHandler extends Handler {
    
	private static Logger loger = Logger.getLogger(TransshipmentHandler.class);
	
	private static String protocol= "NIO";
	private static int timeout = 12000;
	
	public static String getProtocol() {
		return protocol;
	}

	public static void setProtocol(String protocol) {
		TransshipmentHandler.protocol = protocol;
	}

	@Override
	public void handlerRequest(Request request) throws TTransportException, TException {
		// TODO Auto-generated method stub
		if(2 == request.getLevel())
		{ 
			Result result = transData(request.getMessage());
			request.setResult(result);
			this.deal.put(request, true);
		}
	}
	
	/**
	 *
	 * @param messages
	 * @throws TException,TTransportException 
	 * 
	 */
	public Result transData(Message messages) {
		TTransport transport = null;
		String[] destination = messages.getDestination().split(":");
		Result result = new Result();
		String thrifttimeout = PropertiesParse.getInstance().getConfigureMap().get("trantimeout");
		if (null != thrifttimeout && !"".equals(thrifttimeout)) {
			TransshipmentHandler.timeout = Integer.valueOf(thrifttimeout.trim());
		}
		System.out.println("==============================================thrifttimeout=" + thrifttimeout);
		String protocolString = PropertiesParse.getInstance().getConfigureMap().get("Protocol");
		if (null != thrifttimeout && !"".equals(protocolString)) {
			TransshipmentHandler.protocol = protocolString.trim();
		}
		try {
			
			TSocket socket = new TSocket(destination[0],Integer.parseInt(destination[1]), TransshipmentHandler.timeout);
			
			// 协议要和服务端一致
			TProtocol protocol = null;
			if("NIO".equalsIgnoreCase(TransshipmentHandler.protocol))
			{
				transport = new TFramedTransport(socket);
				protocol = new TCompactProtocol(transport);
				
			}
			else
			{
				transport = socket;
				protocol = new TBinaryProtocol(socket);
			}
			
			RDTService.Client client = new RDTService.Client(protocol);
 			transport.open();
			result = client.sendMessge(messages);
			System.out.println("Thrify client result =: " + result);
		} catch (TTransportException e) {
			result.setResultValue(0);
			result.setErrorCode(3001);
			InitializeLocale locale = new InitializeLocale();
			String meg = locale.getMessage("3001",destination);
	        result.setMessage(meg);
	        loger.error(e);
		} catch (TException e) {
			result.setResultValue(0);
			result.setErrorCode(3001);
			InitializeLocale locale = new InitializeLocale();
			String meg = locale.getMessage("3001",destination);
	        result.setMessage(meg);
	        loger.error(e);
		} finally {
			if (null != transport) {
				transport.close();	
			}
		}
		return result;
	}

}
