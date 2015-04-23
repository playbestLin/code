package com.nari.rdt.chain.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.dao.DataAccessException;

import MemDb.MemDBInterfaceRDT;

import com.nari.rdt.InitializeLocale;
import com.nari.rdt.chain.Request;
import com.nari.rdt.thrift.Result;
import com.nari.rdt.util.ByteUtil;
import com.nari.rdt.util.Test;

public class Deliver2H2Handler  extends Handler {
	private static Logger loger = Logger.getLogger(Deliver2H2Handler.class);

	@Override
	public void handlerRequest(Request request) throws Exception  {
		// TODO Auto-generated method stub
		String methodName = "";
//		Test test = (Test)ByteUtil.getObject(request.getMessage().getContent());
//		System.out.println("getIntValue====" + test.getIntValue());
//		System.out.println("getStringValue====" + test.getStringValue());
//		System.out.println("isBooleanValue====" + test.isBooleanValue());
//		this.deal.put(request, true);
		if(9 == request.getLevel())
		{
			//将数据存储至数据库
			try
			{
					methodName = request.getMessage().getExecuteSQL();
					ArrayList<HashMap<String, String>> req;
					req = (ArrayList<HashMap<String,String>>)ByteUtil.getObject(request.getMessage().getContent());
                    MemDBInterfaceRDT md = MemDBInterfaceRDT.getMemDBInterfaceRDT();
                    Class mdClass = md.getClass();
                    Method executeMethod = mdClass.getMethod(methodName, ArrayList.class);
					Object obj = executeMethod.invoke(md, req);
					Result result = request.getResult();
                    if (obj == null)
                    {
                    	result.setResultValue(0);
        				result.setErrorCode(6001);
        				InitializeLocale locale = new InitializeLocale();
        				String meg = locale.getMessage("8001");
        		        result.setMessage(meg);
        				request.setResult(result);
                    }
                    else
                    {
                    	result.setResultValue(1);
                    	ObjectMapper objectMapper = new ObjectMapper();
                		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
                		ArrayList<Map<String,String>> data = (ArrayList<Map<String,String>>)obj;
                		String json = objectMapper.writeValueAsString(data);
            			System.out.println("json====" + json);
            			result.setMessage(json);
        				request.setResult(result);
                    }
					//md.GetPerfValuesByRID(req);
					//md.GetPerfValuesBySubRID(req);

			}
			catch(NoSuchMethodException e)
			{
				Result result = request.getResult();
				result.setResultValue(0);
				result.setErrorCode(6001);
				InitializeLocale locale = new InitializeLocale();
				String[] strArgs = new String[1];
				strArgs[0] = methodName;
				String meg = locale.getMessage("8002",strArgs);
		        result.setMessage(meg);
				request.setResult(result);
				throw e;
			}
			catch(Exception e)
			{
				Result result = request.getResult();
				result.setResultValue(0);
				result.setErrorCode(6001);
				InitializeLocale locale = new InitializeLocale();
				String[] strArgs = new String[1];
				strArgs[0] = methodName;
				String meg = locale.getMessage("8003",strArgs);
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
	
	public void test() throws Exception 
	{
		ArrayList<HashMap<String,String>> req = new ArrayList<HashMap<String,String>>();
		String methodName = "GetPerfValuesByRID";
		 HashMap<String,String> m = new HashMap<String,String>();
		   m.put("kpiname", "Status");
		   m.put("rid", "992323123123");
		   req.add(m);
		   
		   MemDBInterfaceRDT md = MemDBInterfaceRDT.getMemDBInterfaceRDT();
           Class mdClass = md.getClass();
           Method executeMethod = mdClass.getMethod(methodName, ArrayList.class);
			Object obj = executeMethod.invoke(md, req);
			System.out.println("result=" + obj);
//		   md.GetPerfValuesByRID(req);
//		   md.GetPerfValuesBySubRID(req);
	}
	
	
	public static void main(String[] args)
	{
		Deliver2H2Handler dd = new Deliver2H2Handler();
		try {
			dd.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
