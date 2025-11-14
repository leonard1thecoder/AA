package com.users.application.validators;

import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UsersFieldsDataValidator {

    @Getter
    private Short validatedAge;
    private LocalDate currentYearDate;
    private String  validateCellphoneNo;
    public String validateIdentityNo(@NotNull String identityNo) {
        currentYearDate = LocalDate.now();
        var validateIdentityNo = identityNo.trim();
        if (validateIdentityNo.length() == 13) {
            var firstTwoDigitOfId = validateIdentityNo.substring(0,2);
            var currentYear = currentYearDate.getYear();
            int customerYearFormIdentityNo;
            if (firstTwoDigitOfId.startsWith("0")) {
                try {
                    customerYearFormIdentityNo = Integer.parseInt("20" + firstTwoDigitOfId);
                    var calculateAge = currentYear - customerYearFormIdentityNo;
                    validAge((short)calculateAge);
                    validSpecialCharactersAndAlphabets(validateIdentityNo.substring(2));
                    return validateIdentityNo;
                }catch (NumberFormatException e){
                    throw new NumberFormatException("Identity contains characters or alphabets");
                }
            } else if (firstTwoDigitOfId.startsWith("9") || firstTwoDigitOfId.startsWith("8") || firstTwoDigitOfId.startsWith("7") || firstTwoDigitOfId.startsWith("6") || firstTwoDigitOfId.startsWith("5")) {
                customerYearFormIdentityNo = Integer.parseInt("19" + firstTwoDigitOfId);
                var calculateAge = currentYear - customerYearFormIdentityNo;
                validAge((short)calculateAge);
                return validateIdentityNo;
            } else {
                throw new RuntimeException("Identity number is not valid check identity number, check first digit of account");
            }
        } else {
            throw new RuntimeException("users entered identity number which is not equals to length of 13");
        }
    }


    private void validAge(Short nonValidateAge) {
        if (nonValidateAge < 18)
            if (nonValidateAge < 65)
                validatedAge = nonValidateAge;
            else
                throw  new RuntimeException("Unable to use the older than 65");
            else {
            throw new RuntimeException("You younger than 18, access denied");
        }
    }

    private void validSpecialCharactersAndAlphabets(String nonValidateId) {
        try {
            Long.parseLong(nonValidateId);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Identity number has characters or alphabets");
        }
    }

    public String validateCellphoneNo(@NotNull String nonValidatedCellphoneNo){
        var validateCellphoneNo =nonValidatedCellphoneNo.trim();
        if(validateCellphoneNo.startsWith("0")){
            if (validateCellphoneNo.length() == 10){
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo);
                return "+27" + validateCellphoneNo.substring(1);
            }else{
                throw new RuntimeException("Number inserted length should be 10, when start with 0 in cellphone number");
            }
        }else if (validateCellphoneNo.startsWith("+27")){
            if (validateCellphoneNo.length() == 12){
                checkCellphoneNoHasSpecialCharactersOrAlphabets(validateCellphoneNo.substring(1));
                return validateCellphoneNo;
            }else{
                throw new RuntimeException("Number inserted length should be 12 or use 0 instead of +27");
            }
        }else{
            throw new RuntimeException("cellphone number inserted, is not RSA cellphone number. Make sure number start with +27 or 0");
        }

    }

    private void checkCellphoneNoHasSpecialCharactersOrAlphabets  (String validateCellphoneNo){
        try{
            Long.parseLong(validateCellphoneNo);
        }catch(NumberFormatException e){
            throw new NumberFormatException("Cellphone number inserted has characters or alphabets");
        }

    }
}
