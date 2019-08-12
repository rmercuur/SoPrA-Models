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

import contextElements.*;
import socialPracticeElements.*;
import framework.Helper;
import framework.StrengthValues;
import framework.CommutingContextBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import socialPracticeElements.Activity;
import socialPracticeElements.ContextElement;

public abstract class Agent extends ContextElement {
	int ID;
	CommutingContextBuilder myContextBuilder;
	Table<ContextElement,Activity,StrengthValues> myHabitualTriggers;
	Table<Activity,Value,StrengthValues> relatedValues;
	Table<Activity,Value,StrengthValues> childParentRelation;
	
	Map<Value,StrengthValues> myAdheredValues;
	List<Activity> myCandidates;
	String chosenMode;
	
	Activity chosenAction;
	
	double habitRate;
	

	public Agent(int agentID, CommutingContextBuilder myContextBuilder) {
		this.ID = agentID;
		this.myContextBuilder = myContextBuilder;
		myCandidates= myContextBuilder.activities;
		
		
		relatedValues = HashBasedTable.create();
		for(Value V: myContextBuilder.values) {
			for(Activity AC: myContextBuilder.activities) {
				relatedValues.put(AC, V, Helper.normalRangedStrengthValues(0.5,0.1));
			}
		}
		
		myAdheredValues= new HashMap<Value,StrengthValues>();
		for(Value V: myContextBuilder.values) {
				myAdheredValues.put(V, Helper.normalRangedStrengthValues(1,0.5,0,2));
		}
			
		habitRate = getHabitRate(); //maybe this should be edited
	}
		
	//Creates Habitual Triggers After All Context Elements Have Been Added To the Simulation
	public void createHabitualTriggers() {
		myHabitualTriggers = HashBasedTable.create();
		
		for(ContextElement CE: myContextBuilder.contextElements) {
			for(Activity AC: myContextBuilder.activities) {
				myHabitualTriggers.put(CE, AC, new StrengthValues());
			}
		}
	}
	

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
		return chosenAction.name;
	}
	public int getID() {
		return ID;
	}
	
	public String getChosenMode() {
		return chosenMode;
	}
}
