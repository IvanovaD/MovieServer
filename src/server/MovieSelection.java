package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieSelection {
	private String actor1 = "";
	private String actor2 = "";
	private String genre1 = "";
	private String genre2 = "";
	private String order = null;
	private String dir = "./serverFiles/";

	public MovieSelection() {
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public double getMovieRating(String movie) {
		String movieRating = "0";
		String lineText = null;

		try (BufferedReader input = new BufferedReader(new FileReader(dir + movie))) {
			while ((lineText = input.readLine()) != null) {
				if (lineText.contains("imdbRating\":")) {
					movieRating = lineText.replaceAll("\"", "").replaceAll("imdbRating:", "").trim();

				}
			}
		} catch (IOException e) {
			System.out.println("Server could not find the file!");
		}
		return Double.parseDouble(movieRating);
	}

	public boolean isContainingActorsAndGenres(String movie) throws IOException {
		String lineText = null;

		try (BufferedReader input = new BufferedReader(new FileReader(dir + movie))) {
			while ((lineText = input.readLine()) != null) {
				if (lineText.contains("Actors\":")) {
					if (!(lineText.contains(actor1) && lineText.contains(actor2))) {
						return false;
					}
				}
				if (lineText.contains("Genre\":")) {
					if (!(lineText.contains(genre1) && lineText.contains(genre2))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public void parseActors(String actors) {
		actors = actors.replace("actors=", "");
		if (actors.contains(",")) {
			actor1 = actors.split(",")[0];
			actor2 = actors.split(",")[1].trim();
		} else {
			actor1 = actors.trim();
		}
	}

	public void parseGenres(String genres) {
		genres = genres.replace("genres=", "");
		if (genres.contains(",")) {
			genre1 = genres.split(",")[0];
			genre2 = genres.split(",")[1].trim();
		} else {
			genre1 = genres.trim();
		}
	}

	public void parseOrder(String parameter) {
		parameter = parameter.replace("order=", "");
		order = parameter.trim();
	}

	public void parseCommand(String command) {
		String parameters[] = command.split("--");

		for (String parameter : parameters) {
			if (parameter.startsWith("actors=")) {
				parseActors(parameter);
			}
			if (parameter.startsWith("genres=")) {
				parseGenres(parameter);
			}
			if (parameter.startsWith("order=")) {
				parseOrder(parameter);
			}
		}
	}

	public List<String> makeListOfMovieFiles() {
		List<String> movies = new ArrayList<String>();
		try (DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get(dir), "*.txt")) {
			for (Path moviePath : paths) {
				{
					if (!moviePath.toString().contains("season=")
							&& isContainingActorsAndGenres(moviePath.toFile().getName())) {
						movies.add(moviePath.toFile().getName());
					}
				}
			}
		} catch (IOException | DirectoryIteratorException x) {
			System.out.println("Server could not find the file/directory!");
		}
		return movies;
	}

	public List<String> orderMovieList() {
		List<String> movies = makeListOfMovieFiles().stream().collect(Collectors.toList());

		if (order == null) {
			return movies.stream().collect(Collectors.toList());
		}
		Map<String, Double> movieAndRating = movies.stream().collect(Collectors.toMap(x -> x, x -> getMovieRating(x)));

		if (order.equals("asc")) {
			return movies.stream().sorted((String x, String y) -> (int) (movieAndRating.get(x) - movieAndRating.get(y)))
					.collect(Collectors.toList());
		}
		return movies.stream().sorted((String x, String y) -> (int) (movieAndRating.get(y) - movieAndRating.get(x)))
				.collect(Collectors.toList());
	}
}