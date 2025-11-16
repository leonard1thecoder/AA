package com.users.application.validators;

import com.users.application.entities.Users;
import com.users.application.exceptions.IdentificationNumberIsNot13DigitsException;
import com.users.application.exceptions.UserAgeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


class UsersFieldsDataValidatorTest {


    private UsersFieldsDataValidator validator;

    @BeforeEach
    public void setValidator() {
        this.validator = new UsersFieldsDataValidator();
    }

    @Test
    void testValidateIdentityNoWith13DigitsLess() {
        //When
        var user = Users
                .builder()
                .userIdentityNo("001231232")
                .build();

        try {
            //Given
            validator.validateIdentityNo(user.getUserIdentityNo());
        } catch (IdentificationNumberIsNot13DigitsException e) {
            //assert
            Throwable throwable = Assertions.assertThrows(IdentificationNumberIsNot13DigitsException.class, () -> {
                throw new IdentificationNumberIsNot13DigitsException("users entered identity number which is not equals to length of 13");
            });

            Assertions.assertEquals("users entered identity number which is not equals to length of 13", throwable.getMessage());
        }
    }

    @Test
    public void testValidSpecialCharactersAndAlphabetsMethod() {
        //when
        String identityNumber = "7@53123124V321";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validSpecialCharactersAndAlphabets", String.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, identityNumber);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Identity number has characters or alphabets");
            });

            Assertions.assertEquals("Identity number has characters or alphabets", throwable.getMessage());
        }
    }

    @Test
    void testValidateIdentityNoWith13DigitsIdStartWith0() {
        //When
        var user = Users
                .builder()
                .userIdentityNo("0012255212323")
                .build();
        //Given
        var validatedId = validator.validateIdentityNo(user.getUserIdentityNo());

        //Assert
        Assertions.assertEquals("0012255212323", validatedId);

    }

    @Test
    public void testValidateAgeMethod_ageIsLessThan18() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //When
        Short age = 15;

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validAge", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, age);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(UserAgeException.class, () -> {
                throw new UserAgeException("You younger than 18, access denied");
            });

            Assertions.assertEquals("You younger than 18, access denied", throwable.getMessage());
        }
    }

    @Test
    public void testValidateAgeMethod_ageIsBetween18And65() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //When
        Short age = 28;

        //Given
        Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validAge", Short.class);
        validAge.setAccessible(true);
       var ageValidated = validAge.invoke(validator, age);
       //assert
        Assertions.assertEquals(age,ageValidated);
    }

    @Test
    public void testValidateAgeMethod_ageIsGreaterThan65() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //When
        Short age = 66;

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validAge", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, age);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(UserAgeException.class, () -> {
                throw new UserAgeException("Age over 65 not allowed, access denied");
            });

            Assertions.assertEquals("Age over 65 not allowed, access denied", throwable.getMessage());
        }
    }

    @Test
    void testValidateIdentityNoWith13DigitsIdStartWith9() {
        //When
        var user = Users
                .builder()
                .userIdentityNo("9012255212323")
                .build();
        //Given
        var validatedId = validator.validateIdentityNo(user.getUserIdentityNo());

        //Assert
        Assertions.assertEquals("9012255212323", validatedId);

    }

    @Test
    void testValidIdNumbersMonthsMethod_monthIs00() {
        //When
        String month = "00";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validIdNumbersMonths", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, month);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Month can't be 00");
            });

            Assertions.assertEquals("Month can't be 00", throwable.getMessage());
        }
    }

    @Test
    void testValidIdNumbersMonthsMethod_monthIsGreaterThan12() {
        //When
        String month = "13";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validIdNumbersMonths", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, month);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Month can't be greater than 12");
            });

            Assertions.assertEquals("Month can't be greater than 12", throwable.getMessage());
        }
    }

    @Test
    void testValidIdNumbersDaysMethod_dayIs0() {
        //When
        String day = "0";
        String month = "12";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validIdNumbersDays", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, day);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Days can't be 0");
            });

            Assertions.assertEquals("Days can't be 0", throwable.getMessage());
        }
    }
    @Test
    void testValidIdNumbersDaysMethod_daysIsGreaterThan31() {
        //When
        String day = "32";
        String month = "12";
        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validIdNumbersDays", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, month);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Month can't be greater than 12");
            });

            Assertions.assertEquals("Month can't be greater than 12", throwable.getMessage());
        }
    }

    @Test
    void testValidIdNumbersMonthsMethod_monthIs12AndDayIsGreaterThan29() {
        //When
        String month = "12";
        String day = "30";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validIdNumbersDays", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, month);
        } catch (Exception e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Month can't be greater than 12");
            });

            Assertions.assertEquals("Month can't be greater than 12", throwable.getMessage());
        }
    }

    @Test
    void validateCellphoneNo() {
    }
}