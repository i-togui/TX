package core;

import java.util.ArrayList;

public class Sequence {
	float startOffset;
	Attendee whoSpeacking;
	Question question;
	Item type;
	ArrayList<Item> criteriaList;
	String comment;
	
	public void setStartOffset(float startOffset) {
		this.startOffset = startOffset;
	}
	public void setWhoSpeacking(Attendee whoSpeacking) {
		this.whoSpeacking = whoSpeacking;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public void setType(Item type) {
		this.type = type;
	}
	public void setCriteriaList(ArrayList<Item> criteriaList) {
		this.criteriaList = criteriaList;
	}
	public Sequence(float startOffset, Attendee whoSpeacking, Question question) 
	{
		this.startOffset = startOffset;
		this.whoSpeacking = whoSpeacking;
		this.question = question;
		this.comment="";
	}
	public Sequence(float startOffset, Attendee whoSpeacking, Question question, String comment) 
	{
		this.startOffset = startOffset;
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
	public float getStartOffset() 
	{
		return startOffset;
	}
	public Attendee getWhoSpeacking() 
	{
		return whoSpeacking;
	}
	public Question getQuestion() {
		return question;
	}
	public Item getType() {
		return type;
	}
	public ArrayList<Item> getCriteriaList() {
		return criteriaList;
	}
	
	
	public void addType(Item type) 
	{
		this.type = type;
	}
	public void removeType() 
	{
		this.type = null;
	}
	
	public void addCriteria(Item criteria) 
	{
		this.criteriaList.add(criteria);
	}
	public void removeCriteria(Item criteria) 
	{
		int tmp = findItemInList(criteria, this.criteriaList);
		if(tmp>-1) this.criteriaList.remove(tmp);
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
}
