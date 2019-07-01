package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import server.CommandExecutor;
import server.cannotExecuteCommand;

public class CommandExecutorTest {
	private static String command;
	private static String arguments;
	private static String dir;
	private static CommandExecutor executor;
	private static File file;
	
	@BeforeClass
	public static void prepareClass() {
	dir = "./testFiles/";
	}
	
	@Test
	public void shouldGetMovie()
	{
		command = "get-movie";
		arguments = "Tarzan";
		executor = new CommandExecutor(null, command, arguments);
		executor.setDir(dir);
		executor.getMovieByName();
		file = new File(dir + arguments+".txt");
		assertTrue(file.exists());
	    file.delete();
	}
	@Test
	public void shouldGetTvSeries()
	{
		command = "get-tv-series";
		arguments = "Peaky Blinders --season=2";
		executor = new CommandExecutor(null, command, arguments);
		executor.setDir(dir);
		executor.getTvSeries();
		file = new File(dir + arguments + ".txt");
		assertTrue(file.exists());
	    file.delete();
	}
	@Test
	public void shouldGetPoster() throws cannotExecuteCommand
	{
		command = "get-movie-poster";
		arguments = "How to Train your Dragon";
		executor = new CommandExecutor(null, command, arguments);
		executor.setDir(dir);
		executor.getMoviePoster();
		file = new File(dir + arguments + ".jpg");
		assertTrue(file.exists());
	    file.delete();
	}
	@Test
	public void movieShouldBeCached()
	{
		executor = new CommandExecutor(null, command, arguments);
		executor.setDir(dir);
		assertTrue(executor.isMovieCached("Inception.txt"));
		
	}
}
