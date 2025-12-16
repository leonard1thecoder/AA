package com.utils.application.globalExceptions.errorResponse;

import com.utils.application.ResponseContract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@AllArgsConstructor
@Data
public class ErrorResponse implements ResponseContract {
    private String errorOccurredDate;
    private String  message;
    private String resolveIssueDetails;

}
