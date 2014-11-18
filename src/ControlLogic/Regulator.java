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
	
	public Regulator(Validation validation, StateFeedback stateFeedback, ReferenceGenerator refgen) throws IOChannelException{
		this.validation = validation;
		this.stateFeedback = stateFeedback;
		this.refgen = refgen;
		yChan = new AnalogIn(1);
		uChan = new AnalogOut(1);
	}
	
	@Override
	public void run(){
		while(Thread.interrupted()){
			double u = 0;
			try {
				u = stateFeedback.calculateOutput(yChan.get(), refgen.getRef());
			} catch (IOChannelException e) {
				System.err.println("Could not read y.");
			} catch (MatlabInvocationException e) {
				validation.setError(e.getMessage());
			}
			u = limit(u);
			try {
				uChan.set(u);
			} catch (Exception e) {
				System.err.println("Could not set u.");
			}
			try {
				stateFeedback.updateState(u);
			} catch (MatlabInvocationException e) {
				validation.setError(e.getMessage());
			}
		}
	}

	private double limit(double u) {
		return 0;
	}
}
