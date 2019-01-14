package com.SerialDemo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * @author Administrator
 *
 */
public class SerialTool {
	
	/**
	 * 查找所有可用端口
	 * @return 可用端口名称列表
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<String> findPort() {
		//获得当前所有可用接口
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers(); //得到系统中的端口列表
		ArrayList<String> portNameList = new ArrayList<String>();
		//将可用串口名添加到List并返回
		while (portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		System.out.println(portList);
		return portNameList;
	}
	
	
	/**
	 * 向串口发送数据
	 * @param portName 端口名称
	 * @param baudrate 波特率
	 * @return 串口对象
	 * @throws Exception
	 */
	public static final SerialPort openPort(String portName, int baudrate) throws Exception {
		try {
			//通过端口名识别端口
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			//打开端口，设置端口名与超时时间
			CommPort commPort = portIdentifier.open(portName, 2000);
			//判断是不是串口
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				
				try {
					//设置串口的波特率等参数
					serialPort.setSerialPortParams(baudrate, serialPort.DATABITS_8, serialPort.STOPBITS_1, serialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					throw new Exception();
				}
				
				return serialPort;
			} else {
				throw new Exception();
			}
		} catch (NoSuchPortException e1) {
			throw new NoSuchPortException();
		} catch (PortInUseException e2) {
			throw new PortInUseException();
		}
	}
	
	/**
	 * 关闭串口
	 * @param serialPort
	 */
	public static void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
	}
	
	/**
	 * 向串口发送数据
	 * @param serialPort 串口对象
	 * @param order 待发送数据
	 * @throws Exception
	 */
	public static void sendToPort(SerialPort serialPort, byte[] order) throws Exception {
		OutputStream out = null;
		out = serialPort.getOutputStream();
		out.write(order);
		out.flush();
		
		if (out != null) {
			out.close();
			out = null;
		}
	}
	
	/**
	 * 从串口读取数据
	 * @param serialPort 当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 * @throws Exception
	 */
	public static byte[] readFromPort(SerialPort serialPort) throws Exception {
		InputStream in = null;
		byte[] bytes = null;
		in = serialPort.getInputStream();
//		//获取buffer里的数据长度
//		int bufflength = in.available();
//		while (bufflength != 0) {
//			//初始化byte数组为buffer中的数据长度
//			bytes = new byte[bufflength];
//			in.read(bytes);
//			bufflength = in.available();
//		}
//		if (in != null) {
//			in.close();
//			in = null;
//		}
		bytes = IOUtils.toByteArray(in);
		System.out.println(bytes);
		return bytes;
	}
	
	public static void addListener(SerialPort port, SerialPortEventListener listener) throws Exception {
		port.addEventListener(listener);
		//当数据到达时唤醒监听接收线程
		port.notifyOnDataAvailable(true);
		//当通信中断时唤醒中断线程
		port.notifyOnBreakInterrupt(true);
	}
}
















