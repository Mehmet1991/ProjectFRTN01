package ControlLogic;


import javax.swing.*;

import GuiLogic.MainGUI;

import java.awt.*;
import java.awt.event.*;

import se.lth.control.*;

public class ReferenceGenerator extends Thread {
	private double amplitude;
	private int period;
	private double sign = -1.0;
	private double ref;
	private JFrame frame;
	
	private class RefGUI {
		private JPanel paramsLabelPanel = new JPanel();
		private JPanel paramsFieldPanel = new JPanel();
		private BoxPanel paramsPanel = new BoxPanel(BoxPanel.HORIZONTAL);
		private JTextField paramsAmpField = new JTextField();
		
		public RefGUI(double amp, double h) {
			paramsLabelPanel.setLayout(new GridLayout(0,1));
			paramsLabelPanel.add(new JLabel("Amp: "));
			
			paramsFieldPanel.setLayout(new GridLayout(0,1));
			paramsFieldPanel.add(paramsAmpField); 
			paramsPanel.add(paramsLabelPanel);
			paramsPanel.addGlue();
			paramsPanel.add(paramsFieldPanel);
			Double d = new Double(amp);
			paramsAmpField.setText(d.toString());
			d = new Double(h);
			paramsAmpField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tempString = paramsAmpField.getText();
					boolean ok = true;
					Double doubleObject = new Double(0.0);
					try {
						doubleObject = new Double(tempString);
					} catch (Exception x) {
						ok = false;
					}
					double tempValue;
					if (ok) {
						tempValue = doubleObject.doubleValue();
					} else {
						tempValue = -1.0;
					}
						amplitude = tempValue;
				}
			});
			frame = new JFrame("RefGen");
			frame.setVisible(true);
			frame.getContentPane().add(paramsPanel);
			frame.setSize(200, 50);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.addWindowListener(new WindowAdapter() {
					 public void windowClosing(WindowEvent e) {
						 MainGUI.refgenCreated = false;
					 }
			});
		}
	}
	
	public ReferenceGenerator(double h, double a) {
		amplitude = a;
		period = (int)(h*1000/2);
		new RefGUI(a,h);
	}
	
	public synchronized double getRef() 
	{
		return ref;
	}
	
	public void run() {
		try {
			while (!isInterrupted()) {
				synchronized (this) {
					sign = - sign;
					ref = amplitude;
				}
				sleep(period);
			}
		} catch (InterruptedException e) {
			// Requested to stop
		}
	}
	
	public void shutDown(){
		frame.dispose();
		this.interrupt();
	}
}
