package server;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import javax.imageio.ImageIO;

public class DataSender {

	private String arguments;
	private int BUFFER_SIZE = 100000;
	private int BYTES_FOR_SIZE = 4; 
	private  String dir = "./serverFiles/";
	private SocketChannel client;

	public DataSender(String arguments, SocketChannel client) {
		this.arguments = arguments;
		this.client = client;
	}

	public void getMovies() {
	    ByteBuffer bufferOfMovies = ByteBuffer.allocate(BUFFER_SIZE);
	   
		MovieSelection selection = new MovieSelection();
		selection.parseCommand(arguments);
		List<String> movies = selection.orderMovieList();
		for (String movie : movies) {
			bufferOfMovies.put((movie + "\n").getBytes());
		}
		int size = bufferOfMovies.position();
		bufferOfMovies.clear();
		bufferOfMovies.put(ByteBuffer.allocate(BYTES_FOR_SIZE).putInt(size).array());
		for (String movie : movies) {
			bufferOfMovies.put((movie + "\n").getBytes());
		}
		bufferOfMovies.flip();
		while (bufferOfMovies.hasRemaining()) {
			try {
				client.write(bufferOfMovies);
			} catch (IOException e) {
				System.out.println("Connection lost");
			}
		}
	}

	public void writeToClient() {
		File movieInfo = new File(dir + arguments + ".txt");
		
		ByteArrayOutputStream outputTextFile = new ByteArrayOutputStream();
		try (InputStream inputStreamMovieInfo = new FileInputStream(movieInfo)) {
			byte[] buff = new byte[2048];
			int readBytes = 0;
			while ((readBytes = inputStreamMovieInfo.read(buff)) != -1) {
				outputTextFile.write(buff, 0, readBytes);
			}
			
			outputTextFile.flush();
			
			ByteBuffer movieTextFile  = ByteBuffer.allocate(BUFFER_SIZE);
			movieTextFile.put(ByteBuffer.allocate(BYTES_FOR_SIZE).putInt(buff.length).array());
			movieTextFile.put(outputTextFile.toByteArray());
			movieTextFile.flip();
			
			while (movieTextFile.hasRemaining()) {
				client.write(movieTextFile);
			}
		} catch (IOException e) {
			System.out.println("Server could not find the file!");
		}
	}

	public void transferPostertoClient() {
		BufferedImage imageBuffer;
		try {
			imageBuffer = ImageIO.read(new File(dir + arguments + ".jpg"));
			ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
			ImageIO.write(imageBuffer, "jpg", imageOutputStream);
			
			byte[] sizeOfImage = ByteBuffer.allocate(BYTES_FOR_SIZE).putInt(imageOutputStream.size()).array();
			ByteBuffer poster  = ByteBuffer.allocate(BUFFER_SIZE);
			poster.put(sizeOfImage);
			
			byte[] image = imageOutputStream.toByteArray();
			poster.put(image);
			poster.flip();
			
			while (poster.hasRemaining()) {
				client.write(poster);
			}

		} catch (IOException e) {
			System.out.println("Server could not find the file!");
		}
	}

}
