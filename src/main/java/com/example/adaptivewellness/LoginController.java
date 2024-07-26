package com.example.adaptivewellness;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void login(ActionEvent event) throws IOException {
        String username = nameField.getText();
        String password = passwordField.getText();

        if (DatabaseConnection.validateLogin(username, password)) {
            try {

                DatabaseConnection.updatePreviousBMI(username);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bmiinput.fxml"));
                Parent bmiInputParent = loader.load();


                BMIInputController bmiInputController = loader.getController();
                bmiInputController.setLoggedInUsername(username);

                Scene bmiInputScene = new Scene(bmiInputParent);


                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(bmiInputScene);
                window.show();
            } catch (SQLException e) {
                System.out.println("Error updating previous BMI: " + e.getMessage());
            }
        } else {

            System.out.println("Invalid login credentials");
        }
    }

    @FXML
    private void calculateBMI(ActionEvent event) throws IOException {
        Parent bmiInputParent = FXMLLoader.load(getClass().getResource("/bmiresult.fxml"));
        Scene bmiInputScene = new Scene(bmiInputParent);


        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(bmiInputScene);
        window.show();
    }
}