package ua.epam.akoreshev.finalproject.web.listener;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.web.command.Command;
import ua.epam.akoreshev.finalproject.web.command.CommandContainer;
import ua.epam.akoreshev.finalproject.web.command.IndexCommand;

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
//        jakarta.servlet.jsp.jstl.fmt.LocaleSupport l;
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
        getConnection(context);

        // create services
//        ProductService productService = new ProductServiceImpl(productDao);
//        context.setAttribute("addService", productService);
//        LOG.trace("context.setAttribute 'addService': {}", productService);

        CommandContainer commands = new CommandContainer();
        Command command = new IndexCommand();
        commands.addCommand(null, command);
        commands.addCommand("", command);

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
