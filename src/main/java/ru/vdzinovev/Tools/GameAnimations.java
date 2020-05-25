package ru.vdzinovev.Tools;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;


public class GameAnimations {

    /**
     * Анимация пульс + поворот на 360.
     * @param node Элемент для анимации
     * @return Амимация
     */
    public static Transition pulseAndRotate(final Node node) {

        ScaleTransition animation = new ScaleTransition(Duration.seconds(1));
        animation.setByX(-0.15);
        animation.setByY(0.15);
        animation.setCycleCount(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));


        RotateTransition secondAnimation =
                new RotateTransition(Duration.seconds(1));
        secondAnimation.setByAngle(360);
        secondAnimation.setCycleCount(1);


        Transition transition = new SequentialTransition(node,
                animation, secondAnimation, pause);
        transition.setCycleCount(Transition.INDEFINITE);
        transition.setAutoReverse(true);

        return transition;
    }

    /**
     * Постепенно появляющийся элемент.
     * @param node Элемент
     */
    public static void showFrame(final Node node) {
        FadeTransition animation = new FadeTransition(Duration.seconds(0.5));
        animation.setCycleCount(1);
        animation.setFromValue(0);
        animation.setToValue(1);
        Transition transition = new SequentialTransition(node,animation);
        transition.play();
    }

}
