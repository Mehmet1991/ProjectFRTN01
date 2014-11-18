package ControlLogic;
import matlabcontrol.MatlabInvocationException;
import GuiLogic.*;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;
import se.lth.control.realtime.IOChannelException;


public class Regulator extends Thread{
	private Validation validation;
	private StateFeedback stateFeedback;
	private ReferenceGenerator refgen;
	private AnalogIn yChan;
    private AnalogOut uChan;
    private double minValue, maxValue;
    private Reader reader;
    private double testU;
	
	public Regulator(Reader reader, Validation validation, StateFeedback stateFeedback, ReferenceGenerator refgen, double vMin, double vMax) throws IOChannelException{
		this.validation = validation;
		this.stateFeedback = stateFeedback;
		this.refgen = refgen;
		this.reader = reader;
//		yChan = new AnalogIn(1);
//		uChan = new AnalogOut(1);
		minValue = vMin;
		maxValue = vMax;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			double u = 0;
			double yRef = refgen.getRef();
			double y = 0;
			y = yRef;
//			try {
//				y = yChan.get();
//			} catch (IOChannelException e) {
//				System.err.println("Could not read y.");
//			} 
			try {
				
				u = stateFeedback.calculateOutput(y, yRef);
			}catch (MatlabInvocationException e) {
				validation.setError(e.toString());
			}
			u = limit(u);
			try {
//				uChan.set(u);
				testU = u;
			} catch (Exception e) {
				System.err.println("Couldn't set u.");
			}
			try {
				stateFeedback.updateState(u);
				reader.updateParams(u, y, yRef);
			} catch (MatlabInvocationException e) {
				validation.setError(e.getMessage());
			}
		}
	}

	private double limit(double u) {
		return u > maxValue ? maxValue : (u < minValue) ? minValue : u;
	}
}
