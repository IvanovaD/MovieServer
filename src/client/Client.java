package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class Client {

	private static int SERVER_PORT = 4444;
	private static String dir = "./clientFiles/";
	private static int MAX_BYTES_SIZE = 4;

	public static void main(String[] args) throws UnknownHostException, IOException {
		try (Socket socketClient = new Socket("localhost", SERVER_PORT);
				PrintWriter outputToServer = new PrintWriter(socketClient.getOutputStream(), true);
				BufferedReader standardInput = new BufferedReader(new InputStreamReader(System.in));
				InputStream inputFromSocket = socketClient.getInputStream();) {
			String userInput;
			System.out.println("Please, select a command: ");
			while (!(userInput = standardInput.readLine()).equals("quit")) {

				outputToServer.println(userInput);
				if (userInput.startsWith("get-movie-poster")) {
					getImage(inputFromSocket, socketClient, userInput);
				} else {
					getText(inputFromSocket, socketClient);
				}

				System.out.println("Please, select a command: ");
			}
		}
	}

	public static void getImage(InputStream inputFromSocket, Socket socketClient, String command) {
		if (command.indexOf(" ") == -1) {
			getText(inputFromSocket, socketClient);
		} else {
			try {
				String movie = command.split(" ", 2)[1];
				byte[] binarySize = new byte[MAX_BYTES_SIZE];
				inputFromSocket.read(binarySize);
				int imageSize = ByteBuffer.wrap(binarySize).asIntBuffer().get();

				byte[] binaryImage = new byte[imageSize];
				inputFromSocket.read(binaryImage);

				try {
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(binaryImage));
					ImageIO.write(image, "jpg", new File(dir + movie + ".jpg"));
					System.out.println("The poster has been downloaded in the local directory ");
				} catch (IllegalArgumentException i) {
					String formattedText = new String(binaryImage);
					System.out.println(formattedText);
				}

			} catch (IOException e) {
				System.out.println("Image cannot be downloaded");
			}
		}
	}

	public static String getText(InputStream inputFromSocket, Socket socketClient) {
		String formattedText = null;
		try {
			byte[] binarySize = new byte[MAX_BYTES_SIZE];
			inputFromSocket.read(binarySize);
			int sizeOfText = ByteBuffer.wrap(binarySize).asIntBuffer().get();
			byte[] encodedText = new byte[sizeOfText];
			inputFromSocket.read(encodedText);
			formattedText = new String(encodedText);
			System.out.println(formattedText);

		} catch (IOException e) {
			System.out.println("Try again");
		}
		return formattedText;

	}
}