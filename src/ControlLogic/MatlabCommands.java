package ControlLogic;
import java.io.File;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;


public class MatlabCommands {
	private MatlabProxyFactoryOptions options;
	private MatlabProxyFactory factory;
	public MatlabProxy proxy;
	
	public MatlabCommands() throws MatlabConnectionException, MatlabInvocationException {
		/*
		 * �ndra till .setHidden(false) f�r att kunna se exakt vad som h�nder i matlab
		 */
		options = new MatlabProxyFactoryOptions.Builder().setMatlabStartingDirectory(new File("src/")).setHidden(true).build();
		factory = new MatlabProxyFactory(options);
		proxy = factory.getProxy();
		proxy.eval("addpath('" + new File("src").getAbsolutePath() + "')");
	}

	public void performEval() throws MatlabInvocationException, MatlabConnectionException {	
		proxy.eval("SOIDesign");
	}
	
	public void eval(String command) throws MatlabInvocationException{
		proxy.eval(command);
	}
	
	public double[] getVariables(String command) throws MatlabInvocationException{
		return (double[])proxy.getVariable(command);
	}
	
	public void tearDown(){
		try {
			proxy.exit();
		} catch (MatlabInvocationException e) {
			System.err.println("Could not close matlab.");
		}
	}

	public boolean isClosed() {
		return proxy.isConnected();
	}

	public double calculateU(double y, double yRef) throws MatlabInvocationException {
		proxy.eval("y = " + y + ";");
		proxy.eval("uc = " + yRef + ";");
		proxy.eval("u = Lc*uc - L*xhat - li*xi");
		return getVariables("u")[0];
	}

	public double[] updateStates() throws MatlabInvocationException {
		proxy.eval("intermediate = AR*[xhat ; xi] + BRy*y + BRr*uc;xhat=intermediate(1:size(intermediate,1)-1);xi=intermediate(size(intermediate,1));");
		return getVariables("xhat");
	}

	public void setParams(String a, String b, String c, String d, String h,
			String fPole, String oPole) throws MatlabInvocationException {
		proxy.eval("A = " + a + "; B = " +  b + "; C = " + c + "; D = " + d + "; h = " + h + "; fPole = " + fPole +"; oPole = " + oPole  + ";");
	}
}
