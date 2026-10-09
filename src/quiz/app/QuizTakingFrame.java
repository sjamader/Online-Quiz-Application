package quiz.app;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class QuizTakingFrame extends JFrame {
    private QuizManagerDAO manager;
    private JComboBox<String> cmbQuizzes;
    private JButton btnStart, btnSubmit;
    private JLabel lblQuestionTitle, lblFeedback;
    private JRadioButton[] rbOptions;
    private ButtonGroup optionGroup;

    private List<QuestionModel> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String selectedQuizId;

    public QuizTakingFrame() {
        manager = new QuizManagerDAO();

        setTitle("Take a Quiz - User Portal");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL: Topic Selection ---
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelTop.add(new JLabel("Select Quiz Topic:"));

        cmbQuizzes = new JComboBox<>();
        loadQuizDropdown();
        panelTop.add(cmbQuizzes);

        btnStart = new JButton("Start Quiz");
        panelTop.add(btnStart);
        add(panelTop, BorderLayout.NORTH);

        // --- CENTER PANEL: Question & Options (Initially Hidden/Empty) ---
        JPanel panelQuizBody = new JPanel();
        panelQuizBody.setLayout(new BoxLayout(panelQuizBody, BoxLayout.Y_AXIS));
        panelQuizBody.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        lblQuestionTitle = new JLabel("Please select a quiz and click 'Start Quiz'.");
        lblQuestionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        panelQuizBody.add(lblQuestionTitle);
        panelQuizBody.add(Box.createRigidArea(new Dimension(0, 15)));

        rbOptions = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            rbOptions[i] = new JRadioButton();
            rbOptions[i].setVisible(false);
            optionGroup.add(rbOptions[i]);
            panelQuizBody.add(rbOptions[i]);
            panelQuizBody.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        panelQuizBody.add(Box.createRigidArea(new Dimension(0, 15)));
        lblFeedback = new JLabel("");
        lblFeedback.setFont(new Font("Arial", Font.BOLD, 13));
        panelQuizBody.add(lblFeedback);

        add(panelQuizBody, BorderLayout.CENTER);

        // --- BOTTOM PANEL: Submit Button ---
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSubmit = new JButton("Submit Answer");
        btnSubmit.setEnabled(false);
        panelBottom.add(btnSubmit);
        add(panelBottom, BorderLayout.SOUTH);

        // --- ACTION LISTENERS ---

        // 1. Start Quiz Action
        btnStart.addActionListener(e -> {
            String selectedItem = (String) cmbQuizzes.getSelectedItem();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this, "No quizzes available!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Extract quiz_id from dropdown text (Format: "Title (ID)")
            selectedQuizId = selectedItem.substring(selectedItem.lastIndexOf("(") + 1, selectedItem.lastIndexOf(")"));
            questionList = manager.getQuestionsForQuiz(selectedQuizId);

            if (questionList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "This quiz has no questions yet!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            currentQuestionIndex = 0;
            score = 0;
            cmbQuizzes.setEnabled(false);
            btnStart.setEnabled(false);
            btnSubmit.setEnabled(true);
            displayCurrentQuestion();
        });

        // 2. Submit Answer Action
        btnSubmit.addActionListener(e -> {
            int selectedOptionIndex = -1;
            for (int i = 0; i < 4; i++) {
                if (rbOptions[i].isSelected()) {
                    selectedOptionIndex = i;
                    break;
                }
            }

            if (selectedOptionIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select an answer!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            QuestionModel currentQ = questionList.get(currentQuestionIndex);

            // Immediate feedback check
            if (selectedOptionIndex == currentQ.getCorrectIndex()) {
                lblFeedback.setText("<html><font color='green'>✔ Correct!</font></html>");
                score++;
            } else {
                lblFeedback.setText("<html><font color='red'>✖ Incorrect! Correct answer was option " + (currentQ.getCorrectIndex() + 1) + "</font></html>");
            }

            // Disable options temporarily or move to next after short pause / button click
            Timer timer = new Timer(1500, evt -> {
                currentQuestionIndex++;
                lblFeedback.setText("");
                if (currentQuestionIndex < questionList.size()) {
                    displayCurrentQuestion();
                } else {
                    JOptionPane.showMessageDialog(this, "Quiz Finished!\nYour Score: " + score + " / " + questionList.size(), "Results", JOptionPane.INFORMATION_MESSAGE);
                    resetQuizState();
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void loadQuizDropdown() {
        Map<String, String> quizzes = manager.getAllQuizzes();
        cmbQuizzes.removeAllItems();
        for (Map.Entry<String, String> entry : quizzes.entrySet()) {
            cmbQuizzes.addItem(entry.getValue() + " (" + entry.getKey() + ")");
        }
    }

    private void displayCurrentQuestion() {
        QuestionModel q = questionList.get(currentQuestionIndex);
        lblQuestionTitle.setText("Q" + (currentQuestionIndex + 1) + ": " + q.getQuestionTitle());

        optionGroup.clearSelection();
        List<String> opts = q.getOptions();

        for (int i = 0; i < 4; i++) {
            if (i < opts.size()) {
                rbOptions[i].setText(opts.get(i));
                rbOptions[i].setVisible(true);
            } else {
                rbOptions[i].setVisible(false);
            }
        }
    }

    private void resetQuizState() {
        cmbQuizzes.setEnabled(true);
        btnStart.setEnabled(true);
        btnSubmit.setEnabled(false);
        lblQuestionTitle.setText("Please select a quiz and click 'Start Quiz'.");
        for (int i = 0; i < 4; i++) {
            rbOptions[i].setVisible(false);
        }
        loadQuizDropdown();
    }
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizTakingFrame().setVisible(true);

    });

}}
