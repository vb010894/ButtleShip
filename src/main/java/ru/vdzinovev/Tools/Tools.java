package ru.vdzinovev.Tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import ru.vdzinovev.Controllers.Scenes.StartWindowController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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


    public static void showErrorMessage() {
        String scenePath = "/FXML/ErrorMessageScene.fxml";
        Parent errorMessage;
        try {
            errorMessage = FXMLLoader
                    .load(
                            Tools.class
                                    .getResource(
                                            scenePath));
            Group group = new Group(errorMessage);
            Pane content = StartWindowController
                           .getTargetContent();
            group.setLayoutX(content.getPrefWidth() / 2 - 200);
            group.setLayoutY(content.getPrefHeight() / 2 - 100);
            List<Node> children = content.getChildren();
            children.add(group);
            GameAnimations.showFrame(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
