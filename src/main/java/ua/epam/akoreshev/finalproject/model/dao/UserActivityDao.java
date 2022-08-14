package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.model.entity.UserActivity;

public interface UserActivityDao extends BaseDao<UserActivity, Long> {
    boolean setIsActiveFlagForUserAndActivity(long userId, long activityId, boolean bool);
    boolean removeByUserAndActivity(long userId, long activityId);
    boolean removeAllUsersAndActivitiesByUser(long userId);
}

