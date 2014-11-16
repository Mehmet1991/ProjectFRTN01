package ControlLogic;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;
import se.lth.control.realtime.IOChannelException;


public class Regulator extends Thread{
	private StateFeedback stateFeedback;
	private Monitor monitor;
	private ReferenceGenerator refgen;
	private AnalogIn yChan;
    private AnalogOut uChan;
	
	public Regulator(StateFeedback stateFeedback, Monitor monitor, ReferenceGenerator refgen) throws IOChannelException{
		this.stateFeedback = stateFeedback;
		this.monitor = monitor;
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
			}
			u = limit(u);
			try {
				uChan.set(u);
			} catch (Exception e) {
				System.err.println("Could not set u.");
			}
			stateFeedback.updateState(u);
		}
	}

	private double limit(double u) {
		// TODO Auto-generated method stub, måste kolla upp gränser för u
		return 0;
	}
}
