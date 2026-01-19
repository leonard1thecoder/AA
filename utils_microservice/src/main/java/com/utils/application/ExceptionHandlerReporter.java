package com.utils.application;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionHandlerReporter {
    @Getter
    private static String  message;
    @Getter
    private static String resolveIssueDetails;
    @Getter
    private static String issueDateFormatted;

    public static String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        issueDateFormatted = issueDate.format(formatter);
        return issueDateFormatted;
    }

    public static String formatDateTime() {
        LocalDateTime issueDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        issueDateFormatted = issueDate.format(formatter);
        return issueDateFormatted;
    }

    public static String setMessage(String message) {
        ExceptionHandlerReporter.message = message;
        return message;
    }

    public static void setResolveIssueDetails(String resolveIssueDetails) {
        ExceptionHandlerReporter.resolveIssueDetails = resolveIssueDetails;
    }

}
