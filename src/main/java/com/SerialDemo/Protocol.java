package com.SerialDemo;

//协议:type(类型)+INFO+info(信息内容)
public class Protocol {
	//文本类型
	public static final String TEXT = "text";
	//文件类型
	public static final String FILE = "file";
	//接受准备就绪
	public static final String FILE_READY = "file_ready";
	//INFO头部
	public static final String INFO_HEAD = "INFO";
	
	
	private String protocol;
	private String type;
	private String info;
	
	public Protocol(String protocol)
	{
		this.protocol = protocol;
		
		String str = protocol;
		
		//解析类型
		int index = str.indexOf(INFO_HEAD);
		type = str.substring(0,index);
		str = protocol.substring(type.length() + INFO_HEAD.length());
		
		//解析内容
		info = str;
		
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getInfo()
	{
		return info;
	}
}