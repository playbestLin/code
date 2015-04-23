package com.nari.rdt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ibatis.common.resources.Resources;

public class XmlParse {
	private static final String ACTIONPATH = "type.xml";
	private static XmlParse instance = null;
	private static final Logger logger = Logger.getLogger(XmlParse.class);
	private ConcurrentHashMap<String, List<String>> typeMap;
	private static HashMap<String,String> sqlMap = new HashMap<String,String>();
	private static HashMap<String,Document> docMap = new HashMap<String,Document>();
	
	private XmlParse() {
		
	}

	public static XmlParse getInstance() {
		if (instance == null) {
			instance = new XmlParse().loadTypeXml();
			String template = "com/nari/rdt/ibatis/mapping/special.xml";
			loadXML(template);
		}
		return instance;
	}
	
	private XmlParse loadTypeXml() {
		XmlParse xmlParse = new XmlParse();
		ConcurrentHashMap<String, List<String>> hm = new ConcurrentHashMap<String, List<String>>();
		ArrayList<String> typelist; 
		SAXReader reader = new SAXReader();
		String path = null;
			try {
//				path = XmlParse.class.getClassLoader().getResource("")
//						.toURI().getPath();
				path = System.getProperty("user.dir");				
				Document document = reader.read(path +File.separator + ACTIONPATH);
				List<Element> appidElements = document.getRootElement().elements("Appid");
				for (Element element:appidElements)
				{
					typelist = new ArrayList();
					String appid = element.attributeValue("value");
					List<Element> typeElements = element.elements("type");
					for (Element type:typeElements)
					{
						typelist.add(type.attributeValue("value"));
					}
					hm.put(appid, typelist);
				}
				
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		xmlParse.setTypeMap(hm);
		return xmlParse;
	}
	
	public ConcurrentHashMap<String, List<String>> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(ConcurrentHashMap<String, List<String>> typeMap) {
		this.typeMap = typeMap;
	}
	
	public static Document loadXML(String template)
	{
		Document document = null;
		if (docMap.get(template) != null)
		{
			document =  docMap.get(template);
		}
		else
		{
			SAXReader saxReader = new SAXReader();
			try {
				Reader re = Resources.getResourceAsReader(template);
				saxReader.setValidation(false);  
				saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				document = saxReader.read(re);
				docMap.put(template, document);
				Element sqlmap = document.getRootElement();
				List<Element> list = sqlmap.elements();
				for (Element stament:list)
				{
					sqlMap.put(stament.attribute("id").getText(),stament.attribute("id").getText());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		
		return document;
	}
	
	public static Map getSqlMap()
	{
		return sqlMap;
	}
	
	public static void saveDocument(String template,Document document)
	{
		docMap.put(template, document);
	}
	
	public static void writeXML(String name,Document document)
	{
		XMLWriter xmlWriter = null;
		try {
			File templateFile =  Resources.getResourceAsFile(name);
			File classesPath = templateFile
					.getParentFile();
			System.out.println(classesPath.getAbsolutePath());

			xmlWriter = new XMLWriter(new FileOutputStream(
					templateFile));
			xmlWriter.write(document);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			try {
				xmlWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static synchronized void changeSpecialXML(String id, String count) {
		String template = "com/nari/rdt/ibatis/mapping/special.xml";
		Document document = null;
		
		try {
			document = loadXML(template);
			Element sqlmap = document.getRootElement();
			Element statement = sqlmap.addElement("statement");
			statement.addAttribute("id", id);
			statement.addAttribute("parameterClass", "java.util.HashMap");
			statement.addAttribute("remapResults", "true");
			statement.addCDATA(count);
			List<Element> list = sqlmap.elements();
			for (Element stament:list)
			{
				if ("clobtest".equals(stament.attribute("id").getText()))
				{
					stament.clearContent();
					stament.addCDATA(count);
					break;
				}
				
			}

			saveDocument(template,document);
			writeXML(template,document);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args)
	{
		String id = "INSERT INTO statdba.T_BSConfigInfo(corporationid,time,value,dimension_column1) VALUES(#field1#,#field2#,#field3#,#field4#)";
		String count = "INSERT INTO T_BSConfigInfo(corporationid,time, value,dimension_column1) VALUES(#field1#,$field2$,#field3#,$field4$"; 
		XmlParse.changeSpecialXML(id,count);
		//XmlParse.getInsertSql(new HashMap());
	}
}
