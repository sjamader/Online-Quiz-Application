package quiz.app;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizManagementFrame extends JFrame {
    JButton Back;
    private JTextField txtQuizId, txtQuizTitle;
    private JTextField txtQQuizId, txtQTitle, txtOpt1, txtOpt2, txtOpt3, txtOpt4, txtCorrectIndex;
    private JTextField txtViewId;
    private JTextArea txtAreaOutput;
    private QuizManagerDAO manager;


    public QuizManagementFrame() {




        manager = new QuizManagerDAO();

        setTitle("Quiz Management System - Admin Panel");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel backgroundPanel = new JPanel() {

            ImageIcon icon = new ImageIcon(
                    ClassLoader.getSystemResource("icons/register1.jpeg")
            );

            Image image = icon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(
                        image,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }
        };

        backgroundPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(backgroundPanel);


        // Use Tabbed Pane to organize features cleanly
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);

        // --- TAB 1: Create & Delete Quizzes ---
        JPanel panelQuiz = new JPanel(new GridLayout(5, 2, 10, 10));
        panelQuiz.setOpaque(false);
        panelQuiz.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelQuiz.add(new JLabel("Quiz ID (e.g., JAVA_101):"));
        txtQuizId = new JTextField();
        panelQuiz.add(txtQuizId);

        panelQuiz.add(new JLabel("Quiz Title:"));
        txtQuizTitle = new JTextField();
        panelQuiz.add(txtQuizTitle);

        JButton btnCreate = new JButton("Create Quiz");
        JButton btnUpdateTitle = new JButton("Update Title");
        JButton btnDelete = new JButton("Delete Quiz");
        JButton btnBack = new JButton("Back");

        panelQuiz.add(btnCreate);
        panelQuiz.add(btnUpdateTitle);
        panelQuiz.add(btnDelete);
        panelQuiz.add(btnBack);


        tabbedPane.addTab("Manage Quizzes", panelQuiz);

        // --- TAB 2: Add Questions with Options ---
        JPanel panelQuestion = new JPanel(new GridLayout(8, 2, 8, 8));
        panelQuestion.setOpaque(false);
        panelQuestion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelQuestion.add(new JLabel("Target Quiz ID:"));
        txtQQuizId = new JTextField();
        panelQuestion.add(txtQQuizId);

        panelQuestion.add(new JLabel("Question Title:"));
        txtQTitle = new JTextField();
        panelQuestion.add(txtQTitle);

        panelQuestion.add(new JLabel("Option 1:"));
        txtOpt1 = new JTextField();
        panelQuestion.add(txtOpt1);

        panelQuestion.add(new JLabel("Option 2:"));
        txtOpt2 = new JTextField();
        panelQuestion.add(txtOpt2);

        panelQuestion.add(new JLabel("Option 3:"));
        txtOpt3 = new JTextField();
        panelQuestion.add(txtOpt3);

        panelQuestion.add(new JLabel("Option 4:"));
        txtOpt4 = new JTextField();
        panelQuestion.add(txtOpt4);

        panelQuestion.add(new JLabel("Correct Option Index (0 to 3):"));
        txtCorrectIndex = new JTextField();
        panelQuestion.add(txtCorrectIndex);

        JButton btnAddQuestion = new JButton("Add Question");
        panelQuestion.add(btnAddQuestion);

        tabbedPane.addTab("Add Questions", panelQuestion);

        // --- TAB 3: View Quiz Details ---
        JPanel panelView = new JPanel(new BorderLayout(10, 10));
        panelView.setOpaque(false);

        panelView.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel viewTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewTopPanel.add(new JLabel("Quiz ID:"));
        txtViewId = new JTextField(15);
        viewTopPanel.add(txtViewId);
        JButton btnView = new JButton("View Quiz");
        viewTopPanel.add(btnView);

        panelView.add(viewTopPanel, BorderLayout.NORTH);

        txtAreaOutput = new JTextArea();
        txtAreaOutput.setEditable(false);
        txtAreaOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelView.add(new JScrollPane(txtAreaOutput), BorderLayout.CENTER);

        tabbedPane.addTab("View Quizzes", panelView);

        add(tabbedPane, BorderLayout.CENTER);

        // --- ACTION LISTENERS ---

        // 1. Create Quiz
        btnCreate.addActionListener(e -> {
            String quizId = txtQuizId.getText().trim();
            String title = txtQuizTitle.getText().trim();

            if (quizId.isEmpty() || title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Quiz ID and Title!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.createQuiz(quizId, title);
            JOptionPane.showMessageDialog(this, "Quiz created successfully!");
            clearQuizFields();
        });

        // 2. Update Quiz Title
        btnUpdateTitle.addActionListener(e -> {
            String quizId = txtQuizId.getText().trim();
            String newTitle = txtQuizTitle.getText().trim();

            if (quizId.isEmpty() || newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Quiz ID and New Title!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.updateQuizTitle(quizId, newTitle);
            JOptionPane.showMessageDialog(this, "Quiz title updated successfully!");
        });

        // 3. Delete Quiz
        btnDelete.addActionListener(e -> {
            String quizId = txtQuizId.getText().trim();
            if (quizId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Quiz ID to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete quiz '" + quizId + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteQuiz(quizId);
                JOptionPane.showMessageDialog(this, "Quiz deleted successfully!");
                clearQuizFields();
            }
        });

        // 4. Add Question
        btnAddQuestion.addActionListener(e -> {
            String qId = txtQQuizId.getText().trim();
            String qTitle = txtQTitle.getText().trim();
            String o1 = txtOpt1.getText().trim();
            String o2 = txtOpt2.getText().trim();
            String o3 = txtOpt3.getText().trim();
            String o4 = txtOpt4.getText().trim();
            String idxStr = txtCorrectIndex.getText().trim();

            if (qId.isEmpty() || qTitle.isEmpty() || o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty() || idxStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all question fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int correctIndex = Integer.parseInt(idxStr);
                if (correctIndex < 0 || correctIndex > 3) {
                    JOptionPane.showMessageDialog(this, "Correct option index must be between 0 and 3!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<String> options = List.of(o1, o2, o3, o4);
                manager.addQuestionToQuiz(qId, qTitle, options, correctIndex);
                JOptionPane.showMessageDialog(this, "Question added successfully!");
                clearQuestionFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Correct index must be a valid integer number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 5. View Quiz
        btnView.addActionListener(e -> {
            String quizId = txtViewId.getText().trim();
            if (quizId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Quiz ID to view!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Trigger view (Note: your manager prints to console, but you can see confirmation here)
            manager.viewQuiz(quizId);
            txtAreaOutput.setText("Check your IDE Console / Terminal for the detailed output of Quiz: " + quizId);
        });


        btnBack.addActionListener(e -> {

            try {
                loggin frame = new loggin();
                frame.setVisible(true);

                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage()
                );
            }
        });




    }

    private void clearQuizFields() {
        txtQuizId.setText("");
        txtQuizTitle.setText("");
    }

    private void clearQuestionFields() {
        txtQQuizId.setText("");
        txtQTitle.setText("");
        txtOpt1.setText("");
        txtOpt2.setText("");
        txtOpt3.setText("");
        txtOpt4.setText("");
        txtCorrectIndex.setText("");



        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizManagementFrame().setVisible(true);
        });
    }
}

