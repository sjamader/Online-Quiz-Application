package quiz.app;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class loggin extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, registerButton,AS_admin;

    loggin() {


        setTitle("Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("LogIn");
        title.setBounds(180, 30, 150, 50);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(241, 250, 236));
        add(title);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(70, 100, 100, 30);
        usernameLabel.setForeground(new Color(243, 244, 246));
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 100, 200, 30);
        usernameField.setBackground(new Color(238, 242, 246));
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(70, 150, 100, 30);
        passwordLabel.setForeground(new Color(246, 250, 250));
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 150, 200, 30);
        passwordField.setBackground(new Color(232, 239, 248));
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 210, 100, 35);
        loginButton.setBackground(new Color(233, 241, 251));
        add(loginButton);

        registerButton = new JButton("Create Account");
        registerButton.setBounds(215, 210, 150, 35);
        registerButton.setBackground(new Color(235, 245, 255));
        add(registerButton);

        AS_admin = new JButton("ADMIN PANEL");
        AS_admin.setBounds(145, 260, 150, 35);
        AS_admin.setBackground(new Color(229, 237, 246));
        add(AS_admin);

        loginButton.addActionListener(e -> loginUser());


        registerButton.addActionListener(e -> {
            new Register();
        });

        AS_admin.addActionListener(e -> {

            try {
                QuizManagementFrame frame = new QuizManagementFrame();
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


        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/loggin.jpg"));
        Image i = i1.getImage().getScaledInstance(450,450,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i);
        JLabel image = new JLabel(i2);
        image.setBounds(0,0,450,450);
        add(image);

        setVisible(true);

    }

    private void loginUser() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );

            return;
        }

        String sql =
                "SELECT * FROM users WHERE username = ? AND password = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful!"
                );

                // Open your existing Rules page
                new Rules();

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password!"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database connection error!"
            );
        }
    }

    public static void main(String[] args) {
        new loggin();
    }
}
