package de.tum.os.sa.shared;

import java.io.Serializable;

public class Command implements Serializable{

	private static final long serialVersionUID = 386856547368991499L;
	public CommandType commandType;
	
	/**
	 * Empty constructor for serialization.
	 */
	public Command(){
	}
	
	public Command(CommandType commandType){
		this.commandType = commandType;
	}
}
