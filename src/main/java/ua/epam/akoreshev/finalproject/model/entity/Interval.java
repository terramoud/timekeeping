package ua.epam.akoreshev.finalproject.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Interval extends Entity {
    private LocalDateTime start;
    private LocalDateTime finish;
    private long userId;
    private long activityId;

    public Interval() {
    }

    public Interval(LocalDateTime start, LocalDateTime finish, long userId, long activityId) {
        this.start = start;
        this.finish = finish;
        this.userId = userId;
        this.activityId = activityId;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval interval = (Interval) o;
        return id == interval.id &&
                userId == interval.userId &&
                activityId == interval.activityId &&
                Objects.equals(start, interval.start) &&
                Objects.equals(finish, interval.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start, finish, userId, activityId);
    }

    @Override
    public String toString() {
        return "Interval{" +
                "id=" + id +
                ", start=" + start +
                ", finish=" + finish +
                ", userID=" + userId +
                ", activityID=" + activityId +
                '}';
    }
}
