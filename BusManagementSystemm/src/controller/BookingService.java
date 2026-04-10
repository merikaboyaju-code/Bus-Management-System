package controller;

import java.sql.ResultSet;

public interface BookingService {

    boolean saveBooking(String username, String route, String date,
                        String busType, String seats, int totalPrice);

    ResultSet getBookingsByUser(String username);

    boolean deleteBooking(int id);
}