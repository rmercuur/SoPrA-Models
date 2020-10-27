package socialPracticeElements;
public class Activity extends ContextElement {
	public Activity(String string) {
		setName(string);
	}
	
	String activityType;
	
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
