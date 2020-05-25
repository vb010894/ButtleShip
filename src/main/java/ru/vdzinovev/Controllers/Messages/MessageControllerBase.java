package ru.vdzinovev.Controllers.Messages;

import javafx.animation.Transition;
import javafx.scene.control.Button;
import ru.vdzinovev.Tools.GameAnimations;

public class MessageControllerBase {


    /**
     * Анимация кнопки.
     * @param button
     */
    public void setButtonAnimation(Button button) {
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

}
