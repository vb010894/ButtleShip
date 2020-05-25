package ru.vdzinovev.Enums;

/**
 * Перечиление типов сообщений.
 * @author Vboy
 * @version 1.0
 * @since 1.0
 */
public enum  MessageType {
    /**
     * Ошибка.
     */
    Error("/FXML/ErrorMessageScene.fxml"),
    Warning("/FXML/WarningMessageScene.fxml");

    /**
     * Текущее состояние.
     */
    private String state;

    /**
     * Конструктор перечисления.
     * @param targetMessage Текущее состояние перечисления.
     */
    MessageType(final String targetMessage) {
        this.state = targetMessage;
    }

    /**
     * Выводит состояние перечисление.
     * @return Результат
     */
    public String value() {
        return this.state;
    }
}
