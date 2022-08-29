package ua.epam.akoreshev.finalproject.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.model.entity.Role;
import ua.epam.akoreshev.finalproject.model.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

@WebFilter(urlPatterns = "/*",
        initParams = {@WebInitParam(name = "index_page_URI", value = "/timekeeping/")},
        dispatcherTypes = {DispatcherType.REQUEST}
)
public class SecurityFilter implements Filter {
    private String indexPage;
    private static final Logger LOG = LogManager.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.trace("Start init filter");
        indexPage = filterConfig.getInitParameter("index_page_URI");
        LOG.trace("End init filter");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        LOG.trace("Start doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        LOG.debug("Current page is: {}", httpRequest.getRequestURI());
        if (httpRequest.getRequestURI().equals(indexPage)) {
            chain.doFilter(request, response);
            LOG.trace("End doFilter");
            return;
        }
        HttpSession session = httpRequest.getSession(false);
        LOG.debug("Session is: {}", session);
        if (session == null) {
            LOG.debug("Forward page is: {}", Path.INDEX_PAGE);
            request.getRequestDispatcher(Path.INDEX_PAGE).forward(request, response);
            LOG.trace("End doFilter");
            return;
        }
        User user = (User) session.getAttribute("user");
        LOG.debug("Current user is {}", user);
        String currentCmd = request.getParameter("command");
        LOG.debug("Current command is: {}", currentCmd);
        if (user == null) {
            doFilterAvailableCommands(request, response, chain, currentCmd);
            LOG.trace("End doFilter");
            return;
        }
        // Registered user part
        Role userRole = Role.getRole(user.getRoleId());
        LOG.debug("Current user role is {}", userRole);
        boolean isForbiddenCommand = currentCmd != null && Arrays.stream(Role.values())
                .filter(role -> role != userRole)
                .anyMatch(otherRole -> currentCmd.startsWith(otherRole.toString()));
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (isForbiddenCommand && userRole == Role.ADMIN) {
            LOG.debug("Redirect to: {}", Path.ADMIN_PAGE_COMMAND);
            httpResponse.sendRedirect(Path.ADMIN_PAGE_COMMAND);
            LOG.trace("End doFilter");
            return;
        }
        if (isForbiddenCommand && userRole == Role.USER) {
            LOG.debug("Redirect to: {}", Path.USER_PAGE_COMMAND);
            httpResponse.sendRedirect(Path.USER_PAGE_COMMAND);
            LOG.trace("End doFilter");
            return;
        }
        boolean badCommand = (currentCmd == null || currentCmd.isEmpty() || currentCmd.equals("index_page"));
        if (badCommand && userRole == Role.ADMIN) {
            LOG.debug("Redirect to: {}", Path.ADMIN_PAGE_COMMAND);
            httpResponse.sendRedirect(Path.ADMIN_PAGE_COMMAND);
            LOG.trace("End doFilter");
            return;
        }
        if (badCommand && userRole == Role.USER) {
            LOG.debug("Redirect to: {}", Path.USER_PAGE_COMMAND);
            httpResponse.sendRedirect(Path.USER_PAGE_COMMAND);
            LOG.trace("End doFilter");
            return;
        }
        chain.doFilter(request, response);
        LOG.trace("End doFilter");
    }

    private void doFilterAvailableCommands(ServletRequest request,
                                           ServletResponse response,
                                           FilterChain chain,
                                           String currentCmd) throws ServletException, IOException {
        if (currentCmd == null
                || currentCmd.isEmpty()
                || currentCmd.equals(Path.INDEX_PAGE_COMMAND)
                || currentCmd.equals(Path.CHANGE_LOCALE_COMMAND)
                || currentCmd.equals(Path.LOGOUT_COMMAND)
                || currentCmd.equals(Path.LOGIN_COMMAND)
                || currentCmd.equals(Path.REGISTER_COMMAND)) {
            chain.doFilter(request, response);
            return;
        }
        LOG.debug("Forward page is: {}", Path.INDEX_PAGE);
        request.getRequestDispatcher(Path.INDEX_PAGE).forward(request, response);
    }
}
