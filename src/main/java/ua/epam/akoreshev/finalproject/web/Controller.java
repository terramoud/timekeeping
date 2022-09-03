package ua.epam.akoreshev.finalproject.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.web.command.Command;
import ua.epam.akoreshev.finalproject.web.command.CommandContainer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    public static final String COMMAND = "command";
    private CommandContainer commands;

    @Override
    public void init(ServletConfig config) {
        commands = (CommandContainer) config.getServletContext().getAttribute("commandContainer");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String url = getUrl(req, resp);
            LOG.debug("url: {}", url);
            req.getRequestDispatcher(url).forward(req, resp);
            LOG.debug("forward: {}", url);
        } catch (CommandException e) {
            LOG.error("Error has been thrown by {} command", req.getParameter(COMMAND), e);
            send500Error(resp, e);
        } catch (IOException | ServletException e) {
            LOG.error("Error has been thrown by controller servlet", e);
            send500Error(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String url = getUrl(req, resp);
            LOG.debug("url: {}", url);
            resp.sendRedirect(url);
            LOG.debug("redirected: {}", url);
        } catch (CommandException e) {
            LOG.error("Error has been thrown by {} command", req.getParameter(COMMAND), e);
            send500Error(resp, e);
        } catch (IOException e) {
            LOG.error("Error has been thrown by controller servlet", e);
            send500Error(resp, e);
        }
    }

    private String getUrl(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        String commandName = req.getParameter(COMMAND);
        LOG.debug("Request parameter: command name --> '{}'", commandName);
        Command command = commands.getCommand(commandName);
        LOG.debug("Obtained command: {}", command);
        if (command == null) {
            throw new CommandException("Non-existent command");
        }
        return command.execute(req, resp);
    }

    void send500Error(HttpServletResponse resp, Exception e) {
        try {
            resp.sendError(500, e.getMessage());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
