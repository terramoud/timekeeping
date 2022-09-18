package ua.epam.akoreshev.finalproject.web.listener;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.model.dao.*;
import ua.epam.akoreshev.finalproject.model.dao.impl.*;
import ua.epam.akoreshev.finalproject.model.entity.Role;
import ua.epam.akoreshev.finalproject.web.command.*;
import ua.epam.akoreshev.finalproject.web.service.*;
import ua.epam.akoreshev.finalproject.web.service.impl.*;
import ua.epam.akoreshev.finalproject.web.utils.ActivityValidator;
import ua.epam.akoreshev.finalproject.web.utils.CategoryValidator;
import ua.epam.akoreshev.finalproject.web.utils.UserValidator;

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
        CategoryDao categoryDao = new CategoryDaoImpl(getConnection(context));
        RequestDao requestDao = new RequestDaoImpl(getConnection(context));
        IntervalDao intervalDao = new IntervalDaoImpl(getConnection(context));
        LOG.debug("Created 'userDao' is: {}", userDao);
        LOG.debug("Created 'activityDao' is: {}", activityDao);
        LOG.debug("Created 'categoryDao' is: {}", categoryDao);
        LOG.debug("Created 'requestDao' is: {}", activityDao);
        LOG.debug("Created 'intervalDao' is: {}", intervalDao);

        UserValidator userValidator = new UserValidator();
        ActivityValidator activityValidator = new ActivityValidator();
        CategoryValidator categoryValidator = new CategoryValidator();

        UserService userService = new UserServiceImpl(userDao, userValidator);
        ActivityService activityService = new ActivityServiceImpl(activityDao, activityValidator);
        IntervalService intervalService = new IntervalServiceImpl(intervalDao);
        RequestService requestService = new RequestServiceImpl(requestDao);
        CategoryService categoryService = new CategoryServiceImpl(categoryDao, categoryValidator);

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
        commands.addCommand("change_password", new ChangePasswordCommand(userService));
        commands.addCommand("remove_account", new RemoveAccountCommand(userService));
        commands.addCommand(Role.ADMIN + "_dashboard", new AdminDashboardCommand(requestService));
        commands.addCommand(Role.ADMIN + "_list_activities", new ListActivitiesCommand(activityService));
        commands.addCommand(Role.ADMIN + "_list_categories", new ListCategoriesCommand(categoryService));
        commands.addCommand(Role.ADMIN + "_list_users", new ListUsersCommand(userService));
        commands.addCommand(Role.ADMIN + "_timekeeping_report", new TimekeepingReportCommand(intervalService));
        commands.addCommand(Role.ADMIN + "_approve_request", new ApproveRequestCommand(requestService));
        commands.addCommand(Role.ADMIN + "_deny_request", new DenyRequestCommand(requestService));
        commands.addCommand(Role.ADMIN + "_remove_user", new RemoveUserCommand(userService));
        commands.addCommand(Role.ADMIN + "_remove_category", new RemoveCategoryCommand(categoryService));
        commands.addCommand(Role.ADMIN + "_remove_activity", new RemoveActivityCommand(activityService));
        commands.addCommand(Role.ADMIN + "_change_user", new ChangeUserCommand(userService));
        commands.addCommand(Role.ADMIN + "_edit_activity", new EditActivityCommand(activityService));
        commands.addCommand(Role.ADMIN + "_create_activity", new CreateActivityCommand(activityService));
        commands.addCommand(Role.ADMIN + "_edit_category", new EditCategoryCommand(categoryService));
        commands.addCommand(Role.ADMIN + "_create_category", new CreateCategoryCommand(categoryService));
        commands.addCommand(Role.USER + "_page", new UserPageCommand(activityService, intervalService));
        commands.addCommand(Role.USER + "_add_request_for_activity", new AddRequestCommand(requestService));
        commands.addCommand(Role.USER + "_remove_request_for_activity", new RemoveRequestCommand(requestService));
        commands.addCommand(Role.USER + "_set_start_time", new SetStartTimeCommand(intervalService));
        commands.addCommand(Role.USER + "_set_stop_time", new SetStopTimeCommand(intervalService));

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
            LOG.error(e);
            throw new IllegalStateException("Cannot get connection from pool", e);
        }
    }
}
