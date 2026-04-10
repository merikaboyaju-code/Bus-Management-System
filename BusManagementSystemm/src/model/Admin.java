package model;

public class Admin extends User {

    public Admin(String username) {
        super(username);
    }

    // ✅ METHOD OVERRIDING
    @Override
    public void showDashboard() {
        System.out.println("Admin Dashboard - Manage system");
    }
}