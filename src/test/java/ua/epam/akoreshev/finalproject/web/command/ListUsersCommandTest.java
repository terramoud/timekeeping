package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListUsersCommandTest {
    private Command command;
    private UserService userService;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        command = new ListUsersCommand(userService);
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
     * @see ListUsersCommand#execute(HttpServletRequest, HttpServletResponse)
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
                                                             int expectedPageNumber)
            throws ServiceException, CommandException {
        when(req.getParameter("desc")).thenReturn(sortOrder);
        when(req.getParameter("order_by")).thenReturn(columnName);
        when(req.getParameter("pageNumber")).thenReturn(inputPageNumber);
        User user = new User(1L, "UserLogin", "user@mail.com", "userPassword1", 2);
        User admin = new User(2L, "Admin", "admin@mail.com", "adminPassword1", 1);
        List<User> users = List.of(user, admin);
        List<UserRoleBean> userRoleBeans = new LinkedList<>();
        userRoleBeans.add(new UserRoleBean(user, Role.USER));
        userRoleBeans.add(new UserRoleBean(admin, Role.ADMIN));
        Map<Integer, String> userRoles = new LinkedHashMap<>();
        userRoles.put(1, Role.ADMIN.toString());
        userRoles.put(2, Role.USER.toString());
        when(userService.getNumberUsers()).thenReturn(totalRowsInTable);
        when(userService.getUsers(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(users);
        assertEquals(Path.LIST_USERS_PAGE, command.execute(req, resp));
        verify(req).setAttribute("userRoleBeans", userRoleBeans);
        verify(req).setAttribute("userRoles", userRoles);
        verify(req).setAttribute("desc", expectedSortOrder);
        verify(req).setAttribute("order_by", expectedColumnName);
        verify(req).setAttribute("pageNumber", expectedPageNumber);
        verify(req).setAttribute("totalPages", expectedTotalPages);
    }

    /**
     * @see ListUsersCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException() throws ServiceException {
        when(userService.getNumberUsers()).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));

        when(userService.getUsers(anyInt(), anyInt(), anyString(), anyString()))
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