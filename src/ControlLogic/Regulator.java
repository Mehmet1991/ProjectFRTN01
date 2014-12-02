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
    private boolean isSimulation = false;
	
	public Regulator(Reader reader, Validation validation, StateFeedback stateFeedback, ReferenceGenerator refgen, AnalogIn yChan, AnalogOut uChan, double vMin, double vMax) throws IOChannelException{
		this.validation = validation;
		this.stateFeedback = stateFeedback;
		this.refgen = refgen;
		this.reader = reader;
		if(!isSimulation){
			this.yChan = yChan;
			this.uChan = uChan;
		}
		minValue = vMin;
		maxValue = vMax;
		minValue = 0;
		maxValue = 10;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			long start = System.currentTimeMillis();
			double u = 0;
			double yRef = refgen.getRef();
			double y = 0;
			if(isSimulation){
				y = yRef * 2;
			}else{
				try {
					y = yChan.get();
				} catch (IOChannelException e) {
					System.err.println("Could not read y.");
				} 
			}
			try {
				
				u = stateFeedback.calculateOutput(y, yRef);
				u+= 4.3;
			}catch (MatlabInvocationException e) {
				validation.setError(e.toString());
			}
			u = limit(u);
			if(!isSimulation){
				try {
					uChan.set(u);
					long duration = System.currentTimeMillis() - start;
					sleep((long) (200 - duration));
				} catch (Exception e) {
					System.err.println("Couldn't set u.");
				}
			}
			try {
				double[] states = stateFeedback.updateState(u);
				reader.updateParams(u, y + 4.3, yRef + 4.3, states);
			} catch (MatlabInvocationException e) {
				validation.setError(e.getMessage());
			}
		}
		try {
			uChan.set(0);
			refgen.shutDown();
			reader.shutDown();
		} catch (IOChannelException e) {
			validation.setError("Could not stop the process due to \n\t" + e.getMessage());
		}
	}

	private double limit(double u) {
		return u > maxValue ? maxValue : (u < minValue) ? minValue : u;
	}

	public void setRefgen(ReferenceGenerator refgen) {
		this.refgen = refgen;
	}
}

class MyAnalogIn extends AnalogIn{

	public MyAnalogIn(final int index) throws IOChannelException {
		super(index);
	}
	
	public void shutdown() throws IOChannelException{
		close();
	}
	
}
