package ru.vdzinovev.Controllers.Scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import lombok.Getter;
import ru.vdzinovev.Enums.MessageType;
import ru.vdzinovev.Tools.Tools;

import java.awt.*;
import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.Optional;

public class StartWindowController {

    /**
     * Текущий контент.
     */
    @Getter
    private static Pane targetContent;

    /**
     * Контент.
     */
    @FXML
    private Pane content;

    /**
     * Кнопка "Один игрок".
     */
    @FXML
    private Button singlePlayer;


    /**
     * Кнопка "Настройки".
     */
    @FXML
    private Button settings;

    /**
     * Кнопка "X".
     */
    @FXML
    private Button appClose;

    /**
     * Инициализация окна.
     */
    @FXML
    public void initialize() {
        targetContent = this.content;
        appClose.setOnAction(event -> this.closeApp());
        singlePlayer.setOnMouseClicked(event -> {
            try {
                this.playSingleMod();
            } catch (IOException io) {
                Tools.showError(
                        "Ошибка отрисовки сцены битвы",
                                io);

            }
        });
        settings.setOnMouseClicked(event -> {
            try {
                this.showSettings();
            } catch (IOException e) {
                Tools.showError("Ошибка загрузки настроек", e);
            }
        });
    }

    /**
     * Инициализирует одинуочную игру.
     */
    private void playSingleMod()
            throws IOException {
        String scenePath = "/FXML/FieldsView.fxml";
        Parent settingScene  = FXMLLoader
                .load(
                        getClass()
                                .getResource(
                                        scenePath));
        Group group = new Group(settingScene);
        List<Node> children = content.getChildren();
        children.forEach(child -> child.setVisible(false));
        children.add(group);
    }


    /**
     * Показывает настройки игры.
     */
    private void showSettings() throws IOException {
        Tools.showMessage(MessageType.Error, "Тестовая ошибка");
        /*String scenePath = "/FXML/SettingScene.fxml";
        Parent settingScene  = FXMLLoader
                               .load(
                                     getClass()
                                     .getResource(
                                             scenePath));
        Group group = new Group(settingScene);
        List<Node> children = content.getChildren();
        children.forEach(child -> child.setVisible(false));
        children.add(group);*/
    }

    /**
     * Выполняеи закрытие окна.
     * При нажатии на кнопку 'X'
     * Всплывает сообщение,
     * если нажать на "OK" закрывает приложение,
     * иначе закрывает сообщение
     */
    private void closeApp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText("Вы дейстивительно хотите выйти?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()
                &&
           result.get() == ButtonType.OK) System.exit(0);
        else  alert.close();
    }
}
