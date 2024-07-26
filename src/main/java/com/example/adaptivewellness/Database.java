package com.example.adaptivewellness;

import java.sql.*;

class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/adaptive_wellness";
    private static final String USER = "root";
    private static final String PASSWORD = "Adarsh@2004";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
            System.setProperty("javax.net.ssl.trustStore", "/truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "Adarsh@2004");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // If there's a result, login is valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateUserBMI(String username, double newBMI) throws SQLException {
        System.out.println("Attempting to update BMI for user: " + username);
        String query = "UPDATE users SET bmi = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, newBMI);
            pstmt.setString(2, username);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("BMI updated successfully for user: " + username);
            } else {
                System.out.println("No user found with username: " + username);}}}
    public static void updatePreviousBMI(String username) throws SQLException {
        System.out.println("Attempting to update previous BMI for user: " + username);
        String query = "UPDATE users SET previous_bmi = bmi WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Previous BMI updated successfully for user: " + username);
            } else {
                System.out.println("No user found with username: " + username);}}}
    public static double[] fetchUserBMIs(String username) {
        System.out.println("Attempting to fetch BMIs for user: " + username);
        String query = "SELECT bmi, previous_bmi FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double currentBMI = rs.getDouble("bmi");
                    double previousBMI = rs.getDouble("previous_bmi");
                    return new double[]{currentBMI, previousBMI};}}
        } catch (SQLException e) {
            e.printStackTrace();}
        return new double[]{-1.0, -1.0};}}