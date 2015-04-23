package com.nari.rdt.chain;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.aspectj.lang.JoinPoint;
import org.apache.log4j.Logger;

import com.nari.rdt.chain.handler.Handler;

public class ResiponsibilityHandlerAspect
{
	private static Logger loger = Logger.getLogger(ResiponsibilityHandlerAspect.class);
	@SuppressWarnings({ "unchecked", "unused" })
	private void checkResiponsibility(JoinPoint joinPoint)
	{
		HashMap<Request, Boolean> deal = null;
		Handler successor = null;
		boolean isDeal = true;
		try
		{
			Request request = (Request) joinPoint.getArgs()[0];
			Class<Handler> classType = joinPoint.getSignature().getDeclaringType();
			Method getSuccessor = classType.getMethod("getSuccessor",new Class[] {});
			successor = (Handler) getSuccessor.invoke(joinPoint.getThis(), new Object[] {});
			long per = System.currentTimeMillis();
			
			Method getDeal = classType.getMethod("getDeal", new Class[] {});
		    deal = (HashMap<Request, Boolean>)getDeal.invoke(joinPoint.getThis(), new Object[]{});
			if(!deal.containsKey(request)){
				isDeal = false;
			}
			else {
				isDeal = deal.get(request);
			}
			if (successor != null && !isDeal)
			{
				loger.debug("-----[Debug]Start Chain[" + successor.getName() + "]----"
			    );
				loger.debug("to next chainHandler:" + successor.getName());
				//执行责任链
				successor.handlerRequest(request);
				loger.debug("-----[Debug]End Chain [" + successor.getName() + "]----"
			    );   
		        loger.debug("[" + successor.getName() + "]  exec time "
			    + (System.currentTimeMillis() - per) + "ms");
			}
			


		} catch (Exception e)
		{
			loger.info(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			if (deal != null)
			{
				deal.clear();
				deal = null;
			}
			  successor = null;
		}
	}
}
