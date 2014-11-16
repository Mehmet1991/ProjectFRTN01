package TestingOnly;

import java.util.Random;

import GuiLogic.*;
import SimEnvironment.AnalogSink;
import SimEnvironment.AnalogSource;

public class PlotterTester {
	public static void main(String[] args){
		PlotterGUI gui = new PlotterGUI();
		AnalogSink analogOut = gui.getSink(0);
		AnalogSink analogIn = gui.getSink(1);
		AnalogSource analogRef = gui.getSource(0);
		Random rand = new Random();
		while(true){
			analogOut.set(rand.nextDouble()*20 - 10);
			analogIn.set(rand.nextDouble()*10 - 5);
			analogRef.set(rand.nextDouble()*15 -7.5);
		}
	}
}
