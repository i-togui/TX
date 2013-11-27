package core;
//EN gros ceux qui vont assister a la reunion!
public class Attendee 
{
	String name;
	boolean here = true;
	boolean isTheReporter = false;
	public Attendee(String name) 
	{
		this.name = name;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public boolean isHere() 
	{
		return here;
	}
	public void setHere(boolean here) 
	{
		this.here = here;
	}
	public boolean isTheReporter() 
	{
		return isTheReporter;
	}
	public void setTheReporter(boolean isTheReporter) 
	{
		this.isTheReporter = isTheReporter;
	}
	@Override
	public String toString() {
		return "Attendee [name=" + name + ", here=" + here + ", isTheReporter="
				+ isTheReporter + "]";
	}
	
}
