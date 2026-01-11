package com.users.application.validators;

import com.users.application.exceptions.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;


@Component
public class UsersFieldsDataValidator {
    private static UsersFieldsDataValidator instance = new UsersFieldsDataValidator();
    private static Logger logger = LoggerFactory.getLogger(UsersFieldsDataValidator.class);
    @Getter
    private Short validatedAge;
    private String validateCellphoneNo;

    private UsersFieldsDataValidator() {
        super();
    }

    public static UsersFieldsDataValidator getInstance() {
        return instance;
    }

    public String validateIdentityNo(String identityNo) {
        logger.info("validating id number: {}", identityNo);
        LocalDate currentYearDate = LocalDate.now();

        logger.info("validating id number: {} , on date : {}", identityNo, currentYearDate);
        var validateIdentityNo = identityNo.trim();
        logger.info("removing empty spaces  id number: {} ", identityNo);

        if (validateIdentityNo == null) {

            var errorMessage = "validated id instance is null";
            var resolveIssue = "Contact AA Administrator";
            throw throwExceptionAndReport(new NullRequestException(errorMessage), errorMessage, resolveIssue);

        } else if (validateIdentityNo.isEmpty()) {
            var errorMessage = "Identity number is not inserted";
            var resolveIssue = "Please enter your identity number";
            throw throwExceptionAndReport(new IdentityNoIsEmptyException(errorMessage), errorMessage, resolveIssue);

        } else if (validateIdentityNo.length() == 13) {
            var firstTwoDigitOfId = validateIdentityNo.substring(0, 2);
            var currentYear = currentYearDate.getYear();
            int customerYearFromIdentityNo;
            if (firstTwoDigitOfId.startsWith("0")) {
                try {
                    customerYearFromIdentityNo = Integer.parseInt("20" + firstTwoDigitOfId);
                    var calculateAge = currentYear - customerYearFromIdentityNo;
                    validAge((short) calculateAge);
                    validSpecialCharactersAndAlphabets(validateIdentityNo.substring(2));
                    validatesDaysOfMonth((short) customerYearFromIdentityNo, Byte.parseByte(validateIdentityNo.substring(4, 6)), (byte) validateMonths(Byte.parseByte(validateIdentityNo.substring(2, 4))));
                    return validateIdentityNo;
                } catch (NumberFormatException e) {
                    var errorMessage = "Identity number contains invalid";
                    var resolveIssue = "Check your identity number";
                    throw throwExceptionAndReport(new IdentityNuContainsIncorrectValuesException(errorMessage), errorMessage, resolveIssue);
                }
            } else if (firstTwoDigitOfId.startsWith("9") || firstTwoDigitOfId.startsWith("8") || firstTwoDigitOfId.startsWith("7") || firstTwoDigitOfId.startsWith("6") || firstTwoDigitOfId.startsWith("5")) {
                try {
                    customerYearFromIdentityNo = Integer.parseInt("19" + firstTwoDigitOfId);
                    var calculateAge = currentYear - customerYearFromIdentityNo;
                    validAge((short) calculateAge);

                    validSpecialCharactersAndAlphabets(validateIdentityNo.substring(2));
                    validatesDaysOfMonth((short) customerYearFromIdentityNo, Byte.parseByte(validateIdentityNo.substring(4, 6)), (byte) this.validateMonths(Byte.parseByte(validateIdentityNo.substring(2, 4))));
                    return validateIdentityNo;
                } catch (NumberFormatException e) {
                    var errorMessage = "Identity number contains invalid characters or alphabets";
                    var resolveIssue = "Check your identity number";
                    throw throwExceptionAndReport(new IdentityNuContainsIncorrectValuesException(errorMessage), errorMessage, resolveIssue);

                }
            } else {
                var errorMessage = "You are young or older to use the system";
                var resolveIssue = "Check your identity number, it's either young or old";
                throw throwExceptionAndReport(new IdentityNuContainsIncorrectValuesException(errorMessage), errorMessage, resolveIssue);
            }
        } else {

            var errorMessage = "users entered identity number which is not equals to length of 13";
            var resolveIssue = "Check your identity number, it's either young or old";
            throw throwExceptionAndReport(new IdentificationNumberIsNot13DigitsException(errorMessage), errorMessage, resolveIssue);

        }


    }

