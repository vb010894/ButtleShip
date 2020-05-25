package ru.vdzinovev.Controllers.Messages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

/**
 * Класс вывода ошибки.
 * @author Vboy
 * @version 1.0
 * @since 1.0
 */
public class ErrorMessageController
extends MessageControllerBase {


    /**
     * Кнопка "ОК".
     */
    @FXML
    private Button okButton;

    /**
     * Иконка.
     */
    @FXML
    private ImageView messageIcon;

    /**
     * Метод инициализации.
     */
    @FXML
    public void initialize() {
         this.setButtonAnimation(okButton);
         this.setMessageIcon();
         this.okButton.setOnMouseClicked(handler -> System.exit(-1));

    }

    /**
     * Добавляет иконку к сообщению.
     */
    @Override
    void setMessageIcon() {
        InputStream inputStream = this.getClass().getResourceAsStream(
                "/Images/closeIcon.png");
        Image img = new Image(inputStream);
        messageIcon.setImage(img);
    }


}
