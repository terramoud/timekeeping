package ua.epam.akoreshev.finalproject.web.service;


import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.User;

public interface UserService {

    /**
     * Finds all users in DB
     * @return list of all users
     * @throws DaoException if unable to retrieve information for certain reasons
     */
//    List<User> findAllUsers() throws DaoException;

    /**
     * Finds first @param offset products starts from @param from row
     * @param from - first table line number
     * @param offset - number of records to find
     * @return List of users
     * @throws DaoException if unable to retrieve information for certain reasons
     */
//    List<User> findUsers(Integer from, Integer offset) throws DaoException;

    /**
     * Finds all users by Role
     * @param userRole - User role
     * @return List of all users with @param userRole
     * @throws DaoException if unable to retrieve information for certain reasons
     */
//    List<User> findUsersByRole(Role userRole) throws DaoException;

    /**
     * Finds user by User name and password
     * @param name - User name
     * @param password - user password
     * @return user found by username and password
     * @throws DaoException if unable to retrieve information for certain reasons
     */
//    User findUser(String name, String password) throws DaoException;

    /**
     * Adds a new user in DB
     * @param user - user to add
     * @return true if operation success and false if fails
     */
    boolean addUser(User user) throws DaoException;


    User findUserByLoginAndPassword(String login, String password) throws ServiceException;

    /**
     * Updates existent user data in DB
     * @param user -  user to update
     * @return true if operation success and false if fails
     */
//    boolean updateUser(User user);

    /**
     * Deletes user from DB
     * @param user - user to delete
     * @return true if operation success and false if fails
     */
//    boolean deleteUser(User user);
}

