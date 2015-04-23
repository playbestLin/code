package com.nari.rdt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class PropertiesParse {
	private static final String ACTIONPATH = "config.properties";
	private static PropertiesParse instance = null;
	private static final Logger logger = Logger.getLogger(PropertiesParse.class);
	private ConcurrentHashMap<String, String> configureMap;
	
	public ConcurrentHashMap<String, String> getConfigureMap() {
		return configureMap;
	}

	public void setConfigureMap(ConcurrentHashMap<String, String> configureMap) {
		this.configureMap = configureMap;
	}

	private PropertiesParse() {
	}

	public static PropertiesParse getInstance() {
		if (instance == null) {
			instance = new PropertiesParse().loadProperties();
		}
		return instance;
	}

	private PropertiesParse loadProperties() {
		PropertiesParse propertiesParse = new PropertiesParse();
		ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<String, String>();
		InputStream inputStream = null;
		String path = null;
		Properties p = new Properties();
		try {
//			path = PropertiesParse.class.getClassLoader().getResource("")
//					.toURI().getPath();
			path = System.getProperty("user.dir");
			System.out.println("-----------------------------------path=" + path);
			inputStream = new FileInputStream(new File(path)+File.separator + ACTIONPATH);
			p.load(inputStream);
			Set<Map.Entry<Object, Object>> set = p.entrySet();
			Iterator<Entry<Object, Object>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<Object, Object> e = it.next();
				hm.put(e.getKey().toString(), e.getValue().toString());
			}
		} catch (FileNotFoundException e) {
			 logger.error("file not fount ",e);
		} catch (IOException e) {
			 logger.error("config read fail ",e);
//		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
//			logger.error("URISyntaxException",e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("stream close fail ",e);
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("stream close fail ",e);
				}
			}
		}
		propertiesParse.setConfigureMap(hm);
		return propertiesParse;
	}
}
