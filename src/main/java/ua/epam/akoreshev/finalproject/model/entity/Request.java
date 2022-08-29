package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Request extends Entity {
    private long userId;
    private long activityId;
    private long typeId;
    private long statusId;

    public Request(long id, long userId, long activityId, long typeId, long statusId) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.typeId = typeId;
        this.statusId = statusId;
    }

    public Request() {

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

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return userId == request.userId &&
                activityId == request.activityId &&
                typeId == request.typeId &&
                statusId == request.statusId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, activityId, typeId, statusId);
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityId=" + activityId +
                ", typeId=" + typeId +
                ", statusId=" + statusId +
                '}';
    }
}
