package framework;

public class StrengthValues {

	double strength;
	double iBeliefStrength;
	double theyBeliefStrength;

	public StrengthValues(double strength, double iBeliefStrength, double theyBeliefStrength) {
		this.strength = strength;
		this.iBeliefStrength = iBeliefStrength ;
		this.theyBeliefStrength = theyBeliefStrength ;
	}
	
	public StrengthValues() {
		// TODO Auto-generated constructor stub
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
	public double getiBeliefStrength() {
		return iBeliefStrength;
	}
	public void setiBeliefStrength(double iBeliefStrength) {
		this.iBeliefStrength = iBeliefStrength;
	}
	public double getTheyBeliefStrength() {
		return theyBeliefStrength;
	}
	public void setTheyBeliefStrength(double theyBeliefStrength) {
		this.theyBeliefStrength = theyBeliefStrength;
	}
	

}
