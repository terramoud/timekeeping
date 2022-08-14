package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Activity extends Entity {
    private String name;
    private long translationId;
    private long categoryId;

    public Activity(String name, long translationId, long categoryId) {
        this.name = name;
        this.translationId = translationId;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTranslationId() {
        return translationId;
    }

    public void setTranslationId(long translationID) {
        this.translationId = translationID;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id && Objects.equals(name, activity.name) && translationId == activity.translationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, translationId, categoryId);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", translationId=" + translationId +
                ", categoryId=" + categoryId +
                '}';
    }
}
