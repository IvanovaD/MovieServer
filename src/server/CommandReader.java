package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CommandReader {
	private static int BUFFER_SIZE = 100000;
	private static ByteBuffer buffer;
	
	public static ByteBuffer readCommandFromClient(SocketChannel client) throws IOException {
		buffer = ByteBuffer.allocate(BUFFER_SIZE);
		client.read(buffer);
		return buffer;
	}
}
