package org.example;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.util.Duration;

public class WaterTracker extends Application {

    int currentWater = 0;
    int dailyGoal = 2000;

    Label status;
    ProgressBar bar;
    TextField goalInput;
    Label dateLabel;

    @Override
    public void start(Stage stage) {
        Label title = new Label("Wasser Tracker 🚰");
        title.getStyleClass().add("title");
        
        Label goalLabel = new Label("Tagesziel:");
        goalLabel.setStyle("-fx-font-weight: bold;");

        goalInput = new TextField();
        goalInput.setPromptText("ml");
        goalInput.setText(dailyGoal + "");
        goalInput.setPrefWidth(100);

        dateLabel = new Label();

        dateLabel.setText(
                "Heute: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );

        Button saveGoal = new Button("Setzen");
        saveGoal.getStyleClass().add("button");

        HBox goalBox = new HBox(10, goalLabel, goalInput, saveGoal);
        goalBox.setAlignment(Pos.CENTER);

        Button addWater = new Button("Wasser trinken (+250 ml)");
        addWater.setMaxWidth(Double.MAX_VALUE);
        addWater.getStyleClass().add("button");

        Button resetButton = new Button("Tag zurücksetzen");
        resetButton.getStyleClass().add("reset-button");
        resetButton.setMaxWidth(Double.MAX_VALUE);

        resetButton.setOnAction(e -> {
            currentWater = 0;
            updateUI();
        });

        bar = new ProgressBar(0);
        bar.setMaxWidth(Double.MAX_VALUE);

        status = new Label();
        status.getStyleClass().add("status-label");

        updateUI();

        saveGoal.setOnAction(e -> {
            try {
                if (!goalInput.getText().isEmpty()) {
                    dailyGoal = Integer.parseInt(goalInput.getText());
                    updateUI();
                }
            } catch (NumberFormatException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "Bitte eine gültige Zahl eingeben.");
                error.show();
            }
        });

      addWater.setOnAction(e -> {
        currentWater += 250;
        if (currentWater > dailyGoal) {
          currentWater = dailyGoal;
        }

        // NEUE ANIMATION:
        PauseTransition pause = new PauseTransition(Duration.millis(100));
        pause.play();

        updateUI();
        checkGoalReached();
      });

      VBox layout = new VBox(20);  // ← Abstand zwischen Elementen
      layout.setStyle("-fx-padding: 30;");  // ← Innen-Rand
      layout.setAlignment(Pos.TOP_CENTER);  // ← Oben zentriert
        layout.getChildren().addAll(
                dateLabel,
                title,
                goalBox,
                new Separator(),
                status,
                bar,
                addWater,
                new Region(), // Spacer
                resetButton
        );
        VBox.setVgrow(layout.getChildren().get(6), Priority.ALWAYS);


        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 450, 500);
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

    // ÄNDERE DIESE ZEILE:
    status.setText(
        "Getrunken: " + currentWater + " ml\nNoch: " + rest + " ml"
    );

    // ZU DIESER:
    status.setText(
        String.format("%.1f L von %.1f L\n%d ml verbleibend",
            currentWater / 1000.0,
            dailyGoal / 1000.0,
            rest)
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