package ua.epam.akoreshev.finalproject.web.listener;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.dao.impl.ActivityDaoImpl;
import ua.epam.akoreshev.finalproject.model.dao.impl.IntervalDaoImpl;
import ua.epam.akoreshev.finalproject.model.dao.impl.RequestDaoImpl;
import ua.epam.akoreshev.finalproject.model.dao.impl.UserDaoImpl;
import ua.epam.akoreshev.finalproject.model.entity.Role;
import ua.epam.akoreshev.finalproject.web.command.*;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.service.impl.ActivityServiceImpl;
import ua.epam.akoreshev.finalproject.web.service.impl.IntervalServiceImpl;
import ua.epam.akoreshev.finalproject.web.service.impl.RequestServiceImpl;
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
        RequestDao requestDao = new RequestDaoImpl(getConnection(context));
        IntervalDao intervalDao = new IntervalDaoImpl(getConnection(context));
        LOG.debug("Created 'userDao' is: {}", userDao);
        LOG.debug("Created 'activityDao' is: {}", activityDao);
        LOG.debug("Created 'requestDao' is: {}", activityDao);

        UserService userService = new UserServiceImpl(userDao);
        ActivityServiceImpl activityService = new ActivityServiceImpl(activityDao);
        IntervalServiceImpl intervalService = new IntervalServiceImpl(intervalDao);
        RequestServiceImpl requestService = new RequestServiceImpl(requestDao);


        CommandContainer commands = new CommandContainer();
        Command command = new IndexCommand();
        commands.addCommand(null, command);
        commands.addCommand("", command);
        commands.addCommand("index_page", command);
        commands.addCommand("login", new LoginCommand(userService));
        commands.addCommand("register", new RegisterCommand(userService));
        commands.addCommand("logout", new LogoutCommand());
        commands.addCommand("change_locale", new ChangeLocaleCommand());
        commands.addCommand("profile", new ProfileCommand());
        commands.addCommand(Role.ADMIN + "_dashboard", new AdminDashboardCommand(requestService));
        commands.addCommand(Role.ADMIN + "_list_activities", new ListActivitiesCommand());
        commands.addCommand(Role.ADMIN + "_list_categories", new ListCategoriesCommand());
        commands.addCommand(Role.ADMIN + "_list_users", new ListUsersCommand());
        commands.addCommand(Role.ADMIN + "_timekeeping_report", new TimekeepingReportCommand());
        commands.addCommand(Role.USER + "_page", new UserPageCommand(activityService, intervalService));
        commands.addCommand(Role.USER + "_add_request_for_activity", new AddRequestCommand(requestService));
        commands.addCommand(Role.USER + "_set_start_time", new SetStartTimeCommand(intervalService));
        commands.addCommand(Role.USER + "_set_stop_time", new SetStopTimeCommand(intervalService));
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
