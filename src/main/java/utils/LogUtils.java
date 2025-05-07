package utils;

import io.qameta.allure.Attachment;

public class LogUtils {
    private static final StringBuilder logs = new StringBuilder();

    public static void log(String message) {
        System.out.println(message);
        logs.append(message).append("\n");
    }

    @Attachment(value = "Log de ejecución", type = "text/plain")
    public static String attachLogs() {
        return logs.toString();
    }

    public static void clearLogs() {
        logs.setLength(0);
    }
}
