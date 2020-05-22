package ru.vdzinovev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.vdzinovev.Tools.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Hello world!
 *
 */
public class Main extends Application {

    /**
     * Главое окно.
     */
    public static Parent sRoot;

    public static Stage sPrimaryScene;

    /**
     * Координата Х нажатия мыши.
     * Используется для
     * перетаскивания окна.
     */
    private static double sOffsetX;

    /**
     * Координата Y нажатия мыши.
     * Используется для
     * перетаскивания окна.
     */
    private static double sOffsetY;

    /**
     * Задает координаты нажатия мыши.
     * @param event Событие нажатия
     */
    private void sGetDragCoordinateOffset(MouseEvent event) {
        sOffsetX = event.getSceneX();
        sOffsetY = event.getSceneY();
    }

    /**
     * Перемещает окно по рабочему столу.
     * Высчтитывает отностильные координаты перемещения
     * @param dragEvent События перетаскивания
     * @param primaryStage Главная сцена
     */
    private void sDragWindow(MouseEvent dragEvent, Stage primaryStage) {
        primaryStage.setX(dragEvent.getScreenX() - sOffsetX);
        primaryStage.setY(dragEvent.getScreenY() - sOffsetY);
    }

    /**
     * Выполняется при запуске приложения.
     * Если возникают проблемы запуска,
     * выполнение останавливается
     * @param primaryStage Главная сцена.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
           Properties gameProps = new Properties();
            InputStream stream =  Main.class.getResourceAsStream(
                                           "/GameConfig"
                                                   + "/GameConfigs"
                                                   + ".properties");

            gameProps.load(stream);
            Constants.setGameProperties(gameProps);
            sRoot = FXMLLoader.load(getClass().getResource(
                                                    "/FXML"
                                                            + "/StartWindow"
                                                            + ".fxml"));

            ResourceBundle lang = ResourceBundle.getBundle("Texts"
                    + ".ru-Ru");
            Constants.setLanguageBundle(lang);
            sRoot.setOnMousePressed(this::sGetDragCoordinateOffset);
            sRoot.setOnMouseDragged(event -> sDragWindow(event, primaryStage));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("Сапер");
            primaryStage.setScene(new Scene(sRoot, 1200, 600));
            primaryStage.show();
            sPrimaryScene = primaryStage;
        }  catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }


    }

    /**
     * Запускает приложение.
     * @param args Аргументы запуска
     */
    public static void main( String[] args ) {
        launch(args);
    }
}
