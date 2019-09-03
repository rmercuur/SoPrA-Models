package socialPracticeElements;

import contextElements.Location;

public class ContextElement {
	public Location isLocatedAt;
	
	public void locate(Location isLocatedAt) {
		this.isLocatedAt = isLocatedAt;
		isLocatedAt.add(this);
	}
	
	public Location getLocation() {
		return isLocatedAt;
	}
}
