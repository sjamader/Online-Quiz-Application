package quiz.app;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Register extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton registerButton, backButton;

    Register() {


        setTitle("Create Account");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel background = new JPanel() {

            ImageIcon image = new ImageIcon(
                    ClassLoader.getSystemResource("icons/register1.jpeg")
            );

            Image img = image.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(
                        img,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }
        };

        background.setBounds(0, 0, 450, 350);
        background.setLayout(null);
        setContentPane(background);



        JLabel title = new JLabel("Create Account");
        title.setBounds(140, 30, 200, 40);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(70, 100, 100, 30);
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 100, 200, 30);
        usernameField.setBackground(new Color(243, 246, 250));
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(70, 150, 100, 30);
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 150, 200, 30);
        passwordField.setBackground(new Color(241, 244, 248));
        add(passwordField);





        registerButton = new JButton("Register");
        registerButton.setBounds(100, 210, 110, 35);
        registerButton.setBackground(new Color(228, 237, 250));
        add(registerButton);

        backButton = new JButton("Back");
        backButton.setBounds(230, 210, 100, 35);
        backButton.setBackground(new Color(228, 237, 250));
        add(backButton);






        registerButton.addActionListener(e -> registerUser());

        backButton.addActionListener(e -> {
            new loggin();
            dispose();
        });



        setVisible(true);

    }

    private void registerUser() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );
            return;
        }

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully!"
            );

            new loggin();
            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username already exists!"
            );
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}
