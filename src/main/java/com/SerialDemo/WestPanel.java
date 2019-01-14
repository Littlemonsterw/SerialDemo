package com.SerialDemo;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

import gnu.io.*;

public class WestPanel extends JPanel{

		private JPanel comSetPal,recSetPal,toolPal;
		private JLabel serialNo_Lab,baudRate_Lab,testLabel;
		private JComboBox serialNo_cob,baudRate_cob;
		private JButton openCloseBtn,clearRecBtn,stopShowBtn,SendFileBtn;
		private SerialPortManage spm;
		private SerialPort serialPort;
		private SouthPanel southPanel;
		private CenterPanel centerPanel;
		private int stopFlag=0;
		public WestPanel(CenterPanel centerPanel1,SouthPanel southPanel1)
		{
			centerPanel = centerPanel1;
			southPanel = southPanel1;
			
			this.setPreferredSize(new Dimension(180,500));
			
			ButtonListener btnListener = new ButtonListener();
			
			/*添加通讯设置面板的元素*/
			comSetPal = new JPanel();
			comSetPal.setPreferredSize(new Dimension(150,150));
			comSetPal.setBorder(BorderFactory.createTitledBorder("通讯设置"));
			//comSetPal.setLayout(new GridLayout(3,2));
			
			serialNo_Lab = new JLabel("串口号");
			baudRate_Lab = new JLabel("波特率");
			
			String[] serialPort = {"COM1","COM2","COM3","COM4","COM5","COM6","COM7","COM8"};
			serialNo_cob = new JComboBox(serialPort);
			
			String baudRate[] = {"9600" ,"300","600","1200","2400","4800","19200","56000"};
			baudRate_cob = new JComboBox(baudRate);
			
			
			openCloseBtn = new JButton("打开串口");
			openCloseBtn.addActionListener(btnListener);
			
			comSetPal.add(serialNo_Lab);
			comSetPal.add(serialNo_cob);
			comSetPal.add(baudRate_Lab);
			comSetPal.add(baudRate_cob);
			comSetPal.add(openCloseBtn);
			
			/*添加接受设置面板的元素*/
			recSetPal = new JPanel();
			recSetPal.setPreferredSize(new Dimension(150,100));
			//recSetPal.setLayout(new BoxLayout(recSetPal,BoxLayout.Y_AXIS));
			recSetPal.setBorder(BorderFactory.createTitledBorder("接收设置"));
			
			clearRecBtn = new JButton(">清空接受区");
			clearRecBtn.addActionListener(btnListener);
			stopShowBtn = new JButton(" >停止展示   ");
			stopShowBtn.addActionListener(btnListener);
			
			recSetPal.add(clearRecBtn);
			recSetPal.add(stopShowBtn);
			
			toolPal = new JPanel();
			toolPal.setPreferredSize(new Dimension(150,100));
			toolPal.setBorder(BorderFactory.createTitledBorder("工具栏"));
			
			SendFileBtn = new JButton(" >传输文件< ");
			SendFileBtn.addActionListener(btnListener);
			
			toolPal.add(SendFileBtn);
			
			//testLabel = new JLabel();
			//testLabel.setText("测试："+getBaudRate());
			
			//toolPal.add(testLabel);
			
			this.add(comSetPal);
			this.add(recSetPal);
			this.add(toolPal);
			
		}
		
		private String getPortName()
		{
			String portName = serialNo_cob.getSelectedItem().toString();
			return portName;
		}

		private int getBaudRate()
		{
			int baudRate = Integer.parseInt(baudRate_cob.getSelectedItem().toString());
			return baudRate;
		}
		
		private class ButtonListener implements ActionListener
		
	    {
			public void actionPerformed(ActionEvent event)
			{
				if (event.getSource() == openCloseBtn)
				{
					if(openCloseBtn.getText()=="打开串口"&&(serialPort = spm.openPort(getPortName(), getBaudRate()))!=null)
					{
						openCloseBtn.setText("关闭串口");
						southPanel.stateLb.setText("串口打开成功");
					}
					else if(openCloseBtn.getText()=="关闭串口"&&spm.closePort())
					{
						openCloseBtn.setText("打开串口");
						southPanel.stateLb.setText("串口尚未打开");
					}
				}
				else if (event.getSource() == clearRecBtn)
				{
					centerPanel.clearDataView();
				}
				else if(event.getSource() == stopShowBtn)
				{
					if(stopShowBtn.getText() == " >停止展示   ")
					{
						stopShowBtn.setText(" >开始展示   ");
						stopFlag = 1;
					}
					else
					{
						stopShowBtn.setText(" >停止展示   ");
						stopFlag = 0;
					}
				}
				else if(event.getSource() == SendFileBtn)
				{
					File file = FileChooser.openFile();
					if(file!=null)
						spm.sendFile(file);
						
				}
			}
					
			
			
		}
		public SerialPort getSerialPort()
		{
			return serialPort;
		}
		
		public void setSPM(SerialPortManage spm1)
		{
			spm = spm1;
		}
		
		public int getStopFlag()
		{
			return stopFlag;
		}
}
