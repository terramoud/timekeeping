package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;

import java.util.List;

public interface ActivityDao extends BaseDao<Activity, Long> {
    List<Activity> findAllActivitiesByCategory(String categoryName) throws DaoException;
    List<Activity> findAllActivitiesByCategory(long categoryId) throws DaoException;
    List<Activity> findAllActivitiesByUser(String login) throws DaoException;
    List<Activity> findAllActivitiesByUser(long userId) throws DaoException;
}

