package com.users.application.validators;

import com.users.application.exceptions.IdentificationNumberIsNot13DigitsException;
import com.users.application.exceptions.UserAgeException;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class UsersFieldsDataValidator {
    private static Logger logger = LoggerFactory.getLogger(UsersFieldsDataValidator.class);
    @Getter
    private Short validatedAge;
    private LocalDate currentYearDate;
    private String validateCellphoneNo;

    public String validateIdentityNo(@NotNull String identityNo) {
        currentYearDate = LocalDate.now();
        var validateIdentityNo = identityNo.trim();
        if (validateIdentityNo.length() == 13) {
            var firstTwoDigitOfId = validateIdentityNo.substring(0, 2);
            var currentYear = currentYearDate.getYear();
            int customerYearFormIdentityNo;
            if (firstTwoDigitOfId.startsWith("0")) {
                try {
                    customerYearFormIdentityNo = Integer.parseInt("20" + firstTwoDigitOfId);
                    var calculateAge = currentYear - customerYearFormIdentityNo;
                    validAge((short) calculateAge);
                    validSpecialCharactersAndAlphabets(validateIdentityNo.substring(2));
                    logger.info("Months from id {} and days from id {} for 2000",validateIdentityNo.substring(2,4), validateIdentityNo.substring(4,6));
                    this.validIdNumbersDays(validateIdentityNo.substring(4, 6), this.validIdNumbersMonths(validateIdentityNo.substring(2, 4)));
                    return validateIdentityNo;
                } catch (NumberFormatException e) {
                     throw new NumberFormatException("Identity contains characters or alphabets");
                }
            } else if (firstTwoDigitOfId.startsWith("9") || firstTwoDigitOfId.startsWith("8") || firstTwoDigitOfId.startsWith("7") || firstTwoDigitOfId.startsWith("6") || firstTwoDigitOfId.startsWith("5")) {
                try {
                    customerYearFormIdentityNo = Integer.parseInt("19" + firstTwoDigitOfId);
                    var calculateAge = currentYear - customerYearFormIdentityNo;
                    validAge((short) calculateAge);
                    logger.info("Months from id {} and days from id {} for 90s ");

                    validSpecialCharactersAndAlphabets(validateIdentityNo.substring(2));
                    this.validIdNumbersDays(validateIdentityNo.substring(4, 6), this.validIdNumbersMonths(validateIdentityNo.substring(2, 4)));

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

    private String validIdNumbersMonths(String months) {
        var month = Byte.parseByte(months);

        if (month <= 0) {
            throw new IllegalArgumentException("Months can't be 00");
        } else if (month > 12) {
            throw new IllegalArgumentException("Months can't be greater than 12");
        } else {
            return String.valueOf(month);
        }
    }

    private void validIdNumbersDays(String days, String months) {
        var day = Byte.parseByte(days);
        var month = Byte.parseByte(months);

        if (day <= 00) {
            throw new IllegalArgumentException("day can't be 00");
        } else if (day > 29 && month == 2) {
            throw new IllegalArgumentException("day can't be greater than 29 if it's February ");
        } else if (day > 31) {
            throw new IllegalArgumentException("day can't be greater than 31");
        }
    }

    private void validSpecialCharactersAndAlphabets(String nonValidateId) {
        try {
            Long.parseLong(nonValidateId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Identity number has characters or alphabets");
        }
    }

    public String validateCellphoneNo(@NotNull String nonValidatedCellphoneNo) {
        var validateCellphoneNo = nonValidatedCellphoneNo.trim();
        if (validateCellphoneNo.startsWith("0")) {
            if (validateCellphoneNo.length() == 10) {
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo);
                return "+27" + validateCellphoneNo.substring(1);
            } else {
                throw new RuntimeException("Number inserted length should be 10, when start with 0 in cellphone number");
            }
        } else if (validateCellphoneNo.startsWith("+27")) {
            if (validateCellphoneNo.length() == 12) {
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo.substring(1));
                return validateCellphoneNo;
            } else {
                throw new RuntimeException("Number inserted length should be 12 or use 0 instead of +27");
            }
        } else {
            throw new RuntimeException("cellphone number inserted, is not RSA cellphone number. Make sure number start with +27 or 0");
        }

    }

    private void checkCellphoneNoHasSpecialCharactersOrAlphabets(String validateCellphoneNo) {
        try {
            Long.parseLong(validateCellphoneNo);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cellphone number inserted has characters or alphabets");
        }

    }

    //#LLL1231@
    public static boolean checkPasswordValidity(String password) {
// Regular expression for password validation
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*_])[A-Za-z\\d!@#$%&*_]{8,20}$";

// Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

// Return true if the password matches the regex, otherwise false
        return password != null && pattern.matcher(password).matches();
    }
}
