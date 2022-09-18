package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminDashboardCommandTest {
    private Command command;
    private RequestService requestService;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        requestService = Mockito.mock(RequestService.class);
        command = new AdminDashboardCommand(requestService);
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
     * @see AdminDashboardCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @ParameterizedTest
    @MethodSource("testCases")
    void testExecuteShouldReturnUrlAndPutToSessionAttributes(int totalRowsInTable,
                                                             int totalPages,
                                                             String sortOrder,
                                                             boolean expectedSortOrder,
                                                             String columnName,
                                                             String expectedColumnName,
                                                             String inputPageNumber,
                                                             int expectedPageNumber) throws ServiceException, CommandException {
        when(req.getParameter("desc")).thenReturn(sortOrder);
        when(req.getParameter("archiveDesc")).thenReturn(sortOrder);
        when(req.getParameter("order_by")).thenReturn(columnName);
        when(req.getParameter("archiveOrder_by")).thenReturn(columnName);
        when(req.getParameter("pageNumTableRequests")).thenReturn(inputPageNumber);
        when(req.getParameter("pageNumTableArchive")).thenReturn(inputPageNumber);
        List<UserActivityRequest> userActivityRequests = new LinkedList<>();
        when(requestService.getCountRequestsByStatuses(any())).thenReturn(totalRowsInTable);
        when(requestService.findRequestsByStatuses(anyInt(), anyInt(), anyString(), anyString(), any(int[].class)))
                .thenReturn(userActivityRequests);
        assertEquals(Path.ADMIN_PAGE, command.execute(req, resp));
        verify(req).setAttribute("totalPagesForTableRequests", totalPages);
        verify(req).setAttribute("totalPagesForTableArchive", totalPages);
        verify(req).setAttribute("requestsByStatusPending", userActivityRequests);
        verify(req).setAttribute("archiveOfRequests", userActivityRequests);
        verify(req).setAttribute("desc", expectedSortOrder);
        verify(req).setAttribute("archiveDesc", expectedSortOrder);
        verify(req).setAttribute("order_by", expectedColumnName);
        verify(req).setAttribute("archiveOrder_by", expectedColumnName);
        verify(req).setAttribute("pageNumTableRequests", expectedPageNumber);
        verify(req).setAttribute("pageNumTableArchive", expectedPageNumber);
    }

    /**
     * @see AdminDashboardCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException() throws ServiceException {
        when(requestService.findRequestsByStatuses(anyInt(), anyInt(), anyString(), anyString(), any(int[].class)))
                .thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));

        when(requestService.getCountRequestsByStatuses(any(int[].class))).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(100, 20, "true", false, null, "login", "0", 1),
                Arguments.of(0, 0, "false", true, "1111", "1111", "-100", 1),
                Arguments.of(1, 1, "true", false, "", "login", "one_hundred", 1),
                Arguments.of(4, 1, "true", false, "email", "email", "1", 1),
                Arguments.of(5, 1, "true", false, "a", "a", Long.MAX_VALUE+"", 1),
                Arguments.of(6, 2, "true", false, "em ail", "login", Long.MIN_VALUE+"", 1),
                Arguments.of(7, 2, "true", false, " email", "login", "-1", 1),
                Arguments.of(8, 2, "true", false, "email ", "login", "100", 100),
                Arguments.of(9, 2, "true", false, " email ", "login", "100", 100),
                Arguments.of(10, 2, "true", false, "em@ail", "login", "100", 100),
                Arguments.of(11, 3, "true", false, "1email", "1email", "100", 100),
                Arguments.of(Integer.MAX_VALUE, 429496730,
                                    "true", false, "email1", "email1", "100", 100),
                Arguments.of(100, 20, "true", false, "em1ail", "em1ail", "100", 100),
                Arguments.of(100, 20, "true", false, "їemail", "login", "100", 100),
                Arguments.of(100, 20, "true", false, "emailї", "login", "100", 100),
                Arguments.of(100, 20, "true", false, "1", "1", "100", 100),
                Arguments.of(100, 20, "true", false, "email", "email", "100", 100)
        );
    }
}