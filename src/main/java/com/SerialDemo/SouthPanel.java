package com.SerialDemo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class SouthPanel extends JPanel {

	private JLabel txLabel,rxLabel;
	private JButton clearCountBtn;
	private int tx=0, rx=0;
	public JLabel stateLb;
	
	public SouthPanel() {
		this.setPreferredSize(new Dimension(540, 30));
		this.setLayout(new GridLayout(1, 4));
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		
		ButtonListener buttonListener = new ButtonListener();
		
		stateLb = new JLabel("串口未打开");
		txLabel = new JLabel("TX:"+Integer.toString(tx));
		rxLabel = new JLabel("RX:"+Integer.toString(rx));
		
		clearCountBtn = new JButton("清空计数");
		clearCountBtn.addActionListener(buttonListener);
		
		this.add(stateLb);
		this.add(txLabel);
		this.add(rxLabel);
		this.add(clearCountBtn);
	}
	
	public void setTX(int num) {
		tx = num;
	}
	
	public void setRX(int num) {
		rx = num;
	}
	
	public int getTX()
	{
		return tx;
	}
	
	public int getRX()
	{
		return rx;
	}
	
	public void setLabel(int num1,int num2)
	{
		txLabel.setText("TX:"+Integer.toString(num1));
		rxLabel.setText("RX:"+Integer.toString(num2));
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			tx=0;
			rx=0;
			txLabel.setText("TX:"+Integer.toString(tx));
			rxLabel.setText("RX:"+Integer.toBinaryString(rx));
		}
	}
}
