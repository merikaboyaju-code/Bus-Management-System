package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// FIX: Import the controller — no more direct SQL in the view
import controller.RegisterControl;

public class RegistrationPage {

    public void show(Stage stage) {

        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f4f4;");

        Label header = new Label("BUS MANAGEMENT SYSTEM");
        header.setStyle("-fx-text-fill: #337ab7; -fx-font-weight: bold;");

        Label title = new Label("Create New Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(250);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(250);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Admin", "Passenger");
        roleBox.setPromptText("Select Role");
        roleBox.setMinWidth(250);

        Button regButton = new Button("Register");
        regButton.setMinWidth(250);
        regButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-font-weight: bold;");

        Button backButton = new Button("Back to Login");
        backButton.setMinWidth(250);

        // Back to Login
        backButton.setOnAction(_ -> {
            new LoginView().show(stage);
        });

        // Register button logic
        regButton.setOnAction(_ -> {

            String username = userField.getText().trim();
            String password = passField.getText().trim();
            String role     = roleBox.getValue();

            // Validate all fields are filled
            if (username.isEmpty() || password.isEmpty() || role == null) {
                new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
                return;
            }

            // FIX: Use RegisterControl — no direct SQL in the view
            RegisterControl control = new RegisterControl();
            boolean success = control.register(username, password, role);

            if (success) {
                // FIX: Result is now checked — user sees correct feedback
                new Alert(Alert.AlertType.INFORMATION,
                    "Registration Successful! You can now log in.").show();
                new LoginView().show(stage);
            } else {
                new Alert(Alert.AlertType.ERROR,
                    "Registration failed. Username may already exist.").show();
            }
        });

        root.getChildren().addAll(
                header,
                title,
                userField,
                passField,
                roleBox,
                regButton,
                backButton
        );

        Scene scene = new Scene(root, 450, 550);
        stage.setTitle("Bus Management System - Registration");
        stage.setScene(scene);
        stage.show();
    }
}