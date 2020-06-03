package ru.vdzinovev.Controllers.Messages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;


public class ConfirmMessageController extends MessageControllerBase {

    @FXML
    private ImageView messageIcon;

    @FXML
    private Button noButton;

    @FXML
    private Button yesButton;

    /**
     * Метод инициализации.
     */
    @FXML
    public void initialize() {
        this.setButtonAnimation(this.yesButton);
        this.setButtonAnimation(this.noButton);
        this.setMessageIcon();
    }

    @Override
    void setMessageIcon() {
        InputStream inputStream = this.getClass().getResourceAsStream(
                "/Images/closeIcon.png");
        Image img = new Image(inputStream);
        messageIcon.setImage(img);
    }
}
