package com.SerialDemo;

import javax.swing.*;
import java.awt.*;


public class MainFrame {
	
	private static SouthPanel southPanel;
	private static SerialPortManage spm;
	private static WestPanel westPanel;
	private static CenterPanel centerPanel;
	
	public static void main(String[] args)
	{ 
		JFrame frame = new JFrame("串口通信");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(new BorderLayout());
		
		southPanel = new SouthPanel();
		centerPanel = new CenterPanel(southPanel);
	    westPanel = new WestPanel(centerPanel,southPanel);
	    spm = new SerialPortManage(centerPanel);
	    
	    centerPanel.setSPM(spm);
	    westPanel.setSPM(spm);
	    
	    spm.setWestPanel(westPanel);
	    centerPanel.setWestPanel(westPanel);
		
		frame.getContentPane().add(centerPanel,BorderLayout.CENTER);
		frame.getContentPane().add(westPanel, BorderLayout.WEST);
		frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	

}
