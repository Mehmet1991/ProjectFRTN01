import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Window.Type;


public class MainGUI {

	private JFrame frmStateFeedbackController;
	private JTextField txtA;
	private JTextField txtB;
	private JTextField txtC;
	private JTextField txtD;
	private JTextField txtInterval;
	private JTextField txtFeedbackPole;
	private JTextField txtObserverPole;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStateFeedbackController = new JFrame();
		frmStateFeedbackController.getContentPane().setBackground(Color.WHITE);
		frmStateFeedbackController.setResizable(false);
		frmStateFeedbackController.setTitle("State Feedback Controller with Observer");
		frmStateFeedbackController.setBounds(100, 100, 806, 494);
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
		txtA.setBounds(215, 73, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtA);
		txtA.setColumns(10);
		
		txtB = new JTextField();
		txtB.setBounds(215, 100, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtB);
		txtB.setColumns(10);
		
		txtC = new JTextField();
		txtC.setBounds(215, 127, 146, 19);
		frmStateFeedbackController.getContentPane().add(txtC);
		txtC.setColumns(10);
		
		txtD = new JTextField();
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
		
		JLabel lblNewLabel_6 = new JLabel("Observer pole placement");
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
		
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStart.setBackground(Color.LIGHT_GRAY);
		btnStart.setBounds(602, 188, 80, 25);
		frmStateFeedbackController.getContentPane().add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBackground(Color.LIGHT_GRAY);
		btnStop.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStop.setBounds(696, 188, 80, 25);
		frmStateFeedbackController.getContentPane().add(btnStop);
		
		JLabel lblWarnings = new JLabel("Warnings");
		lblWarnings.setFont(new Font("Dialog", Font.BOLD, 17));
		lblWarnings.setBounds(33, 207, 156, 41);
		frmStateFeedbackController.getContentPane().add(lblWarnings);
		
		JLabel lblParameters = new JLabel("Parameters");
		lblParameters.setFont(new Font("Dialog", Font.BOLD, 17));
		lblParameters.setBounds(33, 22, 156, 41);
		frmStateFeedbackController.getContentPane().add(lblParameters);
	}
}
