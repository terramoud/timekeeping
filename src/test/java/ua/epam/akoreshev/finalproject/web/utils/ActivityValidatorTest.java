package ua.epam.akoreshev.finalproject.web.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.epam.akoreshev.finalproject.model.entity.Activity;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ActivityValidatorTest {
    private static ActivityValidator activityValidator;

    @BeforeAll
    public static void setUpBeforeAll() {
        activityValidator = new ActivityValidator();
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @see ActivityValidator#validateId(long)
     */
    @Test
    void testValidateIdShouldReturnTrue() {
        int allowedValue = new Random()
                .ints(1, Integer.MAX_VALUE)
                .findAny()
                .orElse(1);
        assertTrue(activityValidator.validateId(allowedValue));
        assertTrue(activityValidator.validateId(Integer.MAX_VALUE));
    }

    /**
     * @see ActivityValidator#validateId(long)
     */
    @Test
    void testValidateIdShouldReturnFalseWhenNumberIsOutOfIntegerRangeOrNegative() {
        assertFalse(activityValidator.validateId(Long.MAX_VALUE));
        assertFalse(activityValidator.validateId(-1L));
    }

    /**
     * @see ActivityValidator#validateCategoryId(long)
     */
    @Test
    void testValidateCategoryIdShouldReturnTrue() {
        int allowedValue = new Random()
                .ints(1, Integer.MAX_VALUE)
                .findAny()
                .orElse(1);
        assertTrue(activityValidator.validateCategoryId(allowedValue));
        assertTrue(activityValidator.validateCategoryId(Integer.MAX_VALUE));
    }

    /**
     * @see ActivityValidator#validateCategoryId(long)
     */
    @Test
    void testValidateCategoryIdShouldReturnFalseWhenNumberIsOutOfIntegerRangeOrNegative() {
        assertFalse(activityValidator.validateCategoryId(Long.MAX_VALUE));
        assertFalse(activityValidator.validateCategoryId(-1L));
    }

    /**
     * @see ActivityValidator#validate(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesForActivity")
    void testValidate(Activity activity, boolean expected) {
        assertEquals(expected, activityValidator.validate(activity));
    }

    /**
     * @see ActivityValidator#validate(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesForActivityWhenNumericFieldIsInvalid")
    void testValidateShouldReturnFalseWhenNumericFieldsIsInvalid(Activity activity, boolean expected) {
        assertEquals(expected, activityValidator.validate(activity));
    }

    /**
     * @see ActivityValidator#validateAllNames(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesForActivity")
    void testValidateAllNames(Activity activity, boolean expected) {
        assertEquals(expected, activityValidator.validateAllNames(activity));
    }

    private static Stream<Arguments> testCasesForActivity() {
        return Stream.of(
                Arguments.of(new Activity(1L, "abcabcacbacbacbacbacbasdbcabdcada",
                        "іфлафіждавофждловадліводаліофіава", 1L), false),
                Arguments.of(new Activity(1L, "C", "тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity", "Ї", 1L), false),
                Arguments.of(new Activity(1L, " testActivity", "тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity ", "тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity", " тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity", "тестАктивність ", 1L), false),
                Arguments.of(new Activity(1L, "testActivity!", "тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity", "тестАктивність!", 1L), false),
                Arguments.of(new Activity(1L, "testActivity1", "тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity1", "тестАктивність1", 1L), false),
                Arguments.of(new Activity(1L, "1testActivity", "тестАктивність1", 1L), false),
                Arguments.of(new Activity(1L, "testActivity", "1тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity", "тестАктивність", 1L), true),
                Arguments.of(new Activity(1L, "test Activity", "тест Активність", 1L), true),
                Arguments.of(new Activity(1L, "test Активність", "тест Activity", 1L), true),
                Arguments.of(new Activity(1L, "test Актив ність", "тест Activity", 1L), true),
                Arguments.of(new Activity(1L, "TESTACTIVITY", "ТЕСТАКТИВНІТЬ", 1L), true),
                Arguments.of(new Activity(1L, "test Активність", "тест Acti vity", 1L), true)
        );
    }

    private static Stream<Arguments> testCasesForActivityWhenNumericFieldIsInvalid() {
        return Stream.of(
                Arguments.of(new Activity(-1L, "testActivity", "тестАктивність", 1L), false),
                Arguments.of(new Activity(1L, "testActivity", "тестАктивність", -1L), false),
                Arguments.of(new Activity(-1L, "testActivity", "тестАктивність", -1L), false),
                Arguments.of(new Activity(Integer.MAX_VALUE + 1L, "testActivity", "тестАктивність",
                        1L), false),
                Arguments.of(new Activity(1L, "testActivity", "тестАктивність",
                        Integer.MAX_VALUE + 1L), false),
                Arguments.of(new Activity(Integer.MAX_VALUE + 1L, "testActivity", "тестАктивність",
                        Integer.MAX_VALUE + 1L), false)
        );
    }
}