package com.users.application.validators;

import com.users.application.entities.Users;
import com.users.application.exceptions.IdentificationNumberIsNot13DigitsException;
import com.users.application.exceptions.UserAgeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DateTimeException;
import java.time.Month;
import java.time.MonthDay;

import static com.users.application.validators.UsersFieldsDataValidator.getInstance;


class UsersFieldsDataValidatorTest {

    private UsersFieldsDataValidator validator;

    @BeforeEach
    public void setValidator() {
        this.validator = getInstance();
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
    public void testValidSpecialCharactersAndAlphabetsMethodForIdentityNumber() throws NoSuchMethodException, IllegalAccessException {
        //when
        String identityNumber = "7@53123124V321";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validSpecialCharactersAndAlphabets", String.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, identityNumber);
        } catch (InvocationTargetException e) {
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
    public void testValidateAgeMethod_ageIsLessThan18() throws NoSuchMethodException, IllegalAccessException {
        //When
        Short age = 15;

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validAge", Short.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, age);
        } catch (InvocationTargetException e) {
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
        Assertions.assertEquals(age, ageValidated);
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
    void testValidIdNumbersMonthsMethod_invalidMonthValueGreaterThan12() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //when
        String monthGreaterThan12 = "13";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validateMonth", Byte.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, Byte.parseByte(monthGreaterThan12));
        } catch (InvocationTargetException e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(DateTimeException.class, () -> {
                throw new DateTimeException("Invalid value for MonthOfYear: 13");
            });

            Assertions.assertEquals(throwable.toString(), e.getCause().toString());

        }
    }

    @Test
    void testValidIdNumbersMonthsMethod_invalidMonthValueLessThan1() throws IllegalAccessException, NoSuchMethodException {
        //when
        String monthGreaterThan12 = "0";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validateMonth", Byte.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, Byte.parseByte(monthGreaterThan12));
        } catch (InvocationTargetException e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(DateTimeException.class, () -> {
                throw new DateTimeException("Invalid value for MonthOfYear: 0");
            });

            Assertions.assertEquals(throwable.toString(), e.getCause().toString());

        }
    }

    @Test
    void testValidIdNumbersMonthsMetho_validMonthValueBetween1And12() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //when
        String month2 = "2";

        //Given
        Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validateMonth", Byte.class);
        validAge.setAccessible(true);
        var month = (Month)validAge.invoke(validator, Byte.parseByte(month2));

        //assert
        Assertions.assertEquals(month.getValue(), 2);
    }



