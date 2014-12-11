package GuiLogic;
import java.util.regex.Pattern;


public class Validation {
	private MainGUI gui;
	private String matriceRegex = "\\[\\s*(((-?\\d+)(\\.*?\\d*?) *)*(-?\\d+)\\s*;(\\p{Blank})*)*((-?\\d+)(\\.*?\\d*?) *)*(-?\\d+)\\s*]";
	private String samplingIntervalRegex = "\\d+(\\.\\d+)?";
	private String poleRegex = "(-?\\d+(\\.\\d+)?\\p{Blank}+)*(-?\\d+)(\\.\\d+)?";
	

	private String matriceWarning = "Wrong format for matrice ";
	private String samplingIntervalWarning = "Wrong sampling interval format";
	private String feedbackPoleWarning = "Wrong feedback pole placement";
	private String observerPoleWarning = "Wrong observer pole placement";
	
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
					gui.printErrorMessage(matriceWarning + matriceName + ": [" + matrice + "]");
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

	public boolean validateSamplingInterval(String samplingInterval) {
		return validateRegex(samplingInterval, samplingIntervalRegex, samplingIntervalWarning);
	}

	public boolean validateFeedbackPole(String feedbackPole) {
		return validateRegex(feedbackPole, poleRegex, feedbackPoleWarning);
	}

	public boolean validateObserverPole(String observerPoles) {
		return validateRegex(observerPoles, poleRegex, observerPoleWarning);
	}
	
	public boolean validateRegex(String text, String regex, String warningText){
		if (!text.isEmpty()) {
			text = text.trim();

			if(!Pattern.matches(regex, text)){
				gui.printErrorMessage(warningText + ": " + text);
				return false;
			}
			return true;
		} else {
			gui.printErrorMessage(warningText +  " Please fill in!");
			return false;
		}
	}

	public void setError(String message) {
		gui.printErrorMessage(message);
		
	}

	public boolean validateBounds(String vMinString, String vMaxString) {
		try{
			double min = Double.valueOf(vMinString);
			double max = Double.valueOf(vMaxString);
			if(min <= max){
				return true;
			}else{
				gui.printErrorMessage("V-min must be smaller than V-max!");
				return false;
			}
		}catch(NumberFormatException e){
			gui.printErrorMessage("V-min and V-max must be double values!");
			return false;
		}
	}
}

