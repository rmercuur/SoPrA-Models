package agents;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.primitives.Doubles;

import context.*;
import socialPracticeElements.*;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import soPrARepast.CommutingContextBuilder;
import soPrARepast.Helper;

public abstract class Agent extends ContextElement {
	int ID;
	List<Activity> myCandidates;
	String chosenMode;
	
	//connections with social practice
	CommutingContextBuilder myContextBuilder;
	Table<ContextElement,Activity,StrengthValues> myHabitualConnections;
	Table<Activity,Value,StrengthValues> myValueConnections;
	Table<Activity,Activity,StrengthValues> myActivityConnections;
	Map<Value,StrengthValues> myValuePriorities;
	
	//context connections
	List<ContextElement> isInContextOf;
	List<Resource> owns;
	Location isBasedAt;
	public Location isLocatedAt;
	
	//agent decision-making variables
	Activity chosenActivity;
	double habitRate;
	static double habitThreshold;
	double attentionalResources;
	
	double initialHabitStrength = 0.5; //TO BE SET BY MODELLER

	
	public Agent(int agentID, CommutingContextBuilder myContextBuilder) {
		this.ID = agentID;
		this.myContextBuilder = myContextBuilder;
		myCandidates= myContextBuilder.activities;
		
		
		myValueConnections = HashBasedTable.create();
		for(Value V: myContextBuilder.values) {
			for(Activity AC: myContextBuilder.activities) {
				myValueConnections.put(AC, V, Helper.normalRangedStrengthValues(0.5,0.1));
			}
		}
		
		myValuePriorities= new HashMap<Value,StrengthValues>();
		for(Value V: myContextBuilder.values) {
				myValuePriorities.put(V, Helper.normalRangedStrengthValues(1,0.5,0,2));
		}
			
		habitRate = getHabitRate(); //maybe this should be edited
	}
	
	/*Create HabitualConnections*/
	//Creates Habitual Triggers After All Context Elements Have Been Added To the Simulation
	public void createHabitualTriggers() {
		myHabitualConnections = HashBasedTable.create();
		
		for(ContextElement CE: myContextBuilder.contextElements) {
			for(Activity AC: myContextBuilder.activities) {
				myHabitualConnections.put(CE, AC, new StrengthValues(initialHabitStrength));
			}
		}
	}
	
	
	/* Locating Agent */
	public void locate(Location isLocatedAt) {
		this.isLocatedAt = isLocatedAt;
		isLocatedAt.add(this);
	}
	
	public Location getLocation() {
		return isLocatedAt;
	}
	
	/* Decision-making */
	
	@ScheduledMethod(start = 1, interval = 1, priority = 10)
	public abstract void sense();
	
	@ScheduledMethod(start = 1, interval = 1, priority = 9)
	public abstract void decide();
	
	@ScheduledMethod(start = 1, interval = 1, priority = 8)
	public abstract void act();
	
	@ScheduledMethod(start = 1, interval = 1, priority = 7)
	public abstract void update();
	
	/* Getters */
	
	public abstract double getHabitRate();
	public abstract double getAttention();
	
	/*Data-analysis*/
	
	public String getChosenAction() {
		return chosenActivity.getName();
	}
	public int getID() {
		return ID;
	}
	
	public String getChosenMode() {
		return chosenMode;
	}
}
