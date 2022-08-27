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
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/*",
        initParams = {@WebInitParam(name = "index_page_URI", value = "/timekeeping/")},
        dispatcherTypes = {DispatcherType.REQUEST}
)
public class IndexPageFilter implements Filter {
    private String indexPage;
    private static final Logger LOG = LogManager.getLogger(IndexPageFilter.class);

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
        if (httpRequest.getRequestURI().equals(indexPage)) {
            LOG.trace("Current page is index page");
            forwardToUserRoleHomePage(request, response, httpRequest, chain);
            LOG.trace("End doFilter");
            return;
        }
        chain.doFilter(request, response);
        LOG.trace("End doFilter");
    }

    private void forwardToUserRoleHomePage(ServletRequest request,
                                           ServletResponse response,
                                           HttpServletRequest httpRequest,
                                           FilterChain chain) throws ServletException, IOException {
        HttpSession session = httpRequest.getSession(false);
        if (session == null){
            chain.doFilter(request, response);
            return;
        }
        LOG.debug("Current session id is {}", session.getId());
        User user = (User) session.getAttribute("user");
        if (user == null) {
            chain.doFilter(request, response);
            return;
        }
        Role userRole = Role.getRole(user.getRoleId());
        LOG.debug("Current user role is {}", userRole);
        if (userRole == Role.USER) {
            request.getRequestDispatcher(Path.USER_PAGE).forward(request, response);
            LOG.debug("Forward user page is: {}", Path.USER_PAGE);
        }
        if (userRole == Role.ADMIN) {
            request.getRequestDispatcher(Path.ADMIN_PAGE).forward(request, response);
            LOG.debug("Forward admin page is: {}", Path.ADMIN_PAGE);
        }
    }

    @Override
    public void destroy() {
        LOG.trace("Filter destruction starts");
        LOG.trace("Filter destruction finished");
    }
}
