package com.nari.rdt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

import com.nari.rdt.chain.ResiponsibilityHandlerAspect;

public class SpringBeanLoader {
//	private static final String[] LOCATIONS = {"com/nari/rdt/beans/beans-resource.xml","com/nari/rdt/beans/beans-chain.xml","com/nari/rdt/beans/beans-tcp-producer.xml"};
	private static Logger loger = Logger.getLogger(SpringBeanLoader.class);
	private ApplicationContext applicationContext;
	
	private static SpringBeanLoader instance = null;
	
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private SpringBeanLoader(){
		
	}
	
	public static SpringBeanLoader getInstance() {
		if (instance == null) {
			instance = new SpringBeanLoader().loadApplicationContext();
		}
		return instance;
	}
	
	private SpringBeanLoader loadApplicationContext()
	{
		SpringBeanLoader springBeanLoader = new SpringBeanLoader();
		BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance("classpath*:com/nari/rdt/beans/ApplicationContext.xml");
		BeanFactoryReference beanFactoryReference = locator.useBeanFactory("applicationContext");
		ApplicationContext ctx = (ApplicationContext)beanFactoryReference.getFactory();
		springBeanLoader.setApplicationContext(ctx);
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(locations);
//		springBeanLoader.setApplicationContext(ctx);
		return springBeanLoader;
	}
}
