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

import controller.BookingService;
import controller.BookingControl;

public class PaymentSummary {

    private String username;
    private String route;
    private String date;
    private Set<Integer> seats;
    private String busType;

    // ✅ MAIN CONSTRUCTOR
    public PaymentSummary(String username, String route, String date,
                          Set<Integer> seats, String busType) {
        this.username = username;
        this.route = route;
        this.date = date;
        this.seats = seats;
        this.busType = busType;
    }

    // ✅ OVERLOADED CONSTRUCTOR
    public PaymentSummary(String username, String route) {
        this.username = username;
        this.route = route;
        this.date = "Not Selected";
        this.seats = new HashSet<>();
        this.busType = "Standard";
    }

    public void show(Stage stage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #eef1f7;");

        // ================= TOP BAR =================
        BorderPane topBar = new BorderPane();

        Button backBtn = new Button("← Back to Bus Type");
        backBtn.setOnAction(_ -> {
            BusTypeSelection page = new BusTypeSelection(username, route, date, seats);
            page.show(stage);
        });

        HBox rightTop = new HBox(10);
        rightTop.setAlignment(Pos.CENTER_RIGHT);

        Button myBookings = new Button("My Bookings");

        Label userLabel = new Label("Logged in as " + username);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        logoutBtn.setOnAction(_ -> new LoginView().show(stage));

        rightTop.getChildren().addAll(myBookings, userLabel, logoutBtn);

        VBox titleBox = new VBox(5);
        Label title = new Label("Payment Summary");
        title.setFont(Font.font(22));

        Label subtitle = new Label("Review your booking and complete payment");

        titleBox.getChildren().addAll(title, subtitle);

        topBar.setLeft(backBtn);
        topBar.setCenter(titleBox);
        topBar.setRight(rightTop);

        // ================= LEFT PANEL =================
        VBox leftPanel = new VBox(12);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(450);
        leftPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label customerTitle = new Label("Customer Details");
        customerTitle.setFont(Font.font(16));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");

        // 🔥 REAL-TIME VALIDATION (DIGITS + MAX 10)
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {

            String digitsOnly = newVal.replaceAll("[^\\d]", "");

            if (digitsOnly.length() > 10) {
                digitsOnly = digitsOnly.substring(0, 10);
            }

            if (!newVal.equals(digitsOnly)) {
                phoneField.setText(digitsOnly);
            }
        });

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email address");

        leftPanel.getChildren().addAll(
                customerTitle,
                new Label("Full Name"), nameField,
                new Label("Phone Number"), phoneField,
                new Label("Email Address"), emailField
        );

        // ================= RIGHT PANEL =================
        VBox rightPanel = new VBox(12);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(280);
        rightPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label summaryTitle = new Label("Booking Summary");
        summaryTitle.setFont(Font.font(16));

        int pricePerSeat = getPrice(busType);
        int totalSeats = seats != null ? seats.size() : 0;
        int totalPrice = totalSeats * pricePerSeat;

        Label routeLabel = new Label("Route: " + route);
        Label dateLabel = new Label("Date: " + date);
        Label busLabel = new Label("Bus Type: " + busType);
        Label seatLabel = new Label("Seats: " + seats);
        Label countLabel = new Label("Seat Count: " + totalSeats);
        Label fareLabel = new Label("Fare/Seat: ₹" + pricePerSeat);

        Label totalLabel = new Label("Total Fare: ₹" + totalPrice);
        totalLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");

        Button payBtn = new Button("Confirm & Pay ₹" + totalPrice);
        payBtn.setMaxWidth(Double.MAX_VALUE);
        payBtn.setStyle("-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); -fx-text-fill: white;");

        // ================= VALIDATION + PAYMENT =================
        payBtn.setOnAction(_ -> {

            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill all details!").show();
                return;
            }

            // 🔴 Phone must be exactly 10 digits
            if (!phone.matches("\\d{10}")) {
                new Alert(Alert.AlertType.ERROR,
                        "Phone number must be exactly 10 digits!").show();
                return;
            }

            // 🔴 Email validation
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                new Alert(Alert.AlertType.ERROR,
                        "Please enter a valid email address!").show();
                return;
            }

            BookingService control = new BookingControl();

            boolean success = control.saveBooking(
                    username,
                    route,
                    date,
                    busType,
                    seats.toString(),
                    totalPrice
            );

            if (success) {
                new Alert(Alert.AlertType.INFORMATION,
                        "Payment Successful & Booking Saved!").show();

                new Dashboard(username).show(stage);

            } else {
                new Alert(Alert.AlertType.ERROR,
                        "Booking Failed!").show();
            }
        });

        rightPanel.getChildren().addAll(
                summaryTitle,
                routeLabel,
                dateLabel,
                busLabel,
                seatLabel,
                countLabel,
                fareLabel,
                totalLabel,
                payBtn
        );

        // ================= LAYOUT =================
        HBox center = new HBox(20, leftPanel, rightPanel);

        root.setTop(topBar);
        root.setCenter(center);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Payment Summary");
        stage.show();
    }

    // ================= PRICE =================
    private int getPrice(String busType) {
        switch (busType) {
            case "Standard": return 500;
            case "AC Sleeper": return 800;
            case "Luxury": return 1200;
            default: return 0;
        }
    }
}