package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Status extends Entity {
    private String nameEn;
    private String nameUk;

    public Status(String nameEn, String nameUk) {
        this.nameEn = nameEn;
        this.nameUk = nameUk;
    }

    public Status() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return nameEn.equals(status.nameEn) &&
                nameUk.equals(status.nameUk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameEn, nameUk);
    }

    @Override
    public String toString() {
        return "Status{" +
                "nameEn='" + nameEn + '\'' +
                ", nameUk='" + nameUk + '\'' +
                ", id=" + id +
                '}';
    }
}
