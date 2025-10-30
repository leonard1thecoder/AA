package com.aa.AA.utils.exceptions.exceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionHandlerReporter {
    private static String  message;
    private static String resolveIssueDetails;
    private static String issueDateFormatted;

    public static String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        issueDateFormatted = issueDate.format(formatter);
        return issueDateFormatted;
    }

    public static String getIssueDateFormatted() {
        return issueDateFormatted;
    }

    public static String getMessage() {
        return message;
    }

    public static String setMessage(String message) {
        ExceptionHandlerReporter.message = message;
        return message;
    }

    public static String getResolveIssueDetails() {
        return resolveIssueDetails;
    }

    public static void setResolveIssueDetails(String resolveIssueDetails) {
        ExceptionHandlerReporter.resolveIssueDetails = resolveIssueDetails;
    }

}
