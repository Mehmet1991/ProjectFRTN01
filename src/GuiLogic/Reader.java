package GuiLogic;
import SimEnvironment.AnalogSink;

public class Reader extends Thread {
    private OpCom opCom;
    private boolean doIt = true;

	 public AnalogSink yChan;
	 public AnalogSink refChan;
	 public AnalogSink uChan;
	 public AnalogSink sChan[] = new AnalogSink[4];

    /** Constructor. Sets initial values of the controller parameters and initial mode. */
    public Reader(OpCom opCom) {
		  this.opCom = opCom;
    }

    /** Run method. Sends data periodically to Opcom. */
    public void run() {
		  final long h = 25; // period (ms)
		  long duration;
		  long t = System.currentTimeMillis();
		  PlotData pd;
		  double yValue = 0, refValue = 0, uValue = 0;
		  double[] states = new double[sChan.length];
		  double realTime = 0;

		  try {
				yChan = new AnalogSink(0);
				refChan = new AnalogSink(1);
				uChan = new AnalogSink(2);
				for(int i = 0; i < sChan.length; i++){
					sChan[i] = new AnalogSink(3+i);
					
				}
		  } catch (Exception e) {
				System.out.println(e);
		  } 

		  setPriority(7);

		  while (doIt) {
				try {
					yValue = yChan.get();
					refValue = refChan.get();
					uValue = uChan.get();
					for(int i = 0; i < sChan.length; i++){
						 states[i] = sChan[i].get();
					}
				} catch (Exception e) {
					 System.out.println(e);
				} 

				pd = new PlotData();
				pd.y = yValue;
				pd.ref = refValue;
				pd.u = uValue;
				pd.x = realTime;
				pd.states = states;
				opCom.putMeasurementDataPoint(pd);
				opCom.putControlDataPoint(pd);

				realTime += ((double) h)/1000.0;

				t += h;
				duration = (int) (t - System.currentTimeMillis());
				if (duration > 0) {
					 try {
						  sleep(duration);
					 } catch (Exception e) {}
				}
		  }
    }

    /** Stops the thread. */
    private void stopThread() {
		  doIt = false;
    }

    /** Called by Opcom when the Stop button is pressed. */
    public synchronized void shutDown() {
		  stopThread();
    }

	public void updateParams(double u, double y, double yRef, double[] states) {
		yChan.set(y);
		refChan.set(yRef);
		uChan.set(u);
//		for(int i = 0; i < sChan.length; i++){
//			sChan[i].set(states[i]);
//		}
	}

	public void setPlotter(OpCom opCom) {
		this.opCom = opCom;
		
	} 

}