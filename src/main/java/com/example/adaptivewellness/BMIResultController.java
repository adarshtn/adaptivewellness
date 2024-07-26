package com.example.adaptivewellness;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class BMIResultController {

    @FXML
    private Label BmiLabel;

    @FXML
    private Label CategoryLabel;

    @FXML
    private Button suggestionButton;

    @FXML
    private Label Previousbmi;

    private String currentUsername;

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        System.out.println("Current username set to: " + username);
    }

    public void setBMIResult(double bmi) {
        if (currentUsername == null || currentUsername.isEmpty()) {
            System.err.println("Error: Username is null or empty");

            return;
        }

        BmiLabel.setText(String.format("%.2f", bmi));
        setBMICategory(bmi);
        System.out.println("Setting BMI result for user: " + currentUsername);

        try {
            DatabaseConnection.updateUserBMI(currentUsername, bmi);
            loadBMIs();
        } catch (SQLException e) {
            System.err.println("Error updating or fetching BMI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadBMIs() {
        double[] bmis = DatabaseConnection.fetchUserBMIs(currentUsername);
        if (bmis[0] != -1.0) {
            BmiLabel.setText(String.format("%.2f", bmis[0]));
            if (bmis[1] != -1.0) {
                Previousbmi.setText(String.format("%.2f", bmis[1]));
            } else {
                Previousbmi.setText("No previous BMI available");
            }
        } else {
            BmiLabel.setText("No BMI data available");
            Previousbmi.setText("No previous BMI available");
        }
    }

    private void setBMICategory(double bmi) {
        if (bmi < 18.5) {
            CategoryLabel.setText("Underweight");
        } else if (bmi >= 18.5 && bmi < 24.9) {
            CategoryLabel.setText("Normal weight");
        } else if (bmi >= 24.9 && bmi < 29.9) {
            CategoryLabel.setText("Over weight");
        } else {
            CategoryLabel.setText("Obesity");
        }
    }

    @FXML
    private void suggestion(ActionEvent event) throws IOException {
        String category = CategoryLabel.getText();
        String fxmlFile;

        switch (category) {
            case "Underweight":
                fxmlFile = "/suggestion.fxml";
                break;
            case "Normal weight":
                fxmlFile = "/normalweight.fxml";
                break;
            case "Over weight":
                fxmlFile = "/overweight.fxml";
                break;
            case "Obesity":
                fxmlFile = "/obesity.fxml";
                break;
            default:
                System.err.println("Unknown category: " + category);
                return;
        }

        Parent suggestionsParent = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene suggestionsScene = new Scene(suggestionsParent);

        Stage window = (Stage) suggestionButton.getScene().getWindow();
        window.setScene(suggestionsScene);
        window.show();
    }
}