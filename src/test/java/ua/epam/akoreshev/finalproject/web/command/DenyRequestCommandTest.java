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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.epam.akoreshev.finalproject.TestConstants.RESOURCE_BUNDLE_NAME;

class DenyRequestCommandTest {
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
        command = new DenyRequestCommand(requestService);
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
     * @see DenyRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlAndPutToSessionSuccessMessage()
            throws ServiceException, RequestException, CommandException {
        when(requestService.rejectRequest(anyLong())).thenReturn(true);
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("isErrorMessage", false);
        verify(session).setAttribute(eq("message"), any());
    }

    /**
     * @see DenyRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlAndPutToSessionErrorMessage()
            throws ServiceException, RequestException, CommandException {
        when(requestService.rejectRequest(anyLong())).thenReturn(false);
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("isErrorMessage", true);
        verify(session).setAttribute(eq("message"), any());
    }

    /**
     * @see DenyRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlAndPutToSessionErrorMessageWhenServiceThrowRequestException()
            throws ServiceException, RequestException, CommandException {
        when(requestService.rejectRequest(anyLong())).thenThrow(new RequestException("Some message"));
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("isErrorMessage", true);
        verify(session).setAttribute(eq("message"), any());
    }

    /**
     * @see DenyRequestCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException()
            throws ServiceException, RequestException {
        when(requestService.rejectRequest(anyLong())).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));
    }
}