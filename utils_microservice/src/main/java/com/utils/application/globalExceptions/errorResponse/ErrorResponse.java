package com.utils.application.globalExceptions.errorResponse;

import com.utils.application.ResponseContract;
import lombok.Getter;

@Getter
public class ErrorResponse implements ResponseContract {
    private String errorOccurredDate;
    private String  message;
    private String resolveIssueDetails;


    public ErrorResponse(String errorOccurredDate, String resolveIssueDetails, String message) {
        this.errorOccurredDate = errorOccurredDate;
        this.resolveIssueDetails = resolveIssueDetails;
        this.message = message;
    }

    public void setErrorOccurredDate(String errorOccurredDate) {
        this.errorOccurredDate = errorOccurredDate;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResolveIssueDetails(String resolveIssueDetails) {
        this.resolveIssueDetails = resolveIssueDetails;
    }
}
