package ru.vdzinovev.Tools;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import ru.vdzinovev.Controllers.Scenes.StartWindowController;

import java.util.stream.Collectors;


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

    public static void switchScene(final Group targetScene,
                                   final boolean startScene) {
        Pane content = StartWindowController.getTargetContent();
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyValue contentTransformXValue =
                new KeyValue(content.layoutXProperty(),
                            content.getPrefHeight() * 3);
        KeyFrame contentTransformX = new KeyFrame(Duration.seconds(1),
                contentTransformXValue);
        timeline.setOnFinished(event -> switchEnd(targetScene, startScene));

        timeline.getKeyFrames().add(contentTransformX);
        timeline.play();
    }

    private static void switchEnd(final Group targetScene,
                                  final boolean startScene) {
        Pane content = StartWindowController.getTargetContent();
        if (!startScene) {
            content.getChildren().forEach(node -> node.setVisible(false));
            content.getChildren().add(targetScene);
        } else {
            content
                    .getChildren()
                    .stream()
                    .filter(node -> node instanceof Group)
                    .collect(Collectors.toList())
                    .forEach(node -> content
                                    .getChildren()
                                    .remove(node));
            content
                    .getChildren()
                    .stream()
                    .filter(node -> !node.isVisible())
                    .collect(Collectors.toList())
                    .forEach(node -> node.setVisible(true));
        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyValue contentTransformXValue =
                new KeyValue(content.layoutXProperty(),
                        0);
        KeyFrame contentTransformX = new KeyFrame(Duration.seconds(1),
                contentTransformXValue);

        timeline.getKeyFrames().add(contentTransformX);
        timeline.play();
    }

}
