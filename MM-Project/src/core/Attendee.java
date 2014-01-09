package core;

import java.util.ArrayList;

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
		return name;
	}
	public static ArrayList<Attendee> convertArray2Attendees(ArrayList list)
	{
		ArrayList<Attendee> lq = new ArrayList<Attendee>();
		for(int i = 0 ; i< list.size(); i++)
		{
			lq.add(new Attendee(list.get(i).toString()));
		}
		return lq;
	}
	public static ArrayList convertAttendees2Array(ArrayList<Attendee> list)
	{
		ArrayList lq = new ArrayList();
		for(int i = 0 ; i< list.size(); i++)
		{
			lq.add(list.get(i).getName());
		}
		return lq;
	}
}
