package ua.epam.akoreshev.finalproject.web.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        LOG.debug("Start context initialization");
        ServletContext context = sce.getServletContext();
        initDatasource(context);
        LOG.debug("DataSource initialized");
        initServices(context);
        LOG.debug("Services initialized");
    }

    private void initDatasource(ServletContext context) throws IllegalStateException {
        String dataSourceName = context.getInitParameter(DATASOURCE);
        try {
            Context initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup(dataSourceName);
            context.setAttribute(DATASOURCE, dataSource);
            LOG.trace("context.setAttribute 'dataSource': {}", dataSource.getClass().getName());
        } catch (NamingException e) {
            throw new IllegalStateException("Cannot initialize dataSource", e);
        }
    }


    private void initServices(ServletContext context) {
        getConnection(context);
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
