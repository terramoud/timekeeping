package ua.epam.akoreshev.finalproject.web.command;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
	private Map<String, Command> commands = new HashMap<>(); 
	public Command getCommand(String name) {
		return commands.get(name);
	}
	public void addCommand(String name, Command command) {
		commands.put(name, command);
	}

	@Override
	public String toString() {
		return "CommandContainer[" +
				"commands=>" + commands +
				']';
	}
}
