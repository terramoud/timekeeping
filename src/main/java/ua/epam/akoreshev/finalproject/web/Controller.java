package ua.epam.akoreshev.finalproject.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
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
    private CommandContainer commands;

    @Override
    public void init(ServletConfig config) {
        commands = (CommandContainer) config.getServletContext().getAttribute("commandContainer");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            System.out.println("GET!!!!!!!!!!!!!!!!!!!!!!!!!!" + req.getHeader("referer"));
            String url = getUrl(req, resp);
            LOG.debug("url: {}", url);
            req.getRequestDispatcher(url).forward(req, resp);
            LOG.debug("forward: {}", url);
        } catch (CommandException | IOException | ServletException e) {
            LOG.error("Error has been thrown by front controller", e);
            try {
                LOG.debug("REDIRECT TO {}", Path.ERROR_PAGE500);
                req.getRequestDispatcher(Path.ERROR_PAGE500).forward(req, resp);
            } catch (ServletException servletException) {
                servletException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
//            resp.sendError(500, "Cannot process the command");
//			throw new ServletException("Cannot process the command", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            System.out.println("POST!!!!!!!!!!!!!!!!!!!!!!!!!!" + req.getHeader("referer"));
            String url = getUrl(req, resp);
            resp.sendRedirect(url);
            LOG.debug("redirected: {}", url);
        } catch (CommandException | IOException e) {
            LOG.error("Error has been thrown by front controller", e);
            try {
                LOG.debug("REDIRECT TO {}", Path.ERROR_PAGE500);
                req.getRequestDispatcher(Path.ERROR_PAGE500).forward(req, resp);
            } catch (ServletException servletException) {
                servletException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
//            resp.sendError(500, "Cannot process the command");
//			throw new ServletException("Cannot process the command", e);
        }
    }

    private String getUrl(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        System.out.println(req.getMethod());
        String commandName = req.getParameter("command");
        LOG.debug("Request parameter: command name --> \"{}\"", commandName);
        Command command = commands.getCommand(commandName);
        LOG.debug("Obtained command: {}", command);
        if (command == null) {
            return Path.ERROR_PAGE500;
        }
        return command.execute(req, resp);
    }
}
