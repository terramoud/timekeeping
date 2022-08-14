package ua.epam.akoreshev.finalproject.model.entity;


/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public enum Role {
    ADMIN, USER;

    public static Role getRole(User user) {
        int roleId = user.getRoleId();
        return Role.values()[roleId];
    }

    public String getName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
