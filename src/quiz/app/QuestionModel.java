package quiz.app;


import java.util.List;

public class QuestionModel {
    private int questionId;
    private String questionTitle;
    private List<String> options;
    private int correctIndex;

    public QuestionModel(int questionId, String questionTitle, List<String> options, int correctIndex) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public int getQuestionId() { return questionId; }
    public String getQuestionTitle() { return questionTitle; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
}
