package com.users.application.validators;

import com.users.application.exceptions.IdentificationNumberIsNot13DigitsException;
import com.users.application.exceptions.UserAgeException;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.*;
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
            int customerYearFromIdentityNo;
            if (firstTwoDigitOfId.startsWith("0")) {
                try {
                    customerYearFromIdentityNo = Integer.parseInt("20" + firstTwoDigitOfId);
                    var calculateAge = currentYear - customerYearFromIdentityNo;
                    validAge((short) calculateAge);
                    validSpecialCharactersAndAlphabets(validateIdentityNo.substring(2));
                    validatesDaysOfMonth((short)customerYearFromIdentityNo,Byte.parseByte(validateIdentityNo.substring(4, 6)),Byte.parseByte(validateIdentityNo.substring(2, 4)));
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
                    validatesDaysOfMonth((short)customerYearFromIdentityNo,Byte.parseByte(validateIdentityNo.substring(4, 6)),(byte)this.validateMonths(Byte.parseByte(validateIdentityNo.substring(2, 4))));
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

    private void validatesDaysOfMonth(short year, byte days, byte month) {
            if (Year.isLeap(year)) {
                if (Month.of(month).getValue() == 2) {
                    try {
                        MonthDay.of(month, days);
                    } catch (DateTimeException e) {
                        throw new DateTimeException("In February days of month should not be greater 29 in leap year");
                    }
                } else {
                    try {
                        MonthDay.of(month, days);
                    } catch (DateTimeException e) {
                        throw new DateTimeException(e.getMessage());
                    }
                }
            } else {
                if (Month.of(month).getValue() == 2) {
                    try {
                        MonthDay.of(month, days);
                    } catch (DateTimeException e) {
                        throw new DateTimeException("In February days of month should not be greater 28");
                    }
                } else {
                    try {
                        MonthDay.of(month, days);
                    } catch (DateTimeException e) {
                        throw new DateTimeException(e.getMessage());
                    }
                }
            }
    }
    
    private void validateMonth(byte month){
        try{
            Month.of(month);
        }catch(DateTimeException e){
            throw new DateTimeException(e.getMessage());
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
