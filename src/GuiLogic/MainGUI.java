package GuiLogic;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;
import se.lth.control.realtime.IOChannelException;
import ControlLogic.MatlabCommands;
import ControlLogic.MyProcess;
import ControlLogic.ReferenceGenerator;
import ControlLogic.Regulator;
import ControlLogic.StateFeedback;


public class MainGUI {

	private static int yChannel = 0;
	private static int uChannel = 0;
	private JFrame frmStateFeedbackController;
	private JTextField txtA;
	private JTextField txtB;
	private JTextField txtC;
	private JTextField txtD;
	private JTextField txtInterval;
	private JTextField txtFeedbackPole;
	private JTextField txtObserverPole;
	private TextArea textAreaWarnings;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxProcess;
	private MatlabCommands mc;
	private Validation validator;
	private JButton btnStart, btnUpdate, btnStop, btnPlot;
	private JTextField txtVMin;
	private JTextField txtVMax;
	private double vMin, vMax = 0;
	private AnalogIn yChan;
    private AnalogOut uChan;
	
	private Reader reader;
	private Regulator regulator;
	
	private boolean isStarted;
	public static boolean plotterCreated = false;
	public static boolean refgenCreated = false;
	private boolean savedParams = true;
	
	private Map<String, MyProcess> theProcesses;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		} catch (Exception e) { }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frmStateFeedbackController.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		validator = new Validation(this);
		theProcesses = new HashMap<String, MyProcess>();
		MyProcess watertank = new MyProcess("[-0.0502 0 ; 0.0502 -0.0502]", "[0.2500 ; 0]", "[0 1]", "[0]", "0", "10", "0.1", "1 0.525 0.140625", "1 0.36 0.054 0.003375");
		MyProcess DCServo = new MyProcess("[-0.12 0 ; 5 0]", "[2.25 ; 0]", "[0 1]", "[0]", "-10", "10", "0.1", "1 4.308 6.186", "1 15.14 86.12 187.4");
		theProcesses.put("Watertank", watertank);
		theProcesses.put("DC Servo", DCServo);
		initialize();
		setParameters("Watertank");
		try {
			initiateMatlab();
		} catch (MatlabConnectionException | IOException
				| MatlabInvocationException e) {
			printErrorMessage("Could not run matlab, program failure.");
			btnUpdate.setEnabled(false);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		frmStateFeedbackController = new JFrame();
		frmStateFeedbackController.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(mc != null){
					mc.tearDown();
				}
				if(uChan != null){
					try {
						uChan.set(0);
					} catch (IOChannelException e) {
						System.out.println("Could not reset input!");
					}
				}
			}
		});
		frmStateFeedbackController.setResizable(false);
		frmStateFeedbackController.getContentPane().setBackground(UIManager.getColor("Button.background"));
		frmStateFeedbackController.setTitle("State Feedback Controller with Observer");
		frmStateFeedbackController.setBounds(100, 100, 823, 536);
		frmStateFeedbackController.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStateFeedbackController.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("State matrix (A): ");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(33, 75, 164, 15);
		frmStateFeedbackController.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Input matrix (B): ");
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(33, 102, 164, 15);
		frmStateFeedbackController.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Output matrix (C): ");
		lblNewLabel_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(33, 129, 164, 15);
		frmStateFeedbackController.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Feedthrough matrix (D): ");
		lblNewLabel_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(33, 156, 164, 15);
		frmStateFeedbackController.getContentPane().add(lblNewLabel_3);
		
		txtA = new JTextField();
		txtA.setToolTipText("Write the matrix in MATLAB syntax, e.g. [1 2 3; 4 5 6; 6 7 8] for a 3 x 3 matrix");
		txtA.setBounds(215, 73, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtA);
		txtA.setColumns(10);
		
		txtB = new JTextField();
		txtB.setToolTipText("Write the matrix in MATLAB syntax, e.g. [1 2 3; 4 5 6; 6 7 8] for a 3 x 3 matrix");
		txtB.setBounds(215, 100, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtB);
		txtB.setColumns(10);
		
		txtC = new JTextField();
		txtC.setToolTipText("Write the matrix in MATLAB syntax, e.g. [1 2 3; 4 5 6; 6 7 8] for a 3 x 3 matrix");
		txtC.setBounds(215, 127, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtC);
		txtC.setColumns(10);
		
		txtD = new JTextField();
		txtD.setToolTipText("Write the matrix in MATLAB syntax, e.g. [1 2 3; 4 5 6; 6 7 8] for a 3 x 3 matrix");
		txtD.setBounds(215, 154, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtD);
		txtD.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Sampling interval:");
		lblNewLabel_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(389, 75, 209, 15);
		frmStateFeedbackController.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("State feedback pole placement:");
		lblNewLabel_5.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(389, 102, 209, 15);
		frmStateFeedbackController.getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Observer pole placement:");
		lblNewLabel_6.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_6.setBounds(389, 129, 209, 15);
		frmStateFeedbackController.getContentPane().add(lblNewLabel_6);
		
		txtInterval = new JTextField();
		txtInterval.setColumns(10);
		txtInterval.setBounds(630, 73, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtInterval);
		
		txtFeedbackPole = new JTextField();
		txtFeedbackPole.setColumns(10);
		txtFeedbackPole.setBounds(630, 100, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtFeedbackPole);
		
		txtObserverPole = new JTextField();
		txtObserverPole.setColumns(10);
		txtObserverPole.setBounds(630, 127, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtObserverPole);
		
		textAreaWarnings = new TextArea(null, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		textAreaWarnings.setEditable(false);
		textAreaWarnings.setBackground(Color.WHITE);
		textAreaWarnings.setForeground(Color.RED);
		textAreaWarnings.setText(" . . .");
		textAreaWarnings.setBounds(33, 319, 757, 167);
		frmStateFeedbackController.getContentPane().add(textAreaWarnings);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(savedParams){
						btnStart.setEnabled(false);
						btnStop.setEnabled(true);
						btnUpdate.setEnabled(false);
						OpCom opCom = new OpCom();
						reader = new Reader(opCom);
						opCom.initializeGUI();
						opCom.start();
						reader.start();
						ReferenceGenerator refgen = new ReferenceGenerator(0, 0);
						refgen.start();
						if(yChan == null){
							yChan = new AnalogIn(yChannel);
						}
						if(uChan == null){
							uChan = new AnalogOut(uChannel);
						}
						double interval = Double.valueOf(txtInterval.getText());
						regulator = new Regulator(reader, validator, new StateFeedback(mc), refgen, yChan, uChan, vMin, vMax, interval);
						regulator.start();
						isStarted = true;
						plotterCreated = true;
						refgenCreated = true;
					}
				} catch (IOException e) {
					printErrorMessage(e.getLocalizedMessage());
				}
			}
		});
		btnStart.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStart.setBackground(Color.LIGHT_GRAY);
		btnStart.setBounds(602, 188, 80, 25);
		btnStart.setEnabled(false);
		frmStateFeedbackController.getContentPane().add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
				btnUpdate.setEnabled(true);
				isStarted = false;
				regulator.interrupt();
			
				
			}
		});
		btnStop.setBackground(Color.LIGHT_GRAY);
		btnStop.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStop.setBounds(696, 188, 80, 25);
		frmStateFeedbackController.getContentPane().add(btnStop);
		
		JLabel lblWarnings = new JLabel("Warnings");
		lblWarnings.setFont(new Font("Dialog", Font.BOLD, 17));
		lblWarnings.setBounds(33, 264, 156, 49);
		frmStateFeedbackController.getContentPane().add(lblWarnings);
		
		JLabel lblParameters = new JLabel("Parameters");
		lblParameters.setFont(new Font("Dialog", Font.BOLD, 17));
		lblParameters.setBounds(33, 39, 156, 25);
		frmStateFeedbackController.getContentPane().add(lblParameters);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setForeground(UIManager.getColor("Button.background"));
		menuBar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		menuBar.setBackground(UIManager.getColor("Button.background"));
		menuBar.setBounds(0, 0, 817, 25);
		frmStateFeedbackController.getContentPane().add(menuBar);
		
		JMenu mnNewMenu = new JMenu("Create");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewPlotter = new JMenuItem("New Plotter");
		mntmNewPlotter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!plotterCreated) {
					if(!isStarted) {
						JOptionPane.showMessageDialog(frmStateFeedbackController, "Nothing to plot! The process must be running first", "Nothing to plot", JOptionPane.PLAIN_MESSAGE);
					} else {
						OpCom opCom = new OpCom();
						opCom.initializeGUI();
						opCom.start();
						plotterCreated = true;
						if(reader != null) {						
							reader.setPlotter(opCom);
						}
					}
				} else {
					JOptionPane.showMessageDialog(frmStateFeedbackController, "Only one instance of plotter can be running at the same time!", "Warning", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		mnNewMenu.add(mntmNewPlotter);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New ReferenceGenerator");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!refgenCreated) {
					if(isStarted) {
						ReferenceGenerator refgen = new ReferenceGenerator(10, 1);
						refgen.start();
						regulator.setRefgen(refgen);
						refgenCreated = true;
					} else {
						JOptionPane.showMessageDialog(frmStateFeedbackController, "The process must be running first. Press 'Start'", "Start the process", JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frmStateFeedbackController, "Only one instance of reference generator can be running at the same time!", "Warning", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenu mnFile = new JMenu("Help");
		menuBar.add(mnFile);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frmStateFeedbackController, "This software was developed by Mehmet, Soheil, Jaqub and Hassan\n\n For more information, please contact us. All right reserved", "About us", JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnFile.add(mntmAbout);
		
		btnUpdate = new JButton("Update parameters");
		btnUpdate.setBackground(Color.LIGHT_GRAY);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean result = true;
				String A = txtA.getText().trim();
				String B = txtB.getText().trim();
				String C = txtC.getText().trim();
				String D = txtD.getText().trim();
				String h = txtInterval.getText().trim();
				String fPole = txtFeedbackPole.getText().trim();
				String oPole = txtObserverPole.getText().trim();
				String vMinString = txtVMin.getText().trim();
				String vMaxString = txtVMax.getText().trim();
				result &= validator.validateMatrices(A, B, C, D);
				result &= validator.validateSamplingInterval(h);
				result &= validator.validateFeedbackPole(fPole);
				result &= validator.validateObserverPole(oPole);
				result &= validator.validateBounds(vMinString, vMaxString);
				if(result){
					try {
						mc.setParams(A,B,C,D, h, fPole, oPole);
						vMin = Double.valueOf(vMinString);
						vMax = Double.valueOf(vMaxString);
						savedParams = true;
						btnStart.setEnabled(true);
						btnPlot.setEnabled(true);
						yChannel = getProcess().equals("Watertank") ? 31 : 0;
						System.out.println(yChannel);
						uChannel = getProcess().equals("Watertank") ? 30 : 0;
						System.out.println(uChannel);
						try {
							mc.performEval();
						} catch (MatlabConnectionException e) {
							printErrorMessage(e.getLocalizedMessage());
						}
						printErrorMessage("Update successful!");
					} catch (MatlabInvocationException e) {
						printErrorMessage(e.getMessage());
					}
				}
			}
		});
		btnUpdate.setBounds(455, 190, 137, 23);
		frmStateFeedbackController.getContentPane().add(btnUpdate);
		
		JLabel lblVmin = new JLabel("V-min:");
		lblVmin.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVmin.setBounds(33, 183, 70, 15);
		frmStateFeedbackController.getContentPane().add(lblVmin);
		
		JLabel lblVmax = new JLabel("V-max:");
		lblVmax.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVmax.setBounds(33, 210, 70, 15);
		frmStateFeedbackController.getContentPane().add(lblVmax);
		
		txtVMin = new JTextField();
		txtVMin.setToolTipText("Limit the minimum value of control input (must be a number)");
		txtVMin.setBounds(215, 181, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtVMin);
		txtVMin.setColumns(10);
		
		txtVMax = new JTextField();
		txtVMax.setToolTipText("Limit the maximum value of control input (must be a number)");
		txtVMax.setColumns(10);
		txtVMax.setBounds(215, 208, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtVMax);
		
		JLabel lblChooseAProcess = new JLabel("Choose a process:");
		lblChooseAProcess.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblChooseAProcess.setBounds(33, 238, 111, 15);
		frmStateFeedbackController.getContentPane().add(lblChooseAProcess);
		
		comboBoxProcess = new JComboBox(new String[] {"Watertank", "DC Servo", "Other"});
		comboBoxProcess.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBoxProcess.setToolTipText("Helps the user to define the model for the chosen process. Currently there is only support for the watertank process and DC Servo. Choose 'Other' if you wish to run a different process, and enter the model manually.");

		comboBoxProcess.setSelectedIndex(0);
		comboBoxProcess.setBounds(215, 236, 146, 20);
		frmStateFeedbackController.getContentPane().add(comboBoxProcess);
		
		btnPlot = new JButton("Plot step response");
		btnPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mc.plotStep();
				} catch (MatlabInvocationException e1) {
					printErrorMessage("Could not plot the step respons. Please try again later");
				}
			}
		});
		btnPlot.setEnabled(false);
		btnPlot.setBackground(Color.LIGHT_GRAY);
		btnPlot.setBounds(455, 225, 137, 23);
		frmStateFeedbackController.getContentPane().add(btnPlot);
		comboBoxProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setParameters(String.valueOf(comboBoxProcess.getSelectedItem()));
			}
		});
	}
	
	public String getProcess() {
		return String.valueOf(comboBoxProcess.getSelectedItem());
	}
	
	private void setParameters(String processName) {
		if(processName.equals("Other")) {
			txtA.setText("");
			txtB.setText("");
			txtC.setText("");
			txtD.setText("");
			txtFeedbackPole.setText("");
			txtInterval.setText("");
			txtObserverPole.setText("");
			txtVMax.setText("");
			txtVMin.setText("");;
		} else {
			MyProcess p = theProcesses.get(processName);
			txtA.setText(p.A);
			txtB.setText(p.B);
			txtC.setText(p.C);
			txtD.setText(p.D);
			txtInterval.setText(p.interval);
			txtVMax.setText(p.V_MAX);
			txtVMin.setText(p.V_MIN);
			txtFeedbackPole.setText(p.feedbackPolePlacement);
			txtObserverPole.setText(p.observerPolePlacement);
		}
	}
	
	public void printErrorMessage(String warning){
		textAreaWarnings.append("\t" + warning + "\n");
	}

	public void resetErrorMessage() {
		textAreaWarnings.setText("");
		
	}
	
	public void initiateMatlab() throws MatlabConnectionException, IOException, MatlabInvocationException{
		if(mc == null || mc.proxy.isRunningInsideMatlab()){
			mc = new MatlabCommands();
		}
	}
}