    private int validateMonths(byte month) {
        try {
            return Month.of(month).getValue();
        } catch (DateTimeException e) {
            var errorMessage = "Month are out of bound, there is not month in " + month;
            var resolveIssue = "Check your identity number, month in your id is incorrect";
            throw throwExceptionAndReport(new UserDateTimeException(errorMessage), errorMessage, resolveIssue);

        }
    }

    private void validatesDaysOfMonth(Short year, Byte days, Byte month) {
        if (Year.isLeap(year)) {
            if (Month.of(month).getValue() == 2) {
                try {
                    MonthDay.of(month, days);
                } catch (DateTimeException e) {

                    var errorMessage = "In February days of month should not be greater 29 in leap year";
                    var resolveIssue = "Check your identity number, month in your id is incorrect";
                    throw throwExceptionAndReport(new UserDateTimeException(errorMessage), errorMessage, resolveIssue);


                }
            } else {
                try {
                    MonthDay.of(month, days);
                } catch (DateTimeException e) {
                    var errorMessage = "Month out of bound : " + e;
                    var resolveIssue = "Check your identity number, month in your id is incorrect";
                    throw throwExceptionAndReport(new UserDateTimeException(errorMessage), errorMessage, resolveIssue);

                }
            }
        } else {
            if (Month.of(month).getValue() == 2) {
                try {
                    if (MonthDay.of(month, days).getDayOfMonth() == 29) {
                        var errorMessage = "In February days of month should not be greater 28 in leap year";
                        var resolveIssue = "Check your identity number, month in your id is incorrect";
                        throw throwExceptionAndReport(new UserDateTimeException(errorMessage), errorMessage, resolveIssue);
                    }
                    MonthDay.of(month, days);
                } catch (DateTimeException e) {
                    var errorMessage = "Month out of bound : " + e;
                    var resolveIssue = "Check your identity number, month in your id is incorrect";
                    throw throwExceptionAndReport(new UserDateTimeException(errorMessage), errorMessage, resolveIssue);
                }
            } else {
                try {
                    MonthDay.of(month, days);
                } catch (DateTimeException e) {
                    var errorMessage = "Month out of bound : " + e;
                    var resolveIssue = "Check your identity number, month in your id is incorrect";
                    throw throwExceptionAndReport(new UserDateTimeException(errorMessage), errorMessage, resolveIssue);

                }
            }
        }
    }

