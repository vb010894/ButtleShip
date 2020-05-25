package ru.vdzinovev.Controllers.Messages;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ErrorMessageController extends MessageControllerBase {


    @FXML
    private Button okButton;

    @FXML
    private TextArea contentField;

    @FXML
    public void initialize() {
         this.setButtonAnimation(okButton);
         System.out.println(okButton.getOnMouseEntered());
    }
}
