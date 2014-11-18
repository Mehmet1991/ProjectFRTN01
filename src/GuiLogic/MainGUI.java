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

import javax.swing.JButton;
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
import ControlLogic.MatlabCommands;
import ControlLogic.ReferenceGenerator;
import ControlLogic.Regulator;
import ControlLogic.StateFeedback;


public class MainGUI {

	private JFrame frmStateFeedbackController;
	private JTextField txtA;
	private JTextField txtB;
	private JTextField txtC;
	private JTextField txtD;
	private JTextField txtInterval;
	private JTextField txtFeedbackPole;
	private JTextField txtObserverPole;
	private TextArea textAreaWarnings;
	private MatlabCommands mc;
	private Validation validator;
	private JButton btnStart;
	private JTextField txtVMin;
	private JTextField txtVMax;
	private double vMin, vMax = 0;
	private boolean savedParams = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//			UIManager.setLookAndFeel ( "com.alee.laf.WebLookAndFeel" );
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStateFeedbackController = new JFrame();
		frmStateFeedbackController.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(mc != null){
					mc.tearDown();
				}
			}
		});
		frmStateFeedbackController.setResizable(false);
		frmStateFeedbackController.getContentPane().setBackground(UIManager.getColor("Button.background"));
		frmStateFeedbackController.setTitle("State Feedback Controller with Observer");
		frmStateFeedbackController.setBounds(100, 100, 823, 513);
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
		textAreaWarnings.setBounds(33, 287, 757, 167);
		frmStateFeedbackController.getContentPane().add(textAreaWarnings);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(savedParams){
						btnStart.setEnabled(false);
						initiateMatlab();
						mc.performEval();
						OpCom opCom = new OpCom();
						Reader reader = new Reader(opCom);
						opCom.initializeGUI();
						opCom.start();
						reader.start();
						ReferenceGenerator refgen = new ReferenceGenerator(10, 1);
						refgen.start();
						new Regulator(reader, validator, new StateFeedback(mc), refgen, vMin, vMax).start();
					}
				} catch (MatlabInvocationException | MatlabConnectionException | IOException e) {
					printErrorMessage(e.getLocalizedMessage());
				}
			}
		});
		btnStart.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStart.setBackground(Color.LIGHT_GRAY);
		btnStart.setBounds(602, 188, 80, 25);
		frmStateFeedbackController.getContentPane().add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnStart.setEnabled(true);
			}
		});
		btnStop.setBackground(Color.LIGHT_GRAY);
		btnStop.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStop.setBounds(696, 188, 80, 25);
		frmStateFeedbackController.getContentPane().add(btnStop);
		
		JLabel lblWarnings = new JLabel("Warnings");
		lblWarnings.setFont(new Font("Dialog", Font.BOLD, 17));
		lblWarnings.setBounds(33, 237, 156, 49);
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
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frmStateFeedbackController, "This software was developed by Mehmet, Soheil, Jaqub and Hassan\n\n For more information, please contact us. All right reserved©", "About us", JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnFile.add(mntmAbout);
		
		JButton btnNewButton = new JButton("Update parameters");
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean result = true;
				String A = txtA.getText().trim();
				String B = txtB.getText().trim();
				String C = txtC.getText().trim();
				String D = txtD.getText().trim();
				String h = txtInterval.getText().trim();
				String fPole = txtFeedbackPole.getText().trim();
				String oPole = txtObserverPole.getText().trim();
				result &= validator.validateMatrices(A, B, C, D);
				result &= validator.validateSamplingInterval(h);
				result &= validator.validateFeedbackPole(fPole);
				result &= validator.validateObserverPole(oPole);
				if(result){
					try {
						mc.setParams(A,B,C,D, h, fPole, oPole);
						savedParams = true;
					} catch (MatlabInvocationException e) {
						printErrorMessage(e.getMessage());
					}
				}
			}
		});
		btnNewButton.setBounds(455, 190, 137, 23);
		frmStateFeedbackController.getContentPane().add(btnNewButton);
		
		JLabel lblVmin = new JLabel("V-min:");
		lblVmin.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVmin.setBounds(33, 183, 70, 15);
		frmStateFeedbackController.getContentPane().add(lblVmin);
		
		JLabel lblVmax = new JLabel("V-max:");
		lblVmax.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVmax.setBounds(33, 210, 70, 15);
		frmStateFeedbackController.getContentPane().add(lblVmax);
		
		txtVMin = new JTextField();
		txtVMin.setToolTipText("Limit the minimum value of control input");
		txtVMin.setBounds(215, 181, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtVMin);
		txtVMin.setColumns(10);
		
		txtVMax = new JTextField();
		txtVMax.setToolTipText("Limit the maximum value of control input");
		txtVMax.setColumns(10);
		txtVMax.setBounds(215, 208, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtVMax);
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
