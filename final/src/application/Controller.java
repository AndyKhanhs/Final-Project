
package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import javax.security.auth.callback.LanguageCallback;
import javax.swing.JOptionPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;


public class Controller implements Initializable {
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		categoryList = FXCollections.observableArrayList(new CategoryItem("Default"));
		comboboxCategory.setItems(categoryList);
		comboboxCategory.setValue(categoryList.get(0));
		cbbCategoryAddQues.setItems(categoryList);
		cbbCategoryAddQues.setValue(null);
//		questionList = FXCollections.observableArrayList();
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));		
		nameQuesTableAdd.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameQuesTableAddToo.setCellValueFactory(new PropertyValueFactory<>("name"));
		table.setItems(categoryList.get(orNumCategory).getCategoryQuestion());
		
		dragAndDropFile.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if(event.getGestureSource()!=dragAndDropFile&& event.getDragboard().hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});
		
		dragAndDropFile.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if(db.hasFiles()) {
					List<File> listFile = db.getFiles();
					handleFile(listFile.get(0));
					success=true;
					
				}
				event.setDropCompleted(success);
				
				event.consume();
			}
		});
		comboboxCategory.valueProperty().addListener(new ChangeListener<CategoryItem>() {

			@Override
			public void changed(ObservableValue<? extends CategoryItem> cateGory, CategoryItem oldCategory, CategoryItem newCategory) {
				for(int i = 0; i<categoryList.size();i++) {
					if(categoryList.get(i).equals(newCategory)) {
						orNumCategory = i;
						table.setItems(categoryList.get(i).getCategoryQuestion());
						addButtonToTable();
						return;
					}
				}
				
			}
			
		});	
		timeSeconds.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				int sc = (Integer)arg2;
				int hourr = sc/3660;
				hourCountdownLabel.setText("Time left: 0"+Integer.toString(hourr));
				int minutee = (sc-hourr*3600)/60;
				if(minutee>=10) {
					minuteCountdownLabel.setText(Integer.toString(minutee));
				}else {
					minuteCountdownLabel.setText("0"+Integer.toString(minutee));
				}
				int secondd = sc - hourr*3600-minutee*60;
				if(secondd>=10) {
					secondCountdownLabel.setText(Integer.toString(secondd));	
				}else {
					secondCountdownLabel.setText("0"+Integer.toString(secondd));
				}
				if(sc==0) {
					eventFinishAttempt(btnFinish);
				}
			}
		});
		cbbCategoryAddQues.valueProperty().addListener(new ChangeListener<CategoryItem>() {

			@Override
			public void changed(ObservableValue<? extends CategoryItem> cateGory, CategoryItem oldCategory, CategoryItem newCategory) {
				// TODO Auto-generated method stub
				for(int i=0;i<categoryList.size();i++) {
					if(categoryList.get(i).equals(newCategory)) {
						orNumCategoryToo=i;
						tableAddQuestion.setItems(categoryList.get(i).getCategoryQuestion());
						ObservableList<Integer> randomNum = FXCollections.observableArrayList();
						for(int j=1;j<=categoryList.get(i).getCategoryQuestion().size();j++) {
							randomNum.add(j);
						}
						cbbNumRandom.setItems(randomNum);
						return;
					}
				}
			}
		});
		addButtonToTable();
		btnFinish.setPrefWidth(300);
		btnFinish.setAlignment(Pos.BASELINE_LEFT);
		btnFinish.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				if(btnFinish.getText().equals("Finish attempt...")) {
				confirmAnchor.setVisible(true);
				confirmPane.setVisible(true);
				submitButton.setOnAction(e->{
					eventFinishAttempt(btnFinish);
				});
				}
				else eventFinishAttempt(btnFinish);
				
			}
		});
	}
	
	@FXML
	private AnchorPane anchorRoot;
	@FXML
	private Pane popUpQuestionBank_Pane;
	@FXML
	private AnchorPane popUpQuestionBank;
	@FXML
	private AnchorPane firstAnchorPane;
	@FXML
	private AnchorPane tabAnchorPane;
	@FXML
	private AnchorPane confirmPane;
	@FXML
	private AnchorPane confirmAnchor;
	@FXML
	private TableView<Question> table;
	@FXML 
	TabPane tabPane=new TabPane();
	@FXML
	Tab queTab=new Tab();
	@FXML
	Tab catTab=new Tab();
	@FXML
	Tab imTab=new Tab();
	@FXML
	Tab exTab=new Tab();
	@FXML
	private TableView<Question> tableAddQuestion;
	@FXML
	private TableView<Question> tableAddQuestionToo;
	@FXML
	private AnchorPane addFromQuestionBankWd;
	@FXML
	private TableColumn<Question, String> nameQuesTableAdd;
	@FXML
	private VBox frameCbbCategory;
	@FXML
	private AnchorPane categoryAddWd;
	@FXML
	private AnchorPane quizAddWd;
	@FXML
	private TableColumn<Question, String> nameColumn;
	@FXML
	private TableColumn<Question, String> nameQuesTableAddToo;
	@FXML
	private TextField categoryNameText;
	@FXML
	private TextField quizNameText;
	@FXML
	private TextField timeLimitText;
	@FXML
	private VBox dragAndDropFile;
	@FXML
	private TableColumn<Question, Void> actionColumn;
	@FXML
	private Label modeLabel;
	@FXML
	private Label numQuestQuiz;
	@FXML
	private Label sumMarkQuestQuiz;
	@FXML
	private TextField questionName;
	@FXML
	private Button btnSaveAndEdit;
	@FXML
	private AnchorPane QuizWd;
	@FXML
	private VBox frameAnswers;
	@FXML
	private AnchorPane importFileWd;
	@FXML
	private AnchorPane createQuestionWd;
	@FXML
	private ComboBox<CategoryItem> comboboxCategory;
	@FXML
	private ComboBox<CategoryItem> cbbCategoryAddQues;
	@FXML
	private ComboBox<Integer> cbbNumRandom;
	@FXML
	private CheckBox checkSubCategories;
	@FXML
	private CheckBox checkSubCategoriesToo;
	@FXML
	private AnchorPane settingQuizWd;
	@FXML
	private AnchorPane choicePreviewQuiz;
	@FXML
	private AnchorPane previewQuizWd;
	@FXML
	private VBox choiceAddQuiz;
	@FXML
	private VBox vboxQuiz;
	@FXML
	private HBox numRandomHbox;
	@FXML
	private FlowPane flowPaneWrapButton;
	@FXML
	private VBox vBoxContentQuiz;
	@FXML
	private AnchorPane vBoxLabelQuiz;
	@FXML
	private VBox vBoxLabelAndContent;
	@FXML
	private VBox vboxTimeAndMark;
	@FXML
	private GridPane timeAndMark;
	@FXML
	private ScrollBar scrBarQuiz;
	@FXML
	private Button btnFinish = new Button("Finish attempt...");
	@FXML
	private Button submitButton;
	private Timeline timeline;
	private Integer startTime = 3600;
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(startTime);
	@FXML
	private HBox wrapCountdownTime;
	@FXML
	private Label hourCountdownLabel;
	@FXML
	private Label minuteCountdownLabel;
	@FXML
	private Label secondCountdownLabel;
	@FXML
	private HBox hhb;
	@FXML
	private Label lb1;
	@FXML
	private Label lb2;
	@FXML
	private Label labelTimeL;
	@FXML
	private Label labelQuizName=new Label("");
	@FXML
	private Label labelEditQuiz=new Label("");
	@FXML
	private Label startLabel;
	private List<ToggleGroup> toggleGroups = new ArrayList<>();
	private List<Label> labelNameTrueAnswer = new ArrayList<>();
	private String modeCreatQuestion = null;
	private String modeAddQuest = null;
