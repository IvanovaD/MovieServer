package server;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;


public class GetRequest {	
	
	public static void sendGetRequestToOMDb(String arguments, String requestURL, String dir) {
		
		File movieTextfile = new File(dir + arguments + ".txt");
	
		try (PrintWriter writerToMovieFile = new PrintWriter(movieTextfile);) {
			URL webPage = new URL(requestURL);
			BufferedReader movieInfoInputStream = new BufferedReader(new InputStreamReader(webPage.openStream()));
			
			int symbol;
			int count = 0;
			while ((symbol = movieInfoInputStream.read()) != -1) {
				writerToMovieFile.write(symbol);
				if (symbol == '[') {
					count = 0;
					writerToMovieFile.println();
				}
				if ((char) symbol == '"') {
						count++;
					if (count == 4) {
						while ((symbol = movieInfoInputStream.read()) != ',' && symbol != -1) {
							writerToMovieFile.write(symbol);
						}
						writerToMovieFile.println();
						count = 0;
					}
				}
			}
			movieInfoInputStream.close();
		} catch (IOException e) {
			System.out.println("Cannot write to the file");
		}
	}
	
	
	
}
