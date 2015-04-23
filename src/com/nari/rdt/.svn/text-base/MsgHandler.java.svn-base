package com.nari.rdt;

import java.util.UUID;


import com.nari.rdt.thrift.Message;
import com.nari.rdt.util.PasswordEncoder;
import com.nari.rdt.util.SystemUtil;
import com.sun.jmx.snmp.Timestamp;

public class MsgHandler {
	
	private static String mac = SystemUtil.getWindowsMACAddress();
	private static String ip = SystemUtil.getHost();
	
	public static String checkNull(Message message)
    {
    	String result = "";
    	//进行数据格式校验
    	if (( null == message.getAppId()) || ("".equals(message.getAppId())))
    	{
    		result = "APPID";
    	}
    	else if (( null == message.getType()) || ("".equals(message.getType())))
    	{
    		result = "Type";
    	}
    	else if (( null == message.getDestination()) || ("".equals(message.getDestination())))
    	{
    		result = "Destination";
    	}
    	else if (( null == message.getDeliveryMode()) || ("".equals(message.getDeliveryMode())))
    	{
    		result = "DeliveryMode";
    	}
    	else if ( ("DB".equals(message.getDeliveryMode()) &&
    	     ( null == message.getExecuteSQL() || "".equals(message.getExecuteSQL()))))
    	{
    		result = "ExecuteSQL";
    	}
    	else if (( null == message.getContent()) || ("".equals(message.getContent())))
    	{
    		result = "Content";
    	}
    	return result;
    }
	
	
	public static String checkRDTMessage(Message message)
    {
    	String result = MsgHandler.checkNull(message);
    	if ("".equals(result))
    	{
    		if (( null == message.getIPAddress()) || ("".equals(message.getIPAddress())))
        	{
    			result = "IPAddress";
        	}
    		else if (( null == message.getMac()) || ("".equals(message.getMac())))
        	{
    			//result = "Mac";
        	}
    		else if (( null == message.getNodeCode()) || ("".equals(message.getNodeCode())))
        	{
    			result = "NodeCode";
        	}
    	}
    	return result;
    }
	
	public static Message sealMessage(Message message)
	    {
	    	message.setMessageID(UUID.randomUUID().toString());
	    	message.setIPAddress(ip);
	    	message.setMac(mac);
	    	message.setNodeCode(SystemUtil.getHostName());
	    	message.setCheckCode(getCheckCode(message));
	    	if("MQ".equals(message.getDeliveryMode()))
	    	{
		    	if(message.getExpires() == 0)
		    	{
		    		message.setExpires(-1);
		    	}
		    	if(message.getPriority() == 0)
		    	{
		    		message.setPriority(4);
		    	}
	    	}
	    	Timestamp t = new Timestamp();
	    	message.setTimestamp(t.getDateTime());
	    	return message;
	    }
	
	
	 public static  String getCheckCode(Message message)
		{
			//数据安全码生成公式：md5("messageId + appid + ip(若无则为空字符) + mac(若无则为空字符)+发送目标地址", 盐值）
			StringBuffer checkCode = new StringBuffer();
			checkCode.append(message.getMessageID()).append(message.getAppId());
			if (message.getIPAddress() == null)
			{
				checkCode.append("");
			}
			else
			{
				checkCode.append(message.getIPAddress());
			}
			if (message.getMac() == null)
			{
				checkCode.append("");
			}
			else
			{
				checkCode.append(message.getMac());
			}
			checkCode.append(message.getDestination());
			//增加盐值
			String salt = "holliRDTluya";
			PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
			String encode = encoderMd5.encode(checkCode.toString());
			return encode;
		}
}
