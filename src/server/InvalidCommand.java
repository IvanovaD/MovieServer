package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class InvalidCommand {
	public static void sendMessage(SocketChannel client) {
		int BUFFER_SIZE = 1000;
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.clear();
		String message = "Invalid command!";
		buffer.put(ByteBuffer.allocate(4).putInt(message.getBytes().length).array());
		buffer.put(message.getBytes());
		buffer.flip();
		while (buffer.hasRemaining()) {
			try {
				client.write(buffer);
			} catch (IOException e) {
			}
		}

	}
}
