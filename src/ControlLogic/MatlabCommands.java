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
		 * Ändra till .setHidden(false) för att kunna se exakt vad som händer i matlab
		 */
		options = new MatlabProxyFactoryOptions.Builder().setMatlabStartingDirectory(new File("src/")).setHidden(true).build();
		factory = new MatlabProxyFactory(options);
		proxy = factory.getProxy();
		proxy.eval("addpath('" + new File("src").getAbsolutePath() + "')");
	}
	
	/*
	 * Endast för test, se huruvida det fungerar
	 */
	public void performEval() throws MatlabInvocationException, MatlabConnectionException {	
		proxy.eval("SOIDesign");
		proxy.eval("A");
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
}
