package org.example;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private ShoppingCart shoppingCart;

    public User(int id, String username, String password, String name, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.shoppingCart = new ShoppingCart();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    // Other getters and setters...
}