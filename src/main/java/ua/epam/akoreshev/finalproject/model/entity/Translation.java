package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Translation extends Entity {
    private String name;
    private long languageId;

    public Translation() {
    }

    public Translation(String name, long languageId) {
        this.name = name;
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translation that = (Translation) o;
        return id == that.id &&
                languageId == that.languageId &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, languageId);
    }

    @Override
    public String toString() {
        return "Translation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", languageId=" + languageId +
                '}';
    }
}
