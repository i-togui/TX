package core;

import java.util.ArrayList;

import android.util.Log;

public class Sequence {
	float finishOffset;
	Attendee whoSpeacking;
	Question question;
	ArrayList<Item> itemsList;
	String comment;
	
	public void setfinishOffset(float finishOffset) {
		this.finishOffset = finishOffset;
	}
	public void setWhoSpeacking(Attendee whoSpeacking) {
		this.whoSpeacking = whoSpeacking;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public void setCriteriaList(ArrayList<Item> criteriaList) {
		this.itemsList = criteriaList;
	}
	public Sequence(float finishOffset, Attendee whoSpeacking, Question question) 
	{
		this.finishOffset = finishOffset;
		this.whoSpeacking = whoSpeacking;
		this.question = question;
		this.comment="";
	}
	public Sequence(float finishOffset, Attendee whoSpeacking, Question question, String comment) 
	{
		this.finishOffset = finishOffset;
		this.whoSpeacking = whoSpeacking;
		this.question = question;
		this.comment=comment;
	}
	public Sequence()
	{}
	public String getComment() 
	{
		return comment;
	}
	public void setComment(String comment) 
	{
		this.comment = comment;
	}
	public float getfinishOffset() 
	{
		return finishOffset;
	}
	public Attendee getWhoSpeacking() 
	{
		return whoSpeacking;
	}
	public Question getQuestion() {
		return question;
	}

	public ArrayList<Item> getItemsList() {
		return itemsList;
	}
	
	
	
	
	public void addCriteria(Item criteria) 
	{
		this.itemsList.add(criteria);
	}
	public void removeCriteria(Item criteria) 
	{
		int tmp = findItemInList(criteria, this.itemsList);
		if(tmp>-1) this.itemsList.remove(tmp);
	}
	
	public int findItemInList(Item data, ArrayList<Item> list)
	{
		for (int i=0;i<list.size();i++)
		{
			if (list.get(i).toString() == data.toString() ) 
				return i;
		}
		return -1;
	}
	public void save(ArrayList<String> listeItems) {
		// TODO Auto-generated method stub
		
	}

	public static ArrayList getAttendees(ArrayList<Sequence> sequencesList)
	{
		ArrayList list = new ArrayList();
		for(int i = 0; i< sequencesList.size();i++)
		{
			boolean here=false;
			for(int j = 0; j< list.size();j++)
			{
				if(sequencesList.get(i).getWhoSpeacking().getName().compareTo((String)list.get(j))==0)
				{
					here=true;
				}
			}
			if(!here)
			{
				list.add(sequencesList.get(i).getWhoSpeacking().getName());
			}
		}
		return list;
	}
	public static ArrayList getQuestions(ArrayList<Sequence> sequencesList)
	{
		ArrayList list = new ArrayList();
		for(int i = 0; i< sequencesList.size();i++)
		{
			boolean here=false;
			for(int j = 0; j< list.size();j++)
			{
				if(sequencesList.get(i).getQuestion().getQuestion().compareTo((String)list.get(j))==0)
				{
					here=true;
				}
			}
			if(!here)
			{
				list.add(sequencesList.get(i).getQuestion().getQuestion());
			}
		}
		return list;
	}
	public static ArrayList getSequences(ArrayList<Sequence> sequencesList, String question)
	{
		ArrayList list = new ArrayList();
		for(int i = 0; i< sequencesList.size();i++)
		{
			if(sequencesList.get(i).getQuestion().getQuestion().compareTo(question)==0)
				{
					list.add(sequencesList.get(i));
				}
			
		}
		return list;
	}
	public static String getReporter(ArrayList<Attendee> attendeesList)
	{
		
		for(int i = 0; i< attendeesList.size();i++)
		{
			if(attendeesList.get(i).isTheReporter)
				{
					return attendeesList.get(i).getName();
				}
			
		}
		return "";
	}
}
