package GuiLogic;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import SimEnvironment.Plotter;
import SimEnvironment.VirtualProcess;

public class PlotterGUI extends VirtualProcess {
    
    private static final int stateNbr=1;  //number of states
    private static final int inputNbr=3;  //number of inputs
    private static final int outputNbr=3; //number of outputs

    private double kPhi=4.4; //process coefficient for angle


    /**
     * Creates an instance of the virtual process Beam
     */
    public PlotterGUI() {
		super(stateNbr, inputNbr, outputNbr);
		Plotter plotter = new Plotter(3,100,10,-10);
		getSource(0).setPlotter(plotter,0);
		getSink(0).setPlotter(plotter,1);
		getSink(1).setPlotter(plotter,2);
		JFrame frame = new JFrame("Plotter");
		frame.getContentPane().add(plotter.getPanel());
	        frame.pack();
	        frame.setVisible(true);
    }
    
    /**
     * Calculates new output signals for the virtual process Beam
     *
     * @param state process state
     * @param input process input
     * @return new outputs for the process
     */
    public double[] computeOutput(double[] state, double[] input) {
		double[] output = new double[outputNbr];
		output[0] = getSource(0).get();//update beam angle
		return output;
    }

    /**
     * Calculates new states for the virtual process Beam
     *
     * @param state process state
     * @param input process inputs
     * @param h time difference
     * @return new process state
     */
    public double[] updateState(double[] state, double[] input, double h) {
		double ulim;
		double[] newState = new double[stateNbr];
	
		//Euler forward approximation
		ulim = limit(input[0],-10,10);
		newState[0] = state[0] + kPhi*h*ulim;   //update beam angle
		return newState;
    }

    private double limit(double v, double min, double max) {
		if (v < min) {
		    v = min;
		} else {
		    if (v > max) {
			v = max;
		    }
		}
		return v;
    }



} // Beam




