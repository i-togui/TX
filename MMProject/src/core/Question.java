package core;

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
	
}
