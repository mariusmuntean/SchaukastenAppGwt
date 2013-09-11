package de.tum.os.sa.client.models;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

import de.tum.os.sa.client.resources.Showcaseresources;
import de.tum.os.sa.shared.MediaTypes;
import de.tum.os.sa.shared.DTO.Media;

/**
 * Surrogate class for {@link Media}, used in the client.
 * 
 * @author Marius
 * 
 */
public class DisplayableMedia extends BaseModelData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1544904568400317303L;

	private String name = "name";
	private String id = "id";
	private String description = "description";
	private String imgName = "imgName";
	private String type = "type";

	/**
	 * Empty and parameterless constructor for serialization.
	 */
	public DisplayableMedia() {
	}

	public DisplayableMedia(String name, String id, String description,
			String imageName, MediaTypes type) {
		setName(name);
		setID(id);
		setType(type);
		setDescription(description);
		setImageName(imageName);
		
	}

	public void setName(String newName) {
		set(name, newName);
	}

	public String getName() {
		return get(name);
	}

	public void setType(MediaTypes newType) {
		set(type, newType);
	}

	public MediaTypes getType() {
		return get(type);
	}

	public void setImageName(String newImageName) {
//		set(imgName, newImageName);
		String iName = getImageName();
		set(imgName, iName);
	}

	public String getImageName() {
		Showcaseresources res = Showcaseresources.INSTANCE;
		switch (getType()) {
		case audio: {
			return res.mp3().getURL();
		}

		case feed: {
			return res.html().getURL();
		}

		case html: {
			return res.html().getURL();
		}

		case image: {
			return res.jpg().getURL();
		}

		case other: {
			return res.blank().getURL();
		}

		case pdf: {
			return res.pdf().getURL();
		}

		case text: {
			return res.txt().getURL();
		}

		case video: {
			return res.mpg().getURL();
		}
		default:
			return res.blank().getURL();
		}

		// return get(imgName);
	}

	public void setID(String newID) {
		set(id, newID);
	}

	public String getID() {
		return get(id);
	}

	public void setDescription(String newDescription) {
		set(description, newDescription);
	}

	public String getDescription() {
		return get(description);
	}
}
