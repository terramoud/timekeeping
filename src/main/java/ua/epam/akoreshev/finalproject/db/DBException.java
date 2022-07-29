package ua.epam.akoreshev.finalproject.db;

/**
 * Throws an exception that
 * occurs while working with the database
 *
 * @author A.Koreshev
 */
public class DBException extends Exception {
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
