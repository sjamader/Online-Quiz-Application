package quiz.app;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizManagerDAO {

    // 1. Create a Quiz
    public void createQuiz(String quizId, String title) {
        String query = "INSERT INTO quizzes (quiz_id, title) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, quizId);
            pst.setString(2, title);
            pst.executeUpdate();
            System.out.println("Quiz created and saved successfully in database: " + title);

        } catch (SQLException e) {
            System.out.println("Error creating quiz: " + e.getMessage());
        }
    }

    // 2. Add a Question with Options and Correct Answer to a Quiz
    public void addQuestionToQuiz(String quizId, String questionTitle, List<String> options, int correctIndex) {
        String questionQuery = "INSERT INTO questions (quiz_id, question_title, correct_option_index) VALUES (?, ?, ?)";
        String optionQuery = "INSERT INTO options (question_id, option_text) VALUES (?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false); // Transaction start

            try (PreparedStatement qPst = con.prepareStatement(questionQuery, Statement.RETURN_GENERATED_KEYS)) {
                qPst.setString(1, quizId);
                qPst.setString(2, questionTitle);
                qPst.setInt(3, correctIndex);
                qPst.executeUpdate();

                // Get the generated question_id
                ResultSet rs = qPst.getGeneratedKeys();
                if (rs.next()) {
                    int questionId = rs.getInt(1);

                    // Insert options
                    try (PreparedStatement oPst = con.prepareStatement(optionQuery)) {
                        for (String option : options) {
                            oPst.setInt(1, questionId);
                            oPst.setString(2, option);
                            oPst.addBatch();
                        }
                        oPst.executeBatch();
                    }
                }
                con.commit(); // Commit transaction
                System.out.println("Question added and saved to database successfully.");
            } catch (SQLException e) {
                con.rollback();
                System.out.println("Transaction rolled back due to error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    // 3. Edit Quiz Title
    public void updateQuizTitle(String quizId, String newTitle) {
        String query = "UPDATE quizzes SET title = ? WHERE quiz_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, newTitle);
            pst.setString(2, quizId);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Quiz title updated successfully in database.");
            } else {
                System.out.println("Quiz ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating quiz: " + e.getMessage());
        }
    }

    // 4. Delete a Quiz
    public void deleteQuiz(String quizId) {
        String query = "DELETE FROM quizzes WHERE quiz_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, quizId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Quiz deleted successfully from database.");
            } else {
                System.out.println("Quiz ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting quiz: " + e.getMessage());
        }
    }

    // 5. View Quiz Details from MySQL
    public void viewQuiz(String quizId) {
        String quizQuery = "SELECT title FROM quizzes WHERE quiz_id = ?";
        String questionQuery = "SELECT question_id, question_title, correct_option_index FROM questions WHERE quiz_id = ?";
        String optionQuery = "SELECT option_text FROM options WHERE question_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            // Get Quiz Title
            try (PreparedStatement qPst = con.prepareStatement(quizQuery)) {
                qPst.setString(1, quizId);
                ResultSet rsQuiz = qPst.executeQuery();
                if (rsQuiz.next()) {
                    System.out.println("\nQuiz: " + rsQuiz.getString("title") + " (ID: " + quizId + ")");
                } else {
                    System.out.println("Quiz not found!");
                    return;
                }
            }

            // Get Questions
            try (PreparedStatement questPst = con.prepareStatement(questionQuery)) {
                questPst.setString(1, quizId);
                ResultSet rsQuestions = questPst.executeQuery();

                int qNum = 1;
                while (rsQuestions.next()) {
                    int questionId = rsQuestions.getInt("question_id");
                    System.out.println("  Q" + qNum + ": " + rsQuestions.getString("question_title"));

                    // Get Options for this question
                    try (PreparedStatement optPst = con.prepareStatement(optionQuery)) {
                        optPst.setInt(1, questionId);
                        ResultSet rsOptions = optPst.executeQuery();

                        int optNum = 1;
                        while (rsOptions.next()) {
                            System.out.println("    " + optNum + ". " + rsOptions.getString("option_text"));
                            optNum++;
                        }
                    }
                    qNum++;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving quiz: " + e.getMessage());
        }
    }

    // Get all available quizzes (Returns a Map of QuizID -> Title)
    public java.util.Map<String, String> getAllQuizzes() {
        java.util.Map<String, String> quizzes = new java.util.LinkedHashMap<>();
        String query = "SELECT quiz_id, title FROM quizzes";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                quizzes.put(rs.getString("quiz_id"), rs.getString("title"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching quizzes: " + e.getMessage());
        }
        return quizzes;
    }

    // Get all questions for a specific quiz
    public List<QuestionModel> getQuestionsForQuiz(String quizId) {
        List<QuestionModel> questions = new ArrayList<>();
        String questionQuery = "SELECT question_id, question_title, correct_option_index FROM questions WHERE quiz_id = ?";
        String optionQuery = "SELECT option_text FROM options WHERE question_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement qPst = con.prepareStatement(questionQuery)) {

            qPst.setString(1, quizId);
            ResultSet rsQuestions = qPst.executeQuery();

            while (rsQuestions.next()) {
                int questionId = rsQuestions.getInt("question_id");
                String qTitle = rsQuestions.getString("question_title");
                int correctIndex = rsQuestions.getInt("correct_option_index");

                List<String> options = new ArrayList<>();
                try (PreparedStatement optPst = con.prepareStatement(optionQuery)) {
                    optPst.setInt(1, questionId);
                    ResultSet rsOptions = optPst.executeQuery();
                    while (rsOptions.next()) {
                        options.add(rsOptions.getString("option_text"));
                    }
                }
                questions.add(new QuestionModel(questionId, qTitle, options, correctIndex));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
        return questions;
    }

}


