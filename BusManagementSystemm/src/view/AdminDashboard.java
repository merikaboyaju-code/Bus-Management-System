package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.sql.ResultSet;

import controller.BookingControl; // ✅ IMPORTANT (use class, not interface)

public class AdminDashboard {

    private String username;
    private TableView<String[]> table;

    public AdminDashboard(String username) {
        this.username = username;
    }

    public void show(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eef1f7;");

        // ================= TOP BAR =================
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: white;");

        Label title = new Label("Admin Dashboard");
        title.setFont(Font.font(20));

        Label userLabel = new Label("Admin: " + username);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        logoutBtn.setOnAction(_ -> new LoginView().show(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(title, spacer, userLabel, logoutBtn);

        // ================= TABLE =================
        table = new TableView<>();

        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        TableColumn<String[], String> userCol = new TableColumn<>("User");
        TableColumn<String[], String> routeCol = new TableColumn<>("Route");
        TableColumn<String[], String> dateCol = new TableColumn<>("Date");

        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        userCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        routeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));

        table.getColumns().addAll(idCol, userCol, routeCol, dateCol);

        // ✅ LOAD DATA FROM DATABASE
        loadBookings();

        // ================= DELETE BUTTON =================
        Button deleteBtn = new Button("Delete Selected Booking");
        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        deleteBtn.setOnAction(_ -> deleteBooking());

        VBox center = new VBox(15, table, deleteBtn);
        center.setPadding(new Insets(20));

        root.setTop(topBar);
        root.setCenter(center);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    // ================= LOAD BOOKINGS =================
    private void loadBookings() {

        BookingControl control = new BookingControl(); // ✅ FIXED

        try {
            ResultSet rs = control.getAllBookings(); // ✅ FIXED

            table.getItems().clear();

            while (rs != null && rs.next()) {
                table.getItems().add(new String[]{
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("route"),
                        rs.getString("travel_date")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE BOOKING =================
    private void deleteBooking() {

        String[] selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a booking first!").show();
            return;
        }

        int id = Integer.parseInt(selected[0]);

        BookingControl control = new BookingControl(); // ✅ FIXED

        boolean success = control.deleteBooking(id);

        if (success) {
            loadBookings(); // reload table
            new Alert(Alert.AlertType.INFORMATION, "Deleted successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Delete failed!").show();
        }
    }
}