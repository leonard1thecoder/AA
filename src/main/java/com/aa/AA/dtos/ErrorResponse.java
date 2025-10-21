package com.aa.AA.dtos;

import java.time.LocalDateTime;

public class ErrorResponse{
    private LocalDateTime errorOccurredDate;
    private String  message;
    private String resolveIssueDetails;


    public ErrorResponse(LocalDateTime errorOccurredDate, String resolveIssueDetails, String message) {
        this.errorOccurredDate = errorOccurredDate;
        this.resolveIssueDetails = resolveIssueDetails;
        this.message = message;
    }


    public LocalDateTime getErrorOccurredDate() {
        return errorOccurredDate;
    }

    public void setErrorOccurredDate(LocalDateTime errorOccurredDate) {
        this.errorOccurredDate = errorOccurredDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResolveIssueDetails() {
        return resolveIssueDetails;
    }

    public void setResolveIssueDetails(String resolveIssueDetails) {
        this.resolveIssueDetails = resolveIssueDetails;
    }
}
