package GuiLogic;


public class PlotData implements Cloneable { 
    public double ref;
    public double y; 
    public double x; // holds the current time 
    public double u; 
    public double[] states; 
    
    public Object clone() { 
        try { 
	    return super.clone(); 
        } catch (Exception e) {return null;} 
    }
} 