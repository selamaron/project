package org.example;

import org.example.Item;
import org.example.ItemDAO;
import org.example.ShoppingCart;
import org.example.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OnlineStoreApp extends JFrame {
    private DefaultListModel<Item> itemListModel;
    private JList<Item> itemList;
    private DefaultListModel<Item> cartListModel;
    private JList<Item> cartList;
    private ItemDAO itemDAO;
    private ShoppingCart shoppingCart;
    private UserDAO userDAO;
    private User currentUser;

    public OnlineStoreApp() {
        setTitle("Online Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        itemDAO = new ItemDAO();
        shoppingCart = new ShoppingCart();
        userDAO = new UserDAO();

        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToCart();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationDialog();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.add(new JLabel("Available Items"), BorderLayout.NORTH);
        itemPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);
        itemPanel.add(addToCartButton, BorderLayout.SOUTH);
        itemPanel.add(buttonPanel, BorderLayout.WEST);

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCart();
            }
        });

        itemPanel.add(viewCartButton, BorderLayout.EAST);

        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());
        cartPanel.add(new JLabel("Shopping Cart"), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(itemPanel);
        mainPanel.add(cartPanel);

        add(mainPanel);

        initializeItems();

        setVisible(true);
    }

    private void initializeItems() {
        List<Item> items = itemDAO.getAllItems();
        itemListModel.clear();
        for (Item item : items) {
            itemListModel.addElement(item);
        }
    }

    private void addItemToCart() {
        Item selected = itemList.getSelectedValue();
        if (selected != null) {
            if (selected.getStockQuantity() > 0) {
                boolean itemAlreadyInCart = shoppingCart.containsItem(selected);
                if (!itemAlreadyInCart) {
                    shoppingCart.addItem(selected);
                    JOptionPane.showMessageDialog(this, "Item added to cart successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Item is already in the cart.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sorry, this item is out of stock.", "Out of Stock", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(this, userDAO); // Pass the userDAO instance
        loginDialog.setVisible(true);

        if (loginDialog.isUserLoggedIn()) {
            currentUser = loginDialog.getLoggedInUser();
            if (currentUser != null) {
                JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + currentUser.getName() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Please check your credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void showRegistrationDialog() {
        RegistrationDialog registrationDialog = new RegistrationDialog(this, userDAO); // Pass the userDAO instance
        registrationDialog.setVisible(true);

        if (registrationDialog.isRegistrationSuccessful()) {
            User registeredUser = registrationDialog.getRegisteredUser();
            userDAO.addUser(registeredUser); // Store the registered user
            JOptionPane.showMessageDialog(this, "Registration successful. You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void showCart() {
        List<Item> cartItems = shoppingCart.getItems();

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Cart", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JFrame cartFrame = new JFrame("Shopping Cart");
            cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            cartFrame.setSize(400, 300);
            cartFrame.setLocationRelativeTo(this);

            DefaultListModel<Item> cartListModel = new DefaultListModel<>();
            JList<Item> cartList = new JList<>(cartListModel);

            for (Item item : cartItems) {
                cartListModel.addElement(item);
            }

            JButton purchaseButton = new JButton("Purchase");
            purchaseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    completePurchase(cartItems);
                    cartFrame.dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(purchaseButton);

            cartFrame.add(new JScrollPane(cartList));
            cartFrame.add(buttonPanel, BorderLayout.SOUTH);
            cartFrame.setVisible(true);
        }
    }

    private void completePurchase(List<Item> cartItems) {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to complete the purchase.", "Login Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Item item : cartItems) {
            if (item.getStockQuantity() > 0) {
                item.decreaseStockQuantity(); // Decrease stock quantity here
                itemDAO.updateItem(item); // Update item in the database
            }
        }

        shoppingCart.clearCart();
        updateCartList();
        JOptionPane.showMessageDialog(this, "Items purchased successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCartList() {
        cartListModel.clear();
        List<Item> cartItems = shoppingCart.getItems();
        for (Item item : cartItems) {
            cartListModel.addElement(item);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OnlineStoreApp();
            }
        });
    }
}