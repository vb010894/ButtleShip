package ru.vdzinovev.Controllers.Messages;

import javafx.animation.Transition;
import javafx.scene.control.Button;
import ru.vdzinovev.Tools.GameAnimations;

import javax.swing.text.html.ImageView;
import java.io.InputStream;

public abstract class MessageControllerBase {


    /**
     * Анимация кнопки.
     * @param button
     */
    protected void setButtonAnimation(Button button) {
        double buttonWidth = button.getMaxWidth();
        double buttonHeight = button.getMaxWidth();

        Transition pulseOkAnimation = GameAnimations.pulseAndRotate(button);
        button.setOnMouseEntered(handler -> pulseOkAnimation.play());
        button.setOnMouseExited(handler -> {
            pulseOkAnimation.stop();
            button.setPrefWidth(buttonWidth);
            button.setPrefHeight(buttonHeight);
            button.setRotate(0);
        });
    }

    abstract void setMessageIcon();
}
