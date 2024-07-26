package com.example.adaptivewellness;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;

public class BMIInputController {

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private Button calculateButton;

    private String loggedInUsername;

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    @FXML
    private void calculateBMI(ActionEvent event) throws IOException {
        try {

            double weight = Double.parseDouble(weightField.getText());
            double height = Double.parseDouble(heightField.getText()) / 100;
            double bmi = calculateBMIValue(weight, height);
            storeBMIInDatabase(bmi);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bmiresult.fxml"));
            Parent root = loader.load();
            BMIResultController controller = loader.getController();
            controller.setCurrentUsername(loggedInUsername);
            controller.setBMIResult(bmi);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers for weight and height.");
        } catch (SQLException e) {
            System.out.println("Error storing BMI in database: " + e.getMessage());
        }
    }

    private double calculateBMIValue(double weight, double height) {
        return weight / (height * height);
    }
    private void storeBMIInDatabase(double bmi) throws SQLException {
        DatabaseConnection.updateUserBMI(loggedInUsername, bmi);
    }
}