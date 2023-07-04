package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryItem {
	private String category;
	private ObservableList<Question> categoryQuestion;
	private int numQuest;
	public CategoryItem(String category) {
		this.category = category;
		categoryQuestion = FXCollections.observableArrayList();
		this.numQuest=0;
	}
	public CategoryItem() {
		category = null;
		categoryQuestion = FXCollections.observableArrayList();
		this.numQuest=0;
	}
	public int getNumQuest() {
		return numQuest;
	}
	public void setNumQuest(int numQuest) {
		this.numQuest = numQuest;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public ObservableList<Question> getCategoryQuestion() {
		return categoryQuestion;
	}
	public void setCategoryQuestion(ObservableList<Question> categoryQuestion) {
		this.categoryQuestion = categoryQuestion;
	}
	@Override
	public String toString() {
		this.numQuest = categoryQuestion.size();
		if(numQuest==0) {
			return this.getCategory();
		}else {
			return (this.getCategory()+" ("+numQuest+")");
		}
		
	}
}
