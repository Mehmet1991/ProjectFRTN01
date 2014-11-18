package TestingOnly;

import ControlLogic.ReferenceGenerator;
import GuiLogic.OpCom;
import GuiLogic.Reader;

public class OpComTester {    

	 public static void main(String[] argv) {
		  OpCom opcom = new OpCom();
		  opcom.initializeGUI();
		  Reader reader = new Reader(opcom);
		  opcom.start();
		  reader.start();
		  ReferenceGenerator refgen = new ReferenceGenerator(10, 1);
		  refgen.start();
		  opcom.showStatistics(reader, refgen);
	 }
}
