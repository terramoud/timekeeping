package ua.epam.akoreshev.finalproject.model.entity;


import java.util.List;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public enum Role {
    ADMIN, USER;

    public static Role getRole(int userRoleId) {
        if (userRoleId < 1 || userRoleId > Role.values().length) {
            return null;
        }
        int roleId = userRoleId - 1;
        return Role.values()[roleId];
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
