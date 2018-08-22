package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;


/**
 * Created by KCA on 6/27/2018.
 */

public class AboutDialog extends Dialog {

    private String title = "About KingGame";
    private String text = "KingGame \nVersion 1.0 (Build 1024) " +
            "\n\u00A9 2018 Kingsley Ugwudinso. All rights reserved. " +
            "\nKingGame is personal project influenced by the love for chess. " +
            "\nIn other words, it's a simple chess game with some basic features.";
    private String path = "/sample/res/images/icon.jpg";

    public AboutDialog(Window window) {
        this.initOwner(window);
        this.setTitle(title);
        this.setWidth(400);
        this.setHeight(250);
        this.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> this.hide());
        this.getDialogPane().getScene().setOnKeyPressed((event) -> {
            KeyCode keyCode = event.getCode();
            if(keyCode.compareTo(KeyCode.ESCAPE) == 0 || keyCode.compareTo(KeyCode.ENTER) == 0){
                this.getDialogPane().getScene().getWindow().hide();
            }
        });
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(this.getWidth(), this.getHeight());

        Label label = new Label(text);
        ImageView image = new ImageView(path);
        image.setFitWidth(100);
        image.setFitHeight(100);
        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.setCache(true);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(image, label);
        hBox.setSpacing(10);

        Button okButton = new Button("Ok");
        okButton.setPrefWidth(100);
        okButton.setPrefHeight(20);
        okButton.setOnAction((event) -> this.getDialogPane().getScene().getWindow().hide());

        pane.getChildren().addAll(okButton, hBox);
        AnchorPane.setBottomAnchor(okButton, -20.0);
        AnchorPane.setRightAnchor(okButton, -5.0);
        AnchorPane.setTopAnchor(hBox, 0.0);

        this.getDialogPane().setContent(pane);

        this.show();
    }

}
