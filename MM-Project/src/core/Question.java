package core;

import java.util.ArrayList;

public class Question {
	String question;

	public Question(String data) {
		this.question = data;
	}

	@Override
	public String toString() {
		return question;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public static ArrayList<Question> convertArray2Questions(ArrayList list)
	{
		ArrayList<Question> lq = new ArrayList<Question>();
		for(int i = 0 ; i< list.size(); i++)
		{
			lq.add(new Question(list.get(i).toString()));
		}
		return lq;
	}
	public static ArrayList convertQuestions2Array(ArrayList<Question> list)
	{
		ArrayList lq = new ArrayList();
		for(int i = 0 ; i< list.size(); i++)
		{
			lq.add(list.get(i).getQuestion());
		}
		return lq;
	}
}
