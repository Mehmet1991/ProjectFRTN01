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
    private boolean isSimulation = true;
	
	public Regulator(Reader reader, Validation validation, StateFeedback stateFeedback, ReferenceGenerator refgen, double vMin, double vMax) throws IOChannelException{
		this.validation = validation;
		this.stateFeedback = stateFeedback;
		this.refgen = refgen;
		this.reader = reader;
		if(!isSimulation){
			yChan = new AnalogIn(1);
			uChan = new AnalogOut(1);
		}
		minValue = vMin;
		maxValue = vMax;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			double u = 0;
			double yRef = refgen.getRef();
			double y = 0;
			if(isSimulation){
				y = yRef;
			}else{
				try {
					y = yChan.get();
				} catch (IOChannelException e) {
					System.err.println("Could not read y.");
				} 
			}
			try {
				
				u = stateFeedback.calculateOutput(y, yRef);
			}catch (MatlabInvocationException e) {
				validation.setError(e.toString());
			}
			u = limit(u);
			if(!isSimulation){
				try {
					uChan.set(u);
				} catch (Exception e) {
					System.err.println("Couldn't set u.");
				}
			}
			try {
				double[] states = stateFeedback.updateState(u);
				reader.updateParams(u, y, yRef, states);
			} catch (MatlabInvocationException e) {
				validation.setError(e.getMessage());
			}
		}
	}

	private double limit(double u) {
		return u > maxValue ? maxValue : (u < minValue) ? minValue : u;
	}

	public void setRefgen(ReferenceGenerator refgen) {
		this.refgen = refgen;
	}
}
