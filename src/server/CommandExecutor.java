package server;

import java.io.File;
import java.nio.channels.SocketChannel;

public class CommandExecutor {
	private String apiKey = "fd3e4005";
	private String omdbUrlDomain = "http://www.omdbapi.com/?t=";
	private String requestURL = null;
	private String command;
	private String arguments;
	private DataSender dataSender;
	private String dir = "./serverFiles/";
	private SocketChannel client;

	public CommandExecutor(SocketChannel client, String command, String arguments) {
		this.command = command;
		this.arguments = arguments;
		this.client = client;
		dataSender = new DataSender(arguments, client);
	}

	public void executeCommand() throws cannotExecuteCommand {

		if (command.equals("get-movie")) {
			getMovieByName();
			dataSender.writeToClient();
		} else if (command.equals("get-tv-series")) {
			getTvSeries();
			dataSender.writeToClient();
		} else if (command.equals("get-movie-poster")) {
			try{
				getMoviePoster();
				dataSender.transferPostertoClient();}
			catch(cannotExecuteCommand c){
				InvalidCommand.sendMessage(client);
				
			}
			
		} else if (command.equals("get-movies")) {
			dataSender.getMovies();
		} else {
			throw new cannotExecuteCommand("Cannot execute " + command);
		}
	}

	public String buildUrlFromArguments() {
		requestURL = (omdbUrlDomain + arguments.replaceAll(" ", "+") + "&apikey=" + apiKey);
		return requestURL;
	}

	public boolean isMovieCached(String movie) {
		File file = new File(dir + movie);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}

	public void getMovieByName() {
		buildUrlFromArguments();
		if (!isMovieCached(arguments + ".txt")) {
			GetRequest.sendGetRequestToOMDb(arguments, requestURL, dir);
		}
	}

	public void getTvSeries() {
		String seriesName[] = arguments.split(" --season=");
		String seriesBySeason = seriesName[0];
		String season = seriesName[1];

		requestURL = omdbUrlDomain + seriesBySeason.replaceAll(" ", "+") + "&season=" + season + "&apikey=" + apiKey;
		if (!isMovieCached(seriesBySeason + ".txt")) {
			GetRequest.sendGetRequestToOMDb(arguments, requestURL, dir);
		}
	}

	public void getMoviePoster() throws cannotExecuteCommand {
		buildUrlFromArguments();
		if (!isMovieCached(arguments + ".jpg")) {
			Poster image = new Poster();
			image.download(requestURL, arguments, dir);
		}

	}

}
