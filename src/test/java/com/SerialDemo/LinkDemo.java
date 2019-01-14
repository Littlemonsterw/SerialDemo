package com.SerialDemo;

import gnu.io.SerialPort;

public class LinkDemo {

	public static void main(String[] args) throws Exception {
		SerialPort serialPort = SerialTool.openPort("COM1", 9600);
		SerialTool.sendToPort(serialPort, "Test the comm!".getBytes());
		String receive = new String(SerialTool.readFromPort(serialPort));
		System.out.println("从串口调试工具接收到的数据为：" + receive);
	}
}
