package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.util.List;

public interface UserDao extends BaseDao<User, Long> {
    User read(String login) throws DaoException;

    int getNumberUsers() throws DaoException;

    List<User> findAllUsers(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

