package ru.vdzinovev.Tools;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.vdzinovev.Enums.GameMode;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Класс констант.
 * @author Vboy
 * @version 1.0
 * @since 1.0
 */
@Data
public class Constants {

    /**
     * Скрытый конструктор.
     */
    private Constants() {
        // DO nothing.
    }

    @Getter
    @Setter
    private static Properties gameProperties;

    @Getter
    @Setter
    private static GameMode gameMode = GameMode.EASY;

    @Getter
    @Setter
    private static int fieldsWidth = 9;

    @Getter
    @Setter
    private static int fieldsHeight = 9;

    @Getter
    @Setter
    private static int bombCount = 10;

    @Getter
    @Setter
    private static Properties languageBundle;

}
