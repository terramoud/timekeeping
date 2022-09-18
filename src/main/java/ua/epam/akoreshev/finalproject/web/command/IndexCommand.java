package ua.epam.akoreshev.finalproject.web.command;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class IndexCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(IndexCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        LOG.trace("Command starts");
        LOG.trace("Command finished");
        return Path.INDEX_PAGE;
    }
}
