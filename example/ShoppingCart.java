package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class ShoppingCart {
    private List<Item> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (item != null && item.getStockQuantity() > 0) {
            items.add(item);

        } else {
            // Handle the case where the item cannot be added
            JOptionPane.showMessageDialog(null, "Item cannot be added to the cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }
    public void clearCart() {
        items.clear();
    }
    public boolean containsItem(Item item) {
        return items.contains(item);
    }

}