//	private ObservableList<Question> questionList;
	private int orNumCategory = 0;
	private int orNumCategoryToo = 0;
	private int orNumQuiz = 0;
	
	private ObservableList<Quiz> quizList = FXCollections.observableArrayList();
	private ObservableList<CategoryItem> categoryList;
	
	private List<TextField> listTextField = new ArrayList<>();
	
	private List<ComboBox<String>> listCombobox = new ArrayList<>();
	
	private ObservableList<String> grade = FXCollections.observableArrayList("1","0");
	
	private Question questionTable = new Question();
	private void addButtonToTable() {
		Callback<TableColumn<Question, Void>, TableCell<Question, Void>> cellFactory = new Callback<TableColumn<Question,Void>, TableCell<Question,Void>>() {

			@Override
			public TableCell<Question, Void> call(final TableColumn<Question, Void> param) {
				final TableCell<Question, Void> cell = new TableCell<Question,Void>(){
					final GlyphIcon<FontAwesomeIcons> icon=new FontAwesomeIcon();
					final Label btnEdit = new Label("Edit",icon);
					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item,empty);
						if(empty) {
							setGraphic(null);
						}else {
							
							icon.setGlyphName("SORT_DOWN");
							icon.setFill(Color.valueOf("#009fe5"));
							btnEdit.setTextFill(Color.valueOf("#009fe5"));
							btnEdit.setContentDisplay(ContentDisplay.RIGHT);
							btnEdit.setPrefWidth(100);
							btnEdit.setAlignment(Pos.CENTER);
							btnEdit.setOnMouseClicked(e->{
								modeLabel.setText("Editing a Multiple choice Question");
								modeCreatQuestion = "edit";
								createQuestionWd.setVisible(true);
								Question question = getTableView().getItems().get(getIndex());
								questionTable = question;
								setQuestion(question);
							}
							);
							setGraphic(btnEdit);
						}
					}
					
				};
				return cell;
			}
		};
		actionColumn.setCellFactory(cellFactory);
	}
	@FXML
	public void openPopUpQuesBank(MouseEvent event) {
		popUpQuestionBank_Pane.setVisible(true);
		popUpQuestionBank.setVisible(true);
	}
	@FXML
	public void openQuesTab(MouseEvent event) {
		firstAnchorPane.setVisible(false);
		popUpQuestionBank_Pane.setVisible(false);
		popUpQuestionBank.setVisible(false);
		tabPane.getSelectionModel().select(queTab);
	}
	public void openCateTab(MouseEvent event) {
		firstAnchorPane.setVisible(false);
		popUpQuestionBank_Pane.setVisible(false);
		popUpQuestionBank.setVisible(false);
		tabPane.getSelectionModel().select(catTab);
	}
	public void openImTab(MouseEvent event) {
		firstAnchorPane.setVisible(false);
		popUpQuestionBank_Pane.setVisible(false);
		popUpQuestionBank.setVisible(false);
		tabPane.getSelectionModel().select(imTab);
	}
	public void openExTab(MouseEvent event) {
		firstAnchorPane.setVisible(false);
		popUpQuestionBank_Pane.setVisible(false);
		popUpQuestionBank.setVisible(false);
		tabPane.getSelectionModel().select(exTab);
	}
	public void closePopUpQuesBank(MouseEvent event) {
		popUpQuestionBank_Pane.setVisible(false);
		popUpQuestionBank.setVisible(false);
	}
	public void createQuestion() {
		modeLabel.setText("Create a Multiple choice Question");
		modeCreatQuestion = "create";
		createQuestionWd.setVisible(true);
		setQuestion(null);
		btnSaveAndEdit.setVisible(true);
	}
	

	
	public void blankMoreChoice(){
		double h = frameAnswers.getPrefHeight();
		h+=200;
		int i = listTextField.size()+1;
		frameAnswers.setPrefHeight(h);
		GridPane gridPane=new GridPane();
		gridPane.getColumnConstraints().add(new ColumnConstraints(10,60,293));
		gridPane.getColumnConstraints().add(new ColumnConstraints(10,536,544));
		gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
		gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
		gridPane.setStyle("-fx-background-color: #f5f5f5");
		Label numAns = new Label("Choice "+i);
		TextField textAns = new TextField();
		Label labelGrade = new Label("Grade");
		ComboBox<String> cbb = new ComboBox<String>();
		cbb.setItems(grade);
		cbb.setValue("none");
		cbb.setPrefWidth(150);
		listTextField.add(textAns);
		listCombobox.add(cbb);		
		gridPane.add(numAns, 0, 0);
		gridPane.add(labelGrade, 0, 1);
		gridPane.add(textAns, 1, 0);
		gridPane.add(cbb, 1, 1);			
		frameAnswers.getChildren().add(gridPane);
	}
	
	public void setQuestion(Question question) {
		if(checkSubCategories.isSelected()) {
			for(int i=0;i<categoryList.size()-1;i++) {
				addOrRemoveSubCate(false, i);
			}
		}		
		if(modeCreatQuestion == "edit"){
		questionName.setText(question.getName());
		List<Answer> answers = question.getAnswers();
		int i=1;
		double h = frameAnswers.getPrefHeight();
		listTextField = new ArrayList<>();
		listCombobox = new ArrayList<>();
		for(Answer answer :answers) {
			h+=200;
			frameAnswers.setPrefHeight(h);
			GridPane gridPane=new GridPane();
			gridPane.getColumnConstraints().add(new ColumnConstraints(10,60,293));
			gridPane.getColumnConstraints().add(new ColumnConstraints(10,536,544));
			gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
			gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
			Label numAns = new Label("Choice "+i);
			TextField textAns = new TextField();
			textAns.setText(answer.getAlphabet());
			Label labelGrade = new Label("Grade");
			ComboBox<String> cbb = new ComboBox<String>();
			cbb.setItems(grade);
			cbb.setValue(answer.getGrade());
			cbb.setPrefWidth(150);
			listTextField.add(textAns);
			listCombobox.add(cbb);		
			gridPane.add(numAns, 0, 0);
			gridPane.add(labelGrade, 0, 1);
			gridPane.add(textAns, 1, 0);
			gridPane.add(cbb, 1, 1);			
			frameAnswers.getChildren().add(gridPane);
			
			i++;
			}
		}
		if(modeCreatQuestion == "create") {
			listTextField = new ArrayList<>();
			listCombobox = new ArrayList<>();
			frameAnswers.setPrefHeight(300);
			GridPane gridPane=new GridPane();
			gridPane.getColumnConstraints().add(new ColumnConstraints(10,60,293));
			gridPane.getColumnConstraints().add(new ColumnConstraints(10,536,544));
			gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
			gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
			Label numAns = new Label("Choice 1");
			TextField textAns = new TextField();
			Label labelGrade = new Label("Grade");
			ComboBox<String> cbb = new ComboBox<String>();
			cbb.setItems(grade);
			cbb.setValue("none");
			cbb.setPrefWidth(150);
			listTextField.add(textAns);
			listCombobox.add(cbb);		
			gridPane.add(numAns, 0, 0);
			gridPane.add(labelGrade, 0, 1);
			gridPane.add(textAns, 1, 0);
			gridPane.add(cbb, 1, 1);			
			frameAnswers.getChildren().add(gridPane);
			
			GridPane gridPane1=new GridPane();
			gridPane1.getColumnConstraints().add(new ColumnConstraints(10,60,293));
			gridPane1.getColumnConstraints().add(new ColumnConstraints(10,536,544));
			gridPane1.getRowConstraints().add(new RowConstraints(10,46,120));
			gridPane1.getRowConstraints().add(new RowConstraints(10,46,120));
			Label numAns1 = new Label("Choice 2");
			TextField textAns1 = new TextField();
			Label labelGrade1 = new Label("Grade");
			ComboBox<String> cbb1 = new ComboBox<String>();
			cbb1.setItems(grade);
			cbb1.setValue("none");
			cbb1.setPrefWidth(150);
			listTextField.add(textAns1);
			listCombobox.add(cbb1);		
			gridPane1.add(numAns1, 0, 0);
			gridPane1.add(labelGrade1, 0, 1);
			gridPane1.add(textAns1, 1, 0);
			gridPane1.add(cbb1, 1, 1);			
			frameAnswers.getChildren().add(gridPane1);
		}
	}
	
	public void cancelEdit() {
		questionName.setText(null);
		ObservableList<Node> hb = frameAnswers.getChildren();
		frameAnswers.getChildren().removeAll(hb);
		frameAnswers.setPrefHeight(0);
		createQuestionWd.setVisible(false);
		btnSaveAndEdit.setVisible(false);
		if(checkSubCategories.isSelected()) {
			for(int i =0;i<categoryList.size()-1;i++) {
				addOrRemoveSubCate(true, i);
			}
		}
		
	}
	
	public void saveAndEdit() {
		Question qs = new Question();
		qs.setName(questionName.getText());
		List<Answer> as = new ArrayList<>();
		for(int i =0; i<listTextField.size();i++) {
			if(listCombobox.get(i).getValue()=="none") listCombobox.get(i).setValue("0");
			Answer a = new Answer(listTextField.get(i).getText(),listCombobox.get(i).getValue());
			as.add(a);
		}
		qs.setAnswers(as);
		categoryList.get(orNumCategory).getCategoryQuestion().add(qs);
		
		ObservableList<Node> hb = frameAnswers.getChildren();
		frameAnswers.getChildren().removeAll(hb);
		questionName.setText(null);
		listTextField = new ArrayList<>();
		frameAnswers.setPrefHeight(300);
		GridPane gridPane=new GridPane();
		gridPane.getColumnConstraints().add(new ColumnConstraints(10,60,293));
		gridPane.getColumnConstraints().add(new ColumnConstraints(10,536,544));
		gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
		gridPane.getRowConstraints().add(new RowConstraints(10,46,120));
		Label numAns = new Label("Choice 1");
		TextField textAns = new TextField();
		Label labelGrade = new Label("Grade");
		ComboBox<String> cbb = new ComboBox<String>();
		cbb.setItems(grade);
		cbb.setValue("none");
		cbb.setPrefWidth(150);
		listTextField.add(textAns);
		listCombobox.add(cbb);		
		gridPane.add(numAns, 0, 0);
		gridPane.add(labelGrade, 0, 1);
		gridPane.add(textAns, 1, 0);
		gridPane.add(cbb, 1, 1);			
		frameAnswers.getChildren().add(gridPane);
		
		GridPane gridPane1=new GridPane();
		gridPane1.getColumnConstraints().add(new ColumnConstraints(10,60,293));
		gridPane1.getColumnConstraints().add(new ColumnConstraints(10,536,544));
		gridPane1.getRowConstraints().add(new RowConstraints(10,46,120));
		gridPane1.getRowConstraints().add(new RowConstraints(10,46,120));
		Label numAns1 = new Label("Choice 2");
		TextField textAns1 = new TextField();
		Label labelGrade1 = new Label("Grade");
		ComboBox<String> cbb1 = new ComboBox<String>();
		cbb1.setItems(grade);
		cbb1.setValue("none");
		cbb1.setPrefWidth(150);
		listTextField.add(textAns1);
		listCombobox.add(cbb1);		
		gridPane1.add(numAns1, 0, 0);
		gridPane1.add(labelGrade1, 0, 1);
		gridPane1.add(textAns1, 1, 0);
		gridPane1.add(cbb1, 1, 1);			
		frameAnswers.getChildren().add(gridPane1);
	}
	
	public void saveChanges() {
		if(modeCreatQuestion == "edit") {
		
		Question qs = new Question();
		qs.setName(questionName.getText());
		List<Answer> as = new ArrayList<>();

		for(int i =0;i<listTextField.size();i++) {
			if(listCombobox.get(i).getValue().equals("none")) listCombobox.get(i).setValue("0");
			Answer a = new Answer(listTextField.get(i).getText(),listCombobox.get(i).getValue());

			as.add(a);
		}
		qs.setAnswers(as);
		boolean checkQ = true;
		for(int i =0;i<categoryList.get(orNumCategory).getCategoryQuestion().size();i++) {
			if(categoryList.get(orNumCategory).getCategoryQuestion().get(i).equals(questionTable)) {
				categoryList.get(orNumCategory).getCategoryQuestion().set(i, qs);
				checkQ = false;
				break;
			}
		}
		if(checkQ) {
			JOptionPane.showMessageDialog(null, "Khong the chinh sua cau hoi tu subquestion");
		}
		ObservableList<Node> hb = frameAnswers.getChildren();
		questionName.setText(null);
		frameAnswers.getChildren().removeAll(hb);
		frameAnswers.setPrefHeight(0);
		createQuestionWd.setVisible(false);
		}
		if(modeCreatQuestion == "create") {
			ObservableList<Node> hb = frameAnswers.getChildren();
			frameAnswers.getChildren().removeAll(hb);
			frameAnswers.setPrefHeight(0);
			Question qs = new Question();
			qs.setName(questionName.getText());
			String fixNone = "1";
			List<Answer> as = new ArrayList<>();
			for(int i =0;i<listTextField.size();i++) {
				if(listCombobox.get(i).getValue().equals("none")) {
					fixNone= "0";
				}
				Answer a = new Answer(listTextField.get(i).getText(),fixNone);
				as.add(a);
				fixNone="1";
			}
			qs.setAnswers(as);
			questionName.setText(null);
			categoryList.get(orNumCategory).getCategoryQuestion().add(qs);
			createQuestionWd.setVisible(false);
			btnSaveAndEdit.setVisible(false);
		}
		if(checkSubCategories.isSelected()) {
			for(int i =0;i<categoryList.size()-1;i++) {
				addOrRemoveSubCate(true, i);
			}
		}
		
	}
	
	public void handleFile(File file) {
		try {
			if(checkSubCategories.isSelected()) {
				for(int i =0;i<categoryList.size()-1;i++) {
					addOrRemoveSubCate(false, i);
				}
			}
			int lineNum=1,questionNum=0,answerNum=0;
			InputStream is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(isr);
			List<Question> questionsInFile = new ArrayList<>();
			while(true) {
				Question question = new Question();
				List<Answer> as = new ArrayList<>();
				List<Character> alphaAnswer = new ArrayList<>();
				String line = reader.readLine();
				if(line==null) {
					break;
				}
				lineNum++; 
				question.setName(line);
				
				line= reader.readLine();
				
				while(line.charAt(1)=='.'&&line.charAt(2)==' ') {
					if(line.length()<4||line==null) {
						JOptionPane.showMessageDialog(null, "Error at "+lineNum);
						return ;
					}
					
					answerNum++;
					alphaAnswer.add(line.charAt(0));
					String answerInLine = "";
					for(int d = 3;d<line.length();d++) {
						answerInLine+=line.charAt(d);
					}
					Answer answer = new Answer(answerInLine,"0");
					as.add(answer);
					
					lineNum++;
					line= reader.readLine();
					
				}
				if(answerNum<2) {
					JOptionPane.showMessageDialog(null, "Error at "+lineNum);
					return ;
				}
				
				if(!checkNotAnswer(line)) {
					JOptionPane.showMessageDialog(null, "Error at "+lineNum);
					return ;
				}
				char realAnswer = line.charAt(8);
				for(int i=0;i<alphaAnswer.size();i++) {
					if(realAnswer==alphaAnswer.get(i)) {
						as.get(i).setGrade("1");
						break;
					}
				}
				question.setAnswers(as);
				questionsInFile.add(question);
				questionNum++;
				lineNum++;
				line = reader.readLine();
				if(line != null ) {
					if(line.length()!=0) {
						JOptionPane.showMessageDialog(null, "Error at "+lineNum);
						return;
					}
				}
				answerNum=0;
				
			}
			for(Question q : questionsInFile) {
				categoryList.get(orNumCategory).getCategoryQuestion().add(q);
			}
			JOptionPane.showMessageDialog(null, "Success "+questionNum);
			if(checkSubCategories.isSelected()) {
				for(int i =0;i<categoryList.size()-1;i++) {
					addOrRemoveSubCate(true, i);
				}
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	public void chooseFileTxt(ActionEvent e) {
		Node btn = (Node)e.getSource();
		Stage stage = (Stage)btn.getScene().getWindow();
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose file txt");
		File file = fc.showOpenDialog(stage);
		if(file!=null) {
			String filePath = file.toString();
			int n = filePath.length();
			String format="";
			format+=filePath.charAt(n-1);
			format+=filePath.charAt(n-2);
			format+=filePath.charAt(n-3);
			if(!format.equals("txt")) {
				JOptionPane.showMessageDialog(null, "WRONG FORMAT");
			}else {
				handleFile(file);
			}
		}
	}
	public boolean checkNotAnswer(String answer) {
		if(answer.length()!=9) return false;
		else {
			if(answer.charAt(0)!='A') return false;
			if(answer.charAt(1)!='N') return false;
			if(answer.charAt(2)!='S') return false;
			if(answer.charAt(3)!='W') return false;
			if(answer.charAt(4)!='E') return false;
			if(answer.charAt(5)!='R') return false;
			if(answer.charAt(6)!=':') return false;
			if(answer.charAt(7)!=' ') return false;
			return true;
		}
	}
	/*public void openImportFileWd() {
		importFileWd.setVisible(true);
	}
	public void closeImportFileWd() {
		//importFileWd.setVisible(false);
	}*/
	public void openCategoryAddWd() {
		ComboBox<CategoryItem> q = new ComboBox<CategoryItem>();
		q.setItems(categoryList);
		q.setPrefHeight(44);
		q.setPrefWidth(251);
		q.setValue(categoryList.get(0));
		//categoryAddWd.setVisible(true);
		frameCbbCategory.getChildren().add(q);
	}
	public void closeCategoryAddWd() {
		categoryAddWd.setVisible(false);
		ObservableList<Node> q = frameCbbCategory.getChildren();
		frameCbbCategory.getChildren().removeAll(q);
		categoryNameText.setText(null);
	}
	public void addCategoryToCbb(ActionEvent event){
		String nameCategory = categoryNameText.getText();
		ObservableList<Node> q = frameCbbCategory.getChildren(); 
		@SuppressWarnings("unchecked")
		ComboBox<CategoryItem> qq =(ComboBox<CategoryItem>) q.get(0);
		CategoryItem cate = qq.getValue();
		int num = 0;
		for(CategoryItem ci : categoryList) {
			if(cate.equals(ci)) {
				break;
			}num++;
		}
		int numSpace = 0;
		String name = categoryList.get(num).getCategory();
		for(int i = 0;i<name.length();i++) {
			if(name.charAt(i)==' ') {
				numSpace++;
			}else {
				break;
			}
		}
		String newName = "";
		if(cate.getCategory()=="Default") {
			newName = nameCategory;
		}else {
			for(int i =0;i<numSpace;i++) {
				newName+=" ";
			}
			newName+="    ";
			newName+=nameCategory;
		}
		categoryList.add(num+1, new CategoryItem(newName));
		tabPane.getSelectionModel().select(queTab);
		frameCbbCategory.getChildren().removeAll(q);
		categoryNameText.setText(null);
	}
	public void showSubCategories(ActionEvent event) {
		if(checkSubCategories.isSelected()) {
			checkSubCategoriesToo.setSelected(true);
			for(int i =0;i<categoryList.size()-1;i++) {
				addOrRemoveSubCate(true, i);
			}
		}else {
			checkSubCategoriesToo.setSelected(false);
			for(int i=0;i<categoryList.size()-1;i++) {
				addOrRemoveSubCate(false, i);
			}
		}
		table.setItems(categoryList.get(orNumCategory).getCategoryQuestion());
		addButtonToTable();
	}
	
	public void addOrRemoveSubCate(boolean check, int orNumCate) {
		int numSpaceNow = categoryList.get(orNumCate).getCategory().length() - categoryList.get(orNumCate).getCategory().trim().length();
		
		if(check) {
			for(int i = orNumCate+1;i<categoryList.size();i++) {
				int numSpace = categoryList.get(i).getCategory().length() - categoryList.get(i).getCategory().trim().length();
				if(numSpace <= numSpaceNow) break;
				else {
					categoryList.get(orNumCate).getCategoryQuestion().addAll(categoryList.get(i).getCategoryQuestion());
				}
			}
		}else {
			for(int i = orNumCate+1;i<categoryList.size();i++) {
				int numSpace = categoryList.get(i).getCategory().length() - categoryList.get(i).getCategory().trim().length();
				if(numSpace <= numSpaceNow) break;
				else {
					categoryList.get(orNumCate).getCategoryQuestion().removeAll(categoryList.get(i).getCategoryQuestion());
				}
			}

		}
	}
	public void showAddQuizWd(ActionEvent event) {
		quizAddWd.setVisible(true);
	}
	public void addQuizToVbox(ActionEvent event){
		
		String nameQuiz = quizNameText.getText();
		int timeLimit=Integer.parseInt(timeLimitText.getText());
		
		quizList.add(new Quiz(nameQuiz,timeLimit));
		Region stackicon = GlyphsStack.create()
                .add(GlyphsBuilder.create(FontAwesomeIcon.class)
                                .glyph(FontAwesomeIcons.FILE)
                                .size("24px")
                                .style("-fx-fill:#cdf1ff;-fx-stroke:#005eff")
                                .build()
                )
                .add(GlyphsBuilder.create(FontAwesomeIcon.class)
                                .glyph(FontAwesomeIcons.CHECK)
                                .style("-fx-fill:#ff2f00")
                                .size("18px")
                                .build()
                );
		Text quizNameText1 = new Text();
		quizNameText1.setText(nameQuiz);
		quizNameText1.setStyle("-fx-font-size:24px");
		HBox hBox=new HBox(5,stackicon,quizNameText1);
		hBox.setOnMouseClicked(e->{
			for(int i=0;i<quizList.size();i++){
				 if(quizList.get(i).getNameQuiz().equals(nameQuiz)){
					 orNumQuiz = i; // Luu vu tri cua Quiz vua chon
				 	startTime = quizList.get(i).getTimeQuiz()*60;// Sua lai thoi gian lam bai
				 	
				 	break;
				 	}
			}
			
			//modeAddQuest="quiz";
			QuizWd.setVisible(true);
			labelQuizName.setText(nameQuiz);
			labelTimeL.setText("Time limit: "+startTime/60+" minutes"); 
			tableAddQuestionToo.setItems(quizList.get(orNumQuiz).getQuestionQuiz());
			numQuestQuiz.setText("Question: "+quizList.get(orNumQuiz).getQuestionQuiz().size());
			sumMarkQuestQuiz.setText("Total of marks: "+quizList.get(orNumQuiz).getQuestionQuiz().size()+".00");
			
			
		});
		vboxQuiz.getChildren().add(hBox);
		quizNameText.setText(null);
		timeLimitText.setText(null);
		quizAddWd.setVisible(false);
		firstAnchorPane.setVisible(true);
	}
	public void openFirstPane(MouseEvent event) {
		//if(tabAnchorPane.isVisible()) tabAnchorPane.setVisible(false);
		if(quizAddWd.isVisible()) quizAddWd.setVisible(false);
		if(QuizWd.isVisible()) QuizWd.setVisible(false);
		if(settingQuizWd.isVisible()) settingQuizWd.setVisible(false);
		firstAnchorPane.setVisible(true);
	}
	public void cancelAddQuiz(ActionEvent event) {
		quizAddWd.setVisible(false);
		quizNameText.setText(null);
		timeLimitText.setText(null);
		// them nhung code xoa cac muc nhu nameQuiz voi timeQuiz di
	}
	public void openQuizWd(MouseEvent event) {
		QuizWd.setVisible(true);
	}
	public void closeQuizWd(ActionEvent event) {
		QuizWd.setVisible(false);
	}
	public void openSettingQuizWd(MouseEvent event) {
		settingQuizWd.setVisible(true);
		labelEditQuiz.setText("Editting quiz: "+labelQuizName.getText());
		tableAddQuestionToo.setItems(quizList.get(orNumQuiz).getQuestionQuiz());
	}
	public void closeSettingQuizWd(MouseEvent event) {
		settingQuizWd.setVisible(false);
		choiceAddQuiz.setVisible(false);
	}
	public void openOrCloseChoiceAdd(MouseEvent event) {
		boolean check = choiceAddQuiz.isVisible();
		if(check) {
			choiceAddQuiz.setVisible(false);
		}else {
			choiceAddQuiz.setVisible(true);
		}
	}
	public void openAddFromQuestionBankWd(MouseEvent event) {
		addFromQuestionBankWd.setVisible(true);
		modeAddQuest = "select";
		numRandomHbox.setVisible(false);
	}
	public void openAddFromQuestionBankRandomWd(MouseEvent event) {
		addFromQuestionBankWd.setVisible(true);
		modeAddQuest = "random";
		numRandomHbox.setVisible(true);
	}
	
	public void closeAddFromQuestionBankWd(MouseEvent event) {
		addFromQuestionBankWd.setVisible(false);
		cbbCategoryAddQues.setValue(null); 
		tableAddQuestion.setItems(null);
		choiceAddQuiz.setVisible(false);
		cbbNumRandom.setItems(null);
	}
	public void showSubCategoriesToo(ActionEvent event) {
		if(checkSubCategoriesToo.isSelected()) {
			checkSubCategories.setSelected(true);
			for(int i =0;i<categoryList.size()-1;i++) {
				addOrRemoveSubCate(true, i);
			}
		}else {
			checkSubCategories.setSelected(false);
			for(int i=0;i<categoryList.size()-1;i++) {
				addOrRemoveSubCate(false, i);
			}
		}
		tableAddQuestion.setItems(categoryList.get(orNumCategoryToo).getCategoryQuestion());
	}
	public void addSelectedQuestion(ActionEvent event ){
		if(modeAddQuest=="select") {
			if(cbbCategoryAddQues.getValue()==null) {
				JOptionPane.showMessageDialog(null, "Chua chon category");
				return;
			}

			tableAddQuestionToo.setItems(categoryList.get(orNumCategoryToo).getCategoryQuestion());
			int numQuestionQuiz = categoryList.get(orNumCategoryToo).getNumQuest();
			quizList.get(orNumQuiz).setQuestionQuiz(tableAddQuestionToo.getItems());
			numQuestQuiz.setText("Question: "+quizList.get(orNumQuiz).getQuestionQuiz().size());
			sumMarkQuestQuiz.setText("Total of marks: "+quizList.get(orNumQuiz).getQuestionQuiz().size()+".00");
		}
		else
		{
			ObservableList<Question> rdQues = FXCollections.observableArrayList();
			int maxNumRd = cbbCategoryAddQues.getValue().getCategoryQuestion().size();
			for(int i = 1;i<=cbbNumRandom.getValue();i++) {
				int random = ThreadLocalRandom.current().nextInt(1,maxNumRd);
				rdQues.add(categoryList.get(orNumCategoryToo).getCategoryQuestion().get(random));
			}
			tableAddQuestionToo.setItems(rdQues);
			quizList.get(orNumQuiz).setQuestionQuiz(tableAddQuestionToo.getItems());
			numQuestQuiz.setText("Question: "+quizList.get(orNumQuiz).getQuestionQuiz().size());
			sumMarkQuestQuiz.setText("Total of marks: "+quizList.get(orNumQuiz).getQuestionQuiz().size()+".00");
			
		}
		choiceAddQuiz.setVisible(false);
		cbbNumRandom.setItems(null);
		addFromQuestionBankWd.setVisible(false);
		tableAddQuestion.setItems(null);
		cbbCategoryAddQues.setValue(null);
	}
	public void saveEditQuestion() {
		quizList.get(orNumQuiz).setQuestionQuiz(tableAddQuestionToo.getItems());
		settingQuizWd.setVisible(false);
		choiceAddQuiz.setVisible(false);
	}
	public void openChoicePreviewQuiz(ActionEvent event) {
		startLabel.setText("Your attempt will have a time limit of "+startTime/60+" minutes. When you start, the timer will begin to count down and cannot be paused. You must finish your attempt before it expires. Are you sure you wish to start now?");
		choicePreviewQuiz.setVisible(true);
	}
	public void closeChoicePreviewQuiz(MouseEvent event) {
		choicePreviewQuiz.setVisible(false);
	}
	public void cancelChoicePreviewQuiz(ActionEvent event) {
		choicePreviewQuiz.setVisible(false);
	}
	public void openPreviewQuiz(ActionEvent event) {
		Stage st = (Stage)((Node)event.getSource()).getScene().getWindow();
		st.setWidth(1500);
		st.centerOnScreen();
		int j = quizList.get(orNumQuiz).getQuestionQuiz().size();
		scrBarActive(j);
		double dist=-1.0;
		vBoxLabelAndContent.setPrefHeight(dist);	
		for(int i = 0 ; i < j;i++) {
			Button b = new Button(Integer.toString(i+1));
			flowPaneWrapButton.getChildren().add(b);
			b.setStyle("-fx-background-color:white;"+"-fx-border-color:grey;");
			b.setPrefWidth(37);
			b.setOnAction(e->{
				if(quizList.get(orNumQuiz).getQuestionQuiz().size()>=3) {
					int ii = (Integer.parseInt(b.getText())-1)*200;
					if(ii<=scrBarQuiz.getMax()) {
						scrBarQuiz.setValue(ii);
						return;
					}scrBarQuiz.setValue(scrBarQuiz.getMax());
				}
			});
			VBox vBoxLabel=new VBox();
			vBoxLabel.setMaxSize(110, 100);
			vBoxLabel.setBackground(new Background(new BackgroundFill(Color.valueOf("#f8f9fa"), CornerRadii.EMPTY, Insets.EMPTY)));
			vBoxLabel.setStyle("-fx-border-color:#edeff2");
			Label lbNumQuest=new Label("Question "+(i+1));
			lbNumQuest.setPrefWidth(110);
			lbNumQuest.setStyle("-fx-font-size:16px;"+"-fx-font-weight:BOLD;");
			lbNumQuest.setAlignment(Pos.CENTER);
			lbNumQuest.setTextFill(Color.RED);
			Label stateLabel=new Label("Not yet answered");
			stateLabel.setPrefWidth(110);
			stateLabel.setAlignment(Pos.CENTER);
			vBoxLabel.getChildren().addAll(lbNumQuest,stateLabel,new  Label(" Marked out of 1.00"),new Label(" Flag question",GlyphsDude.createIcon(FontAwesomeIcons.FLAG_ALT,"12px")));
			VBox vBoxContent = new VBox();
			vBoxContent.setPadding(new Insets(10, 10, 10, 10));
			vBoxContent.setBackground(new Background(new BackgroundFill(Color.valueOf("#e7f3f5"), null, null)));
			//vBoxContent.setPrefHeight(200); //setLaterToo
			vBoxContent.setPrefSize(930, dist);
			Question q = quizList.get(orNumQuiz).getQuestionQuiz().get(i);
			Label lb=new Label(q.getName());
			lb.setWrapText(true);
			vBoxContent.getChildren().add(lb);
			vBoxContent.setSpacing(3);
			ToggleGroup toggleGroup = new ToggleGroup();
			HBox hb = new HBox();
			hb.setPadding(new Insets(10, 10, 10, 10));
			hb.setPrefHeight(50);
			hb.setBackground(new Background(new BackgroundFill(Color.valueOf("#fcefdc"), null, null)));
			Label crlB=new Label("The correct answer is:");
			crlB.setMinWidth(124);
			hb.getChildren().add(crlB);
			Label lbNameTrueAns = new Label();
			lbNameTrueAns.setWrapText(true);
			for(Answer a : q.getAnswers()) {
				RadioButton rdB = new RadioButton(a.getAlphabet());
				rdB.setWrapText(true);
				rdB.setToggleGroup(toggleGroup);
				rdB.setOnAction(e->{
					if (rdB.isSelected()) { 
						b.setStyle("-fx-background-color:lightgrey;"+"-fx-border-color:grey;");
						stateLabel.setText("Answer saved");
					}
				});
				vBoxContent.getChildren().add(rdB);
				if(a.getGrade()=="1") {
					lbNameTrueAns.setText(a.getAlphabet());
				
				}
			}
			hb.setPrefSize(930, dist);
			vBoxContent.setPrefHeight(dist);
			hb.setVisible(false);
			hb.getChildren().add(lbNameTrueAns);
			VBox ct=new VBox(3, vBoxContent,hb);
			HBox wrapBox=new HBox();
			wrapBox.setPrefSize(1060, 200);
			wrapBox.getChildren().addAll(vBoxLabel,ct);
			wrapBox.setSpacing(20);
			vBoxLabelAndContent.getChildren().add(wrapBox);
			vBoxLabelAndContent.setSpacing(0);
			toggleGroups.add(toggleGroup);
			
			
			labelNameTrueAnswer.add(lbNameTrueAns);
		}
		btnFinish.setText("Finish attempt...");
		btnFinish.setStyle("-fx-background-color:white");
		btnFinish.setAlignment(Pos.BASELINE_LEFT);
		
		flowPaneWrapButton.getChildren().add(btnFinish);
		flowPaneWrapButton.setStyle("-fx-border-color:lightgrey");
		previewQuizWd.setVisible(true);
		choicePreviewQuiz.setVisible(false);

		// Tao dong ho dem nguoc
		wrapCountdownTime.setVisible(true);
		if(timeline!=null) {
			timeline.stop();
		}
		timeSeconds.set(startTime);
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(startTime+1),
				new KeyValue(timeSeconds, 0)
						)
				);
		timeline.playFromStart();
	}
	
	// Ham xu ly su kien nhan nut finish attempt

	public void eventFinishAttempt(Button btnChoice) {
		if(confirmAnchor.isVisible()) {
			confirmAnchor.setVisible(false);
			confirmPane.setVisible(false);
		}
		if(btnChoice.getText().equals("Finish attempt...")) {
			btnChoice.setText("Finish review");
			int mark = 0, j = quizList.get(orNumQuiz).getQuestionQuiz().size();
			for(int i = 0 ; i < j;i++) {
				String a = labelNameTrueAnswer.get(i).getText();
				if(toggleGroups.get(i).getSelectedToggle() == null) {
					
					a+=" ( Not answered! )";
					labelNameTrueAnswer.get(i).setText(a);
					HBox hb =(HBox)labelNameTrueAnswer.get(i).getParent();
					hb.setVisible(true);
				}else {
					String b = ((RadioButton)toggleGroups.get(i).getSelectedToggle()).getText();
					if(b.equals(a)) {
						mark++;
						a+=" ( True! )";
						labelNameTrueAnswer.get(i).setText(a);
						HBox hb = (HBox)labelNameTrueAnswer.get(i).getParent();
						hb.setVisible(true);
					}else {
						a+=" ( False! )";
						labelNameTrueAnswer.get(i).setText(a);
						HBox hb = (HBox)labelNameTrueAnswer.get(i).getParent();
						hb.setVisible(true);
					}
		
				}
			}
			double grade = (double)mark/j*10;
			double gradeFix = (double)Math.round(grade*100)/100;
			
			int realT = startTime - timeSeconds.get();
			
			hhb.setSpacing(1);
			int realH = realT/3600;
			if(realH>0&&realH<10) {
				hhb.getChildren().add(new Label("0"+realH+" hours "));
			}else {
				hhb.getChildren().add(new Label(""));
			}
			int realM = (realT - realH*3600)/60;
			if(realM>0&&realM<10) {
				hhb.getChildren().add(new Label("0"+realM+" mins "));
			}else {
				if(realM>=10) {
					hhb.getChildren().add(new Label(realM+" mins "));
				}
			}
			int realS = realT - realH*3600 - realM*60;
			if(realS>0&&realS<10) {
				hhb.getChildren().add(new Label("0"+realS+" secs"));
			}else {
				if(realS>=10) {
					hhb.getChildren().add(new Label(realS+" secs"));
				}else {
					hhb.getChildren().add(new Label("00 secs"));
				}
			}
			lb1.setText(mark+".00/"+j+".00");
			lb2.setText(gradeFix+" out of 10.00"+" ("+(gradeFix*10)+"%)");	
			timeAndMark.setVisible(true);
			timeline.stop();
			wrapCountdownTime.setVisible(false);
		}else {
			Stage st = (Stage)btnChoice.getScene().getWindow();
			st.setWidth(1000);
			st.centerOnScreen();
			previewQuizWd.setVisible(false);
			toggleGroups = new ArrayList<>();
			labelNameTrueAnswer = new ArrayList<>();
			ObservableList<Node> bttt = flowPaneWrapButton.getChildren();
			flowPaneWrapButton.getChildren().removeAll(bttt);
			ObservableList<Node> bttt1 = vBoxLabelAndContent.getChildren();
			vBoxLabelAndContent.getChildren().removeAll(bttt1);
			vBoxLabelAndContent.setPrefHeight(0);
			vBoxLabelAndContent.setLayoutY(136);
			ObservableList<Node> bttt3 = hhb.getChildren();
			hhb.getChildren().removeAll(bttt3);
			timeAndMark.setVisible(false);
			firstAnchorPane.setVisible(true);
			QuizWd.setVisible(false);
			//vboxTimeAndMark.getChildren().add(wrapCountdownTime);
		}
	}
	public void submitAll(ActionEvent event) {
		confirmAnchor.setVisible(false);
		confirmPane.setVisible(false);
		previewQuizWd.setVisible(false);
	}
	public void cancelSubmit(ActionEvent event) {
		confirmAnchor.setVisible(false);
		confirmPane.setVisible(false);
	}
	public void scrBarActive(int numofQuest) {
		if(numofQuest<=2) {
			scrBarQuiz.setVisible(false);
			return;
		}
		else {
			scrBarQuiz.setVisible(true);
			scrBarQuiz.setMin(0);
			scrBarQuiz.setMax((numofQuest-2)*200);
			previewQuizWd.setOnScroll((ScrollEvent event)->{
				double a = scrBarQuiz.getValue();
				if(event.getDeltaY()>0) {
					if(a-50 < 0) {
						scrBarQuiz.setValue(0);
						return;
					}else {
						scrBarQuiz.setValue(a-50);
						return;
					}
				}else {
					if(a+50 > scrBarQuiz.getMax()) {
						scrBarQuiz.setValue(scrBarQuiz.getMax());
						return;
					}else {
						scrBarQuiz.setValue(a+50);
						return;
					}
				}
			});
			scrBarQuiz.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					vBoxLabelAndContent.setLayoutY(136-(double) arg2);
						
				}
			});
		}
	}
}

	