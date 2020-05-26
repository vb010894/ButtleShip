package ru.vdzinovev.Controllers.Scenes;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import ru.vdzinovev.Enums.MessageType;
import ru.vdzinovev.Tools.Constants;
import ru.vdzinovev.Tools.GameAnimations;
import ru.vdzinovev.Tools.Tools;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GameSceneController {

    @FXML
    private TextField gameTime;

    @FXML
    private TextField bombCount;

    @FXML
    private Pane gameWindow;

    @FXML
    private Pane gameField;

    @FXML
    private ImageView repeatButton;

    @FXML
    private ImageView backButton;

    List<int[]> bombs = new LinkedList<>();

    int totalCell;
    int openedCellCount = 0;

    private int second = 0;
    private int minutes = 0;

    private Thread timer;

    /**
     * Инициализация окна.
     */
    @FXML
    public void initialize() {

        this.printField();
        this.generateBomb();
        this.startTimer();
        this.setBombCount(Constants.getBombCount());
        this.repeatButton.setOnMouseClicked(handler -> this.userRestart());
        this.backButton.setOnMouseClicked(handler -> backToStart());
    }

    private void userRestart() {
        final String message =
                                "Вы уверены что хотите перезапустить игру?";
        Tools.showMessage(MessageType.Message, message, "restartGame",
                "backToStart", this);
    }

    private void backToStart() {
        GameAnimations.switchScene(null, true);
    }

    /**
     * Запускает таймер.
     */
    private void startTimer() {
        timer = new Thread(this::changeTimer);
        timer.start();
    }

    /**
     * Процедура выполнения потока.
     */
    private void changeTimer() {
        try {
            while (true) {
                gameTime.setText(minutes + ":" + second);
                Thread.sleep(1000);
                second++;
                if(second == 60) {
                    second = 0;
                    minutes++;
                }
            }
        } catch (InterruptedException e) {
            Tools.showError("Ошибка потока", e);
        }
    }

    /**
     * Задает количество оставшихся бомб.
     * @param count Оставшееся количество.
     */
    private void setBombCount(final int count) {
        this.bombCount.setText(count + "");
    }

    /**
     * Генерирует бомбы.
     */
    private void generateBomb() {
        this.bombs = new ArrayList<>();
        Random random = new Random();

        int bombCount = Constants.getBombCount();

        for (int i = 0; i < bombCount; i++) {

            int columnIndex;
            int rowIndex;
            do {
                columnIndex = random.nextInt(8);
                rowIndex = random.nextInt(8);
            } while (this.isBound(columnIndex, rowIndex));

            bombs.add(new int[] {columnIndex, rowIndex});
        }


    }

    /**
     * Проверяет является ли кнопка бомбой.
     * @param columnIndex Индекс колонки.
     * @param rowIndex Индекс строки
     * @return Результат проверки
     */
    private boolean isBound(final int columnIndex, final int rowIndex) {
          return bombs
                .stream()
                .anyMatch(point -> point[0] == columnIndex & point[1] == rowIndex);

    }

    /**
     * Отрисовывает сетку.
     */
    private void printField() {

        int columnCount = Constants.getFieldsWidth();
        int rowCount = Constants.getFieldsHeight();

        this.totalCell = columnCount * rowCount;

        double height = gameField.getPrefHeight() / columnCount;
        double width =  gameField.getPrefWidth() / rowCount;
        double x = 0;
        double y = 0;

        for (int i = 0; i < rowCount; i++) {

            for (int j = 0; j < columnCount; j++) {
                Button cell = new Button();
                cell.setPrefWidth(width);
                cell.setLayoutX(x);
                cell.setLayoutY(y);
                cell.setPrefHeight(height);
                cell.setStyle(
                                "-fx-border-width: 2px;"
                                + "-fx-border-style: solid");
                cell.setId(j + "," + i);
                cell.setOnMouseClicked(event -> this.cellClicked(cell, event));
                this.gameField.getChildren().add(cell);
                x += width;
            }
            y += height;
            x = 0;
        }
    }

    /**
     * Нажатие на кнопку.
     * @param button Кнопка
     */
    private void cellClicked(Button button, MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY)
            this.mouseLeftClick(button);

        if (event.getButton() == MouseButton.SECONDARY)
            this.mouseRightClick(button);
    }

    private void mouseRightClick(Button button) {
        String[] buttonPictures = new String[] {"", "B", "?"};
        switch (button.getText()) {
            default:

                int targetBombCount =
                        Integer.parseInt(this.bombCount.getText());
                if(targetBombCount != 0) {
                    this.bombCount.setText(targetBombCount - 1 + "");
                    button.setText(buttonPictures[1]);
                } else {
                    button.setText(buttonPictures[2]);
                }
            break;
            case "B":
                button.setText(buttonPictures[2]);
                targetBombCount =
                        Integer.parseInt(this.bombCount.getText());
                this.bombCount.setText(targetBombCount + 1 + "");
            break;
            case "?":
                button.setText(buttonPictures[0]);
            break;
        }
    }

    /**
     * Срабатывает при нажатии на правую кнопку.
     * @param button Кнопка.
     */
    private void mouseLeftClick(Button button) {
        String[] point = button.getId().split(",");
        int columnIndex = Integer.parseInt(point[0].trim());
        int rowIndex =  Integer.parseInt(point[1].trim());
        if (isBound(columnIndex, rowIndex)) {
            this.openBombButton(columnIndex, rowIndex, button);
        } else {
            int count = checkBombsNear(columnIndex, rowIndex);
            this.openSaveButton(columnIndex, rowIndex,  count);
            if(count == 0) {
                this.openVertical(columnIndex, rowIndex);
            }

            long closedCell =
                    this.gameField.getChildren().stream().filter(node -> {
                        Button btn = (Button) node;
                        return btn.getText().length() == 0 | btn.getText().equals("B");
                    }).count();

            if(closedCell == Constants.getBombCount()) {
                this.gameWin();
            }
        }
    }

    /**
     * Открывает бомбу.
     * @param columnIndex Индекс колонки
     * @param rowIndex Индекс строки
     * @param button Нажатая кнопка
     */
    private void openBombButton(int columnIndex, int rowIndex, Button button) {

        button.setVisible(false);
        List<int[]> neighbor = this.getNeighbor(columnIndex, rowIndex);

        List<Button> fields = new ArrayList<>();
        neighbor.forEach(element ->
                                    fields.add(
                                                this
                                                .getElementById(element[0],
                                                                element[1])));

        InputStream inputStream = this.getClass().getResourceAsStream(
                "/Images/bombBlow.gif");
        Image img = new Image(inputStream);
        ImageView view = new ImageView(img);
        view.setLayoutY(button.getLayoutY());
        view.setLayoutX(button.getLayoutX());
        this.gameField.getChildren().add(view);


        this.blowButton(fields, view);

        final String message = "Вы попали на бомбу.\r\n Игра проиграна. "
                + "Желаете "
                + "повторить?";
        Tools.showMessage(MessageType.Message,
                message,
                "restartGame",
                "backToStart",
                this);
    }

    /**
     * Закрывает игру.
     */
    private void closeApp() {
        List<Node> startControls =
                StartWindowController
                        .getTargetContent()
                        .getChildren()
                        .stream()
                        .filter(node -> !node.isVisible())
                        .collect(Collectors.toList());
        this.gameWindow.getChildren().clear();
        this.gameField.setVisible(false);
        startControls.forEach(node -> node.setVisible(true));
    }

    /**
     * Перезапускает игру.
     */
    private void restartGame() {
        List<Node> groups = StartWindowController
                .getTargetContent()
                .getChildren()
                .stream()
                .filter(node -> node instanceof Group)
                .collect(Collectors.toList());
        groups.forEach(node -> {
            if(node.toString().contains("messageScene")) {
                StartWindowController
                        .getTargetContent()
                        .getChildren().remove(node);
            }
        });
        minutes = 0;
        second = 0;
        this.gameField.getChildren().clear();
        this.initialize();

    }

    /**
     * Вызрывает кнопки(Анимация)
     * @param fields Соседи кнопкт с бомбой
     * @param view Анимация взрыва
     */
    private void blowButton(List<Button> fields, ImageView view) {


        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setOnFinished(handler -> this.markStep(view));
        timeline.setDelay(Duration.seconds(0.5));

        fields.forEach(elem -> {
            elem.toFront();
            Random random = new Random();
            int maxY = (int) this.gameField.getPrefHeight();
            int maX = (int) this.gameField.getPrefWidth();
            int maxRotate = 320;

            int x = random.ints(0, maX).findFirst().getAsInt();
            int y = random.ints(0, maxY).findFirst().getAsInt();

            KeyValue xkv = new KeyValue(elem.layoutXProperty(),
                    x);
            KeyFrame xkf = new KeyFrame(Duration.seconds(1), xkv);

            KeyValue ykv = new KeyValue(elem.layoutYProperty(),
                    y);
            KeyFrame ykf = new KeyFrame(Duration.seconds(1), ykv);

            KeyValue rkv = new KeyValue(elem.rotateProperty(),
                    random.nextInt(maxRotate));
            KeyFrame rkf = new KeyFrame(Duration.seconds(1), rkv);

            timeline.getKeyFrames().add(xkf);
            timeline.getKeyFrames().add(ykf);
            timeline.getKeyFrames().add(rkf);

        });
        timeline.play();

    }

    /**
     * Оставляет след после взрыва
     * @param view Картинка с анимацией
     */
    private void markStep(ImageView view) {
        InputStream inputStream = this.getClass().getResourceAsStream(
                "/Images/blowMark.png");
        Image img = new Image(inputStream);
        view.setImage(img);
    }

    private Button getElementById(int columnIndex, int rowIndex) {
        return (Button) this.gameField
                .getChildren()
                .stream()
                .filter(element -> element.getId().equals(columnIndex + "," + rowIndex))
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * Открывает ячейки без бомб.
     * @param columnIndex Индекс колоники.
     * @param rowIndex Индекс строки
     * @param text Конличество бомб, стоящих рядом
     */
    private void openSaveButton(
            final int columnIndex,
            final int rowIndex,
            final int text) {
        Button button = getElementById(columnIndex, rowIndex);

        String textColor;

        switch (text) {
            default: textColor = "-fx-text-fill:green";
                break;
            case 1: textColor = "-fx-text-fill:#ffb846";
                break;
            case 2: textColor = "-fx-text-fill:blue";
                break;
            case 3: textColor = "-fx-text-fill:pink";
                break;
            case 4: textColor = "-fx-text-fill:brown";
                break;
            case 5: textColor = "-fx-text-fill:red";
                break;
            case 6: textColor = "-fx-text-fill:#ff5b65";
                break;
            case 7: textColor = "-fx-text-fill:purple";
                break;
            case 8: textColor = "-fx-text-fill:lightblue";
                break;
        }

        button.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-width: 2px;"
                        + "-fx-border-style: solid;"
                        + textColor);

        button.setText("" + text);
        openedCellCount++;
    }

    private void gameWin() {
        this.timer.interrupt();
        this.gameField.getChildren().forEach(node -> node.setDisable(true));
        final String message =
                                        "Вы выиграли игру."
                                        + "\r\n"
                                        + " Общее время: "
                                        + minutes
                                        + ":"
                                        + second;
            Tools.showMessage(MessageType.Message, message, "restartGame",
                    "backToStart", this);
    }

    /**
     * Открытие нулевый клеток по вертикали.
     * @param columnIndex Начальны индекс колнки кнопки
     * @param rowIndex Начальный индекс строки кнопки
     */
    private void openVertical(int columnIndex, int rowIndex) {
        for (int i = rowIndex; i < Constants.getFieldsHeight(); i++) {
            int t;
            if ((t = this.checkBombsNear(columnIndex, i)) == 0) {
                this.openHorizontal(columnIndex, i);
            } else {
                this.openSaveButton(columnIndex, i, t);
                break;
            }
        }

        for (int i = rowIndex; i >= 0; i--) {
            int t;
            if ((t = this.checkBombsNear(columnIndex, i)) == 0) {
                this.openHorizontal(columnIndex, i);
            } else {
                this.openSaveButton(columnIndex, i, t);
                break;
            }
        }
    }

    /**
     * Открытие нулевый клеток по горизонтали
     * @param columnIndex Начальный индекс колонки
     * @param rowIndex Начальный индекс строки
     */
    private void openHorizontal(int columnIndex, int rowIndex) {

            for (int i = columnIndex; i >= 0; i--) {
                int t;
                if((t = this.checkBombsNear(i, rowIndex)) == 0) {
                    this.openSaveButton(i, rowIndex, t);
                } else {
                    this.openSaveButton(i, rowIndex, t);
                    break;
                }
            }

            for (int i = columnIndex; i < Constants.getFieldsWidth(); i++) {
                int t;
                if((t = this.checkBombsNear(i, rowIndex)) == 0) {
                    this.openSaveButton(i, rowIndex, t);
                } else {
                    this.openSaveButton(i, rowIndex, t);
                    break;
                }
            }
    }



    /**
     * Вычисление координат соседних ячеек.
     * @param startColumn Индекс стартовой колонки.
     * @param startRow Индекс стартой строки
     * @return Список соседей
     */
    private List<int[]> getNeighbor(final int startColumn, final int startRow) {
        List<int[]> result = new ArrayList<>();

        // Проверка левой ячейки
        int leftPosition;
        if ((leftPosition = startColumn - 1) >= 0) {
            result.add(new int[] {leftPosition, startRow});
        }

        // Проверка правой ячейки
        int rightPosition;
        if ((rightPosition = startColumn + 1) <= Constants.getFieldsWidth() - 1) {
            result.add(new int[] {rightPosition, startRow});
        }

        // Проверка нижней ячейки
        int bottomPosition;
        if ((bottomPosition = startRow + 1) <= Constants.getFieldsHeight() - 1) {
            result.add(new int[] {startColumn, bottomPosition});
        }

        // Верхней верхней ячейки
        int topPosition;
        if ((topPosition = startRow - 1) >= 0) {
            result.add(new int[] {startColumn, topPosition});
        }

        // Проверка верхней левой ячейки
        if (leftPosition >= 0 & topPosition >= 0) {
            result.add(new int[] {leftPosition, topPosition});
        }

        // Проверка верхней правой ячейки
        if (rightPosition <= Constants.getFieldsWidth() - 1 & topPosition >= 0) {
            result.add(new int[] {rightPosition, topPosition});
        }

        // Проверка нижней левой ячейки
        if (leftPosition >= 0 & bottomPosition <= Constants.getFieldsHeight() - 1) {
            result.add(new int[] {leftPosition, bottomPosition});
        }

        // Проверка верхней правой ячейки
        if (rightPosition <= Constants.getFieldsWidth() - 1 & bottomPosition <= Constants.getFieldsHeight() - 1) {
            result.add(new int[] {rightPosition, bottomPosition});

        }

        return result;
    }

    /**
     * Проверка рядостящих бомб.
     * @param startColumn Индекс стартовой колонки.
     * @param startRow Индекс стартой строки
     * @return количество рядом стоящих бомб.
     */
    private int checkBombsNear(final int startColumn, final int startRow) {
        int bombCount;

        List<int[]> neighbor = this.getNeighbor(startColumn, startRow);
        bombCount = (int) neighbor
                .stream()
                .filter(element -> (isBound(element[0], element[1])))
                .count();

        return bombCount;
    }
}
