package com.SerialDemo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class CenterPanel extends JPanel{

	private JPanel dataViewPal,dataSendPal;
	public JTextArea dataView,dataSend;
	private JButton sendTextBtn;
	private JScrollPane scrollDataView,scrollDataSend;
	public SouthPanel southPanel;
	private SerialPortManage spm;
	private WestPanel westPanel;
	
	public CenterPanel(SouthPanel southPanel1)
	{
		southPanel = southPanel1;
		Init();
		
	}
	
	private void Init()
	{

		this.setPreferredSize(new Dimension(380,500));
		
		ButtonListener btnListener = new ButtonListener();
		
		dataViewPal = new JPanel();
		dataSendPal = new JPanel();
	
		dataViewPal.setPreferredSize(new Dimension(350,280));
		
		dataView = new JTextArea(15,30);
		dataView.setLineWrap(true);
		scrollDataView = new JScrollPane(dataView);
		
		dataViewPal.add(scrollDataView);
		
		dataSendPal.setPreferredSize(new Dimension(350,200));
		dataSendPal.setBorder(BorderFactory.createTitledBorder("发送区"));
				
		dataSend = new JTextArea(8,20);
		dataSend.setLineWrap(true);
		scrollDataSend = new JScrollPane(dataSend);
		
		sendTextBtn = new JButton(" >发送信息< ");
		sendTextBtn.addActionListener(btnListener);
		
		dataSendPal.add(scrollDataSend);
		dataSendPal.add(sendTextBtn);
		
		this.add(dataViewPal);
		this.add(dataSendPal);
	}
	
	
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			
			try {
				String message = dataSend.getText();
				if(message.length()>0) {
					spm.sendText( message);
					dataSend.setText(null);
					if(westPanel.getStopFlag() == 0)
						dataView.append("【Send】"+message+"\n");
					southPanel.setTX(southPanel.getTX()+message.length());
					southPanel.setLabel(southPanel.getTX(), southPanel.getRX());
				}
				else 
					JOptionPane.showMessageDialog(null, "错误！发送信息为空！");
				}catch(Exception e) {
					e.printStackTrace();;
				}
			
			
		}
	}
	
	public void clearDataView()
	{
		dataView.setText("");
	}
	
	
	public void setSPM(SerialPortManage spm1)
	{
		spm = spm1;
	}
	
	public void setWestPanel(WestPanel westPanel1)
	{
		westPanel = westPanel1;
	}
}
