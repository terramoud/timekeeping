package ua.epam.akoreshev.finalproject.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DB manager. Works with Mysql DB.
 * Realises methods for working with DAO classes
 * Creates static instance for connection pool.
 * Returns Tomcat's connection pool.
 *
 * @author A.Koreshev
 */
public class DBManager {
    private static final String DATASOURCE_NAME = "jdbc/timekeeping";
    private static final String CONFIGURATION_ENV = "java:/comp/env";
    private static DBManager instance;
    private static final Logger LOG = LogManager.getLogger();

    private DBManager() {}

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() throws DBException {
        LOG.debug("Getting Tomcat connection pool from context");
        Connection con;
        try {
            // TODO: Simplify getting datasource from context to one string
            // TODO: Are there many logs in the code?
            Context initialContext = new InitialContext();
            LOG.trace("JNDI context initialization completed successfully");
            Context envContext = (Context) initialContext.lookup(CONFIGURATION_ENV);
            LOG.trace("Environment context from '" + CONFIGURATION_ENV + "' obtained successfully");
            DataSource ds = (DataSource) envContext.lookup(DATASOURCE_NAME);
            LOG.trace("DataSource instance successfully created from environment context");
            con = ds.getConnection();
        } catch (NamingException e) {
            LOG.error("Cannot get the DataSource class from JNDI context", e);
            throw new DBException("Cannot create DataSource instance", e);
        } catch (SQLException e) {
            LOG.error("Can't get Tomcat connection pool", e);
            throw new DBException("Cannot get a connection from the pool", e);
        }
        LOG.debug("Connection has already obtained and represents: " + con);
        return con;
    }

    public void commitAndClose(Connection con) throws DBException {
        try {
            LOG.trace("Start commit for changes in database and close connection");
            con.commit();
            LOG.trace("End commit for changes in database and close connection successfully");
            con.close();
            LOG.trace("Connection closed successfully");
        } catch (SQLException e) {
            LOG.error("Database changes don't committed and connection are still open", e);
            throw new DBException("Cannot commit or close connection from pool", e);
        }
    }

    public void rollbackAndClose(Connection con) throws DBException {
        try {
            LOG.trace("Start rollback changes in database and close connection");
            con.rollback();
            LOG.trace("End rollback changes in database successfully");
            con.close();
            LOG.trace("Connection closed successfully");
        } catch (SQLException e) {
            LOG.error("Database changes are not rolled back and connection are still open", e);
            throw new DBException("Cannot rollback and close connection from pool", e);
        }
    }
}
