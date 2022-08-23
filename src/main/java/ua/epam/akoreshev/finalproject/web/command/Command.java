package ua.epam.akoreshev.finalproject.web.command;


import ua.epam.akoreshev.finalproject.exceptions.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Command {
    protected Command() {
    }

    public abstract String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException;

    @Override
    public String toString() {
        return "(Command is instanceof " + getClass().getSimpleName() + ")";
    }
}
