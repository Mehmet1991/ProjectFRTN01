package ControlLogic;
import se.lth.control.DoublePoint;
import GuiLogic.OpCom;
import SimEnvironment.AnalogSink;

public class Reader extends Thread {
    private OpCom opcom;
    private boolean doIt = true;

	 public AnalogSink velChan;
	 public AnalogSink posChan;
	 public AnalogSink ctrlChan;

    /** Constructor. Sets initial values of the controller parameters and initial mode. */
    public Reader(OpCom opcom) {
		  this.opcom = opcom;
    }

    /** Run method. Sends data periodically to Opcom. */
    public void run() {
		  final long h = 25; // period (ms)
		  long duration;
		  long t = System.currentTimeMillis();
		  DoublePoint dp;
		  PlotData pd;
		  double vel = 0, pos = 0, ctrl = 0;
		  double realTime = 0;

		  try {
				velChan = new AnalogSink(0);
				posChan = new AnalogSink(1);
				ctrlChan = new AnalogSink(2);
		  } catch (Exception e) {
				System.out.println(e);
		  } 

		  setPriority(7);

		  while (doIt) {
				try {
					 vel = velChan.get();
					 pos = posChan.get();
					 ctrl = ctrlChan.get();
				} catch (Exception e) {
					 System.out.println(e);
				} 

				pd = new PlotData();
				pd.setY(vel);
				pd.setRef(pos);
				pd.setX(realTime);
				opcom.putMeasurementDataPoint(pd);
	    
				dp = new DoublePoint(realTime,ctrl);
				opcom.putControlDataPoint(dp);

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

}