package ru.vdzinovev.Controllers.Scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import ru.vdzinovev.Enums.MessageType;
import ru.vdzinovev.Tools.GameAnimations;
import ru.vdzinovev.Tools.Tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

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
     * Логотип.
     */
    @FXML
    private ImageView logoView;

    /**
     * Инициализация окна.
     */
    @FXML
    public void initialize() {
        InputStream inputStream =
                this
                .getClass()
                .getResourceAsStream("/Images/bomb.png");
        Image img = new Image(inputStream);
        this.logoView.setImage(img);
        targetContent = this.content;
        appClose.setOnAction(event -> this.closeApp());
        singlePlayer.setOnMouseClicked(event -> {
            try {
                this.playSingleMod();
            } catch (IOException io) {
                Tools.showError("Ошибка Сцены игры", io);
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
        GameAnimations.switchScene(group, false);
    }


    /**
     * Показывает настройки игры.
     */
    private void showSettings() throws IOException {

        String scenePath = "/FXML/SettingScene.fxml";
        Parent settingScene  = FXMLLoader
                               .load(
                                     getClass()
                                     .getResource(scenePath));
        Group group = new Group(settingScene);
        GameAnimations.switchScene(group, false);
    }

    /**
     * Закрытие приложение
     * по кнопке "Да".
     */
    private void closeAppYesButtonEvent() {
        System.exit(0);
    }

    /**
     * Закрытие окна сообщения
     * по кнопке "Нет".
     */
    private void closeAppNoButtonEvent() {
        List<Node> message =
                    this
                    .content
                    .getChildren()
                    .stream()
                    .filter(node -> node instanceof Group)
                    .collect(Collectors.toList());

        if (message.size() > 0) {
            message.forEach(node -> this
                                        .content
                                        .getChildren()
                                        .remove(node));
        }
    }

    /**
     * Выполняеи закрытие окна.
     * При нажатии на кнопку 'X'
     * Всплывает сообщение,
     * если нажать на "OK" закрывает приложение,
     * иначе закрывает сообщение
     */
    private void closeApp() {
        Tools.showMessage(MessageType.Message,
                "Вы дейстивительно хотите выйти?",
                "closeAppYesButtonEvent",
                "closeAppNoButtonEvent",
                this);
    }
}
