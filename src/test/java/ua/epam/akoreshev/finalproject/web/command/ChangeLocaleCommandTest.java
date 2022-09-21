package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChangeLocaleCommandTest {
    private Command command;
    private HttpSession session;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        command = new ChangeLocaleCommand();
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        resp.setHeader(eq("referer"), any());
        session = Mockito.mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(req.getAttribute(any())).thenReturn("");
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see ChangeLocaleCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldReturnUrlAndPutToSessionLanguage() throws CommandException {
        when(req.getParameter("language")).thenReturn("en");
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("language", "en");
        verify(resp).addCookie(any());

        when(req.getParameter("language")).thenReturn("uk");
        assertEquals(req.getHeader("referer"), command.execute(req, resp));
        verify(session).setAttribute("language", "uk");
        verify(resp, atLeast(2)).addCookie(any());
    }
}