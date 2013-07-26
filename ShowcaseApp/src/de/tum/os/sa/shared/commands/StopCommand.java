package de.tum.os.sa.shared.commands;

import de.tum.os.sa.shared.CommandType;
import de.tum.os.sa.shared.DTO.Media;

/**
 * Instructs Android clients to stop playing back the media in an event. Upon
 * receiving this command, the Android Client stops displaying any {@link Media}.
 * 
 * @author marius
 * 
 */
public class StopCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4690824638356947563L;

	public StopCommand(String eventId) {
		super(CommandType.stop, eventId);
		// TODO Auto-generated constructor stub
	}

	public StopCommand() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
