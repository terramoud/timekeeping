package ua.epam.akoreshev.finalproject.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserActivityBean implements Serializable {
    private static final long serialVersionUID = -1921391169668545481L;
    private User user;
    private Activity activity;

    public UserActivityBean() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivityBean that = (UserActivityBean) o;
        return user.equals(that.user) &&
                activity.equals(that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, activity);
    }

    @Override
    public String toString() {
        return "UserActivityBean{" +
                "user=" + user +
                ", activity=" + activity +
                '}';
    }
}
