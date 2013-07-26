package de.tum.os.sa.shared.commands;

import de.tum.os.sa.shared.CommandType;
import de.tum.os.sa.shared.DTO.Media;

/**
 * Instructs Android clients to start playing back the media in an event. Upon
 * receiving this command, the Android Client retrieves a list of {@link Media}
 * objects from the server and stars displaying them.
 * 
 * @author marius
 * 
 */
public class PlayCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -825801626307916747L;

	public PlayCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlayCommand(String eventId) {
		super(CommandType.play, eventId);
		// TODO Auto-generated constructor stub
	}

}
