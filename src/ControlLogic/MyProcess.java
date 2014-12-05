package ControlLogic;

public class MyProcess {
	public String A, B, C, D, V_MIN, V_MAX, interval, feedbackPolePlacement,
			observerPolePlacement;

	public MyProcess(String A, String B, String C, String D, String V_MIN,
			String V_MAX, String interval, String feedbackPolePlacement,
			String observerPolePlacement) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
		this.V_MAX = V_MAX;
		this.V_MIN = V_MIN;
		this.feedbackPolePlacement = feedbackPolePlacement;
		this.observerPolePlacement = observerPolePlacement;
		this.interval = interval;
	}
}
