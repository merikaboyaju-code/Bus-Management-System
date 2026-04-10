package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.DBConnection;

public class LoginControl {

    // ✅ MAIN LOGIN METHOD (username + password)
    public boolean login(String username, String password) {

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                rs.close();
                stmt.close();
                conn.close();
                return true; // login success
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // login failed
    }

    // ✅ OVERLOADED METHOD 1 (username only)
    public boolean login(String username) {
        // Default password (for testing/demo purpose)
        return login(username, "1234");
    }

    // ✅ OVERLOADED METHOD 2 (email + password)
    public boolean login(String email, String password, boolean isEmailLogin) {

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                rs.close();
                stmt.close();
                conn.close();
                return true; // login success
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // login failed
    }
}