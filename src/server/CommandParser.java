package server;
import java.nio.ByteBuffer;

public class CommandParser {
	private String command;
	private String arguments;
	
	public String getCommand() {
		return command;
	}
	public String getArguments() {
		return arguments;
	}
	public void parseCommandFromClient(ByteBuffer buffer) throws cannotExecuteCommand {
		String inputFromClient = new String(buffer.array());
		inputFromClient = inputFromClient.trim();

		if (inputFromClient.indexOf(" ") == -1) {
			throw new cannotExecuteCommand("Server: Invalid command");
		}
		String commandWithArguments[] = inputFromClient.split(" ", 2);
		command = commandWithArguments[0];
		arguments = commandWithArguments[1];
	}
}
