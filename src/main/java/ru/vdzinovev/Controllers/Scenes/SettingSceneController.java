package ru.vdzinovev.Controllers.Scenes;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import ru.vdzinovev.Controllers.StartWindowController;
import ru.vdzinovev.Enums.GameMode;
import ru.vdzinovev.Tools.Constants;
import ru.vdzinovev.Tools.Tools;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SettingSceneController {


    @FXML
    private ToggleGroup GameModeGroup;

    @FXML
    private CheckBox userSettingCheckBox;

    @FXML
    private TextField bombCount;

    @FXML
    private TextField heightCount;

    @FXML
    private TextField widthCount;

    @FXML
    private Button saveButton;

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

    private void inputStart(TextField field) {
        field.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"),
                                                    CornerRadii.EMPTY,
                                                    Insets.EMPTY)));
    }


    private void switchUserSettings(MouseEvent mouseEvent) {
            this.GameModeGroup.getToggles().forEach(this::switchStateToggle);
            this.switchUserInput();
    }

    private void switchStateToggle(Toggle toggle) {
        RadioButton radio = (RadioButton) toggle;
        boolean state = !radio.isDisable();
        radio.setDisable(state);
    }

    private void switchUserInput() {
        boolean state = !this.bombCount.isEditable();
        this.bombCount.setEditable(state);
        this.heightCount.setEditable(state);
        this.widthCount.setEditable(state);
    }

    private void backToScene(MouseEvent mouseEvent) {
       List<Node> thisScene = StartWindowController
                                                    .getTargetContent()
                                                    .getChildren()
                                                    .stream()
                                                    .filter(child -> child.isVisible())
                                                    .collect(Collectors.toList());

       List<Node> startScene = StartWindowController
                                                   .getTargetContent()
                                                   .getChildren()
                                                   .stream()
                                                   .filter(child -> !child.isVisible())
                                                   .collect(Collectors.toList());
        thisScene.forEach(found -> found.setVisible(false));
        startScene.forEach(found -> found.setVisible(true));
    }

    private void setGameConfig(MouseEvent mouseEvent) {

        if (userSettingCheckBox.isSelected()) {
            this.configureByUserMode();
            return;
        }

        RadioButton tg = (RadioButton) this.GameModeGroup.getSelectedToggle();
        switch (tg.getId()) {
            default                 : this.configureByToggleMode(
                                                                GameMode.EASY,
                                                                "Easy");
            case "mediumModeRadio"  : this.configureByToggleMode(
                                                                GameMode.MEDIUM,
                                                                "Medium");
            case "hardModeRadio"    : this.configureByToggleMode(
                                                                GameMode.HARD,
                                                                "Hard");
        }
    }

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
                            .getString("Warning.Numeric.Field.Input.Header"),
                    Constants
                            .getLanguageBundle()
                            .getString("Warning.Numeric.Field.Input.Content"));
            return;
        }

        if(this.checkUserInput(width, height, bombCount)) {
            return;
        }

        Constants.setFieldsHeight(height);
        Constants.setFieldsWidth(width);
        Constants.setBombCount(bombCount);
    }

    private boolean checkUserInput(final int width,
                                   final int height,
                                   final int bombCount) {

        if (bombCount > (height * width)) {
            Tools.showWarning(Constants
                            .getLanguageBundle()
                            .getString("Warning.Bomb.Count.Header"),
                    Constants
                            .getLanguageBundle()
                            .getString("Warning.Bomb.Count.Content"));

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
                            .getString("Warning.Max.Height.Header"),
                    Constants
                            .getLanguageBundle()
                            .getString("Warning.Max.Height.Content"));

            this.bombCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            return false;
        }

        if (height > maxHeight) {
            Tools.showWarning(Constants
                            .getLanguageBundle()
                            .getString("Warning.Max.Width.Header"),
                    Constants
                            .getLanguageBundle()
                            .getString("Warning.Max.Width.Content"));

            this.bombCount.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            return false;
        }

        return true;
    }

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
