package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField emailField;
    private JButton registerButton;
    private boolean registrationSuccessful = false;
    private User registeredUser = null;
    private UserDAO userDAO;
    public RegistrationDialog(JFrame parent, UserDAO userDAO) {
        super(parent, "Create Account", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        nameField = new JTextField();
        emailField = new JTextField();

        registerButton = new JButton("Register");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String email = emailField.getText();

                UserDAO userDAO = new UserDAO();
                boolean registrationResult = userDAO.registerUser(username, password, name, email);

                if (registrationResult) {
                    registrationSuccessful = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegistrationDialog.this, "Registration failed. Username already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isRegistrationSuccessful() {
        return registrationSuccessful;
    }
    public User getRegisteredUser() {
        return registeredUser;
    }
}
