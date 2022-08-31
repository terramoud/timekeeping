package ua.epam.akoreshev.finalproject.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserActivityRequest implements Serializable {
    private static final long serialVersionUID = -6795726138330263064L;
    private long requestId;
    private User user;
    private Activity activity;
    private Status status;
    private Type type;

    public UserActivityRequest(User user, Activity activity, Status status, Type type) {
        this.user = user;
        this.activity = activity;
        this.status = status;
        this.type = type;
    }

    public UserActivityRequest() {

    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivityRequest that = (UserActivityRequest) o;
        return requestId == that.requestId &&
                user.equals(that.user) &&
                activity.equals(that.activity) &&
                status.equals(that.status) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, user, activity, status, type);
    }

    @Override
    public String toString() {
        return "UserActivityRequest{" +
                "requestId=" + requestId +
                ", user=" + user +
                ", activity=" + activity +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
