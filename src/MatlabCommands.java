import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;


public class MatlabCommands {
	private MatlabProxyFactory factory;
	private MatlabProxy proxy;
	
	public MatlabCommands() throws MatlabConnectionException {
		factory = new MatlabProxyFactory();
		proxy = factory.getProxy();
	}
	
	/*
	 * Endast f�r test, se huruvida det fungerar
	 */
	public void performEval() throws MatlabInvocationException, MatlabConnectionException {		
		proxy.eval("disp('hello world')");
	}
}
