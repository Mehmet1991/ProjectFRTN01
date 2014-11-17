package ControlLogic;


public class PlotData implements Cloneable { 
    private double ref;
	private double y; 
    private double x; // holds the current time 
    
    public Object clone() { 
        try { 
	    return super.clone(); 
        } catch (Exception e) {return null;} 
    }

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getRef() {
		return ref;
	}

	public void setRef(double ref) {
		this.ref = ref;
	}

	public void setY(double y) {
		this.y = y;
	} 
} 