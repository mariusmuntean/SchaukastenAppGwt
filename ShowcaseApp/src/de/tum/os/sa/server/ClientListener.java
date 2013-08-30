package de.tum.os.sa.server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * Listens for Android Clients to connect and adds them in a mapping (from
 * client ID to socket).
 * 
 * @author marius
 * 
 */
public class ClientListener extends Thread {

	private ServerSocket serverSocket;
	private ConcurrentHashMap<String, Socket> clientIdToSocketMap = new ConcurrentHashMap<String, Socket>();

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(3535);
		} catch (Exception e) {
			System.out.println("Could not listen on port 3535!!!");
			return;
		}
		boolean go = true;
		while (go) {
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
			} catch (Exception e) {
				System.out.println("Error accepting client!!!");
				e.printStackTrace();
				return;
			}

			UUID clientID = UUID.randomUUID();
			clientIdToSocketMap.put(clientID.toString(), clientSocket);
			try {
				PrintWriter out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				out.println(clientID.toString());
			} catch (Exception e) {
				System.out.println("Could not greet client! !");
				return;
			}
		}
	}

	/**
	 * Tries to start listening for connections. If start is successful a thread
	 * safe mapping of client IDs to their socket connection is returned.
	 * 
	 * @return - A client IDs to Socket mapping if start was successful, null
	 *         otherwise.
	 */
	public ConcurrentHashMap<String, Socket> startAndGetClientIdsMap() {
		try {
			this.start();
		} catch (Exception e) {
			System.out.println("Failed to start!");
			return null;
		}
		return this.clientIdToSocketMap;
	}

	public ConcurrentHashMap<String, Socket> getClientIsMap() {
		return this.clientIdToSocketMap;
	}

}
