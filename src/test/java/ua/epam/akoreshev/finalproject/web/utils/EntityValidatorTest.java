package ua.epam.akoreshev.finalproject.web.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Entity;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityValidatorTest {
    private static EntityValidator entityValidator;

    @BeforeEach
    void setUp() {
        entityValidator = Mockito.mock(EntityValidator.class, Mockito.CALLS_REAL_METHODS);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @see EntityValidator#invokeMethod(Entity, Method)
     */
    @Test
    void testInvokeMethod() throws NoSuchMethodException {
        Activity activity = new Activity(1L, "testActivity", "тестАктивність", 1L);
        assertEquals("testActivity",
                entityValidator.invokeMethod(activity, activity.getClass().getMethod("getNameEn")));
        assertEquals("тестАктивність",
                entityValidator.invokeMethod(activity, activity.getClass().getMethod("getNameUk")));
    }

    /**
     * @see EntityValidator#validateField(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesForEntityField")
    void testValidateField(String field, boolean expected) {
        assertEquals(expected, entityValidator.validateField(field));
    }

    private static Stream<Arguments> testCasesForEntityField() {
        return Stream.of(
                Arguments.of("abcabcacbacbacbacbacbasdbcabdcada", false),
                Arguments.of("іфлафіждавофждловадліводаліофіава", false),
                Arguments.of("abcabcacbacbacbacbacbasdbcabdcad", true),
                Arguments.of("іфлафіждавофждловадліводаліофіав", true),
                Arguments.of("CC", true),
                Arguments.of("ЇЇ", true),
                Arguments.of("C", false),
                Arguments.of("Ї", false),
                Arguments.of(" entity", false),
                Arguments.of("entity ", false),
                Arguments.of(" entity", false),
                Arguments.of("entity ", false),
                Arguments.of(" entity ", false),
                Arguments.of(" сутність", false),
                Arguments.of("сутність ", false),
                Arguments.of("1entity", false),
                Arguments.of("entity1", false),
                Arguments.of("!entity", false),
                Arguments.of("ent@ity", false),
                Arguments.of("'entity", false),
                Arguments.of("entity'", false),
                Arguments.of("entity", true),
                Arguments.of("сутність", true),
                Arguments.of("ENTITY", true),
                Arguments.of("СУТНІСТЬ", true),
                Arguments.of("ent ity", true),
                Arguments.of("I'm entity", true),
                Arguments.of("су тність", true),
                Arguments.of("ім'я сутності", true)
        );
    }
}