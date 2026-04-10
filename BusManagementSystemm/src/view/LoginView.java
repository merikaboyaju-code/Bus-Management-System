package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.DBConnection;
import model.User;
import model.Admin;
import model.Passenger;

public class LoginView {

    public void show(Stage stage) {

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #eef2f7, #d9e2ec);");

        // ================= CARD =================
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(350);

        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);"
        );

        // ================= HEADER =================
        Label header = new Label("BUS MANAGEMENT SYSTEM");
        header.setStyle("-fx-text-fill: #4a90e2; -fx-font-weight: bold;");

        // ================= TITLE =================
        Label title = new Label("Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        // ================= USERNAME =================
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        // ================= PASSWORD =================
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        // ================= ROLE =================
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Admin", "Passenger");
        roleBox.setPromptText("Select Role");
        roleBox.setMaxWidth(250);

        // ================= LOGIN BUTTON =================
        Button loginButton = new Button("Login");
        loginButton.setMinWidth(250);

        // ================= MESSAGE =================
        Label message = new Label();
        message.setTextFill(Color.RED);

        // ================= REGISTER LINK =================
        Hyperlink registerLink = new Hyperlink("Create new account");
        registerLink.setOnAction(_ -> new RegistrationPage().show(stage));

        // ================= LOGIN LOGIC =================
        loginButton.setOnAction(_ -> {

            try {
                Connection con = DBConnection.getConnection();

                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String role = roleBox.getValue();

                if (username.isEmpty() || password.isEmpty() || role == null) {
                    message.setText("Please fill all fields!");
                    return;
                }

                String query = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    // OOP: Inheritance + Polymorphism
                    User user;

                    if (role.equalsIgnoreCase("Admin")) {
                        user = new Admin(username);
                    } else {
                        user = new Passenger(username);
                    }

                    user.showDashboard();

                    Stage newStage = new Stage();
                    
                    // example Polymorphism (just for understanding
                    User user1 = new Admin("AdminUser");
                    User user2 = new Passenger("PassengerUser");

                    user1.showDashboard();
                    user2.showDashboard();

                    // Role-based dashboard
                    if (role.equalsIgnoreCase("Admin")) {
                        new AdminDashboard(username).show(newStage);
                    } else {
                        new Dashboard(username).show(newStage);
                    }

                    stage.close();

                } else {
                    message.setText("Invalid credentials!");
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                message.setText("Database error!");
            }
        });

        // ================= ADD COMPONENTS =================
        card.getChildren().addAll(
                header,
                title,
                usernameField,
                passwordField,
                roleBox,
                loginButton,
                message,
                registerLink
        );

        root.getChildren().add(card);

        Scene scene = new Scene(root, 450, 550);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
}