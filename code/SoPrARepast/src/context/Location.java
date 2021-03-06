package context;
import java.util.ArrayList;
import java.util.List;

import socialPracticeElements.ContextElement;

public class Location extends ContextElement {
	List<ContextElement> locatedHere;
	
	public boolean add(ContextElement e) {
		return locatedHere.add(e);
	}

	public List<ContextElement> getLocatedHere() {
		return locatedHere;
	}
	
	public Location() {
		locatedHere=new ArrayList<ContextElement>();
		locatedHere.add(this); //locate the location at itself
	}
}
