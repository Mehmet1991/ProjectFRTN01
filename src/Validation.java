import java.util.regex.Pattern;

import javax.swing.JTextField;


public class Validation {
	private MainGUI gui;
	private String matriceRegex = "\\[((\\d+ )*\\d+\\;(\\p{Blank})?)*(\\d+ )*\\d+\\]";
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
		matrice = matrice.trim();
		if(!Pattern.matches(matriceRegex, matrice)){
			gui.printErrorMessage(matriceWarning + matriceName + ": " + matrice);
			return false;
		}
		String[] matriceSplit = matrice.split(";");
		if(matriceSplit != null){
			int arrayLength = matriceSplit[0].split(" ").length;
			for(int i = 1; i < matriceSplit.length; i++){
				if(arrayLength != matriceSplit[i].trim().split(" ").length){
					gui.printErrorMessage(matriceWarning + matriceName + "2: " + matrice);
					return false;
				}
			}
		}
		return true;
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
