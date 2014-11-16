package TestingOnly;

import ControlLogic.ReferenceGenerator;
import GuiLogic.PlotterGUI;
import SimEnvironment.AnalogSink;

public class PlotterTester {
	public static void main(String[] args){
		PlotterGUI gui = new PlotterGUI(3, 4);
		
		AnalogSink U = gui.getSinkPlotter1(0);
		AnalogSink Y = gui.getSinkPlotter1(1);
		AnalogSink Yref = gui.getSinkPlotter1(2);
		ReferenceGenerator refgen = new ReferenceGenerator(10, 1);
		refgen.start();
		
		AnalogSink state1 = gui.getSinkPlotter2(0);
		AnalogSink state2 = gui.getSinkPlotter2(1);
		AnalogSink state3 = gui.getSinkPlotter2(2);
		AnalogSink state4 = gui.getSinkPlotter2(3);
		
		while(true){
			U.set(refgen.getRef()*2);
			Y.set(refgen.getRef()*6);
			Yref.set(refgen.getRef()*7);
			
			state1.set(refgen.getRef()*2);
			state2.set(refgen.getRef()*6);
			state3.set(refgen.getRef()*7);
			state4.set(refgen.getRef()*3);
		}
	}
}
