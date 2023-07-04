package application;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private String name;
	private List<Answer> answers;
	
	public  Question() {
		this.answers = new ArrayList<Answer>();
		this.name=null;
	}
	public Question(String name,List<Answer> answers) {
		this.answers=answers;
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
}
