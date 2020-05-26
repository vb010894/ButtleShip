package ru.vdzinovev.Controllers.Scenes;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import ru.vdzinovev.Enums.GameMode;
import ru.vdzinovev.Tools.Constants;
import ru.vdzinovev.Tools.GameAnimations;
import ru.vdzinovev.Tools.Tools;

import java.util.Properties;

/**
 * Контроллер сцены настройки.
 * @author Vboy
 * @version 1.0
 * @since 1.0
 */
public class SettingSceneController {


    /**
     * Переключатели.
     */
    @FXML
    private ToggleGroup GameModeGroup;

    /**
     * Пользовательские настройки.
     */
    @FXML
    private CheckBox userSettingCheckBox;

    /**
     * Количество бомб.
     */
    @FXML
    private TextField bombCount;

    /**
     * Высота.
     */
    @FXML
    private TextField heightCount;

    /**
     * Ширина.
     */
    @FXML
    private TextField widthCount;

    /**
     * Кнопка "Созранить".
     */
    @FXML
    private Button saveButton;

    /**
     * Кнопка "Возврат".
     */
    @FXML
    private Button backButton;

    /**
     * Инициализация окна.
     */
    @FXML
    public void initialize() {
        this.saveButton.setOnMouseClicked(this::setGameConfig);
        this.backButton.setOnMouseClicked(this::backToScene);
        this.userSettingCheckBox.setOnMouseClicked(this::switchUserSettings);
        this.bombCount.setOnMouseClicked(event-> this.inputStart(bombCount));
        this.heightCount.setOnMouseClicked(event-> this.inputStart(heightCount));
        this.widthCount.setOnMouseClicked(event-> this.inputStart(widthCount));

    }

    /**
     * Сброс цвета в полях.
     * @param field Поле
     */
    private void inputStart(final TextField field) {
        field.setBackground(
                new Background(
                        new BackgroundFill(Paint.valueOf("white"),
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY)));
    }

    /**
     * Меняет настройки.
     * @param mouseEvent Действие
     */
    private void switchUserSettings(final MouseEvent mouseEvent) {
            this
                    .GameModeGroup
                    .getToggles()
                    .forEach(this::switchStateToggle);
            this.switchUserInput();
    }

    /**
     * Меняет состояние переключателей.
     * @param toggle Переключатели
     */
    private void switchStateToggle(final Toggle toggle) {
        RadioButton radio = (RadioButton) toggle;
        boolean state = !radio.isDisable();
        radio.setDisable(state);
    }

    /**
     * Изменение состояний полей
     * для пользовательского ввода.
     */
    private void switchUserInput() {
        boolean state = !this.bombCount.isEditable();
        this.bombCount.setEditable(state);
        this.heightCount.setEditable(state);
        this.widthCount.setEditable(state);
    }

    /**
     * Вернуться к стартовому окну.
     * @param mouseEvent Событие
     */
    private void backToScene(final MouseEvent mouseEvent) {
        System.out.println(Constants.getFieldsHeight());
        System.out.println(Constants.getFieldsWidth());
        System.out.println(Constants.getBombCount());
        GameAnimations.switchScene(
                        null,
                        true);
    }

    /**
     * Сохраняет настройки.
     * @param mouseEvent Событие
     */
    private void setGameConfig(final MouseEvent mouseEvent) {
        if (this.userSettingCheckBox.isSelected()) {
            this.configureByUserMode();
        } else {
            RadioButton tg = (RadioButton) this.GameModeGroup.getSelectedToggle();
            switch (tg.getId()) {
                default:
                    this.configureByToggleMode(
                            GameMode.EASY,
                            "Easy");
                case "mediumModeRadio":
                    this.configureByToggleMode(
                            GameMode.MEDIUM,
                            "Medium");
                case "hardModeRadio":
                    this.configureByToggleMode(
                            GameMode.HARD,
                            "Hard");
            }
        }
    }

    /**
     * Изменение настроек
     * пользовательским способом.
     */
    private void configureByUserMode() {

        Constants.setGameMode(GameMode.USER);
        int height;
        int width;
        int bombCount;

        try {
            height = Integer.parseInt(this.heightCount.getText());
            width = Integer.parseInt(this.widthCount.getText());
            bombCount = Integer.parseInt(this.bombCount.getText());
        } catch (Exception ex) {
            this.heightCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            this.widthCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            this.bombCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            Tools.showWarning(
                    Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Numeric.Field.Input.Header"),
                    Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Numeric.Field.Input.Content"));
            return;
        }

        if(!this.checkUserInput(width, height, bombCount)) {
            return;
        }

        Constants.setFieldsHeight(height);
        Constants.setFieldsWidth(width);
        Constants.setBombCount(bombCount);
    }

    /**
     * Проверяет пользовательский ввод.
     * @param width Поле ширина сетки
     * @param height Поле высота сетки
     * @param bombCount Количество бомб
     * @return Результат проверки
     */
    private boolean checkUserInput(final int width,
                                   final int height,
                                   final int bombCount) {

        if (bombCount > (height * width)) {
            Tools.showWarning(Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Bomb.Count.Header"),
                    Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Bomb.Count.Content"));

            this.bombCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            return false;
        }

        int maxHeight =
                Integer.parseInt(Constants
                        .getGameProperties()
                        .getProperty("Max.Width"));

        int maxWidth =
                Integer.parseInt(Constants
                        .getGameProperties()
                        .getProperty("Max.Width"));

        if (width > maxWidth) {
            Tools.showWarning(Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Max.Height.Header"),
                    Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Max.Height.Content"));

            this.bombCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            return false;
        }

        if (height > maxHeight) {
            Tools.showWarning(Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Max.Width.Header"),
                    Constants
                            .getLanguageBundle()
                            .getProperty("Warning.Max.Width.Content"));

            this.bombCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            return false;
        }

        return true;
    }

    /**
     * Настройки с помощью переключателей.
     * @param mode Текущее значение
     * @param modePrefix Префикс переменной
     */
    private void configureByToggleMode(
                                      final GameMode mode,
                                      final String modePrefix) {
        Constants.setGameMode(mode);
        Properties props = Constants.getGameProperties();
        int width = Integer.parseInt(
                                    props.getProperty(
                                                        modePrefix
                                                         + ".Mode"
                                                         + ".Width"));
        int height = Integer.parseInt(
                                    props.getProperty(
                                            modePrefix
                                                    + ".Mode"
                                                    + ".Height"));

        int bombCount = Integer.parseInt(
                                        props.getProperty(
                                                modePrefix
                                                        + ".Mode"
                                                        + ".Bomb"));
        Constants.setFieldsHeight(height);
        Constants.setFieldsWidth(width);
        Constants.setBombCount(bombCount);
    }

}
