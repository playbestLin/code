package com.nari.rdt.chain.handler;

import java.util.HashMap;
import com.nari.rdt.chain.Request;

public abstract class Handler
{
	protected String name;
	protected Handler successor;
	protected HashMap<Request, Boolean> deal;
	public Handler(){
		this.deal = new HashMap<Request, Boolean>();
	}
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean getSpecificDeal(Request request)
	{
		return deal.get(request);
	}

	public HashMap<Request, Boolean> getDeal()
	{
		return deal;
	}
	
	public abstract void handlerRequest(Request request) throws Exception;

	public Handler getSuccessor()
	{
		return successor;
	}

	public void setSuccessor(Handler successor)
	{
		this.successor = successor;
	}

}
