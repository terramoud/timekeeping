package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Activity extends Entity {
    private String nameEn;
    private String nameUk;
    private long categoryId;

    public Activity(String nameEn, String nameUk, long categoryId) {
        this.nameEn = nameEn;
        this.nameUk = nameUk;
        this.categoryId = categoryId;
    }

    public Activity() {
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameUk() {
        return nameUk;
    }

    public void setNameUk(String nameUk) {
        this.nameUk = nameUk;
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
        return id == activity.id &&
                categoryId == activity.categoryId &&
                nameEn.equals(activity.nameEn) &&
                nameUk.equals(activity.nameUk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameEn, nameUk, categoryId);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", nameEn='" + nameEn + '\'' +
                ", nameUk='" + nameUk + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
