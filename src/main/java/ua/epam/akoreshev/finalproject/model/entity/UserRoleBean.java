package ua.epam.akoreshev.finalproject.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleBean implements Serializable {
    private static final long serialVersionUID = -8708254124499350502L;
    private User user;
    private Role role;

    public UserRoleBean(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleBean that = (UserRoleBean) o;
        return user.equals(that.user) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }

    @Override
    public String toString() {
        return "UserRoleBean{" +
                "user=" + user +
                ", role=" + role +
                '}';
    }
}
