package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ChangeLocaleCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ChangeLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String locale = req.getParameter("language");
        LOG.debug("App locale is: {}", locale);

        HttpSession session = req.getSession();
        session.setAttribute("language", locale);
        boolean isLocaleCookieExists = Arrays.stream(req.getCookies())
                .anyMatch(e -> e.getName().equals("defaultLocale"));
        if (!isLocaleCookieExists)
            resp.addCookie(new Cookie("defaultLocale", locale));

        String sourcePage = req.getParameter("source_page");
        String redirectUrl = req.getParameterMap()
                .entrySet().stream()
                .filter(param -> !param.getKey().equals("source_page"))
                .filter(param -> !param.getKey().equals("command"))
                .map(e -> e.getKey().concat("=").concat(e.getValue()[0]))
                .collect(Collectors.joining("&", "?","&"))
                .concat("command=").concat(sourcePage);
        LOG.debug("Redirect url is: {}", redirectUrl);
        return redirectUrl;
    }
}
