package com.SerialDemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JOptionPane;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialPortManage {

	public SerialPort serialPort;
	private CenterPanel centerPanel;
	private WestPanel westPanel;
	private FileInputStream fis = null;
	
	public SerialPortManage(CenterPanel centerPanel1) {
		centerPanel = centerPanel1;
	}
	
	/**
	 * @return 可用端口
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<String> findPort() {
		
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<String>();
		
		while(portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		return portNameList;
	}
	
	/**
	 * @param portName
	 * @param baudRate
	 * @return 打开串口
	 */
	public SerialPort openPort(String portName, int baudRate) {
		
		try {
			//通过端口号识别端口
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			//打开端口，设置端口名与timeout(超时时间)
			CommPort commPort = portIdentifier.open(portName, 2000);
			//判断是不是串口
			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;
				try {
					//设置串口参数
					serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, "串口参数设置错误");
				}
				addListener(new SerialListener());
				return serialPort;
			} else {
				//不是串口
				JOptionPane.showMessageDialog(null, "不是串口");
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "打开串口错误");
		}
		return null;
	}
	
	/**
	 * @return 关闭串口
	 */
	public boolean closePort() {
		try {
			if(serialPort != null)
				serialPort.close();
				return true;
		} catch(Exception e){
			return false;
		}
	}
	
	/**
	 * @param message 发送文本信息
	 */
	public void sendText(String message){
		OutputStream out = null;
		String protocolStr = Protocol.TEXT+Protocol.INFO_HEAD+message;
		
		try {
			PrintStream ps = new PrintStream(serialPort.getOutputStream());
			ps.println(protocolStr);
			ps.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(out != null) {
					out.close();
					out = null;
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param file 发送文件
	 */
	public void sendFile(File file) {
//		OutputStream out = null;
		
		String fileName = file.getName();
		
		//用文件对象初始化fis对象
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		String sendProtocolStr;
		try {
			sendProtocolStr = Protocol.FILE+Protocol.INFO_HEAD+fileName+"+"+fis.available();
			
			PrintStream ps = new PrintStream(serialPort.getOutputStream());
			ps.println(sendProtocolStr);
			ps.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * @return 从串口读取数据
	 */
	public byte[] readFromPort() {
		InputStream is = null;
		byte[] bytes = null;
		
		try {
			is = serialPort.getInputStream();
			int buffLength = is.available();
			while(buffLength >0)
			{
				bytes = new byte[buffLength];
				is.read(bytes);
				buffLength = is.available();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {  
			try {
				if(is != null)
				{
					is.close();
					is = null;
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return bytes;
	}
	
	/**
	 * @param listener 给串口添加监听器
	 */
	public void addListener(SerialPortEventListener listener) {
		try {
			serialPort.addEventListener(listener);
			
			//设置当有数据到达时唤醒监听接收线程
			serialPort.notifyOnDataAvailable(true);
			//设置当通信中断时唤醒终端线程
			serialPort.notifyOnBreakInterrupt(true);
		}catch(Exception e) {	
			e.printStackTrace();
		}
	}
	
	public void setWestPanel(WestPanel westPanel1) {
		westPanel = westPanel1;
	}
	
	private  class SerialListener implements SerialPortEventListener
	{
		 public void serialEvent(SerialPortEvent serialPortEvent)
		 {
			 if(serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
			 {
				 String reProtocolStr=null;
				 String msg = null;
				 String type = null;
				 InputStream is = null;
				 File file = null;
				 FileOutputStream fos = null;
				 byte[] buffer = new byte[1023];
				 
				//先读一行协议
				 try {
					 is = serialPort.getInputStream();
					 InputStreamReader isr = new InputStreamReader(is);
					 BufferedReader br = new BufferedReader(isr);
					 
					 reProtocolStr = br.readLine();
				 }catch(Exception e) {
					 e.printStackTrace();} 
				 
				 //解析协议
				 Protocol protocolStr = new Protocol(reProtocolStr);
				 type = protocolStr.getType();
				 msg = protocolStr.getInfo();
					 
				 System.out.println(type);
				 //消息是文本类	 
				 if(type.equals(Protocol.TEXT)) {
					 if(westPanel.getStopFlag() == 0) 
						 centerPanel.dataView.append("【Receive】"+msg+"\n");
					 centerPanel.southPanel.setRX(centerPanel.southPanel.getRX()+msg.length());
					 centerPanel.southPanel.setLabel(centerPanel.southPanel.getTX(),centerPanel.southPanel.getRX());
				 }
				 //消息是文件类	 
				 if(type.equals(Protocol.FILE)) {
					 int index = msg.indexOf("+");
					 //解析文件名
					 String fileName = msg.substring(0,index);
					 msg = msg.substring(fileName.length()+1);
					 //解析长度
					 String fileSize = msg;
						 
					 System.out.println(fileName);
					 System.out.println(fileSize);
						 
					 //创建文件
					 try {
						 file = FileChooser.saveFile(fileName);
						 if(!file.exists())
							 file.createNewFile();
					 } catch (IOException e) {
							// TODO Auto-generated catch block
						 	 e.printStackTrace();
					 }
					
					//发送准备就绪的信号 
					PrintStream ps;
					try {
						 ps = new PrintStream(serialPort.getOutputStream());
						 ps.println(Protocol.FILE_READY+Protocol.INFO_HEAD);
						 ps.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						 
					 
				     //接受文件的信息
					 try {
						 fos = new FileOutputStream(file);
						 long file_size = Long.parseLong(fileSize);
						 //is = serialPort.getInputStream();
						 int size = 0;
						 long count = 0;
						 
						 while(count<file_size&&size!=-1) {
							 size = is.read(buffer);
							 
							 fos.write(buffer, 0, size);
							 fos.flush();
							 
							 count+=size;
		
							 System.out.println("接受数据包，大小为："+size);
							 
							 if(size==0)
								 size=-1;
						 }
					 }catch(Exception e) {
						 e.printStackTrace();
					 }
						try {
							fos.close();
							System.out.println("写入文件完成！");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
				 
					if(type.equals(Protocol.FILE_READY)) {
						try {
							OutputStream out = serialPort.getOutputStream();
							int size=0;
							while((size = fis.read(buffer)) != -1) {
								out.write(buffer, 0, size);
								out.flush();
								System.out.println("发送数据包，大小为："+size);
							}
						}catch(Exception e) {
							e.printStackTrace();
						}
						
						try {
							fis.close();
							System.out.println("读取文件完成！");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
			
			 }
		 }
	}
}












