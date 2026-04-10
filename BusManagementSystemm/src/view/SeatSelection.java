package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class SeatSelection {

    private String username;
    private String route;
    private String date;

    private Set<Integer> selectedSeats = new HashSet<>();
    private Set<Integer> bookedSeats = new HashSet<>();

    private Label selectedLabel;
    private Button continueBtn;

    public SeatSelection(String username, String route, String date) {
        this.username = username;
        this.route = route;
        this.date = date;

        // 🔴 Dummy booked seats
        bookedSeats.add(3);
        bookedSeats.add(7);
        bookedSeats.add(12);
        bookedSeats.add(18);
    }

    public void show(Stage stage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #eef1f7;");

        // ================= TOP BAR =================
        BorderPane topBar = new BorderPane();

        // 🔙 Back Button
        Button backBtn = new Button("← Back to Route Selection");
        backBtn.setOnAction(_ -> {
            Dashboard dashboard = new Dashboard(username);
            dashboard.show(stage);
        });

        // 👤 Right side (User + Logout)
        HBox rightTop = new HBox(10);
        rightTop.setAlignment(Pos.CENTER_RIGHT);

        Label userLabel = new Label("Logged in: " + username);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        logoutBtn.setOnAction(_ -> {
            LoginView login = new LoginView();
            login.show(stage);
        });

        rightTop.getChildren().addAll(userLabel, logoutBtn);

        // 🔥 Title section
        VBox titleBox = new VBox(5);
        Label title = new Label("Select Your Seats");
        title.setFont(Font.font(20));

        Label routeInfo = new Label(route + " | " + date);

        titleBox.getChildren().addAll(title, routeInfo);

        topBar.setLeft(backBtn);
        topBar.setCenter(titleBox);
        topBar.setRight(rightTop);

        // ================= CENTER =================
        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(15);
        seatGrid.setVgap(15);
        seatGrid.setPadding(new Insets(20));

        int seatNumber = 1;

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 4; col++) {

                Button seat = new Button(String.valueOf(seatNumber));
                seat.setPrefSize(60, 60);

                int currentSeat = seatNumber;

                if (bookedSeats.contains(currentSeat)) {
                    seat.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    seat.setDisable(true);
                } else {
                    seat.setStyle("-fx-background-color: #d3d3d3;");
                    seat.setOnAction(_ -> toggleSeat(seat, currentSeat));
                }

                seatGrid.add(seat, col, row);
                seatNumber++;
            }
        }

        // ================= RIGHT PANEL =================
        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(15));
        rightPanel.setPrefWidth(250);
        rightPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label summaryTitle = new Label("Booking Summary");
        summaryTitle.setFont(Font.font(16));

        Label routeLabel = new Label("Route: " + route);
        Label dateLabel = new Label("Date: " + date);

        selectedLabel = new Label("No seats selected");

        continueBtn = new Button("Continue (0 seats)");
        continueBtn.setMaxWidth(Double.MAX_VALUE);
        continueBtn.setDisable(true);

        // ✅ Continue → Bus Type Page
        continueBtn.setOnAction(_ -> {

            Stage newStage = new Stage();

            BusTypeSelection busPage = new BusTypeSelection(
                    username,
                    route,
                    date,
                    selectedSeats
            );

            busPage.show(newStage);
            stage.close();
        });

        rightPanel.getChildren().addAll(
                summaryTitle,
                routeLabel,
                dateLabel,
                selectedLabel,
                continueBtn
        );

        // ================= LEGEND =================
        HBox legend = new HBox(15);
        legend.setPadding(new Insets(10));

        Label available = new Label("Available");
        available.setStyle("-fx-background-color: #d3d3d3; -fx-padding: 5;");

        Label selected = new Label("Selected");
        selected.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5;");

        Label booked = new Label("Booked");
        booked.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5;");

        legend.getChildren().addAll(available, selected, booked);

        // ================= SET =================
        root.setTop(topBar);
        root.setCenter(seatGrid);
        root.setRight(rightPanel);
        root.setBottom(legend);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Seat Selection");
        stage.show();
    }

    // ================= TOGGLE =================
    private void toggleSeat(Button seat, int seatNumber) {

        if (selectedSeats.contains(seatNumber)) {
            selectedSeats.remove(seatNumber);
            seat.setStyle("-fx-background-color: #d3d3d3;");
        } else {
            selectedSeats.add(seatNumber);
            seat.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        }

        updateSummary();
    }

    // ================= UPDATE =================
    private void updateSummary() {

        if (selectedSeats.isEmpty()) {
            selectedLabel.setText("No seats selected");
            continueBtn.setText("Continue (0 seats)");
            continueBtn.setDisable(true);
        } else {
            selectedLabel.setText("Seats: " + selectedSeats);
            continueBtn.setText("Continue (" + selectedSeats.size() + " seats)");
            continueBtn.setDisable(false);
        }
    }
}