package de.tum.os.sa.shared.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import com.google.gwt.user.client.rpc.IsSerializable;

import de.tum.os.sa.shared.EventState;

/**
 * DTO Representing an Event. Multiple devices are in an event, each playing
 * back none, one or multiple Media files.
 * 
 * @author Marius
 * 
 */
@Entity
public class Event implements Serializable, IsSerializable {

	String eventName;
	@Id
	String eventId;
	String eventDescription;
	String eventLocation;
	String eventPictureUrl;
	
//	@ElementCollection
//	List<Media> eventMedia;
//	
//	@ElementCollection
//	List<PlaybackDevice> eventDevices;
	
	Map<PlaybackDevice, List<Media>> eventMediaToDeviceMapping;
	EventState eventState;

	// Empty constructor for serialization.
	public Event() {
	}

	public Event(String eventName, String eventId, String eventDescription,
			String eventLocation) {
		super();
		this.eventName = eventName;
		this.eventId = eventId;
		this.eventDescription = eventDescription;
		this.eventLocation = eventLocation;
		this.eventState = EventState.stoped;
	}

	/**
	 * Searches for a device in this event.
	 * 
	 * @param playbackDevice
	 *            - The device to search for.
	 * @return - true if the given device is in this event, false otherwise.
	 */
	public Boolean containsDevice(PlaybackDevice playbackDevice) {
		if (this.eventMediaToDeviceMapping == null) {
			return false;
		}

		return this.eventMediaToDeviceMapping.containsKey(playbackDevice);
	}

	/**
	 * Searches for a device in this event.
	 * 
	 * @param deviceID
	 *            - The ID of the device to search for.
	 * @return - true if a device with the given ID is in this event, false
	 *         otherwise.
	 */
	public Boolean containsDeviceId(String deviceID) {
		if (this.eventMediaToDeviceMapping == null) {
			return false;
		}

		Boolean result = false;
		for (PlaybackDevice pd : this.eventMediaToDeviceMapping.keySet()) {
			if (pd.getDeviceId().equals(deviceID)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * @return the eventPictureUrl
	 */
	public String getEventPictureUrl() {
		return eventPictureUrl;
	}

	/**
	 * @param eventPictureUrl
	 *            the eventPictureUrl to set
	 */
	public void setEventPictureUrl(String eventPictureUrl) {
		this.eventPictureUrl = eventPictureUrl;
	}

	/**
	 * @return the eventState
	 */
	public EventState getEventState() {
		return eventState;
	}

	/**
	 * @param eventState
	 *            the eventState to set
	 */
	public void setEventState(EventState eventState) {
		this.eventState = eventState;
	}

	/**
	 * @return the eventMediaToDeviceMapping
	 */
	public Map<PlaybackDevice, List<Media>> getEventMediaToDeviceMapping() {
		return eventMediaToDeviceMapping;
	}

	/**
	 * @param eventMediaToDeviceMapping
	 *            the eventMediaToDeviceMapping to set
	 */
	public void setEventMediaToDeviceMapping(
			HashMap<PlaybackDevice, List<Media>> eventMediaToDeviceMapping) {
		this.eventMediaToDeviceMapping = eventMediaToDeviceMapping;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 36300861794043137L;

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName
	 *            the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the eventDescription
	 */
	public String getEventDescription() {
		return eventDescription;
	}

	/**
	 * @param eventDescription
	 *            the eventDescription to set
	 */
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	/**
	 * @return the eventLocation
	 */
	public String getEventLocation() {
		return eventLocation;
	}

	/**
	 * @param eventLocation
	 *            the eventLocation to set
	 */
	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	/**
	 * @return the eventMedia
	 */
	public ArrayList<Media> getEventMedia() {
		if (eventMediaToDeviceMapping != null) {
			Collection<List<Media>> mediaLists = eventMediaToDeviceMapping.values();
			HashSet<Media> media = new HashSet<Media>();
			// Adding all Media to a set guarantees us that each Media element
			// is present only once.
			for (List<Media> mediaList : mediaLists) {
				if (mediaList != null && mediaList.size() > 0) {
					media.addAll(mediaList);
				}
			}
			return new ArrayList<Media>(media);
		} else {
			return null;
		}
	}

	/**
	 * @return the Devices in this event or null if there are none.
	 */
	public List<PlaybackDevice> getEventDevices() {
		if (eventMediaToDeviceMapping != null) {
			return new ArrayList<PlaybackDevice>(
					eventMediaToDeviceMapping.keySet());
		} else {
			return null;
		}
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}

}
