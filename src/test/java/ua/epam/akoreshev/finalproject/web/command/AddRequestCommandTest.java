package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.RequestException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ua.epam.akoreshev.finalproject.TestConstants.RESOURCE_BUNDLE_NAME;

class AddRequestCommandTest {
    private Command command;
    private HttpSession session;
    private RequestService requestService;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        requestService = Mockito.mock(RequestService.class);
        command = new AddRequestCommand(requestService);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        resp.setHeader(eq("referer"), any());
        session = Mockito.mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("user")).thenReturn(new User());
        when(req.getAttribute(any())).thenReturn("");
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        when(req.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(any())).thenReturn(RESOURCE_BUNDLE_NAME);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see AddRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlAndPutToSessionSuccessMessage() throws ServiceException, RequestException, CommandException {
        when(requestService.createRequest(any())).thenReturn(true);
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("isErrorMessage", false);
        verify(session).setAttribute(eq("message"), any());
    }

    /**
     * @see AddRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlAndPutToSessionErrorMessage() throws ServiceException, RequestException, CommandException {
        when(requestService.createRequest(any())).thenReturn(false);
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("isErrorMessage", true);
        verify(session).setAttribute(eq("message"), any());
    }

    /**
     * @see AddRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlAndPutToSessionErrorMessageWhenServiceThrowRequestException() throws ServiceException, RequestException, CommandException {
        when(requestService.createRequest(any())).thenThrow(new RequestException("Cannot add request"));
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("isErrorMessage", true);
        verify(session).setAttribute(eq("message"), any());
    }

    /**
     * @see AddRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException() throws ServiceException, RequestException {
        when(requestService.createRequest(any())).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));
    }

    /**
     * @see AddRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenSessionOrUserIsNotExists() throws ServiceException, RequestException {
        Mockito.reset(session);
        when(requestService.createRequest(any())).thenReturn(true);
        assertThrows(NullPointerException.class, () -> command.execute(req, resp));
    }
}