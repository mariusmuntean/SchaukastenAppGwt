package de.tum.os.sa.shared.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.tum.os.sa.client.models.DisplayablePlaybackDevice;
import de.tum.os.sa.shared.EventState;

/**
 * DTO Representing an Event. Multiple devices are in an event, each playing back none, one or multiple Media files.
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

	ArrayList<Media> eventMedia = new ArrayList<Media>();

	// ArrayList<PlaybackDevice> eventDevices = new ArrayList<PlaybackDevice>();
	/**
	 * @gwt.typeArgs <java.util.HashMap<de.tum.os.sa.shared.DTO.PlaybackDevice,
	 *               java.util.ArrayList<de.tum.os.sa.shared.DTO.Media>>>
	 */
	HashMap<PlaybackDevice, ArrayList<Media>> eventMediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
	EventState eventState;
	@Temporal(TemporalType.TIMESTAMP)
	Date eventCreation;

	// Empty constructor for serialization.
	public Event() {
	}

	public Event(String eventName, String eventId, String eventDescription,
			String eventLocation) {
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
		if (this.eventMediaToDeviceMapping == null
				|| eventMediaToDeviceMapping.keySet().size() == 0) {
			return false;
		}

		return this.containsDeviceId(playbackDevice.getDeviceId());
	}

	/**
	 * Searches for a device in this event.
	 * 
	 * @param deviceID
	 *            - The ID of the device to search for.
	 * @return - true if a device with the given ID is in this event, false otherwise.
	 */
	public Boolean containsDeviceId(String deviceID) {
		if (this.eventMediaToDeviceMapping == null
				|| eventMediaToDeviceMapping.keySet().size() == 0) {
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
	public HashMap<PlaybackDevice, ArrayList<Media>> getEventMediaToDeviceMapping() {
		return eventMediaToDeviceMapping;
	}

	/**
	 * @param eventMediaToDeviceMapping
	 *            the eventMediaToDeviceMapping to set
	 */
	public void setEventMediaToDeviceMapping(
			HashMap<PlaybackDevice, ArrayList<Media>> eventMediaToDeviceMapping) {

		// ToDo if new mapping is set, update the media list and the devices
		// list
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
			for (List<Media> mediaList : this.eventMediaToDeviceMapping
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
	public ArrayList<PlaybackDevice> getEventDevices() {
		return new ArrayList<PlaybackDevice>(eventMediaToDeviceMapping.keySet());
	}

	public void setEventDevices(ArrayList<PlaybackDevice> newEventDevices) {
		if (eventMediaToDeviceMapping == null) {
			eventMediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
		}

		eventMediaToDeviceMapping.keySet().clear();

		if (newEventDevices != null) {
			for (PlaybackDevice pd : newEventDevices) {
				eventMediaToDeviceMapping.put(pd, null);
			}
		}
	}

	public void addDevice(PlaybackDevice pd) {
		if (this.eventMediaToDeviceMapping == null) {
			eventMediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
		}

		if (pd != null) {
			this.eventMediaToDeviceMapping.put(pd, null);
		}

	}

	public void addDevices(ArrayList<PlaybackDevice> pd) {
		if (this.eventMediaToDeviceMapping == null) {
			this.eventMediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
		}

		if (pd != null && pd.size() > 0) {
			for (PlaybackDevice dpd : pd) {
				dpd.setAvailable(false);
				eventMediaToDeviceMapping.put(dpd, null);
			}
		}

	}

	public void removeDevice(String deviceId) {
		// Just some sanitization
		if (this.eventMediaToDeviceMapping == null || deviceId == null
				|| deviceId.isEmpty()) {
			return;
		}
		// Search for a device with the given ID
		PlaybackDevice pdToRemove = null;
		for (PlaybackDevice pd : this.eventMediaToDeviceMapping.keySet()) {
			if (pd.getDeviceId().equals(deviceId)) {
				pdToRemove = pd;
				break;
			}
		}
		// If any is found remove it.
		if (pdToRemove != null) {
			if (this.eventMediaToDeviceMapping.containsKey(pdToRemove)) {
				this.eventMediaToDeviceMapping.remove(pdToRemove);
				pdToRemove.setAvailable(true);
			}
		}
	}

	public void removeDevices(ArrayList<String> deviceIds) {
		for (String deviceID : deviceIds) {
			removeDevice(deviceID);
		}
	}

	public void removeDeviceList(ArrayList<PlaybackDevice> devicesToRemove) {
		if (this.eventMediaToDeviceMapping == null || devicesToRemove == null
				|| devicesToRemove.size() < 1) {
			return;
		}

		for (PlaybackDevice pd : devicesToRemove) {
			PlaybackDevice pdToRemove = null;
			for (PlaybackDevice pds : eventMediaToDeviceMapping.keySet()) {
				if (pds.getDeviceId().equals(pd.getDeviceId())) {
					pdToRemove = pds;
					break;
				}
			}
			if (pdToRemove != null) {
				pdToRemove.setAvailable(true);
				eventMediaToDeviceMapping.keySet().remove(pdToRemove);
			}
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

	/**
	 * Tries to find and returns a {@link PlaybackDevice} in this event.
	 * 
	 * @param deviceID
	 *            - The ID of the device to look for.
	 * @return - The {@link PlaybackDevice} with the given ID or null.
	 */
	public PlaybackDevice getPlaybackDeviceById(String deviceID) {
		if (deviceID == null || deviceID.isEmpty()
				|| this.eventMediaToDeviceMapping == null
				|| this.eventMediaToDeviceMapping.keySet().size() == 0) {
			return null;
		}

		PlaybackDevice result = null;
		for (PlaybackDevice pd : eventMediaToDeviceMapping.keySet()) {
			if (pd.getDeviceId().equals(deviceID)) {
				result = pd;
				break;
			}
		}

		return result;
	}

	public Media getMediaById(String mediaID) {
		if (mediaID == null || mediaID.isEmpty() || this.eventMedia == null
				|| this.eventMedia.size() == 0) {
			return null;
		}

		Media result = null;
		for (Media md : this.eventMedia) {
			if (md.getId().equals(mediaID)) {
				result = md;
				break;
			}
		}

		return result;
	}

	/**
	 * Maps a {@link Media} item denoted by its ID to a {@link PlaybackDevice} item denoted by its ID.
	 * 
	 * @param mediaID
	 * @param deviceID
	 */
	public Boolean mapMediaToDevice(String mediaID, String deviceID) {
		// Input sanitization
		if (mediaID == null || mediaID.isEmpty() || deviceID == null
				|| deviceID.isEmpty()) {
			return false;
		}

		final Media mediaToMap = getMediaById(mediaID);
		if (mediaToMap == null) {
			return false;
		}

		PlaybackDevice deviceToGetMedia = getPlaybackDeviceById(deviceID);
		if (deviceToGetMedia == null) {
			return false;
		}

		if (this.eventMediaToDeviceMapping == null) {
			// Make a mapping structure and add the mapping
			this.eventMediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
			ArrayList<Media> media = new ArrayList<Media>();
			media.add(mediaToMap);
			this.eventMediaToDeviceMapping.put(deviceToGetMedia, media);
		} else {
			// If there already are some Media items mapped to the device,
			// append the new media to the list, otherwise create new mapping.
			if (this.eventMediaToDeviceMapping.keySet().contains(
					deviceToGetMedia)) {
				if (this.eventMediaToDeviceMapping.get(deviceToGetMedia) != null) {
					this.eventMediaToDeviceMapping.get(deviceToGetMedia).add(
							mediaToMap);
				} else {
					ArrayList<Media> media = new ArrayList<Media>();
					media.add(mediaToMap);
					this.eventMediaToDeviceMapping.put(deviceToGetMedia, media);
				}
			} else {
				ArrayList<Media> media = new ArrayList<Media>();
				media.add(mediaToMap);
				this.eventMediaToDeviceMapping.put(deviceToGetMedia, media);
			}
		}

		return true;
	}

	/**
	 * If both parameters represent a {@link Media} and a {@link PlaybackDevice} from this event and if the specified
	 * {@link PlaybackDevice} has the {@link Media} mapped to it, the method tries to remove the mapping.
	 * 
	 * @param mediaID
	 *            - An ID representing a {@link Media} item from this event.
	 * @param deviceID
	 *            - An ID representing a {@link PlaybackDevice} from this event.
	 * @return - True if the operation succeeds, false otherwise.
	 */
	public Boolean unmapMediaFromDevice(String mediaID, String deviceID) {
		// Input sanitization
		if (mediaID == null || mediaID.isEmpty() || deviceID == null
				|| deviceID.isEmpty()) {
			return false;
		}

		final Media mediaToRemove = getMediaById(mediaID);
		if (mediaToRemove == null) {
			return false;
		}

		PlaybackDevice deviceToLoseMedia = getPlaybackDeviceById(deviceID);
		if (deviceToLoseMedia == null) {
			return false;
		}

		// Look if any media is mapped to this PlaybackDevice
		List<Media> deviceMedia = this.eventMediaToDeviceMapping
				.get(deviceToLoseMedia);
		if (deviceMedia == null) {
			// If no media is mapped to this device return false
			return false;
		} else {
			// If there is some media mapped to this device, try to remove the
			// one with the given ID. The remove(...) method return false if its parameter wasn't in the list.
			return deviceMedia.remove(mediaToRemove);
		}

	}
}
