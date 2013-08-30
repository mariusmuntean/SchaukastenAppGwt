package de.tum.os.sa.client.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;

import de.tum.os.sa.shared.EventState;
import de.tum.os.sa.shared.DTO.Media;
import de.tum.os.sa.shared.DTO.PlaybackDevice;

public class DisplayableEvent extends BaseModelData {

	// String eventName;
	// String eventId;
	// String eventDescription;
	// String eventLocation;
	// String eventPictureUrl;
	// ArrayList<Media> eventMedia;
	// ArrayList<PlaybackDevice> eventDevices;
	// HashMap<PlaybackDevice, ArrayList<Media>> eventMediaToDeviceMapping;
	// EventState eventState;

	String eventName = "name";
	String eventID = "id";
	String eventDescription = "description";
	String eventLocation = "location";
	String eventPictureName = "picture";
	String eventMedia = "media";
	String eventDevices = "devices";
	String eventMediaToDevicesMap = "mediaToDevicesMap";
	String eventState = "state";

	public DisplayableEvent(String name, String id, String description, String location,
			String pictureName) {
		setName(name);
		setID(id);
		setDescription(description);
		setLocation(location);
		setPictureName(pictureName);

	}

	public String getName() {
		return get(eventName);
	}

	public void setName(String newName) {
		set(eventName, newName);
	}

	public String getID() {
		return get(eventID);
	}

	public void setID(String newID) {
		set(eventID, newID);
	}

	public String getDescription() {
		return get(eventDescription);
	}

	public void setDescription(String newDescription) {
		set(eventDescription, newDescription);
	}

	public String getLocation() {
		return get(eventLocation);
	}

	public void setLocation(String newLocation) {
		set(eventLocation, newLocation);
	}

	public String getPictureName() {
		return get(eventPictureName);
	}

	public void setPictureName(String newPictureName) {
		set(eventPictureName, newPictureName);
	}

	public void setMedia(ArrayList<Media> newMedia) {
		set(eventMedia, newMedia);
	}

	public ArrayList<Media> getMedia() {
		return get(eventMedia);
	}

	public ArrayList<PlaybackDevice> getDevices() {
		return get(eventDevices);
	}

	public void setDevices(List<PlaybackDevice> list) {
		set(eventDevices, list);
	}

	public EventState getState() {
		return get(eventState);
	}

	public void setState(EventState newState) {
		set(eventState, newState);
	}

	public HashMap<PlaybackDevice, ArrayList<Media>> getMediaToDeviceMapping() {
		return get(eventMediaToDevicesMap);
	}

	public void setMediaToDeviceMapping(
			Map<PlaybackDevice, List<Media>> newMapping) {
		set(eventMediaToDevicesMap, newMapping);
	}
}
