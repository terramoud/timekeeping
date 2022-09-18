package ua.epam.akoreshev.finalproject.web.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CategoryValidatorTest {
    private static CategoryValidator categoryValidator;

    @BeforeAll
    public static void setUpBeforeAll() {
        categoryValidator = new CategoryValidator();
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @see CategoryValidator#validateId(long)
     */
    @Test
    void testValidateIdShouldReturnTrue() {
        int allowedValue = new Random()
                .ints(1, Integer.MAX_VALUE)
                .findAny()
                .orElse(1);
        assertTrue(categoryValidator.validateId(allowedValue));
        assertTrue(categoryValidator.validateId(Integer.MAX_VALUE));
    }

    /**
     * @see CategoryValidator#validateId(long)
     */
    @Test
    void testValidateIdShouldReturnFalseWhenNumberIsOutOfIntegerRangeOrNegative() {
        assertFalse(categoryValidator.validateId(Long.MAX_VALUE));
        assertFalse(categoryValidator.validateId(-1L));
    }

    /**
     * @see CategoryValidator#validate(Category)
     */
    @ParameterizedTest
    @MethodSource("testCasesForCategory")
    void testValidate(Category category, boolean expected) {
        assertEquals(expected, categoryValidator.validate(category));
        assertFalse(categoryValidator.validate(new Category(-1L, "testCategory", "тестКатегорія")));
        assertFalse(categoryValidator.validate(new Category(
                Integer.MAX_VALUE + 1L, "testCategory", "тестКатегорія")));
    }

    /**
     * @see CategoryValidator#validateAllNames(Category)
     */
    @ParameterizedTest
    @MethodSource("testCasesForCategory")
    void validateAllNames(Category category, boolean expected) {
        assertEquals(expected, categoryValidator.validateAllNames(category));
    }

    private static Stream<Arguments> testCasesForCategory() {
        return Stream.of(
                Arguments.of(new Category(1L, "abcabcacbacbacbacbacbasdbcabdcada",
                        "іфлафіждавофждловадліводаліофіава"), false),
                Arguments.of(new Category(1L, "C", "тестКатегорія"), false),
                Arguments.of(new Category(1L, "testCategory", "Ї"), false),
                Arguments.of(new Category(1L, " testCategory", "тестКатегорія"), false),
                Arguments.of(new Category(1L, "testCategory ", "тестКатегорія"), false),
                Arguments.of(new Category(1L, "testCategory", " тестКатегорія"), false),
                Arguments.of(new Category(1L, "testCategory", "тестКатегорія "), false),
                Arguments.of(new Category(1L, "testCategory!", "тестКатегорія"), false),
                Arguments.of(new Category(1L, "testCategory", "тестКатегорія!"), false),
                Arguments.of(new Category(1L, "testCategory1", "тестКатегорія"), false),
                Arguments.of(new Category(1L, "testCategory1", "тестКатегорія1"), false),
                Arguments.of(new Category(1L, "1testCategory", "тестКатегорія1"), false),
                Arguments.of(new Category(1L, "testCategory", "1тестКатегорія"), false),
                Arguments.of(new Category(1L, "testCategory", "тестКатегорія"), true),
                Arguments.of(new Category(1L, "test Category", "тест Категорія"), true),
                Arguments.of(new Category(1L, "test Категорія", "тест Category"), true),
                Arguments.of(new Category(1L, "test Катег орія", "тест Category"), true),
                Arguments.of(new Category(1L, "test Категорія", "тест Cate gory"), true)
        );
    }
}