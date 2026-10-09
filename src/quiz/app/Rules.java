package quiz.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Rules extends JFrame implements ActionListener {

    JButton start,back;

    Rules(){


        JLabel heading = new JLabel("Welcome"+"to QUIZ TEST ");
        heading.setBounds(250,100,700,30);
        heading.setFont(new Font("hand ITC",Font.BOLD,28));
        heading.setForeground(new Color(22,98,75));
        add(heading);

        JLabel rules = new JLabel();
        rules.setBounds(70,150,700,350);
        rules.setFont(new Font("Tahoma",Font.PLAIN,16));
        rules.setForeground(new Color(22,98,75));
        rules.setText(

                "<html>" + "<h2>QUIZ TEST RULES</h2>" +
                        "<ol>" + "<li>The quiz will consist of multiple-choice questions (MCQs).</li>" +
                        "<li>Each question will have four options: A, B, C, and D.</li>" +
                        "<li>Only one option will be correct.</li>" +
                        "<li>Each question must be answered within the given time.</li>" +
                        "<li>Mobile phones, books, and notes are not allowed.</li>" +
                        "<li>Each correct answer will carry the specified marks.</li>" +
                        "<li>No marks will be awarded for unanswered questions.</li>" +
                        "<li>Do not discuss answers with other participants.</li>" +
                        "<li>Use of unfair means may result in disqualification.</li>" +
                        "<li>In case of a tie, a tie-breaker round may be conducted.</li>" +
                        "<li>The decision of the quiz coordinator will be final.</li>" +
                        "</ol>" +
                        "</html>"
        );
        add(rules);

        start = new JButton("Start");
        start.setBounds(450,500,120,25);
        start.setBackground(new Color(22,98,75));
        start.setForeground(Color.WHITE);
        start.addActionListener(this);
        add(start);

        back = new JButton("Back");
        back.setBounds(250,500,120,25);
        back.setBackground(new Color(22,98,75));
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);


        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/back.png"));
        Image i = i1.getImage().getScaledInstance(800,650,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i);
        JLabel image = new JLabel(i2);
        image.setBounds(0,0,800,650);
        add(image);


        setSize(800,650);
        setLocation(350,100);

        setLayout(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==start){
            setVisible(false);
            QuizTakingFrame quizFrame = new QuizTakingFrame();
            quizFrame.setVisible(true);
        }
        else {
            setVisible(false);
            new loggin();
        }
    }


    static void main(String[] args) {

    new Rules();
}
}