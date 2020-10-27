package soPrARepast;


import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import agents.Agent;
import agents.CommutingAgent;
import context.CommutingBike;
import context.CommutingCar;
import context.CommutingHome;
import context.Location;
import context.Resource;
import context.Timepoint;
import context.CommutingWork;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.*;
import socialPracticeElements.Activity;
import socialPracticeElements.ContextElement;
import socialPracticeElements.Value;

public class CommutingContextBuilder implements ContextBuilder<Object> {
	public Context<Object> context;
	public Grid<Object> grid;
	public GridFactory myGridFactory;
	public List<ContextElement> contextElements;
	public List<Value> values;
	public List<Agent> agents;
	public List<Activity> activities;
	public List<Location> locations;
	public List<Resource> resources; //not used in current implementation - instead list of context-elements is used
	public List<Timepoint> timepoints;  //not used in current implementation - instead list of context-elements is used
	public List<CommutingHome> homes;
	public List<CommutingWork> workPlaces;

	public int agentID;
	
	
	@Override
	public Context build(Context<Object> context) {
		this.context = context;
		agents=new ArrayList<Agent>();
		agentID= 1;
		locations=new ArrayList<Location>();
		homes=new ArrayList<CommutingHome>();
		workPlaces=new ArrayList<CommutingWork>();
		contextElements=new ArrayList<ContextElement>();
		activities=new ArrayList<Activity>();
		values=new ArrayList<Value>();
	
		context.setId("SoPrARepast");
		makeGrid();
		addLocations();
		createValues();
		createActivities();
		
		addAgents();
		agents.forEach(agent -> agent.createHabitualTriggers()); //create after each context-element has been created
		
		/*Specifies simulation endTime*/
		RunEnvironment.getInstance().endAt(RunEnvironment.getInstance().getParameters().getInteger("EndTime"));
		
		
		return context;
	}
	
	private void createActivities() {
		Activity takeCar =new Activity("takeCar");
		Activity rideBike=new Activity("rideBike");
		Activity walk =new Activity("walk");
		activities.add(takeCar);
		activities.add(rideBike);
		activities.add(walk);
	}

	private void createValues() {
		Value enviromentalism =new Value("environmentalism");
		Value effeciency =new Value("effeciency");
		values.add(enviromentalism);
		values.add(effeciency);
	}

	public void makeGrid() {
		myGridFactory = GridFactoryFinder.createGridFactory(null);
		int width = 20;
		int height = 20;
		grid = myGridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
				new RandomGridAdder<Object>(),
				true, width, height)); //width and height are amount of games and space for 3;
	}

	
	public void addLocations() {
		int homeCount = 3;
		for (int i=0; i < homeCount; i++){
			CommutingHome newHome=new CommutingHome();
			context.add(newHome);
			locations.add(newHome);
			homes.add(newHome);
			contextElements.add(newHome);
		}
		int workCount =3;
		for (int i=0; i < workCount; i++){
			CommutingWork newWork=new CommutingWork();
			context.add(newWork);
			locations.add(newWork);
			workPlaces.add(newWork);
			contextElements.add(newWork);
		}
	}
	
	/*
	 * Creates a family
	 * Creats resources for that family (car)
	 * Selects a family Home
	 * Creates agents with those resources and home
	 */
	public void addAgents() {
		int familyCount =5;
		int agentsPerFamilyCount =3;
		for (int i=0; i < familyCount; i++){
			CommutingHome familyHome = homes.get(RandomHelper.nextIntFromTo(0,homes.size()-1));
			List<Resource> familyResources =createFamilyResources(familyHome);
			for (int j=0; j <agentsPerFamilyCount; j++) {
				createAgent(familyHome, familyResources);
			}
		}
	}
	
	private List<Resource> createFamilyResources(Location myLocation) {
		CommutingCar myCar =new CommutingCar();
		myCar.locate(myLocation);
		contextElements.add(myCar);
		List<Resource> familyResources = Arrays.asList(myCar);
		return familyResources;
	}

	public void createAgent(CommutingHome familyHome, List<Resource> familyResources) {
		List<Resource> personalResources = createPersonalResources(familyHome);
		CommutingWork myWork = workPlaces.get(RandomHelper.nextIntFromTo(0,workPlaces.size()-1));
		Agent newAgent=new CommutingAgent(agentID, this, familyHome, myWork, familyResources, personalResources);
		agentID++;
		newAgent.locate(familyHome);
		context.add(newAgent);
		contextElements.add(newAgent);
		agents.add(newAgent);
	}

	private List<Resource> createPersonalResources(Location myLocation) {
		CommutingBike myBike = new CommutingBike();
		myBike.locate(myLocation);
		contextElements.add(myBike);
		List<Resource> personalResources = Arrays.asList(myBike);
		return personalResources;
	}

}