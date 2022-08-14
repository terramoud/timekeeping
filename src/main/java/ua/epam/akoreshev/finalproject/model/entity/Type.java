package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Type extends Entity {
    private String name;
    private long translationId;

    public Type() {
    }

    public Type(String name, long translationId) {
        this.name = name;
        this.translationId = translationId;
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

    public void setTranslationId(long translationId) {
        this.translationId = translationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return id == type.id &&
                translationId == type.translationId &&
                name.equals(type.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, translationId);
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", translationID=" + translationId +
                '}';
    }
}
