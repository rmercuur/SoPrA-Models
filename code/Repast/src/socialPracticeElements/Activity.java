package socialPracticeElements;
public class Activity extends ContextElement {
	public String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Activity(String string) {
		setName(string);
	}
	
}
