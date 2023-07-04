package application;

public class Answer {

	private String alphabet;
	private String grade;
	public Answer(String alphabet,String grade) {
		this.alphabet=alphabet;
		this.grade=grade;
	}
	public Answer(String alphabet) {
		this.alphabet=alphabet;
		this.grade="0";
	}

	public String getAlphabet() {
		return alphabet;
	}
	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String imageLink) {
		this.grade = imageLink;
	}
	
}
