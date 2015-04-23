package com.nari.rdt;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.support.ResourceBundleMessageSource;

public class InitializeLocale {

 private static Logger loger = Logger.getLogger(InitializeLocale.class);	
 private ResourceBundleMessageSource resource;
 
 public ResourceBundleMessageSource getResource() {
	 if (resource == null)
	 {
		 resource = (ResourceBundleMessageSource)SpringBeanLoader.getInstance().getApplicationContext().getBean("messageSource");
	 }
	 
	return resource;
}
 

public void setResource(ResourceBundleMessageSource resource) {
	this.resource = resource;
}

public String getMessage(String key)
{
	String msg = "";
	msg = getResource().getMessage(key, null, null);
	System.out.println("msg=" + msg);
	return msg;
}

public String getMessage(String key,String[] args)
{
	String msg = "";
	msg = getResource().getMessage(key, args, null);
//	System.out.println("msg=" + msg);
	return msg;
}
 
}
