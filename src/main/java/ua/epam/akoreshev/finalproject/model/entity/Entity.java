package ua.epam.akoreshev.finalproject.model.entity;

/**
 * Basic class for every model entity
 */
public abstract class Entity {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
