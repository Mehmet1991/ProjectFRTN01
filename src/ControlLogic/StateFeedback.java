package ControlLogic;

import matlabcontrol.MatlabInvocationException;

public class StateFeedback {
	MatlabCommands mc;
	
	public StateFeedback(MatlabCommands mc){
		this.mc = mc;
	}

	public void updateState(double u) throws MatlabInvocationException {
		mc.updateStates();
	}

	public double calculateOutput(double y, double yRef) throws MatlabInvocationException {
		double u = mc.calculateU(y, yRef);
		return u;
	}

}
