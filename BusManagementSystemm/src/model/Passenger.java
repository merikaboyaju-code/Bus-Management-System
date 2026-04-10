package model;

public class Passenger extends User {

    public Passenger(String username) {
        super(username);
    }

    // ✅ METHOD OVERRIDING
    @Override
    public void showDashboard() {
        System.out.println("Passenger Dashboard - Book tickets");
    }
}