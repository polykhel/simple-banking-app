package com.polykhel.banking;

import java.sql.*;
import java.util.Optional;

public class CardRepository {

    private final String url;

    CardRepository(String db) {
        this.url = "jdbc:sqlite:" + db;
        this.createInitialTable();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void createInitialTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER PRIMARY KEY," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0);";


        try (Connection connection = this.connect()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertAccount(Card card) {
        String sql = "INSERT INTO card (number, pin) VALUES (?, ?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, card.getNumber());
            statement.setString(2, card.getPin());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Optional<Card> findCardByNumberAndPin(String number, String pin) {
        String sql = "SELECT id, number, pin, balance FROM card WHERE number = ? and pin = ?";
        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.setString(2, pin);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Card(resultSet.getInt("id"),
                        resultSet.getString("number"),
                        resultSet.getString("pin"),
                        resultSet.getInt("balance")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Card> findCardByNumber(String number) {
        String sql = "SELECT id, number, pin, balance FROM card WHERE number = ?";
        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Card(resultSet.getInt("id"),
                        resultSet.getString("number"),
                        resultSet.getString("pin"),
                        resultSet.getInt("balance")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public void deleteCard(int id) {
        String sql = "DELETE FROM card WHERE id = ?";
        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateBalance(int id, long amount) {
        String sql = "UPDATE card SET balance = balance + ? WHERE id = ?";
        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, amount);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