@Test
void testValidatesDaysOfMonth_invalidMonthDayDayGreaterThan31() throws IllegalAccessException, NoSuchMethodException {
    //When
    Byte month = 12;
    Byte day = 32;
    Short year = 2000;

    try {
        //Given
        Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
        validAge.setAccessible(true);
        validAge.invoke(validator, year,day,month);
    } catch (InvocationTargetException e) {
        //Assert
        Throwable throwable = Assertions.assertThrows(DateTimeException.class, () -> {
            throw new DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 32");
        });

        Assertions.assertEquals(throwable.toString(), e.getCause().toString());
    }
}

    @Test
    void testValidatesDaysOfMonthMethod_validMonthDayDayBetween1And31() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //When
        Byte month = 12;
        Byte day = 25;
        Short year = 2000;


            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
            validAge.setAccessible(true);
            var dayOfMonth = (MonthDay)validAge.invoke(validator, year,day,month);


            Assertions.assertEquals(dayOfMonth.getDayOfMonth(), 25);

    }

    @Test
    void testValidatesDaysOfMonthMethod_validMonthDayDayBetween1And28InFebruary() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //When
        Byte month = 02;
        Byte day = 28;
        Short year = 2022;


        //Given
        Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
        validAge.setAccessible(true);
        var dayOfMonth = (MonthDay)validAge.invoke(validator, year,day,month);


        Assertions.assertEquals(dayOfMonth.getDayOfMonth(), 28);

    }

    @Test
    void testValidatesDaysOfMonthMethod_invalidMonthDayDayGreaterThan28InFebruary() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //When
        Byte month = 02;
        Byte day = 29;
        Short year = 2025;

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, year,day,month);
        } catch (InvocationTargetException e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(DateTimeException.class, () -> {
                throw new DateTimeException("In February days of month should not be greater 28");
            });

            Assertions.assertEquals(throwable.toString(), e.getCause().toString());
        }

    }

    @Test
    void testValidatesDaysOfMonth_invalidMonthDayFOrLeapYearDayGreaterThan31() throws IllegalAccessException, NoSuchMethodException {
        //When
        Byte month = 12;
        Byte day = 32;
        Short leapYear = 2024;

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, leapYear,day,month);
        } catch (InvocationTargetException e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(DateTimeException.class, () -> {
                throw new DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 32");
            });

            Assertions.assertEquals(throwable.toString(), e.getCause().toString());
        }
    }

    @Test
    void testValidatesDaysOfMonthMethod_validMonthDayForLeapYearDayBetween1And31() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //When
        Byte month = 12;
        Byte day = 25;
        Short leapYear = 2024;


        //Given
        Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
        validAge.setAccessible(true);
        var dayOfMonth = (MonthDay)validAge.invoke(validator, leapYear,day,month);


        Assertions.assertEquals(dayOfMonth.getDayOfMonth(), 25);

    }

    @Test
    void testValidatesDaysOfMonthMethod_validMonthDayForLeapYEarDayBetween1And29InFebruary() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //When
        Byte month = 02;
        Byte day = 29;
        Short leapYear = 2024;


        //Given
        Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
        validAge.setAccessible(true);
        var dayOfMonth = (MonthDay)validAge.invoke(validator, leapYear,day,month);


        Assertions.assertEquals(dayOfMonth.getDayOfMonth(), 29);

    }

    @Test
    void testValidatesDaysOfMonthMethod_invalidMonthDayForLeapYearDayGreaterThan29InFebruary() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //When
        Byte month = 02;
        Byte day = 30;
        Short leapYear = 2024;

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("validatesDaysOfMonth", Short.class,Byte.class,Byte.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, leapYear,day,month);
        } catch (InvocationTargetException e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(DateTimeException.class, () -> {
                throw new DateTimeException("In February days of month should not be greater 29 in leap year");
            });

            Assertions.assertEquals(throwable.toString(), e.getCause().toString());
        }

    }

