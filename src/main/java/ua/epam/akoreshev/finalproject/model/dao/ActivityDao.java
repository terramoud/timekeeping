package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.model.entity.Activity;

import java.util.List;

public interface ActivityDao extends BaseDao<Activity, Long> {
    List<Activity> findAllActivitiesByCategory(String categoryName);
    List<Activity> findAllActivitiesByCategory(long categoryId);
    List<Activity> findAllActivitiesByUser(String login);
    List<Activity> findAllActivitiesByUser(long userId);
    boolean removeActivityByUser(String login);
    boolean removeActivityByUser(long id);
}

