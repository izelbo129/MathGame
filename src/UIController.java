import javafx.animation.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.Scene;

import java.nio.file.CopyOption;
import java.util.Stack;

public class UIController {

    private final StackPane mathPane;

    private final MathController mathController;

    private Label setLabel;
    private Button undoButton;

    private Button restartButton;
    private Label debugLabel;
    private StackPane optionsPane;

    public UIController() {
        this.mathPane = new StackPane();
        this.mathController = new MathController(this.mathPane,this);
        setUpUIElements();

    }


    public void setUpUIElements() {
        Label targetLabel = new Label("Target Number: " + this.mathController.getTarget());
        this.setLabel = new Label("Number Set: " + this.mathController.getSet());
        targetLabel.setStyle("-fx-font-size: 20px;");
        this.setLabel.setStyle("-fx-font-size: 24px;");
        this.undoButton = new Button("Undo");
        this.undoButton.setStyle("-fx-background-color: #F1F1F1FF;");
        this.undoButton.setFocusTraversable(true);
        createOptionsPane();




        this.undoButton.setOnAction(event -> {
            this.mathController.undo();
        });


        HBox buttonBox = new HBox(100, this.undoButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setPadding(new Insets(25,25,25,25));


        VBox vert = new VBox(20, targetLabel, this.setLabel);
        vert.setAlignment(Pos.TOP_CENTER);
        vert.setPadding(new Insets(150, 0, 0, 0));


        this.mathPane.getChildren().addAll(vert,buttonBox);
        this.mathPane.setStyle("-fx-background-color: #fafafa;");

    }




    public void setSetLabel(String labelText) {
        this.setLabel.setText(labelText);
    }

    public Parent getRoot() {
        return this.mathPane;
    }

    public void createOptionsPane() {

        this.restartButton = new Button("Restart");
        this.restartButton.setStyle("-fx-background-color: #c5c5c5;");

        Button quitButton = new Button("Quit");

        quitButton.setStyle("-fx-background-color: #c5c5c5;");

        Button exitButton = new Button("<-- Go Back");
        exitButton.setStyle("-fx-background-color: #c5c5c5;");

        StackPane exitPane = new StackPane(exitButton);
        exitPane.setAlignment(Pos.TOP_LEFT);
        exitPane.setPadding(new Insets(-125,50,0,50));


        this.optionsPane = new StackPane();

        this.optionsPane.setPrefSize(950, 560);

        Rectangle background = new Rectangle(this.optionsPane.getPrefWidth(), this.optionsPane.getPrefHeight());
        background.setFill(Color.WHITE);

        Text label = new Text("Options");
        label.setFont(Font.font(50));
        VBox optionsBox = new VBox(exitPane, label, this.restartButton, quitButton);

        optionsBox.setSpacing(50);
        optionsBox.setAlignment(Pos.CENTER);
        this.optionsPane.getChildren().addAll(background, optionsBox);


        this.restartButton.setOnAction(event -> {
            System.out.println("DEBUG: RESTART");
            UIController newController = new UIController();
            Scene scene = this.restartButton.getScene();
            scene.setRoot(newController.getRoot());
            this.optionsPane.toBack();
        });

        quitButton.setOnAction(event -> {
            System.exit(0);
        });


        exitButton.setOnAction(event -> {
            this.mathPane.setStyle("-fx-background-color: #fafafa;");
            this.optionsPane.setOpacity(0);
            this.optionsPane.toBack();
            this.mathController.setFocusInput();

        });


        this.optionsPane.setOpacity(0);
        this.mathPane.getChildren().add(this.optionsPane);


    }

    public void showOptionsPane() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), this.optionsPane);
        this.optionsPane.toFront();
        this.mathPane.setStyle("-fx-background-color: #efefef;");
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        this.restartButton.toFront();
    }


    public void undoButtonColorGray() {
        this.undoButton.setStyle("-fx-background-color: #f1f1f1;");
    }

    public void undoButtonColorDefault() {
        this.undoButton.setStyle("-fx-background-color: #c5c5c5;");
    }

    public void setDebugLabel(String label) {
        this.debugLabel.setText(label);
    }
}


