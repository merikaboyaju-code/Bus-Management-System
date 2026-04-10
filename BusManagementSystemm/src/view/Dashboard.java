package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Dashboard {

    private String username;
    private VBox selectedCard = null;
    private String selectedRoute = null;

    // ✅ Constructor (REQUIRED)
    public Dashboard(String username) {
        this.username = username;
    }

    public void show(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eef1f7;");

        // ================= TOP BAR =================
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: white;");

        Label title = new Label("Select Your Route");
        title.setFont(Font.font(20));

        Button myBookings = new Button("My Bookings");

        Label userLabel = new Label("Logged in: " + username);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");

        logoutBtn.setOnAction(_ -> {
            LoginView login = new LoginView();
            login.show(stage);
        });

        // 👉 OPEN MY BOOKINGS PAGE
        myBookings.setOnAction(_ -> {
            Stage newStage = new Stage();
            MyBookings page = new MyBookings(username);
            page.show(newStage);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(title, spacer, myBookings, userLabel, logoutBtn);

        // ================= LEFT SIDE (ROUTES) =================
        VBox routesBox = new VBox(15);
        routesBox.setPadding(new Insets(15));

        routesBox.getChildren().addAll(
                createRoute("Kathmandu", "Pokhara", "6h 30m", "200 km"),
                createRoute("Kathmandu", "Biratnagar", "10h", "400 km"),
                createRoute("Kathmandu", "Bhaktapur", "45m", "13 km"),
                createRoute("Kathmandu", "Dharan", "11h 30m", "425 km"),
                createRoute("Kathmandu", "Dhangadhi", "16h", "670 km"),
                createRoute("Biratnagar", "Dharan", "1h 30m", "60 km"),
                createRoute("Pokhara", "Chitwan", "4h", "135 km")
        );

        ScrollPane scrollPane = new ScrollPane(routesBox);
        scrollPane.setFitToWidth(true);

        // ================= RIGHT SIDE =================
        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(15));
        rightPanel.setPrefWidth(250);
        rightPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label travelTitle = new Label("Travel Details");
        travelTitle.setFont(Font.font(16));

        Label dateLabel = new Label("Travel Date");
        DatePicker datePicker = new DatePicker();

        Label steps = new Label(
                "Next Steps:\n" +
                "1. Select route\n" +
                "2. Choose travel date\n" +
                "3. Pick your seats\n" +
                "4. Select bus type\n" +
                "5. Complete payment"
        );

        Button continueBtn = new Button("Continue to Seat Selection");
        continueBtn.setMaxWidth(Double.MAX_VALUE);
        continueBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        // 🔥 CONTINUE BUTTON LOGIC
        continueBtn.setOnAction(_ -> {

            if (selectedRoute == null) {
                showAlert("Please select a route!");
                return;
            }

            if (datePicker.getValue() == null) {
                showAlert("Please select a travel date!");
                return;
            }

            // 👉 OPEN SEAT SELECTION
            Stage newStage = new Stage();

            SeatSelection seatPage = new SeatSelection(
                    username,
                    selectedRoute,
                    datePicker.getValue().toString()
            );

            seatPage.show(newStage);
            stage.close();
        });

        rightPanel.getChildren().addAll(
                travelTitle, dateLabel, datePicker, steps, continueBtn
        );

        // ================= SET LAYOUT =================
        root.setTop(topBar);
        root.setCenter(scrollPane);
        root.setRight(rightPanel);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }

    // ================= ROUTE CARD =================
    private VBox createRoute(String from, String to, String time, String distance) {

        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label route = new Label("From " + from + " → " + to);
        route.setStyle("-fx-font-weight: bold;");

        Label details = new Label(time + " | " + distance);

        card.getChildren().addAll(route, details);

        card.setOnMouseClicked(_ -> {

            // remove previous highlight
            if (selectedCard != null) {
                selectedCard.setStyle("-fx-background-color: white;");
            }

            // highlight current
            card.setStyle("-fx-background-color: #cce5ff;");
            selectedCard = card;

            // save selected route
            selectedRoute = from + " → " + to;

            System.out.println("Selected: " + selectedRoute);
        });

        return card;
    }

    // ================= ALERT =================
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.show();
    }
}