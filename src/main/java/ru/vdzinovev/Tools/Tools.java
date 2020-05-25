package ru.vdzinovev.Tools;

import javafx.animation.Animation;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import ru.vdzinovev.Controllers.Scenes.StartWindowController;
import ru.vdzinovev.Enums.MessageType;

import java.io.IOException;
import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс инструментов для приложения.
 * @author Vboy
 * @
 */
public final class Tools {

    /**
     * Скрытый конструктор.
     */
    private Tools() { /*DO NOTHING*/ }

    /**
     * Базовый метод отрисовки окна сообщения.
     * Используется для вызова из методов
     * конкретных сообщений:
     * 1) Информативное сообщение
     * 2) Сообщение об ошибке
     * 3) Сообщение подтверждения
     * 4) и др.
     * @param type Тип сообщения JAVAFX
     * @param title Заглавие сообщения
     * @param header Краткое описание сообщения
     * @param content Полное описание сообщения
     *                или StackTrace ошибки
     */
    private static void showMessage(final Alert.AlertType type,
                                   final String title,
                                   final String header,
                                   final String content) {
        Alert message = new Alert(type);
        message.setTitle(title);
        message.setHeaderText(header);
        message.setContentText(content);
        message.show();
    }

    /**
     * Выводит окно предупреждения.
     * [*Используется для предупреждения пользователя*]
     * @param header Краткое описание предупреждения
     * @param content Полное описание предупреждения
     */
    public static void showWarning(final String header,
                                    final String content) {

         showMessage(
                Alert.AlertType.WARNING,
                "Предупреждение",
                header,
                content);
    }

    /**
     * Выводит окно исключения в коде.
     * @param header Краткое описание исключения
     * @param throwable Исключение
     */
    public static void showError(final String header,
                                  final Throwable throwable) {
         showMessage(Alert.AlertType.ERROR,
                    "Ошибка выполнения",
                    header,
                    getErrorInfoText(throwable));
    }

    /**
     * Переводит информацию об исключении в строку.
     * @param throwable Исключение
     * @return Строка информации
     */
    public static String getErrorInfoText(Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        builder.append("Сообщение\r\n")
                .append(throwable.getMessage())
                .append("\r\n")
                .append("StackTrace")
                .append("\r\n");

        Arrays
                .asList(throwable.getStackTrace())
                .forEach(
                            stack ->
                                    builder
                                            .append(stack)
                                            .append("\r\n"));
        return builder.toString();
    }


    public static void showMessage(final MessageType type,
                                        final String message) {
        Group messageWindow;
        try {
            switch (type) {

                default: messageWindow = showMessage(
                        type.value(),
                        message);
                break;
                case Warning:
                    messageWindow = showMessage(
                            type.value(),
                            message);
                break;
            }

            GameAnimations.showFrame(messageWindow);
        } catch (Exception ex) {
            showCrashMessage(ex);
        }

    }

    private static Group showMessage(final String scenePath,
                                    final String message)
    throws Exception {
        Parent errorMessage = FXMLLoader
                    .load(
                            Tools.class
                                    .getResource(
                                            scenePath));
            Group group = new Group(errorMessage);
            Pane content = StartWindowController
                    .getTargetContent();
            group.setLayoutX(content.getPrefWidth() / 2 - 200);
            group.setLayoutY(content.getPrefHeight() / 2 - 100);
            content.getChildren().add(group);

            ObservableList<Node> children = errorMessage
                                            .getChildrenUnmodifiable();


            Optional<Node> messageField =
                                        children
                                        .stream()
                                        .filter(node -> node
                                                        instanceof TextArea)
                                        .findAny();

        if (messageField.isPresent()) {

            TextArea area = (TextArea)  messageField.stream().collect(Collectors.toList()).get(0);
            area.setText(message);
        } else {
            throw new RuntimeException("Не найдено поле вывода ошибки");
        }

        TextArea area = (TextArea) children.get(2);
        area.setText(message);

        return group;
    }

    private static void showCrashMessage(Throwable throwable) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка основного фрейма вывода");
        alert.setContentText("Сообщение:\r\n"
                            + throwable.getMessage());
        alert.getButtonTypes().clear();
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().add(okButton);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == okButton) System.exit(-1);
        alert.setOnCloseRequest(handler -> System.exit(-1));
    }

}
