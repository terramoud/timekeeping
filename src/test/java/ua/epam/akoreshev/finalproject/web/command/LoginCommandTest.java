package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.RequestException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.exceptions.UserException;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginCommandTest {
    private Command command;
    private HttpSession session;
    private UserService userService;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        command = new LoginCommand(userService);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see LoginCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlToUserPage() throws ServiceException, CommandException, UserException {
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        when(userService.findUserByLoginAndPassword(anyString(), anyString())).thenReturn(testUser);
        assertEquals(Path.USER_PAGE_COMMAND, command.execute(req, resp));
        verify(session).setAttribute("user", testUser);
    }

    /**
     * @see LoginCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlToAdminPage() throws ServiceException, CommandException, UserException {
        User admin = new User(1, "testAdmin1", "testUser1@mail.com", "testPassword", 1);
        when(userService.findUserByLoginAndPassword(anyString(), anyString())).thenReturn(admin);
        assertEquals(Path.ADMIN_PAGE_COMMAND, command.execute(req, resp));
        verify(session).setAttribute("user", admin);
    }

    /**
     * @see LoginCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlToIndexPageAndPutErrorMessageToSession()
            throws ServiceException, CommandException, UserException {
        when(userService.findUserByLoginAndPassword(anyString(), anyString()))
                .thenThrow(new UserException("Some message"));
        assertEquals("?command=" + Path.INDEX_PAGE_COMMAND, command.execute(req, resp));
        verify(session).setAttribute("isErrorMessage", true);
    }

    /**
     * @see LoginCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException() throws ServiceException, UserException {
        when(userService.findUserByLoginAndPassword(anyString(), anyString())).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));
    }
}