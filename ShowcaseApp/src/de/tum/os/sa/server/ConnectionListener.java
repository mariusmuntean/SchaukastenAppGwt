package de.tum.os.sa.server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionListener extends Thread {

	private ServerSocket serverSocket;
	private ConcurrentHashMap<String, Socket> clientIdToSocketMap = new ConcurrentHashMap<String, Socket>();
	private boolean healthy = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(3535);
		} catch (Exception e) {
			System.out.println("Could not listen on port 3535!!!");
			healthy = false;
			return;
		}
		boolean go = true;
		while (go) {
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
			} catch (Exception e) {
				System.out.println("Error accepting client!!!");
				healthy = false;
				return;
			}

			UUID clientID = UUID.randomUUID();
			clientIdToSocketMap.putIfAbsent(clientID.toString(), clientSocket);
			try {
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				System.out.println(clientID.toString());
			} catch (Exception e) {
				System.out.println("Could not greet client!");
				return;
			}
		}
	}

	/**
	 * Tries to start listening for connections. If start is successful a thread safe mapping of client IDs to their socket connection
	 * is returned.
	 * 
	 * @return - A client IDs to Socket mapping if start was successful, null otherwise.
	 */
	public ConcurrentHashMap<String, Socket> startAndGetClientIdsMap() {
		try {
			this.start();
		} catch (Exception e) {
			System.out.println("Failed to start!");
			return null;
		}
		if (healthy) {
			return this.clientIdToSocketMap;
		} else {
			return null;
		}
	}

	public ConcurrentHashMap<String, Socket> getClientIsMap() {
		return this.clientIdToSocketMap;
	}

}
