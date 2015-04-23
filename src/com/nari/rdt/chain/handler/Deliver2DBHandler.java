package com.nari.rdt.chain.handler;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.nari.rdt.InitializeLocale;
import com.nari.rdt.chain.Request;
import com.nari.rdt.chain.ResiponsibilityHandlerAspect;
import com.nari.rdt.service.ICommonService;
import com.nari.rdt.thrift.Message;
import com.nari.rdt.thrift.Result;

public class Deliver2DBHandler extends Handler {

	private ICommonService commonService ;
	private static Logger loger = Logger.getLogger(Deliver2DBHandler.class);
	
	public ICommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	@Override
	public void handlerRequest(Request request) throws Exception {
		// TODO Auto-generated method stub
		String sql = "";
		if(4 == request.getLevel())
		{
			//将数据存储至数据库
			try
			{
				if (request.getMessage() != null)
				{
					sql = request.getMessage().getExecuteSQL();
					if ((sql !=null) && (sql.length() > 6))
					{
						sql = sql.trim();
						if ("select".equalsIgnoreCase(sql.substring(0,6)))
						{
							String jsonString = commonService.extractFromDBByJsonObeject(request.getMessage());
							Result result = request.getResult();
							result.setMessage(jsonString);
							request.setResult(result);
						}
						else
						{
							commonService.save2DBService(request.getMessage()); 
						}
					}
				}	
			}
			catch(Exception e)
			{
				if (e instanceof  ClassCastException)
				{
					loger.info("ClassCastException:sql=" + sql);
					Result result = request.getResult();
					result.setResultValue(0);
					result.setErrorCode(5002);
					InitializeLocale locale = new InitializeLocale();
					String meg = locale.getMessage("5002");
			        result.setMessage(meg);
					request.setResult(result);
				}
				else if (e instanceof  DataAccessException)
				{
					loger.error("DataAccessException:sql=" + sql);
					Result result = request.getResult();
					result.setResultValue(0);
					result.setErrorCode(5001);
					InitializeLocale locale = new InitializeLocale();
					String meg = locale.getMessage("5001");
					String error = e.getMessage();
					int index = error.indexOf("java.sql");
					String sqlerror = "";
					if (index > 0 )
					{
						sqlerror = error.substring(index,error.length());
					}
					else
					{
						sqlerror = error;
					}
			        result.setMessage(meg + " error detail = " + sqlerror);
					request.setResult(result);
				}
				else
				{
					loger.info("Exception:sql=" + sql);
					Result result = request.getResult();
					result.setResultValue(0);
					result.setErrorCode(5003);
					InitializeLocale locale = new InitializeLocale();
					String meg = locale.getMessage("5003");
			        result.setMessage(meg);
					request.setResult(result);
					e.printStackTrace();
					loger.error(e.getLocalizedMessage());
				}
			}
			finally
			{
				this.deal.put(request, true);
			}
		}
	}
	
	public boolean save2DBService(Message message) throws Exception
	{
		boolean result = false;
		//save to database
		//以下是测试代码
		result = commonService.save2DBService(message);
		return result;
	}
}
