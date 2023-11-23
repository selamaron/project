package org.example;


import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private List<User> users;

    public UserDAO() {
        users = new ArrayList<>();
        // Initialize with some sample users (for demonstration purposes)
        users.add(new User(1, "user1", "password1", "Selam Estifanos", "john@example.com"));
        users.add(new User(2, "user2", "password2", "Jane Smith", "jane@example.com"));
    }

    public boolean registerUser(String username, String password, String name, String email) {
        // Check if the username is already taken
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // Username is already taken
            }
        }

        // Generate a unique ID for the new user (you may use a more robust ID generation method)
        int newUserId = users.size() + 1;

        // Create and add the new user
        User newUser = new User(newUserId, username, password, name, email);
        users.add(newUser);
        return true; // Registration successful
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Login successful, return the user object
            }
        }
        return null; // Login failed
    }
    public void addUser(User user) {
        users.add(user);
    }
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    // Other methods for user management (e.g., retrieving user by ID, updating user info, etc.)
}
