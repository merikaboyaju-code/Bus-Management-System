package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;



public class Main extends Application {
    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView();
        loginView.show(stage);
//         OrderView view = new OrderView();
//            view.start(stage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