    private void validAge(Short nonValidateAge) throws UserAgeException {
        logger.info("Age inserted by user is {}", nonValidateAge);
        if (nonValidateAge >= (short) 18) {
            if (nonValidateAge < 65) {
                validatedAge = nonValidateAge;
            } else {
                var errorMessage = "Unable to use the older than 65";
                var resolveIssue = "can't register you older than 65";
                throw throwExceptionAndReport(new UserAgeException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = "Unable to use the younger than 18";
            var resolveIssue = "can't register you younger than 18";
            throw throwExceptionAndReport(new UserAgeException(errorMessage), errorMessage, resolveIssue);
        }
    }


    private void validSpecialCharactersAndAlphabets(String nonValidateId) {
        try {
            Long.parseLong(nonValidateId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Identity number has characters or alphabets");
        }
    }

    public String validateCellphoneNo(String nonValidatedCellphoneNo) {
        var validateCellphoneNo = nonValidatedCellphoneNo.trim();
        if (validateCellphoneNo == null) {
            var errorMessage = "validated cellphone nu instance is null";
            var resolveIssue = "Contact AA Administrator";
            throw throwExceptionAndReport(new NullRequestException(errorMessage), errorMessage, resolveIssue);
        } else if (validateCellphoneNo.isEmpty()) {
            throw new IllegalArgumentException("Cellphone Number  is empty");
        } else if (validateCellphoneNo.startsWith("0")) {
            if (validateCellphoneNo.length() == 10) {
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo);
                return "+27" + validateCellphoneNo.substring(1);
            } else {
                var errorMessage = "Cellphone nu length is not 10";
                var resolveIssue = "Check your cellphone number";
                throw throwExceptionAndReport(new CellphoneNuException(errorMessage), errorMessage, resolveIssue);
            }
        } else if (validateCellphoneNo.startsWith("+27")) {
            if (validateCellphoneNo.length() == 12) {
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo.substring(1));
                return validateCellphoneNo;
            } else {
                var errorMessage = "Cellphone nu length is not 12";
                var resolveIssue = "Check your cellphone number";
                throw throwExceptionAndReport(new CellphoneNuException(errorMessage), errorMessage, resolveIssue);
            }
        } else {

            var errorMessage = "cellphone number inserted, is not RSA cellphone number. Make sure number start with +27 or 0";
            var resolveIssue = "Check your cellphone number";
            throw throwExceptionAndReport(new CellphoneNuException(errorMessage), errorMessage, resolveIssue);

        }

    }

    private void checkCellphoneNoHasSpecialCharactersOrAlphabets(String validateCellphoneNo) {
        try {
            Long.parseLong(validateCellphoneNo);
        } catch (NumberFormatException e) {
            var errorMessage = "Cellphone number contains invalid characters or alphabets";
            var resolveIssue = "Check your cellphone number";
            throw throwExceptionAndReport(new CellphoneNuException(errorMessage), errorMessage, resolveIssue);
        }

    }

    //#LLL1231@
    public String checkPasswordValidity(String password) {

        String passwordSpecialCharacters = "!@#$%^&*()?><;'{}|";
        var passwordMinLength = 8;
        var passwordMaxLength = 16;
        String passwordLowerAlphabets = "qwertyuioplkjhgfdsazxcvbnm";
        String passwordUpperAlphabets = "QWERTYUIOPLKJHGFDSAZXCVBNM";
        String passwordNumbers = "1234567890";

        try {
            if (password == null) {
                throw new NullPointerException("password is null");
            } else if (!checkPasswordContainsValidators(splitPasswordValidators(passwordSpecialCharacters), password)) {
                throw new PasswordStandardException("Password should have at one special characters {" + passwordSpecialCharacters + "}");
            } else if (!checkPasswordContainsValidators(splitPasswordValidators(passwordLowerAlphabets), password)) {
                throw new PasswordStandardException("password should have at least one small letter");
            } else if (!checkPasswordContainsValidators(splitPasswordValidators(passwordUpperAlphabets), password)) {
                throw new PasswordStandardException("password should have at least one upper case letter");
            } else if (!checkPasswordContainsValidators(splitPasswordValidators(passwordNumbers), password)) {
                throw new PasswordStandardException("password should have at least one number");
            } else if (password.length() < passwordMinLength) {
                throw new PasswordStandardException("Password minimum length starts from 8");
            } else if (password.length() > passwordMaxLength) {
                throw new PasswordStandardException("Password maximum length starts from 16");
            }
            return password;
        } catch (PasswordStandardException e) {
            var errorMessage = "password error "+ e.getMessage();
            var resolveIssue = e.getMessage();
            throw throwExceptionAndReport(new CellphoneNuException(errorMessage), errorMessage, resolveIssue);

        }

    }

    public String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return issueDate.format(formatter);
    }

    private List<String> splitPasswordValidators(String validateType) {
        List<String> list = new ArrayList<>();
        for (int x = 0; x < validateType.length(); x++) {
            list.add(String.valueOf(validateType.charAt(x)));
        }
        logger.info("password characters : {}", list);
        return list;
    }

    private boolean checkPasswordContainsValidators(List<String> listValidators, String password) {
        for (String listValidator : listValidators) {
            if (password.contains(listValidator)) {
                logger.info("Contained : {} ", password.contains(listValidator));
                return true;
            }
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Must contain '@'
        int atIndex = email.indexOf('@');
        if (atIndex < 1) { // '@' cannot be first
            return false;
        }

        // Must contain '.' after '@'
        int dotIndex = email.indexOf('.', atIndex);
        if (dotIndex < atIndex + 2) { // need at least one char between '@' and '.'
            return false;
        }

        // '.' cannot be the last character
        if (dotIndex == email.length() - 1) {
            return false;
        }

        // No spaces allowed
        if (email.contains(" ")) {
            return false;
        }

        return true;
    }


    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        // Trim spaces
        name = name.trim();

        // Must be at least 2 characters
        if (name.length() < 2) {
            return false;
        }

        // No digits allowed
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return false;
            }
        }

        // No special symbols except space or hyphen
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ' && c != '-') {
                return false;
            }
        }

        // Must start with a letter
        if (!Character.isLetter(name.charAt(0))) {
            return false;
        }

        return true;
    }


}