package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class WaterTracker extends Application {

    int currentWater = 0;
    int dailyGoal = 2000;

    Label status;
    ProgressBar bar;
    TextField goalInput;

    @Override
    public void start(Stage stage) {
        Label title = new Label("Wasser Tracker 🚰");
        title.getStyleClass().add("title");

        goalInput = new TextField();
        goalInput.setPromptText("Ziel in ml");
        goalInput.setText(dailyGoal + "");

        Button saveGoal = new Button("Ziel setzen");
        Button addWater = new Button("+250 ml");

        Button resetButton = new Button("Reset");

        resetButton.setOnAction(e -> {
            currentWater = 0;
            updateUI();
        });

        bar = new ProgressBar(0);
        bar.setPrefWidth(300);

        status = new Label();

        updateUI();

        saveGoal.setOnAction(e -> {
            if (!goalInput.getText().isEmpty()) {
                dailyGoal = Integer.parseInt(goalInput.getText());
                updateUI();
            }
        });

        addWater.setOnAction(e -> {
            currentWater += 250;

            if (currentWater > dailyGoal) {
                currentWater = dailyGoal;
            }

            updateUI();
            checkGoalReached();

        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(
                title,
                goalInput,
                saveGoal,
                bar,
                status,
                addWater,
                resetButton
        );


        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        try {
            // Versuche erst den absoluten Pfad (Standard für Maven/Resources)
            java.net.URL styleSheet = getClass().getResource("/org/example/style.css");
            
            // Fallback: Versuche es relativ zur Klasse
            if (styleSheet == null) {
                styleSheet = getClass().getResource("style.css");
            }

            if (styleSheet != null) {
                scene.getStylesheets().add(styleSheet.toExternalForm());
            } else {
                System.err.println("Warning: style.css not found! Checked /org/example/style.css and relative path.");
            }
        } catch (Exception e) {
            System.err.println("Error loading style.css: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setTitle("Wasser Tracker");
        stage.show();
    }

    private void updateUI() {
        bar.setProgress((double) currentWater / dailyGoal);

        int rest = dailyGoal - currentWater;

        status.setText(
                "Getrunken: " + currentWater + " ml\nNoch: " + rest + " ml"
        );
    }

    private void checkGoalReached() {
        if (currentWater >= dailyGoal) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ziel erreicht 🎉");
            alert.setHeaderText(null);
            alert.setContentText("Glückwunsch! Du hast dein Tagesziel erreicht.");

            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}