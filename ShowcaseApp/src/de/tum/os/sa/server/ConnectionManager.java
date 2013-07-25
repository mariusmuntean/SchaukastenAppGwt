package de.tum.os.sa.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import de.tum.os.sa.shared.commands.Command;

public class ConnectionManager {

	private ConcurrentHashMap<String, Socket> clientsIdToSocketMap;
	private ConcurrentHashMap<String, ObjectOutputStream> clientIdToOosMap = new ConcurrentHashMap<String, ObjectOutputStream>();

	public ConnectionManager(ConcurrentHashMap<String, Socket> clientIdToSocketMap) {
		this.clientsIdToSocketMap = clientIdToSocketMap;
	}

	public void sendCommandAsync(final Command command, String clientId) {
		// Check for OOS, if missing open one
		final ObjectOutputStream oos;
		if (!clientIdToOosMap.containsKey(clientId)
				&& clientsIdToSocketMap.containsKey(clientId)) {
			oos = openOosToClient(clientId);
			clientIdToOosMap.put(clientId, oos);
		} else {
			oos = clientIdToOosMap.get(clientId);
		}

		Runnable sendRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					oos.writeObject(command);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread sendThread = new Thread(sendRunnable);
		sendThread.start();
	}

	private ObjectOutputStream openOosToClient(String clientId) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(clientsIdToSocketMap.get(clientId)
					.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oos;
	}

	public void doAsync(Runnable r) {
		if (r == null)
			return;

		Thread t = new Thread(r);
		try {
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
