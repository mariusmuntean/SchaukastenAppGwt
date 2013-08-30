package de.tum.os.sa.shared.DTO;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.tum.os.sa.shared.MediaTypes;

/**
 * DTO representing a media file that a device can play back.
 * 
 * @author Marius
 * 
 */
public class Media implements Serializable, IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9112586033216435998L;
	String name;

	String id;
	String description;
	String location;

	MediaTypes type;

	// Empty constructor for serialization.
	public Media() {
	}

	public Media(String name, String id, String description, String location,
			MediaTypes type) {
		super();
		this.name = name;
		this.id = id;
		this.description = description;
		this.location = location;
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the type
	 */
	public MediaTypes getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(MediaTypes type) {
		this.type = type;
	}

}
