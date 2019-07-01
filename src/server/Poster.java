package server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

public class Poster {
	public Poster() {
	}

	public void download(String requestURL, String filename, String dir) throws cannotExecuteCommand {
		String lineText = null;
		BufferedReader inputFromWebPage = null;
		try {
			URL webPage = new URL(requestURL);
			inputFromWebPage = new BufferedReader(new InputStreamReader(webPage.openStream()));
			lineText = inputFromWebPage.readLine();
			if (lineText.contains("\"Poster\":")) {
				String poster = lineText.replaceAll("\"", "").split("Poster:")[1];
				poster = poster.split(",", 2)[0];

				try {
					URL url = new URL(poster);
					BufferedImage image = ImageIO.read(url);
					File file = new File(dir + filename + ".jpg");
					ImageIO.write(image, "jpg", file);
				} catch (IOException e) {
					System.out.println("Cannot download image");

				}
			} else {
				throw new cannotExecuteCommand("Movie not found");
			}
		} catch (IOException e) {
			System.out.println("Sorry, movie not found.");

		}

	}
}
