package com.utils.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonMethods {

    public static String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return issueDate.format(formatter);
    }
}
