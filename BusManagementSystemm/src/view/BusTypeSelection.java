package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Set;

public class BusTypeSelection {

    private String username;
    private String route;
    private String date;
    private Set<Integer> seats;

    private VBox selectedCard = null;
    private String selectedBusType = "";

    private Button continueBtn;

    public BusTypeSelection(String username, String route, String date, Set<Integer> seats) {
        this.username = username;
        this.route = route;
        this.date = date;
        this.seats = seats;
    }

    public void show(Stage stage) {

        VBox root = new VBox(25);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #eef1f7;");

        // ================= TOP BAR =================
        BorderPane topBar = new BorderPane();

        // 🔙 Back Button
        Button backBtn = new Button("← Back to Seat Selection");
        backBtn.setOnAction(_ -> {
            SeatSelection seatPage = new SeatSelection(username, route, date);
            seatPage.show(stage);
        });

        // 👤 RIGHT SIDE
        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Button myBookings = new Button("My Bookings");

        Label userLabel = new Label("Logged in as " + username);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        logoutBtn.setOnAction(_ -> {
            LoginView login = new LoginView();
            login.show(stage);
        });

        rightBox.getChildren().addAll(myBookings, userLabel, logoutBtn);

        // ================= TITLE =================
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);

        Label title = new Label("Select Bus Type");
        title.setFont(Font.font(24));

        Label subtitle = new Label("Choose the type of bus for your journey");

        titleBox.getChildren().addAll(title, subtitle);

        topBar.setLeft(backBtn);
        topBar.setCenter(titleBox);
        topBar.setRight(rightBox);

        // ================= BUS CARDS =================
        HBox cardContainer = new HBox(25);
        cardContainer.setAlignment(Pos.CENTER);

        VBox standard = createCard(
                "Standard",
                "₹500/seat",
                "Basic Seating\nNo AC\nStandard Comfort"
        );

        VBox ac = createCard(
                "AC Sleeper",
                "₹800/seat",
                "Air Conditioned\nReclining Seats\nBlankets\nUSB Charging"
        );

        VBox luxury = createCard(
                "Luxury",
                "₹1200/seat",
                "Premium AC\nWiFi\nRefreshments\nEntertainment\nExtra Legroom"
        );

        cardContainer.getChildren().addAll(standard, ac, luxury);

        // ================= CONTINUE BUTTON =================
        continueBtn = new Button("Continue to Payment Summary");
        continueBtn.setDisable(true);
        continueBtn.setPrefWidth(320);
        continueBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        continueBtn.setOnAction(_ -> {

            Stage newStage = new Stage();

            PaymentSummary paymentPage = new PaymentSummary(
                    username,
                    route,
                    date,
                    seats,
                    selectedBusType
            );

            paymentPage.show(newStage);
            stage.close();
        });

        VBox bottom = new VBox(continueBtn);
        bottom.setAlignment(Pos.CENTER);

        // ================= ADD =================
        root.getChildren().addAll(topBar, cardContainer, bottom);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Bus Type Selection");
        stage.show();
    }

    // ================= CARD =================
    private VBox createCard(String titleText, String price, String features) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setAlignment(Pos.TOP_CENTER);

        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label title = new Label(titleText);
        title.setFont(Font.font(16));

        Label priceLabel = new Label(price);
        priceLabel.setStyle("-fx-text-fill: #007bff; -fx-font-size: 14;");

        Label featureLabel = new Label(features);

        card.getChildren().addAll(title, priceLabel, featureLabel);

        // 🔥 CLICK HANDLER
        card.setOnMouseClicked(_ -> {

            // remove old selection
            if (selectedCard != null) {
                selectedCard.setStyle("-fx-background-color: white;");
            }

            // highlight selected
            card.setStyle("-fx-background-color: #cce5ff;");
            selectedCard = card;
            selectedBusType = titleText;

            // enable button
            continueBtn.setDisable(false);
            continueBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        });

        return card;
    }
}