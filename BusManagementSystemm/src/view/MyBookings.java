package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;

import controller.BookingControl;

public class MyBookings {

    private String username;

    public MyBookings(String username) {
        this.username = username;
    }

    public void show(Stage stage) {

        Label title = new Label("My Bookings");
        Label userLabel = new Label("Logged in: " + username);

        TableView<String[]> table = new TableView<>();

        // Columns
        TableColumn<String[], String> routeCol = new TableColumn<>("Route");
        TableColumn<String[], String> dateCol = new TableColumn<>("Date");
        TableColumn<String[], String> busCol = new TableColumn<>("Bus Type");
        TableColumn<String[], String> seatCol = new TableColumn<>("Seats");

        routeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        busCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        seatCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));

        table.getColumns().addAll(routeCol, dateCol, busCol, seatCol);

        // 🔥 LOAD DATA FROM DATABASE
        loadBookings(table);

        VBox root = new VBox(15, title, userLabel, table);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("My Bookings");
        stage.show();
    }

    private void loadBookings(TableView<String[]> table) {

        BookingControl control = new BookingControl();

        try {
            ResultSet rs = control.getBookingsByUser(username);

            while (rs != null && rs.next()) {
                table.getItems().add(new String[]{
                        rs.getString("route"),
                        rs.getString("travel_date"),
                        rs.getString("bus_type"),
                        rs.getString("seats")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}