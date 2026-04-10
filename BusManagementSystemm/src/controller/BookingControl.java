package controller;

import model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingControl implements BookingService {

    // ── MAIN METHOD (OVERRIDDEN FROM INTERFACE) ────────────────
    @Override
    public boolean saveBooking(String username, String route, String date,
                               String busType, String seats, int totalPrice) {

        String sql = "INSERT INTO bookings (username, route, travel_date, bus_type, seats, total_price)"
                   + " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, route);
            ps.setString(3, date);
            ps.setString(4, busType);
            ps.setString(5, seats);
            ps.setInt(6, totalPrice);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ── OVERLOADED METHOD 1 (QUICK BOOKING) ────────────────────
    public boolean saveBooking(String username, String route) {

        String date = "N/A";
        String busType = "Standard";
        String seats = "[]";
        int totalPrice = 0;

        return saveBooking(username, route, date, busType, seats, totalPrice);
    }

    // ── OVERLOADED METHOD 2 (WITHOUT PRICE) ────────────────────
    public boolean saveBooking(String username, String route, String date,
                               String busType, String seats) {

        int totalPrice = 0;

        return saveBooking(username, route, date, busType, seats, totalPrice);
    }

    // ── GET BOOKINGS FOR SPECIFIC USER ─────────────────────────
    @Override
    public ResultSet getBookingsByUser(String username) {

        String sql = "SELECT * FROM bookings WHERE username = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ── GET ALL BOOKINGS (FOR ADMIN DASHBOARD) ─────────────────
    public ResultSet getAllBookings() {

        String sql = "SELECT * FROM bookings";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ── DELETE BOOKING ─────────────────────────────────────────
    @Override
    public boolean deleteBooking(int id) {

        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}