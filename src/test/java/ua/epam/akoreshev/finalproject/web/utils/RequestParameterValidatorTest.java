package ua.epam.akoreshev.finalproject.web.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RequestParameterValidatorTest {
    private HttpServletRequest req;

    @BeforeEach
    void setUp() {
        req = Mockito.mock(HttpServletRequest.class);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @see RequestParameterValidator#getLong(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesForLong")
    void testGetLong(String testParameter, long expectedLong) {
        when(req.getParameter(any())).thenReturn(testParameter);
        RequestParameterValidator validator = new RequestParameterValidator(req);
        assertEquals(expectedLong, validator.getLong(testParameter));
    }

    /**
     * @see RequestParameterValidator#getInt(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesForInteger")
    void testGetInt(String testParameter, int expectedInt) {
        when(req.getParameter(any())).thenReturn(testParameter);
        RequestParameterValidator validator = new RequestParameterValidator(req);
        assertEquals(expectedInt, validator.getInt(testParameter));
    }

    /**
     * @see RequestParameterValidator#getString(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesForString")
    void getString(String testParameter, String expectedString) {
        when(req.getParameter(any())).thenReturn(testParameter);
        RequestParameterValidator validator = new RequestParameterValidator(req);
        assertEquals(expectedString, validator.getString(testParameter));
    }

    /**
     * @see RequestParameterValidator#getPaginationPageNumber(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesForPagination")
    void testGetPaginationPageNumber(String testParameter, int expectedInt) {
        when(req.getParameter(any())).thenReturn(testParameter);
        RequestParameterValidator validator = new RequestParameterValidator(req);
        assertEquals(expectedInt, validator.getPaginationPageNumber(testParameter));
    }

    /**
     * @see RequestParameterValidator#getBoolean(String)
     */
    @ParameterizedTest
    @MethodSource("testCasesForBoolean")
    void getBoolean(String testParameter, boolean expectedBoolean) {
        when(req.getParameter(any())).thenReturn(testParameter);
        RequestParameterValidator validator = new RequestParameterValidator(req);
        assertEquals(expectedBoolean, validator.getBoolean(testParameter));
    }

    private static Stream<Arguments> testCasesForString() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of("!", "!"),
                Arguments.of("em ail", "em ail"),
                Arguments.of(" email", " email"),
                Arguments.of("email ", "email "),
                Arguments.of(" email ", " email "),
                Arguments.of("em@ail", "em@ail"),
                Arguments.of("emailї", "emailї"),
                Arguments.of("emїail", "emїail"),
                Arguments.of("їemail", "їemail"),
                Arguments.of("-email", "-email"),
                Arguments.of("-email-", "-email-"),
                Arguments.of("email-", "email-"),
                Arguments.of("_email", "_email"),
                Arguments.of("_email_", "_email_"),
                Arguments.of("email_", "email_"),
                Arguments.of("1email", "1email"),
                Arguments.of("em1ail", "em1ail"),
                Arguments.of("em-ail", "em-ail"),
                Arguments.of("em-ai-l", "em-ai-l"),
                Arguments.of("em-ai_l", "em-ai_l"),
                Arguments.of("emai_l", "emai_l"),
                Arguments.of("e_m_a_i_l", "e_m_a_i_l"),
                Arguments.of("a", "a"),
                Arguments.of("1", "1"),
                Arguments.of("email", "email")
        );
    }

    private static Stream<Arguments> testCasesForLong() {
        return Stream.of(
                Arguments.of(null, 0L),
                Arguments.of("", 0L),
                Arguments.of("long", 0L),
                Arguments.of("-1", 0L),
                Arguments.of(Long.MIN_VALUE + "", 0L),
                Arguments.of(Long.MAX_VALUE + "" + Long.MAX_VALUE, 0L),
                Arguments.of(Long.MAX_VALUE + "", Long.MAX_VALUE),
                Arguments.of("#", 0L),
                Arguments.of("'0'", 0L),
                Arguments.of("'1'", 0L),
                Arguments.of("0", 0L),
                Arguments.of("1", 1L)
        );
    }

    private static Stream<Arguments> testCasesForInteger() {
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of("", 0),
                Arguments.of("integer", 0),
                Arguments.of("-1", 0),
                Arguments.of(Integer.MIN_VALUE + "", 0),
                Arguments.of(Integer.MAX_VALUE + "" + Integer.MAX_VALUE, 0),
                Arguments.of(Integer.MAX_VALUE + "", Integer.MAX_VALUE),
                Arguments.of("#", 0),
                Arguments.of("'0'", 0),
                Arguments.of("'1'", 0),
                Arguments.of("0", 0),
                Arguments.of("1", 1)
        );
    }

    private static Stream<Arguments> testCasesForPagination() {
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 1),
                Arguments.of("integer", 1),
                Arguments.of("-1", 1),
                Arguments.of(Integer.MIN_VALUE + "", 1),
                Arguments.of(Integer.MAX_VALUE + "" + Integer.MAX_VALUE, 1),
                Arguments.of(Integer.MAX_VALUE + "", Integer.MAX_VALUE),
                Arguments.of("#", 1),
                Arguments.of("'0'", 1),
                Arguments.of("'1'", 1),
                Arguments.of("0", 1),
                Arguments.of("1", 1)
        );
    }

    private static Stream<Arguments> testCasesForBoolean() {
        return Stream.of(
                Arguments.of("true", true),
                Arguments.of("false", false),
                Arguments.of(null, false),
                Arguments.of("", false),
                Arguments.of("integer", false),
                Arguments.of("-1", false),
                Arguments.of("10000000000000", false),
                Arguments.of("0", false),
                Arguments.of("#", false),
                Arguments.of("'0'", false),
                Arguments.of("'1'", false),
                Arguments.of("1", false)
        );
    }
}