package ControlLogic;
import matlabcontrol.MatlabInvocationException;
import GuiLogic.*;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;
import se.lth.control.realtime.IOChannelException;


public class Regulator extends Thread{
	private Validation validation;
	private MatlabCommands mc;
	private ReferenceGenerator refgen;
	private AnalogIn yChan;
    private AnalogOut uChan;
    private double minValue, maxValue;
    private Reader reader;
    private double interval;
	
	public Regulator(Reader reader, Validation validation, MatlabCommands matlabCommands, ReferenceGenerator refgen, AnalogIn yChan, AnalogOut uChan, double vMin, double vMax, double interval) throws IOChannelException{
		this.validation = validation;
		this.mc = matlabCommands;
		this.refgen = refgen;
		this.reader = reader;
		this.yChan = yChan;
		this.uChan = uChan;
		minValue = vMin;
		maxValue = vMax;
		this.interval = interval;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			long start = System.currentTimeMillis();
			double u = 0;
			double yRef = refgen.getRef() ;
			double y = 0;
			try {
				y = yChan.get();
			} catch (IOChannelException e) {
				System.err.println("Could not read y.");
			} 
			try {
				
				u = mc.calculateU(y, yRef);
			}catch (MatlabInvocationException e) {
				validation.setError(e.toString());
			}
			u = limit(u);
			try {
				uChan.set(u);
				long duration = System.currentTimeMillis() - start;
				try{
					sleep((long) (interval * 1000 - duration));
				}catch(InterruptedException e){
					break;
				}
			} catch (Exception e) {
				System.err.println("Couldn't set u.");
			}
			try {
				double[] states = mc.updateStates();
				reader.updateParams(u, y, yRef , states);
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
