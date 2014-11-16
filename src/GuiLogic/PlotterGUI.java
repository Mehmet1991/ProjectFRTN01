package GuiLogic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JLabel;

import SimEnvironment.AnalogSink;
import SimEnvironment.Plotter;
import SimEnvironment.VirtualProcess;

public class PlotterGUI {
	PlotterPanel plotterPanel1, plotterPanel2;
	
    public PlotterGUI(int nbrStates) {
    	plotterPanel1 = new PlotterPanel(0, 3, 0);
    	plotterPanel2 = new PlotterPanel(0, nbrStates, 0);

    	Panel panel1 = new Panel();
    	Panel panel2 = new Panel();

    	panel1.add(new JLabel("Controller:"), BorderLayout.NORTH);
    	panel2.add(new JLabel("States:"), BorderLayout.CENTER);
    	panel1.add(plotterPanel1.getPanel(), BorderLayout.NORTH);
    	panel2.add(plotterPanel2.getPanel(), BorderLayout.CENTER);
		
		JFrame frame = new JFrame("Plotter");
		Container contentPane = frame.getContentPane();
		GridLayout layout = new GridLayout(2,1);
        contentPane.setLayout(layout);
        contentPane.add(panel1);
        contentPane.add(panel2);
        frame.pack();
        frame.setVisible(true);
    }

	public AnalogSink getSinkPlotter1(int i) {
		return plotterPanel1.getSink(i);
	}

	public AnalogSink getSinkPlotter2(int i) {
		return plotterPanel2.getSink(i);
	}
}

class PlotterPanel extends VirtualProcess{
	private int outputNbr;
	private Plotter plotter;
	public PlotterPanel(int stateNbr, int inputNbr, int outputNbr) {
		super(stateNbr, inputNbr, outputNbr);
		this.outputNbr = outputNbr;
		
		plotter = new Plotter(inputNbr,100,10,-10);
		for(int i = 0; i < inputNbr; i++){
			getSink(i).setPlotter(plotter,i);
		}
	}

	public Component getPanel() {
		return plotter.getPanel();
	}

	@Override
	public double[] computeOutput(double[] state, double[] input) {
		double[] output = new double[outputNbr];
		for(int i = 0; i < outputNbr; i++){
			output[i] = getSink(i).get();
		}
		return output;
	}

	@Override
	public double[] updateState(double[] state, double[] input, double h) {
		return state;
	}
	
}




