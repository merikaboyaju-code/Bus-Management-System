package model;

public class User {

    private String username;

    public User(String username) {
        this.username = username;
    }

    // Method to be overridden
    public void showDashboard() {
        System.out.println("User Dashboard");
    }

    public String getUsername() {
        return username;
    }
}