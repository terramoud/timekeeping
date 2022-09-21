package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.UserStatistic;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TimekeepingReportCommandTest {
    private Command command;
    private IntervalService intervalService;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        intervalService = Mockito.mock(IntervalService.class);
        command = new TimekeepingReportCommand(intervalService);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see TimekeepingReportCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @ParameterizedTest
    @MethodSource("testCases")
    void testExecuteShouldReturnUrlAndPutToSessionAttributes(int totalRowsInTable,
                                                             int expectedTotalPages,
                                                             String sortOrder,
                                                             boolean expectedSortOrder,
                                                             String columnName,
                                                             String expectedColumnName,
                                                             String inputPageNumber,
                                                             int expectedPageNumber) throws ServiceException, CommandException {
        when(req.getParameter("desc")).thenReturn(sortOrder);
        when(req.getParameter("order_by")).thenReturn(columnName);
        when(req.getParameter("pageNumber")).thenReturn(inputPageNumber);
        List<UserStatistic> spentTimeForActivities = new LinkedList<>();
        when(intervalService.getNumberUsersActivities()).thenReturn(totalRowsInTable);
        when(intervalService.getStatisticsByUsers(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(spentTimeForActivities);
        assertEquals(Path.REPORT_PAGE, command.execute(req, resp));
        verify(req).setAttribute("spentTimeForActivities", spentTimeForActivities);
        verify(req).setAttribute("desc", expectedSortOrder);
        verify(req).setAttribute("order_by", expectedColumnName);
        verify(req).setAttribute("pageNumber", expectedPageNumber);
        verify(req).setAttribute("totalPages", expectedTotalPages);
    }

    /**
     * @see TimekeepingReportCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException() throws ServiceException {
        when(intervalService.getNumberUsersActivities()).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));

        when(intervalService.getStatisticsByUsers(anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(100, 20, "true", false, null, "login", "0", 1),
                Arguments.of(0, 0, "false", true, "1111", "1111", "-100", 1),
                Arguments.of(1, 1, "true", false, "", "login", "one_hundred", 1),
                Arguments.of(4, 1, "true", false, "email", "email", "1", 1),
                Arguments.of(5, 1, "true", false, "a", "a", Long.MAX_VALUE + "", 1),
                Arguments.of(100, 20, "true", false, "email", "email", "100", 100),
                Arguments.of(100, 20, "true", false, "login", "login", "100", 100)
        );
    }
}