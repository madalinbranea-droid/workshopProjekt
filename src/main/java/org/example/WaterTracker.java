package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WaterTracker extends Application {

    int currentWater = 0;
    int dailyGoal = 2000;

    Label status;
    ProgressBar bar;
    TextField goalInput;

    @Override
    public void start(Stage stage) {
        Label title = new Label("Wasser Tracker 🚰");

        goalInput = new TextField();
        goalInput.setPromptText("Ziel in ml");

        Button saveGoal = new Button("Ziel setzen");
        Button addWater = new Button("+250 ml");

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
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(
                title,
                goalInput,
                saveGoal,
                bar,
                status,
                addWater
        );

        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);

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

    public static void main(String[] args) {
        launch();
    }
}