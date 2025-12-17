package com.users.application.validators;

import com.users.application.exceptions.IdentificationNumberIsNot13DigitsException;
import com.users.application.exceptions.PasswordStandardException;
import com.users.application.exceptions.UserAgeException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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
            throw new NullPointerException("validated id instance is null");
        } else if (validateIdentityNo.isEmpty()) {
            throw new IllegalArgumentException("identity number is empty");
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
                    throw new NumberFormatException("Identity contains characters or alphabets");
                }
            } else if (firstTwoDigitOfId.startsWith("9") || firstTwoDigitOfId.startsWith("8") || firstTwoDigitOfId.startsWith("7") || firstTwoDigitOfId.startsWith("6") || firstTwoDigitOfId.startsWith("5")) {
                try {
                    customerYearFromIdentityNo = Integer.parseInt("19" + firstTwoDigitOfId);
                    var calculateAge = currentYear - customerYearFromIdentityNo;
                    validAge((short) calculateAge);
                    logger.info("Months from id {} and days from id {} for 90s ");

                    validSpecialCharactersAndAlphabets(validateIdentityNo.substring(2));
                    validatesDaysOfMonth((short) customerYearFromIdentityNo, Byte.parseByte(validateIdentityNo.substring(4, 6)), (byte) this.validateMonths(Byte.parseByte(validateIdentityNo.substring(2, 4))));
                    return validateIdentityNo;
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Identity contains characters or alphabets");

                }
            } else {
                throw new RuntimeException("Identity number is not valid check identity number, check first digit of account");
            }
        } else {
            throw new IdentificationNumberIsNot13DigitsException("users entered identity number which is not equals to length of 13");
        }


    }

    private int validateMonths(byte month) {
        try {
            return Month.of(month).getValue();
        } catch (DateTimeException e) {
            throw new DateTimeException("Month are out of bound, there is not month in " + month);
        }
    }

    private MonthDay validatesDaysOfMonth(Short year, Byte days, Byte month) {
        if (Year.isLeap(year)) {
            if (Month.of(month).getValue() == 2) {
                try {
                    return MonthDay.of(month, days);
                } catch (DateTimeException e) {
                    throw new DateTimeException("In February days of month should not be greater 29 in leap year");
                }
            } else {
                try {
                    return MonthDay.of(month, days);
                } catch (DateTimeException e) {
                    throw new DateTimeException(e.getMessage());
                }
            }
        } else {
            if (Month.of(month).getValue() == 2) {
                try {
                    if (MonthDay.of(month, days).getDayOfMonth() == 29) {
                        throw new DateTimeException("In February days of month should not be greater 28");
                    }
                    return MonthDay.of(month, days);
                } catch (DateTimeException e) {
                    throw new DateTimeException(e.getMessage());
                }
            } else {
                try {
                    return MonthDay.of(month, days);
                } catch (DateTimeException e) {
                    throw new DateTimeException(e.getMessage());
                }
            }
        }
    }

    private short validAge(Short nonValidateAge) throws UserAgeException {
        logger.info("Age inserted by user is {}", nonValidateAge);
        if (nonValidateAge >= (short) 18) {
            if (nonValidateAge < 65)
                return validatedAge = nonValidateAge;
            else
                throw new UserAgeException("Unable to use the older than 65");
        } else {
            throw new UserAgeException("You younger than 18, access denied");
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
            throw new NullPointerException("Password instance is null");
        } else if (validateCellphoneNo.isEmpty()) {
            throw new IllegalArgumentException("Cellphone Number  is empty");
        } else if (validateCellphoneNo.startsWith("0")) {
            if (validateCellphoneNo.length() == 10) {
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo);
                return "+27" + validateCellphoneNo.substring(1);
            } else {
                throw new IllegalArgumentException("Number inserted length should be 10, when start with 0 in cellphone number");
            }
        } else if (validateCellphoneNo.startsWith("+27")) {
            if (validateCellphoneNo.length() == 12) {
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo.substring(1));
                return validateCellphoneNo;
            } else {
                throw new IllegalArgumentException("Number inserted length should be 12 or use 0 instead of +27");
            }
        } else {
            throw new IllegalArgumentException("cellphone number inserted, is not RSA cellphone number. Make sure number start with +27 or 0");
        }

    }

    private String checkCellphoneNoHasSpecialCharactersOrAlphabets(String validateCellphoneNo) {
        try {
            var cellphoneNumber = Long.parseLong(validateCellphoneNo);
            return String.valueOf(cellphoneNumber);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cellphone number inserted has characters or alphabets");
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
        logger.info("password characters : {}",list);
        return list;
    }

    private boolean checkPasswordContainsValidators(List<String> listValidators, String password) {
        for (String listValidator : listValidators) {
            if (password.contains(listValidator)) {
                logger.info("Contained : {} " , password.contains(listValidator));
                return true;
            }
        }
        return false;
    }
}
