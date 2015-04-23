package com.nari.rdt.chain.handler;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.nari.rdt.InitializeLocale;
import com.nari.rdt.MsgHandler;
import com.nari.rdt.XmlParse;
import com.nari.rdt.chain.*;
import com.nari.rdt.thrift.Message;
import com.nari.rdt.thrift.Result;
public class SecurityCheckHandler extends Handler {

	private static Logger loger = Logger.getLogger(SecurityCheckHandler.class);
	@Override
	public void handlerRequest(Request request) {
		// TODO Auto-generated method stub
		long per = System.currentTimeMillis();
		loger.debug("-----[Debug]Start Chain[SecurityCheck]----");
		if(1 == request.getLevel())
		{ 
			//安全码校验
			if(securityAuthority(request))
			{
				//逻辑代码
				String destination = request.getMessage().getDestination();
				ConcurrentHashMap<String, String> destMap =  new ConcurrentHashMap<String, String>();
				String[] dest = destination.split(":");
				if ((dest != null) &&(dest.length == 2))
				{
					loger.debug("[Debug] dest.length=" + dest.length);
					destMap.put("host", dest[0]);
					destMap.put("port", dest[1]);
					request.setDestination(destMap);
				}
				else
				{
					Result result = request.getResult();
					result.setResultValue(0);
					result.setErrorCode(1003);
					InitializeLocale locale = new InitializeLocale();
					String[] strArgs = new String[1];
					strArgs[0] = "数据发送目标地址";
					String meg = locale.getMessage("1003",strArgs);
			        result.setMessage(meg);
					request.setResult(result);
					this.deal.put(request, true);
				}
			}else
			{
				this.deal.put(request, true);
			}
		}
		loger.debug("-----[Debug]End Chain Chain[SecurityCheck]----"
	    );   
        loger.debug("Chain[SecurityCheck]  exec time "
	    + (System.currentTimeMillis() - per) + "ms");
	}
	
	private boolean securityAuthority(Request request)
	{
		/*安全码校验
		包含：数据完整性校验，数据安全码校验，数据源合法性校验（APPID、Ip、Mac是否合法）
		数据安全码生成公式：md5("messageId + appid + ip(若无则为空字符) + mac(若无则为空字符)+发送目标地址", 盐值）
		*/
		boolean checkflage = false;
		Message message = request.getMessage();	
		InitializeLocale locale = new InitializeLocale();
		if (checkMessage(request,locale) == true)
		{
          if (!message.getCheckCode().equals(MsgHandler.getCheckCode(message)))
		   {
        	  Result result = request.getResult();
        	  result.setResultValue(0);
    		  result.setErrorCode(2004);
	          String meg = locale.getMessage("2004");
	          result.setMessage(meg);
	          request.setResult(result);
		  }
          else
          {
        	  checkflage = true;
          }
		}
		else
		{
			  Result result = request.getResult();
      	      result.setResultValue(0);
	          request.setResult(result);
		}
    	return checkflage;	
	}
	
	
	private  boolean checkMessage(Request request,InitializeLocale locale)
	{
		boolean checkflage = false;
		Message message = request.getMessage();
		Result result = request.getResult();
		String msg = MsgHandler.checkRDTMessage(message);
		if ("".equals(msg) )
		{
			Map<String, List<String>> typeMap = XmlParse.getInstance().getTypeMap();
			if (typeMap.get(message.getAppId()) == null)
			{
				result.setErrorCode(2001);
				result.setMessage(locale.getMessage("2001"));
				request.setResult(result);
			}
			else
			{
				boolean flag = false;
				for (String type:typeMap.get(message.getAppId()))
				{
					if (type.equals(message.getType()))
					{
						flag = true;
						break;
					}
				}
				if (flag == false)
				{
					result.setErrorCode(2002);
					result.setMessage(locale.getMessage("2002"));
					request.setResult(result);	
				}
				else
				{
					checkflage = true;
				}
			}
		}
		else
		{
			result.setErrorCode(1001);
			String[] strArgs = new String[1];
			strArgs[0] = msg;
	        String meg = locale.getMessage("1001",strArgs);
	        result.setMessage(meg);
		}
		return checkflage;
	}
	
	

}
