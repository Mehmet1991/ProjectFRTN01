package GuiLogic;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import se.lth.control.BoxPanel;
import se.lth.control.plot.PlotterPanel;
import ControlLogic.ReferenceGenerator;


/** Class that creates and maintains a GUI for the Ball and Beam process. 
	 Uses two internal threads to update plotters */

public class OpCom {    

    private PlotterPanel measurementPlotter; // has internal thread
    private PlotterPanel controlPlotter; // has internal thread
    
    // Declaration of main frame.
    private JFrame frame;

    // Declaration of panels.
    private BoxPanel plotterPanel;

    private double range = 10.0; // Range of time axis
    private int divTicks = 5;    // Number of ticks on time axis
    private int divGrid = 5;     // Number of grids on time axis

       
    /** Constructor. Creates the plotter panels. */
    public OpCom() {
		  measurementPlotter = new PlotterPanel(3, 4); // Two channels
		  controlPlotter = new PlotterPanel(2, 4);
    }

    /** Starts the threads. */
    public void start() {
		  measurementPlotter.start();
		  controlPlotter.start();
    }

    /** Stops the threads. */
    public void stopThread() {
		  measurementPlotter.stopThread();
		  controlPlotter.stopThread();
    }

    /** Creates the GUI. Called from Main. */
    public void initializeGUI() {
		  // Create main frame.
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		} catch (Exception e) { }
		  frame = new JFrame("Plotter");

	
		  // Create a panel for the two plotters.
		  plotterPanel = new BoxPanel(BoxPanel.VERTICAL);
		  // Create plot components and axes, add to plotterPanel.
		  measurementPlotter.setYAxis(20, -10, 4, 4);
		  measurementPlotter.setXAxis(range, divTicks, divGrid);
		  measurementPlotter.setTitle("Process signals");
		  plotterPanel.add(measurementPlotter);
		  plotterPanel.addFixed(10);
		  controlPlotter.setYAxis(20, -10, 4, 4);
		  controlPlotter.setXAxis(range, divTicks, divGrid);
		  controlPlotter.setTitle("Feedback states");
		  plotterPanel.add(controlPlotter);
	
		  frame.add(plotterPanel);
	
		  // WindowListener that exits the system if the main window is closed.
		  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		  frame.addWindowListener(new WindowAdapter() {
					 public void windowClosing(WindowEvent e) {
						 MainGUI.plotterCreated = false;
					 }
				});

		  // Set guiPanel to be content pane of the frame.
		  frame.getContentPane().add(plotterPanel, BorderLayout.CENTER);

		  // Pack the components of the window.
		  frame.pack();

		  // Position the main window at the screen center.
		  Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
		  Dimension fd = frame.getSize();
		  frame.setLocation((sd.width-fd.width)/2, (sd.height-fd.height)/2);
	
		  // Make the window visible.
		  frame.setVisible(true);
    }

    /** Called by Reader to put a control signal data point in the buffer. */
    public synchronized void putControlDataPoint(PlotData pd) {
		  double time = pd.x;
		  double s1 = pd.states[0];
		  double s2 = pd.states[1];
//		  double s3 = pd.states[2];
//		  double s4 = pd.states[3];
		  controlPlotter.putData(time, s1, s2);
    }
    
    /** Called by Reader to put a measurement data point in the buffer. */
    public synchronized void putMeasurementDataPoint(PlotData pd) {
		  double time = pd.x;
		  double ref = pd.ref;
		  double y = pd.y;
		  double u = pd.u;
		  measurementPlotter.putData(time, ref, y, u);
    }   
    
    public void showStatistics(Reader reader, ReferenceGenerator refgen) {
		  while(true){
			  reader.yChan.set(refgen.getRef()*2);
			  reader.refChan.set(refgen.getRef()*6);
			  reader.uChan.set(refgen.getRef()*7);
			  for(int i = 0; i < 4; i++){
				  reader.sChan[i].set(refgen.getRef()*(i+4));
			  }
		  }
    }
    
    public void shutDown(){
    	frame.dispose();
    }
}
