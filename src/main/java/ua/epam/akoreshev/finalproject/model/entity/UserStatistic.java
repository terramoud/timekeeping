package ua.epam.akoreshev.finalproject.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserStatistic implements Serializable {
    private static final long serialVersionUID = -6795726138330263064L;
    private User user;
    private Activity activity;
    private Time total;
    private long attempts;

    public UserStatistic(User user, Activity activity, Time total, int attempts) {
        this.user = user;
        this.activity = activity;
        this.total = total;
        this.attempts = attempts;
    }

    public UserStatistic() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Time getTotal() {
        return total;
    }

    public void setTotal(Time total) {
        this.total = total;
    }

    public long getAttempts() {
        return attempts;
    }

    public void setAttempts(long attempts) {
        this.attempts = attempts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStatistic that = (UserStatistic) o;
        return attempts == that.attempts &&
                user.equals(that.user) &&
                activity.equals(that.activity) &&
                total.equals(that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, activity, total, attempts);
    }

    @Override
    public String toString() {
        return "UserStatistic{" +
                "user=" + user +
                ", activity=" + activity +
                ", total=" + total +
                ", attempts=" + attempts +
                '}';
    }
}
