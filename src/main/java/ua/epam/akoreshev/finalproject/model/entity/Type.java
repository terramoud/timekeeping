package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Type extends Entity {
    private String nameEn;
    private String nameUk;

    public Type(String nameEn, String nameUk) {
        this.nameEn = nameEn;
        this.nameUk = nameUk;
    }

    public Type() {
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
        Type type = (Type) o;
        return nameEn.equals(type.nameEn) &&
                nameUk.equals(type.nameUk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameEn, nameUk);
    }

    @Override
    public String toString() {
        return "Type{" +
                "nameEn='" + nameEn + '\'' +
                ", nameUk='" + nameUk + '\'' +
                ", id=" + id +
                '}';
    }
}
