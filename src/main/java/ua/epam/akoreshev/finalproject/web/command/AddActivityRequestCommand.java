package ua.epam.akoreshev.finalproject.web.command;

import ua.epam.akoreshev.finalproject.exceptions.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddActivityRequestCommand extends Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        return req.getHeader("referer");
    }
}
