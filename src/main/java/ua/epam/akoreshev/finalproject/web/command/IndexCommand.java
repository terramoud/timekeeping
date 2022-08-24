package ua.epam.akoreshev.finalproject.web.command;


import ua.epam.akoreshev.finalproject.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class IndexCommand extends Command {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		return Path.INDEX_PAGE;
	}
	
}