package ua.epam.akoreshev.finalproject.web.listener;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.dao.impl.ActivityDaoImpl;
import ua.epam.akoreshev.finalproject.model.dao.impl.UserDaoImpl;
import ua.epam.akoreshev.finalproject.web.command.*;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.service.impl.ActivityServiceImpl;
import ua.epam.akoreshev.finalproject.web.service.impl.UserServiceImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener, HttpSessionListener {
    private static final Logger LOG = LogManager.getLogger(ContextListener.class);
    private static final String DATASOURCE = "datasource";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        javax.servlet.jsp.jstl.fmt.LocaleSupport l;
        LOG.trace("Start context initialization");
        ServletContext context = sce.getServletContext();
        initDatasource(context);
        LOG.trace("DataSource initialized");
        initServices(context);
        LOG.trace("Services initialized");
    }

    private void initDatasource(ServletContext context) throws IllegalStateException {
        String dataSourceName = context.getInitParameter(DATASOURCE);
        try {
            Context initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup(dataSourceName);
            context.setAttribute(DATASOURCE, dataSource);
            LOG.debug("context.setAttribute 'dataSource': {}", dataSource.getClass().getName());
        } catch (NamingException e) {
            throw new IllegalStateException("Cannot initialize dataSource", e);
        }
    }


    private void initServices(ServletContext context) {
        UserDao userDao = new UserDaoImpl(getConnection(context));
        ActivityDao activityDao = new ActivityDaoImpl(getConnection(context));
        LOG.debug("Created 'userDao' is: {}", userDao);
        LOG.debug("Created 'activityDao' is: {}", activityDao);

        UserService userService = new UserServiceImpl(userDao);

        CommandContainer commands = new CommandContainer();
        Command command = new IndexCommand();
        commands.addCommand(null, command);
        commands.addCommand("", command);
        commands.addCommand("index_page", command);
        commands.addCommand("login", new LoginCommand(userService));
        commands.addCommand("register", new RegisterCommand(userService));
        commands.addCommand("logout", new LogoutCommand());
        commands.addCommand("admin_dashboard", new AdminDashboardCommand());
        commands.addCommand("list_activities", new ListActivitiesCommand());
        commands.addCommand("list_categories", new ListCategoriesCommand());
        commands.addCommand("list_users", new ListUsersCommand());
        commands.addCommand("timekeeping_report", new TimekeepingReportCommand());
        commands.addCommand("user_page", new UserPageCommand(new ActivityServiceImpl(activityDao)));
        commands.addCommand("profile", new ProfileCommand());
        commands.addCommand("change_locale", new ChangeLocaleCommand());
        commands.addCommand("add_activity_request", new AddActivityRequestCommand());
//        commands.addCommand("save_profile", new SaveProfileCommand());

        context.setAttribute("commandContainer", commands);
        LOG.debug("context.setAttribute 'commandContainer': {}", commands);
    }

    private Connection getConnection(ServletContext context) {
        DataSource dataSource = (DataSource) context.getAttribute(DATASOURCE);
        LOG.debug("DataSource that using for connection is: {}", dataSource);
        try {
            Connection connection = dataSource.getConnection();
            LOG.debug("Connection has established successfully. Connection is: {}", connection);
            return connection;
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new IllegalStateException("Cannot get connection from pool", e);
        }
    }
}
