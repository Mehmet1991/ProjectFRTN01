package GuiLogic;
import java.util.regex.Pattern;
import javax.swing.JTextField;


public class Validation {
	private MainGUI gui;
	private String matriceRegex = "\\[\\s*((\\d+(\\.*?\\d*?) *)*\\d+\\s*;(\\p{Blank})*)*(\\d+(\\.*?\\d*?) *)*\\d+\\s*]";
	
	
	private String matriceWarning = "Wrong format for matrice ";
	
	public Validation(MainGUI gui){
		this.gui = gui;
	}
	
	public boolean validateMatrices(String matriceA, String matriceB, String matriceC, String matriceD){
		gui.resetErrorMessage();
		boolean result = true;
		result &= validateMatrice(matriceA, "A");
		result &= validateMatrice(matriceB, "B");
		result &= validateMatrice(matriceC, "C");
		result &= validateMatrice(matriceD, "D");
		return result;
	}
	
	private boolean validateMatrice(String matrice, String matriceName){
	  if (!matrice.isEmpty()) {
		
		matrice = matrice.trim();

		if(!Pattern.matches(matriceRegex, matrice)){
			gui.printErrorMessage(matriceWarning + matriceName + ": " + matrice);
			return false;
		}
		matrice = matrice.substring(1);
		matrice = matrice.substring(0, matrice.length()-1);
		matrice = matrice.trim();
		String[] trimThis = matrice.split(";");
		String[] matriceSplit = new String[trimThis.length];
		for (int i = 0; i < trimThis.length; i++) {
			matriceSplit[i] = trimThis[i].trim();
		}
		    
		if(matriceSplit != null){
			int arrayLength = matriceSplit[0].split("\\s+").length;
			for(int i = 1; i < matriceSplit.length; i++){
				if(arrayLength != matriceSplit[i].split("\\s+").length){
					gui.printErrorMessage(matriceWarning + matriceName + "2: [" + matrice + "]");
					return false;
				}
			}
		}
		return true;
		
	  } else {
		  gui.printErrorMessage(matriceWarning + matriceName + " Please fill in!");
		  return false;
	  }
	}

	public boolean validateSamplingInterval(JTextField txtInterval) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean validateFeedbackPole(JTextField txtFeedbackPole) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean validateObserverPole(JTextField txtObserverPole) {
		// TODO Auto-generated method stub
		return false;
	}
	

		

	
	
}

