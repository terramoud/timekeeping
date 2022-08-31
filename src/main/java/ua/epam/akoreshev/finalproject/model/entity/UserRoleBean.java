package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

public class UserRoleBean {
    private User user;
    String role;

    public UserRoleBean(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleBean that = (UserRoleBean) o;
        return user.equals(that.user) &&
                role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }


}
