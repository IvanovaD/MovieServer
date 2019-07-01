package tests;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import server.CommandParser;
import server.cannotExecuteCommand;

public class CommandParserTest {

	@Test
	public void testParsingCommand() throws cannotExecuteCommand {
		String command = "get-movie Titanic";
		ByteBuffer buffer = ByteBuffer.wrap(command.getBytes());
		CommandParser parser = new CommandParser();
		parser.parseCommandFromClient(buffer);
		assertEquals(parser.getArguments(), "Titanic");
		
	}
	@Test(expected = cannotExecuteCommand.class)
	public void testCannotExecuteCommand() throws cannotExecuteCommand 
	{String command = "get-movieTitanic";
	ByteBuffer buffer = ByteBuffer.wrap(command.getBytes());
	CommandParser parser = new CommandParser();
	parser.parseCommandFromClient(buffer);
		
		
	}

}
