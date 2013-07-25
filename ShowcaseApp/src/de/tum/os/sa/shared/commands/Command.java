package de.tum.os.sa.shared.commands;

import java.io.Serializable;

import de.tum.os.sa.shared.CommandType;

public class Command implements Serializable{

	private static final long serialVersionUID = 386856547368991499L;
	private CommandType commandType;
	private String eventId;
	
	/**
	 * Empty constructor for serialization.
	 */
	public Command(){
	}
	
	public Command(CommandType commandType, String eventId){
		this.commandType = commandType;
		this.eventId = eventId;
	}
	public CommandType getCommandType(){
		return this.commandType;
	}
	
	public String getEventId(){
		return this.eventId;
	}
}
