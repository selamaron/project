package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private static final String SELECT_ALL_ITEMS = "SELECT * FROM items";
    private static final String UPDATE_STOCK_QUANTITY = "UPDATE items SET stockQuantity = ? WHERE id = ?";
    private static final String DELETE_ITEM = "DELETE FROM items WHERE id = ?";

    private List<Item> items = new ArrayList<>();

    public ItemDAO() {
        loadItemsFromDatabase();
    }

    public List<Item> getAllItems() {
        return items;
    }

    public void updateItem(Item item) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE_STOCK_QUANTITY)) {

            statement.setInt(1, item.getStockQuantity());
            statement.setInt(2, item.getId());
            statement.executeUpdate();

            // Update the stock quantity in the local items list
            for (Item storedItem : items) {
                if (storedItem.getId() == item.getId()) {
                    storedItem.setStockQuantity(item.getStockQuantity());
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(Item item) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE_ITEM)) {

            statement.setInt(1, item.getId());
            statement.executeUpdate();
            items.remove(item);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadItemsFromDatabase() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL_ITEMS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("stockQuantity");
                items.add(new Item(id, name, price, stockQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}