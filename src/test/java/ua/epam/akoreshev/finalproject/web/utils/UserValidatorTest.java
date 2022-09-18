package ua.epam.akoreshev.finalproject.web.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private static UserValidator userValidator;

    @BeforeAll
    public static void setUpBeforeAll() {
        userValidator = new UserValidator();
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @see UserValidator#validateId(long)
     */
    @Test
    void testValidateIdShouldReturnTrue() {
        int allowedValue = new Random()
                .ints(1, Integer.MAX_VALUE)
                .findAny()
                .orElse(1);
        assertTrue(userValidator.validateId(allowedValue));
        assertTrue(userValidator.validateId(Integer.MAX_VALUE));
    }

    /**
     * @see UserValidator#validateId(long)
     */
    @Test
    void testValidateIdShouldReturnFalseWhenNumberIsOutOfIntegerRangeOrNegative() {
        assertFalse(userValidator.validateId(Long.MAX_VALUE));
        assertFalse(userValidator.validateId(-1L));
    }

    /**
     * @see UserValidator#validateRoleId(int)
     */
    @Test
    void validateRoleIdShouldReturnTrue() {
        int allowedValue = new Random()
                .ints(1, Integer.MAX_VALUE)
                .findAny()
                .orElse(1);
        assertTrue(userValidator.validateRoleId(allowedValue));
        assertTrue(userValidator.validateId(Integer.MAX_VALUE));
    }

    /**
     * @see UserValidator#validateRoleId(int)
     */
    @Test
    void validateRoleIdShouldReturnFalseWhenNumberIsNegative() {
        assertFalse(userValidator.validateRoleId(0));
        assertFalse(userValidator.validateRoleId(-1));
    }


    /**
     * @see UserValidator#validate(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserHasInvalidFields")
    void testValidateShouldReturnFalse(User testUser) {
        assertFalse(userValidator.validate(testUser));
    }

    /**
     * @see UserValidator#validate(User)
     */
    @Test
    void testValidateShouldReturnTrue() {
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "Passtest1", 2);
        assertTrue(userValidator.validate(testUser));
    }


    /**
     * @see UserValidator#validateEmail(String)
     */
    @Test
    void validateEmailShouldReturnTrue() {
        assertTrue(userValidator.validateEmail("t@s.co"));
        assertTrue(userValidator.validateEmail("test@email.com"));
    }

    /**
     * @see UserValidator#validateEmail(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserHasInvalidEmail")
    void validateEmailShouldReturnFalse(String email) {
        assertFalse(userValidator.validateEmail(email));
    }

    /**
     * @see UserValidator#validatePassword(String)
     */
    @Test
    void testValidatePasswordShouldReturnTrue() {
        assertTrue(userValidator.validatePassword("Password1"));
    }

    /**
     * @see UserValidator#validatePassword(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserHasInvalidPassword")
    void testValidatePasswordShouldReturnFalse(String password) {
        assertFalse(userValidator.validatePassword(password));
    }

    /**
     * @see UserValidator#validateLogin(String)
     */
    @Test
    void testValidateLoginShouldReturnTrue() {
        assertTrue(userValidator.validateLogin("UserLogin1"));
    }

    /**
     * @see UserValidator#validateLogin(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserHasInvalidLogin")
    void testValidateLoginShouldReturnFalse(String login) {
        assertFalse(userValidator.validateLogin(login));
    }

    private static Stream<Arguments> testCasesWhenUserHasInvalidEmail() {
        return Stream.of(
                Arguments.of("11111"),
                Arguments.of("!!!!"),
                Arguments.of("ЇtestUser1@mail.com"),
                Arguments.of("testUser1@mail,com"),
                Arguments.of("testUser1@mail.com "),
                Arguments.of(" testUser1@mail.com"),
                Arguments.of("testUser1@@mail.com"),
                Arguments.of("testUser1@mail.c"),
                Arguments.of("t@s.c"),
                Arguments.of("t@s!.com"),
                Arguments.of("t@s.!com"),
                Arguments.of("t!@s.com"),
                Arguments.of("!t@s.com"),
                Arguments.of("t@.com"),
                Arguments.of("t.com"),
                Arguments.of("@.com"),
                Arguments.of("t@com"),
                Arguments.of("t@. com"),
                Arguments.of("t@ .com"),
                Arguments.of("t @ .com"),
                Arguments.of("t @ .c!o")
        );
    }

    private static Stream<Arguments> testCasesWhenUserHasInvalidLogin() {
        return Stream.of(
                Arguments.of("11111"),
                Arguments.of("!!!!"),
                Arguments.of("ЇtestUser1@"),
                Arguments.of("testUser1,com"),
                Arguments.of("testUser1.com "),
                Arguments.of(" testUser1"),
                Arguments.of("test"),
                Arguments.of("testUser1 "),
                Arguments.of("test User1"),
                Arguments.of("testUser1111111111111111111111111"),
                Arguments.of("t@s.!com"),
                Arguments.of("t!@s.com"),
                Arguments.of("!t@s.com"),
                Arguments.of("t@.com"),
                Arguments.of("t.com"),
                Arguments.of("@.com"),
                Arguments.of("t@com"),
                Arguments.of("t@. com"),
                Arguments.of("t@ .com"),
                Arguments.of("t @ .com"),
                Arguments.of("t @ .c!o")
        );
    }

    private static Stream<Arguments> testCasesWhenUserHasInvalidPassword() {
        return Stream.of(
                Arguments.of("Password"),
                Arguments.of("Password111111111111111111111111"),
                Arguments.of("password1"),
                Arguments.of("Passwo1"),
                Arguments.of("Password1!"),
                Arguments.of("!Password1"),
                Arguments.of("Pass@word1"),
                Arguments.of("Pas sword1"),
                Arguments.of(" Password1"),
                Arguments.of("Password1 "),
                Arguments.of("їPassword1"),
                Arguments.of("Password1ї"),
                Arguments.of("1111"),
                Arguments.of("PASSWORD1")
        );
    }

    private static Stream<Arguments> testCasesWhenUserHasInvalidFields() {
        String veryLongEmail = "test"
                .concat(Arrays.toString(new int[243]).replaceAll("[, \\[\\]]", ""))
                .concat("@mail.com");
        return Stream.of(
                Arguments.of(new User(1, null, "testUser1@mail.com", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", null, "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", null, 2)),
                Arguments.of(new User(1, "test", "testUser1@mail.com", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "@mail.com", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "Pas", 2)),
                Arguments.of(new User(1, "1111&^", "##$", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "^&%87", 2)),
                Arguments.of(new User(1, "", "testUser1@mail.com", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "", 2)),
                Arguments.of(new User(1, "testUser1.", "testUser1@mail.com", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com.$", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "Passtest1$", 2)),
                Arguments.of(new User(1, "testUser0123456789012345678912345", "testUser1@mail.com",
                        "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", veryLongEmail, "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com",
                        "Passtest0123456789012345678912345", 2)),
                Arguments.of(new User(1, "testUser1", "ЇtestUser1@mail.comЇ", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "ЇівдлаоііяюєЬ12", 2)),
                Arguments.of(new User(1, "testUser1", "пошта@укр.нет", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "ЇPasstestЇ1", 2)),
                Arguments.of(new User(-1, "testUser1", "testUser1@mail.com", "Passtest1", 2)),
                Arguments.of(new User(Long.MAX_VALUE, "testUser1", "testUser1@mail.com", "Passtest1", 2)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "Passtest1", 0)),
                Arguments.of(new User(1, "testUser1", "testUser1@mail.com", "Passtest1", -1))
        );
    }
}