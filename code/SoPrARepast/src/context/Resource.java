package context;

import socialPracticeElements.ContextElement;

public class Resource extends ContextElement {
	public Location isLocatedAt;
	
	public void locate(Location isLocatedAt) {
		this.isLocatedAt = isLocatedAt;
		isLocatedAt.add(this);
	}
	
	public Location getLocation() {
		return isLocatedAt;
	}
}
