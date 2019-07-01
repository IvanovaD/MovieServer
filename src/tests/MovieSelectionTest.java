package tests;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

import server.MovieSelection;

public class MovieSelectionTest {

	private static String dir;
	private static MovieSelection movieSelection1;
	private static MovieSelection movieSelection2;
	private static MovieSelection movieSelection3;
	private static MovieSelection movieSelection4;
	private static String movie;
	private static List<String> movieList;
	private static List<String> folderMovieList;


	@BeforeClass
	public static void prepare() {
		dir = "./testFiles/";
		movieSelection1 = new MovieSelection();
		movieSelection1.setDir(dir);
		movie = "Titanic.txt";

		movieList = movieSelection1.makeListOfMovieFiles();
		folderMovieList = new ArrayList<String>();
		folderMovieList.add("Inception.txt");
		folderMovieList.add("Titanic.txt");

		movieSelection2 = new MovieSelection();
		movieSelection2.setDir(dir);
		movieSelection3 = new MovieSelection();
		movieSelection3.setDir(dir);
		movieSelection4 = new MovieSelection();
		movieSelection4.setDir(dir);

	}

	@Test
	public void shouldReturnTheRightRating() {
		assertEquals(7.8, movieSelection1.getMovieRating(movie), 1);
	}

	@Test
	public void shouldContainActorsAndGenres1() throws IOException {
		movieSelection1.parseCommand("--order=asc --actors=Leonardo DiCaprio");
		assertTrue(movieSelection1.isContainingActorsAndGenres(movie));
	}

	@Test
	public void shouldContainActorsAndGenres2() throws IOException {
		movieSelection1.parseCommand("--order=asc --actors=Leonardo DiCaprio, Kate Winslet");
		assertTrue(movieSelection1.isContainingActorsAndGenres(movie));
	}

	@Test
	public void shouldContainActorsAndGenres3() throws IOException {
		movieSelection2.parseCommand("--order=asc --genres=Drama --actors=Leonardo DiCaprio");
		assertTrue(movieSelection2.isContainingActorsAndGenres(movie));
	}

	@Test
	public void should_NOT_ContainActorsAndGenres() throws IOException {
		movieSelection3.parseCommand("--order=asc --actors=Lena Headey");
		assertFalse(movieSelection3.isContainingActorsAndGenres(movie));
	}

	@Test
	public void checkMovieList() {
		for (int i = 0; i < folderMovieList.size(); i++) {
			assertEquals(folderMovieList.get(i), movieList.get(i));
		}
	}

	@Test
	public void shouldOrderMoviesDesc() {
		movieSelection4.parseCommand("--order=desc");
		List<String> orderDesc = movieSelection4.orderMovieList();
		for (int i = 0; i < folderMovieList.size(); i++) {
			assertEquals(folderMovieList.get(i), orderDesc.get(i));
		}
	}

	@Test
	public void shouldOrderMoviesAsc() {
		movieSelection4.parseCommand("--order=asc");
		List<String> folderMovieList2 = new ArrayList<String>();
		folderMovieList2.add("Titanic.txt");
		folderMovieList2.add("Inception.txt");

		List<String> orderAsc = movieSelection4.orderMovieList();
		for (int i = 0; i < folderMovieList2.size(); i++) {
			assertEquals(folderMovieList2.get(i), orderAsc.get(i));

		}
	}

}
