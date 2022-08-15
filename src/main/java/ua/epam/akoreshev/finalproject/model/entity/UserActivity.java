package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class UserActivity extends Entity {
    private long userId;
    private long activityId;
    private boolean isActive;

    public UserActivity() {
    }

    public UserActivity(long userId, long activityId, boolean isActive) {
        this.userId = userId;
        this.activityId = activityId;
        this.isActive = isActive;
    }

    @Override
    public long getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setId(long id) {
        throw new UnsupportedOperationException();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivity that = (UserActivity) o;
        return userId == that.userId &&
                activityId == that.activityId &&
                isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, activityId, isActive);
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "userId=" + userId +
                ", activityId=" + activityId +
                ", isActive=" + isActive +
                '}';
    }
}
