package socialPracticeElements;

public class StrengthValues {

	double strength;
	double personalView;
	double myCollectiveView;

	public StrengthValues(double strength, double personalView, double myCollectiveView) {
		this.strength = strength;
		this.personalView = personalView ;
		this.myCollectiveView = myCollectiveView ;
	}

	public StrengthValues(double newHabitStrength) {
		this.strength = newHabitStrength;
	}

	public double getStrength() {
		return strength;
	}
	public void setStrength(double strength) {
		this.strength = strength;
	}
	public double getPersonalView() {
		return personalView;
	}
	public void setPersonalView(double personalView) {
		this.personalView = personalView;
	}
	public double getMyCollectiveView() {
		return myCollectiveView;
	}
	public void setMyCollectiveView(double myCollectiveView) {
		this.myCollectiveView = myCollectiveView;
	}
	

}
