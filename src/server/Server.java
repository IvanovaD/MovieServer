package server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

	public static final int SERVER_PORT = 4444;

	public static void main(String[] args) throws IOException, cannotExecuteCommand {

		ServerSocketChannel socketChannel;
		socketChannel = ServerSocketChannel.open();
		socketChannel.socket().bind(new InetSocketAddress(SERVER_PORT));
		Selector selector = Selector.open();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_ACCEPT);
		ByteBuffer buffer = null;
		
		while (true) {
			int readyChannels = selector.select();
			if (readyChannels == 0)
				continue;
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if (key.isAcceptable()) {
					register(selector, socketChannel);
				} else if (key.isReadable()) {
					buffer = connectForReading(key, selector, socketChannel);
				} else if (key.isWritable()) {
					connectForWriting(key, buffer, selector);
				}
				keyIterator.remove();
			}
		}
	}

	private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {

		SocketChannel client = serverSocket.accept();
		client.configureBlocking(false);
		client.register(selector, SelectionKey.OP_READ);
	}

	private static void connectForWriting(SelectionKey key, ByteBuffer buffer, Selector selector) {
		SocketChannel client = (SocketChannel) key.channel();
		CommandParser parser = new CommandParser();
		try {
			parser.parseCommandFromClient(buffer);
			CommandExecutor commandExecutor = new CommandExecutor(client, parser.getCommand(), parser.getArguments());
			commandExecutor.executeCommand();
			client.register(selector, SelectionKey.OP_READ);
		} catch (cannotExecuteCommand e1) {
			System.out.println("Cannot execute the command");
		} catch (IOException e) {
			System.out.println("Invalid input");
		}
	}

	private static ByteBuffer connectForReading(SelectionKey key, Selector selector,
			ServerSocketChannel socketChannel) {
		ByteBuffer buffer = null;
		try {
			SocketChannel client = (SocketChannel) key.channel();
			buffer = CommandReader.readCommandFromClient(client);
			client.register(selector, SelectionKey.OP_WRITE);
		} catch (IOException io) {
			System.out.println("Socket is closed");

		}
		return buffer;
	}
}