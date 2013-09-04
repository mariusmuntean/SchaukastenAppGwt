package de.tum.os.sa.shared.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	@ElementCollection(fetch = FetchType.EAGER)
	ArrayList<Media> eventMedia;

	@ElementCollection(fetch = FetchType.EAGER)
	ArrayList<PlaybackDevice> eventDevices;

	HashMap<PlaybackDevice, ArrayList<Media>> eventMediaToDeviceMapping;
	EventState eventState;
	@Temporal(TemporalType.TIMESTAMP)
	Date eventCreation;

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
		this.eventCreation = new Date();
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
	public Map<PlaybackDevice, ArrayList<Media>> getEventMediaToDeviceMapping() {
		return eventMediaToDeviceMapping;
	}

	/**
	 * @param eventMediaToDeviceMapping
	 *            the eventMediaToDeviceMapping to set
	 */
	public void setEventMediaToDeviceMapping(
			HashMap<PlaybackDevice, ArrayList<Media>> eventMediaToDeviceMapping) {
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
		// if (eventMediaToDeviceMapping != null) {
		// Collection<ArrayList<Media>> mediaLists =
		// eventMediaToDeviceMapping.values();
		// HashSet<Media> media = new HashSet<Media>();
		// // Adding all Media to a set guarantees us that each Media element
		// // is present only once.
		// for (List<Media> mediaList : mediaLists) {
		// if (mediaList != null && mediaList.size() > 0) {
		// media.addAll(mediaList);
		// }
		// }
		// return new ArrayList<Media>(media);
		// } else {
		// return null;
		// }
		return this.eventMedia;
	}

	public void setEventMedia(ArrayList<Media> newEventMedia) {
		if (newEventMedia != null) {
			this.eventMedia = newEventMedia;
		} else {
			this.eventMedia = new ArrayList<Media>();
		}

		// Maybe also clear the media to device mapping now?!
		this.eventMediaToDeviceMapping.clear();
	}

	public void addMedia(Media newMedia) {
		if (this.eventMedia == null) {
			this.eventMedia = new ArrayList<Media>();
		}

		this.eventMedia.add(newMedia);
	}

	public void addMedia(ArrayList<Media> newMedia) {
		if (this.eventMedia == null) {
			this.eventMedia = new ArrayList<Media>();
		}

		if (newMedia == null || newMedia.size() < 1) {
			return;
		}

		this.eventMedia.addAll(newMedia);
	}

	public void removeMedia(String mediaId) {
		// sanitization
		if (this.eventMedia == null || mediaId == null || mediaId.isEmpty()) {
			return;
		}

		// Make sure the media item really exists
		Media mdToRemove = null;
		for (Media md : this.eventMedia) {
			if (md.id.equals(mediaId)) {
				mdToRemove = md;
				break;
			}
		}

		// If anything is found, remove it.
		if (mdToRemove != null) {
			this.eventMedia.remove(mdToRemove);
			// Also remove it from every list in the mapping.
			for (ArrayList<Media> mediaList : this.eventMediaToDeviceMapping
					.values()) {
				for (Media md : mediaList) {
					if (md.id.equals(mdToRemove.id)) {
						mediaList.remove(md);
					}
				}
			}
		}
	}

	public void removeMedia(ArrayList<Media> mediaToRemove) {
		for (Media med : mediaToRemove) {
			removeMedia(med.getId());
		}
	}

	/**
	 * @return the Devices in this event or null if there are none.
	 */
	public List<PlaybackDevice> getEventDevices() {
		// if (eventMediaToDeviceMapping != null) {
		// return new ArrayList<PlaybackDevice>(
		// eventMediaToDeviceMapping.keySet());
		// } else {
		// return null;
		// }
		return this.eventDevices;
	}

	public void setEventDevices(ArrayList<PlaybackDevice> neweventDevices) {
		if (neweventDevices != null) {
			this.eventDevices = neweventDevices;
		} else {
			this.eventDevices = new ArrayList<PlaybackDevice>();
		}

		// Maybe also clear the media to device mapping
		this.eventMediaToDeviceMapping.clear();
	}

	public void addDevice(PlaybackDevice pd) {
		if (this.eventDevices == null) {
			this.eventDevices = new ArrayList<PlaybackDevice>();
		}

		if (pd != null) {
			this.eventDevices.add(pd);
		}

	}

	public void addDevices(ArrayList<PlaybackDevice> pd) {
		if (this.eventDevices == null) {
			this.eventDevices = new ArrayList<PlaybackDevice>();
		}

		if (pd != null && pd.size() > 0) {
			this.eventDevices.addAll(pd);
		}

	}

	public void removeDevice(String deviceId) {
		// Just some sanitization
		if (this.eventDevices == null || deviceId == null || deviceId.isEmpty()) {
			return;
		}
		// Search for a device with the given ID
		PlaybackDevice pdToRemove = null;
		for (PlaybackDevice pd : this.eventDevices) {
			if (pd.getDeviceId().equals(deviceId)) {
				pdToRemove = pd;
				break;
			}
		}
		// If any is found remove it.
		if (pdToRemove != null) {
			this.eventDevices.remove(pdToRemove);
			this.eventMediaToDeviceMapping.remove(pdToRemove);
		}
	}

	public void removeDevices(ArrayList<String> deviceIds) {
		for (String deviceID : deviceIds) {
			removeDevice(deviceID);
		}
	}

	public void removeDeviceList(ArrayList<PlaybackDevice> devicesToRemove) {
		if (this.eventDevices == null || devicesToRemove == null
				|| devicesToRemove.size() < 1) {
			return;
		}

		for (PlaybackDevice pd : devicesToRemove) {
			this.eventDevices.remove(pd);
			this.eventMediaToDeviceMapping.remove(pd);
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

	public Date getEventCreation() {
		return eventCreation;
	}

}