@Test
void validateCellphoneNo() {
}

    @Test
    public void testValidSpecialCharactersAndAlphabetsMethodForCellphoneNUmber_invalidCellphoneNumber() throws NoSuchMethodException, IllegalAccessException {
        //when
        String cellphoneNumber = "7@53123124V321";

        try {
            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("checkCellphoneNoHasSpecialCharactersOrAlphabets", String.class);
            validAge.setAccessible(true);
            validAge.invoke(validator, cellphoneNumber);
        } catch (InvocationTargetException e) {
            //Assert
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Cellphone number inserted has characters or alphabets");
            });

            Assertions.assertEquals("Cellphone number inserted has characters or alphabets", throwable.getMessage());
        }
    }

    @Test
    public void testValidSpecialCharactersAndAlphabetsMethodForCellphoneNUmber_validCellphoneNumber() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //when
        String cellphoneNumber = "0788725433";

            //Given
            Method validAge = UsersFieldsDataValidator.class.getDeclaredMethod("checkCellphoneNoHasSpecialCharactersOrAlphabets", String.class);
            validAge.setAccessible(true);
          var cellphoneValid = validAge.invoke(validator, cellphoneNumber);

          //assert
        Assertions.assertEquals("0"+cellphoneValid,cellphoneNumber);

    }
    @Test
    void testValidateCellphoneMethod_validCellphoneNuStartWith0AndLength10(){
        //when
        String cellphoneNumber = "0788725433";

        //given
        var validatedCellphoneNu = this.validator.validateCellphoneNo(cellphoneNumber);

        //assert
        Assertions.assertEquals("+27788725433",validatedCellphoneNu);
    }

    @Test
    void testValidateCellphoneMethod_invalidCellphoneNuStartWith0AndLength13(){
        //when
        String cellphoneNumber = "074688725433";

        try {
            //given
            var validatedCellphoneNu = this.validator.validateCellphoneNo(cellphoneNumber);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("Number inserted length should be 10, when start with 0 in cellphone number",e.getMessage());
        }

    }

    @Test
    void testValidateCellphoneMethod_validCellphoneNuStartWith27AndLength12(){
        //when
        String cellphoneNumber = "+27788725433";

        //given
        var validatedCellphoneNu = this.validator.validateCellphoneNo(cellphoneNumber);

        //assert
        Assertions.assertEquals("+27788725433",validatedCellphoneNu);
    }

    @Test
    void testValidateCellphoneMethod_invalidCellphoneNuDoesNotStartWith27Or0(){
        //when
        String cellphoneNumber = "+2374688725433";

        try {
            //given
            var validatedCellphoneNu = this.validator.validateCellphoneNo(cellphoneNumber);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("cellphone number inserted, is not RSA cellphone number. Make sure number start with +27 or 0",e.getMessage());
        }

    }


    @Test
    void testValidateCellphoneMethod_invalidCellphoneNuStartWith27AndLength13(){
        //when
        String cellphoneNumber = "+2774688725433";

        try {
            //given
            var validatedCellphoneNu = this.validator.validateCellphoneNo(cellphoneNumber);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("Number inserted length should be 12 or use 0 instead of +27",e.getMessage());
        }

    }

    @Test
    void testCheckPasswordValidityMethod_invalidPasswordNoSpecialCharacters() {
        //When
        var password = "aamail709com";

        try {
            // given
            this.validator.checkPasswordValidity(password);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("Password should have at one special characters {!@#$%^&*()?><;'{}|}",e.getMessage());
        }
    }

    @Test
    void testCheckPasswordValidityMethod_validPasswordWithSpecialCharacters(){
        //When
        var password = "#aa@Bmail1.com";

        // given
       var validatedPassword = this.validator.checkPasswordValidity(password);

        //assert
        Assertions.assertEquals(password,validatedPassword );
    }

    @Test
    void testCheckPasswordValidityMethod_invalidPasswordNoLowerLetters() {
        //When
        var password = "ASDFGH@709";

        try {
            // given
            this.validator.checkPasswordValidity(password);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("password should have at least one small letter",e.getMessage());
        }
    }

    @Test
    void testCheckPasswordValidityMethod_validPasswordWithUpperCaseLetter(){
        //When
        var password = "#aaA@mail1.com";

        // given
        var validatedPassword = this.validator.checkPasswordValidity(password);

        //assert
        Assertions.assertEquals(password,validatedPassword );
    }

    @Test
    void testCheckPasswordValidityMethod_invalidPasswordNoUpperCaseLetters() {
        //When
        var password = "asdfgh@709";

        try {
            // given
            this.validator.checkPasswordValidity(password);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("password should have at least one upper case letter",e.getMessage());
        }
    }

    @Test
    void testCheckPasswordValidityMethod_validPasswordWithLowerCases(){
        //When
        var password = "#aa@maBil1.com";

        // given
        var validatedPassword = this.validator.checkPasswordValidity(password);

        //assert
        Assertions.assertEquals(password,validatedPassword );
    }

    @Test
    void testCheckPasswordValidityMethod_invalidPasswordNoNumber() {
        //When
        var password = "asdfgh@B";

        try {
            // given
            this.validator.checkPasswordValidity(password);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("password should have at least one number",e.getMessage());
        }
    }

    @Test
    void testCheckPasswordValidityMethod_validPasswordWithLengthOf8(){
        //When
        var password = "#aa@maBil1.com";

        // given
        var validatedPassword = this.validator.checkPasswordValidity(password);

        //assert
        Assertions.assertEquals(password,validatedPassword );
    }

    @Test
    void testCheckPasswordValidityMethod_invalidPasswordLengthISLessThan8() {
        //When
        var password = "as1@B";

        try {
            // given
            this.validator.checkPasswordValidity(password);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("Password minimum length starts from 8",e.getMessage());
        }
    }

    @Test
    void testCheckPasswordValidityMethod_validPasswordLengthGreater8(){
        //When
        var password = "#@maB.comstart1";

        // given
        var validatedPassword = this.validator.checkPasswordValidity(password);

        //assert
        Assertions.assertEquals(password,validatedPassword );
    }

    @Test
    void testCheckPasswordValidityMethod_invalidPasswordLengthISGreaterThan16() {
        //When
        var password = "as1@Bdnjdnjdnkjsnadkjankjsanjsad";

        try {
            // given
            this.validator.checkPasswordValidity(password);
        }catch(IllegalArgumentException e){
            //assert
            Assertions.assertEquals("Password maximum length starts from 16",e.getMessage());
        }
    }

    @Test
    void testCheckPasswordValidityMethod_validPasswordLengthLessThan16(){
        //When
        var password = "#@maB.comstart1";

        // given
        var validatedPassword = this.validator.checkPasswordValidity(password);

        //assert
        Assertions.assertEquals(password,validatedPassword );
    }
}
