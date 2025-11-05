package com.utils.application.globalExceptions.errorResponse;

public class ErrorResponse{
    private String errorOccurredDate;
    private String  message;
    private String resolveIssueDetails;


    public ErrorResponse(String errorOccurredDate, String resolveIssueDetails, String message) {
        this.errorOccurredDate = errorOccurredDate;
        this.resolveIssueDetails = resolveIssueDetails;
        this.message = message;
    }

    public String getErrorOccurredDate() {
        return errorOccurredDate;
    }

    public void setErrorOccurredDate(String errorOccurredDate) {
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
