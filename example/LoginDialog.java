package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private boolean userLoggedIn = false;
    private String loggedInEmail = null;
    private User loggedInUser;
    private UserDAO userDAO;

    public LoginDialog(JFrame parent, UserDAO userDAO) {
        super(parent, "Login", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                UserDAO userDAO = new UserDAO();
                User user = userDAO.loginUser(username, password);

                if (user != null) {
                    userLoggedIn = true;
                    loggedInUser = user;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this, "Login failed. Please check your credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }


    public String getLoggedInEmail() {

        return loggedInEmail;
    }
}