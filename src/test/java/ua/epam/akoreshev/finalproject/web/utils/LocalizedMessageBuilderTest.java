package ua.epam.akoreshev.finalproject.web.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ua.epam.akoreshev.finalproject.TestConstants.RESOURCE_BUNDLE_NAME;

class LocalizedMessageBuilderTest {
    private HttpServletRequest req;

    @BeforeEach
    void setUp() {
        req = Mockito.mock(HttpServletRequest.class);
        when(req.getAttribute(any())).thenReturn("");
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        when(req.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(any())).thenReturn(RESOURCE_BUNDLE_NAME);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @see LocalizedMessageBuilder#getLocalizedMessage(String)
     */
    @Test
    void testGetLocalizedMessageDefaultLocale() {
        LocalizedMessageBuilder lmb = new LocalizedMessageBuilder(req);
        assertEquals("test message",
                lmb.getLocalizedMessage("localized_message_builder.test.message"));
    }

    /**
     * @see LocalizedMessageBuilder#getLocalizedMessage(String)
     */
    @Test
    void testGetLocalizedMessageEn() {
        HttpSession session = Mockito.mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("language")).thenReturn("en");
        LocalizedMessageBuilder lmb = new LocalizedMessageBuilder(req);
        assertEquals("test message",
                lmb.getLocalizedMessage("localized_message_builder.test.message"));
    }

    /**
     * @see LocalizedMessageBuilder#getLocalizedMessage(String)
     */
    @Test
    void testGetLocalizedMessageUk() {
        HttpSession session = Mockito.mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("language")).thenReturn("uk");
        LocalizedMessageBuilder lmb = new LocalizedMessageBuilder(req);
        assertEquals("тестове повідомлення",
                lmb.getLocalizedMessage("localized_message_builder.test.message"));
    }

    /**
     * @see LocalizedMessageBuilder#getLocalizedMessage(String)
     */
    @Test
    void testGetLocalizedMessageWhenInputKeyNull() {
        LocalizedMessageBuilder lmb = new LocalizedMessageBuilder(req);
        assertEquals("Warning! Cannot load message about result of action",
                lmb.getLocalizedMessage(null));
    }
}