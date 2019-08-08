package agents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.primitives.Doubles;

import contextElements.Home;
import contextElements.Resource;
import contextElements.Work;
import framework.Helper;
import framework.StrengthValues;
import framework.CommutingContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import socialPracticeElements.Activity;
import socialPracticeElements.ContextElement;
import socialPracticeElements.Value;

public class HabitualAgent extends Agent{

	static double habitThreshold = 0.5;
	List<ContextElement> myPerformanceContext;
	
	public HabitualAgent(int agentID, CommutingContextBuilder myContextBuilder, Home familyHome, Work myWork,
			List<Resource> familyResources, List<Resource> personalResources) {
		super(agentID, myContextBuilder);
		
		myHome = familyHome;
		this.myWork = myWork;
		myResources =new ArrayList<Resource>();
		myResources.addAll(familyResources);
		myResources.addAll(personalResources);
		myPerformanceContext =new ArrayList<ContextElement>();
	}
	
	Home myHome;
	Work myWork;
	List<Resource> myResources;
	
	public void sense() {
		myPerformanceContext.clear();
		myPerformanceContext.addAll(isLocatedAt.getLocatedHere());
	}
	
	public void decide() {
		double attention = getAttention();
		List<Activity> currentCandidates =new ArrayList<Activity>();
		for(Activity Ac: myCandidates) {
			double habitStrength = calculateHabitStrength(Ac);
			if (habitStrength > attention * habitThreshold) {
				currentCandidates.add(Ac);
			}
		}
		
		if (currentCandidates.size() ==0) {
			chosenAction = intentionalDecision(myCandidates);
			chosenMode = "Intentional0";
		}
		if (currentCandidates.size() ==1) {
			chosenAction = currentCandidates.get(0);
			chosenMode = "Habitual";
		}
		if (currentCandidates.size() > 1) {
			if (currentCandidates.size() == myCandidates.size()) { //the case where they are all habitual somehow;
				chosenAction = intentionalDecision(currentCandidates);
				chosenMode = "IntentionalAll";
			}
			else {
				chosenAction = intentionalDecision(currentCandidates);
				chosenMode = "IntentionalFiltered";
			}
		}

	}
	
	private Activity intentionalDecision(List<Activity> currentCandidates) {
		return currentCandidates.stream().
				max(Comparator.comparing(candidate -> candidateRating(candidate))).get();
	}
	
	
	public void act() {
		
	}
	
	public void update() {
		for(ContextElement CE:myPerformanceContext) {
			for(Activity AC:myCandidates) {
				double oldHabitStrength = myHabitualTriggers.get(CE, AC).getStrength();
				double newHabitStrength = 0;
				if (AC == chosenAction) {
					newHabitStrength = ((1-habitRate) *oldHabitStrength) + (habitRate *1);
				} else {
					newHabitStrength =  RunEnvironment.getInstance().getParameters().getBoolean("habitsDecrease") ?
							((1-habitRate) * oldHabitStrength) + (habitRate * 0):
								oldHabitStrength;
				}
				myHabitualTriggers.put(CE, AC, new StrengthValues(newHabitStrength));
			}
		}
	}
	
	/*Get Ratings, Habitscores, Attention*/
	
	public double getAttention() {
		double timeAfterIntervention =
				RunEnvironment.getInstance().getCurrentSchedule().getTickCount()-
				RunEnvironment.getInstance().getParameters().getInteger("InterventionTime");
		double lessenAttention = Math.pow(0.995, timeAfterIntervention);		
		double multiplier = 
				RunEnvironment.getInstance().getCurrentSchedule().getTickCount() > 
					RunEnvironment.getInstance().getParameters().getInteger("InterventionTime") 
		
				? Doubles.constrainToRange(
						RunEnvironment.getInstance().getParameters().getDouble("getExtraAttention")
						*lessenAttention,1,10)
						:1;
		return multiplier*Helper.normalRanged(1.0,0.3);
	}

	public double getHabitRate() {
		double habitRate = 0;
		while(habitRate == 0|| habitRate == 1){
        	habitRate = Helper.normalRanged(0.03,0.5);
        } 
		return habitRate;
	}
	
	private double candidateRating(Activity candidate) {
		return myContextBuilder.values.stream().
				mapToDouble(value -> candidateValueRating(candidate, value)).	
				sum();
	}
	
	private double candidateValueRating(Activity candidate, Value value) {
		double multiplierWalk = 
				RunEnvironment.getInstance().getCurrentSchedule().getTickCount() > 
					RunEnvironment.getInstance().getParameters().getInteger("InterventionTime") 
				&& "walk".equals(candidate.getName()) 
				? 2:1;
		double multiplierBike = 
							RunEnvironment.getInstance().getCurrentSchedule().getTickCount() > 
								RunEnvironment.getInstance().getParameters().getInteger("InterventionTime") 
							&& "rideBike".equals(candidate.getName()) 
							&& RunEnvironment.getInstance().getParameters().getBoolean("motivateMultipleAlternatives")
							? 2:1;

		double multiplierCar = 
			"takeCar".equals(candidate.getName())  
			? 1.5:1;
		return multiplierCar* multiplierWalk* multiplierBike* relatedValues.get(candidate, value).getStrength() * myAdheredValues.get(value).getStrength();
	}
	
	private double calculateHabitStrength(Activity AC) {
		return myPerformanceContext.stream().
				mapToDouble(i -> myHabitualTriggers.get(i,AC).getStrength()).average().getAsDouble();
	}

	

	
	/*
	 * Data Analysis
	 */
	
	public double getWalkHabitStrength() {
		Activity activityToReport = myCandidates.stream()
				  .filter(candidate -> "walk".equals(candidate.getName()))
				  .findAny()
				  .orElse(null);

		return calculateHabitStrength(activityToReport);
	}
	
	public double getRideBikeHabitStrength() {
		Activity activityToReport = myCandidates.stream()
				  .filter(candidate -> "rideBike".equals(candidate.getName()))
				  .findAny()
				  .orElse(null);

		return calculateHabitStrength(activityToReport);
	}
	
	public double getTakeCarHabitStrength() {
		Activity activityToReport = myCandidates.stream()
				  .filter(candidate -> "takeCar".equals(candidate.getName()))
				  .findAny()
				  .orElse(null);

		return calculateHabitStrength(activityToReport);
	}
	
	public double getWalkIntentionStrength() {
		Activity activityToReport = myCandidates.stream()
				  .filter(candidate -> "walk".equals(candidate.getName()))
				  .findAny()
				  .orElse(null);

		return candidateRating(activityToReport);
	}
	
	public double getRideBikeIntentionStrength() {
		Activity activityToReport = myCandidates.stream()
				  .filter(candidate -> "rideBike".equals(candidate.getName()))
				  .findAny()
				  .orElse(null);

		return candidateRating(activityToReport);
	}
	
	public double getTakeCarIntentionStrength() {
		Activity activityToReport = myCandidates.stream()
				  .filter(candidate -> "takeCar".equals(candidate.getName()))
				  .findAny()
				  .orElse(null);

		return candidateRating(activityToReport);
	}
}
