package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.ActivityCategoryBean;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.util.List;

public interface ActivityDao extends BaseDao<Activity, Long> {
    List<Activity> findAllActivitiesByCategory(long categoryId) throws DaoException;

    List<Activity> findAllActivitiesByUser(long userId) throws DaoException;

    List<Category> findAllCategories() throws DaoException;

    int getNumberActivities() throws DaoException;

    List<ActivityCategoryBean> findAllActivities(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

