package application;
//Lop de thi dung cho yeu cau tao moi de thi, chinh sua de thi...
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Quiz {
	private String nameQuiz;
	private ObservableList<Question> questionQuiz;
	private int timeQuiz;
	
	public int getTimeQuiz() {
		return timeQuiz;
	}
	public void setTimeQuiz(int timeQuiz) {
		this.timeQuiz = timeQuiz;
	}
	public Quiz(String nameQuiz) {
		this.nameQuiz = nameQuiz;
		questionQuiz = FXCollections.observableArrayList();
		this.timeQuiz = 0;
	}
	public Quiz(String nameQuiz,int timeQuiz) {
		this.nameQuiz = nameQuiz;
		this.timeQuiz = timeQuiz;
		questionQuiz = FXCollections.observableArrayList();
	}
	public Quiz() {
		this.nameQuiz = null;
		questionQuiz = FXCollections.observableArrayList();
		this.timeQuiz = 0;
	}
	public String getNameQuiz() {
		return nameQuiz;
	}
	public void setNameQuiz(String nameQuiz) {
		this.nameQuiz = nameQuiz;
	}
	public ObservableList<Question> getQuestionQuiz() {
		return questionQuiz;
	}
	public void setQuestionQuiz(ObservableList<Question> questionQuiz) {
		this.questionQuiz = questionQuiz;
	}
}
