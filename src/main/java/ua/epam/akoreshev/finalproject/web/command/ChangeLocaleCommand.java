package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangeLocaleCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ChangeLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String locale = req.getParameter("language");
        LOG.debug("Set locale to: {}", locale);
        HttpSession session = req.getSession();
        session.setAttribute("language", locale);
        LOG.debug("Session.setAttribute language is: {}", locale);
        resp.addCookie(new Cookie("defaultLocale", locale));
        LOG.debug("Cookie.setAttribute language is: {}", locale);
        return req.getHeader("referer");
    }
}
