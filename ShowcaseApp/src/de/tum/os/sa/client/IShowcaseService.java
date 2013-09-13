package de.tum.os.sa.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.tum.os.sa.shared.DTO.Event;
import de.tum.os.sa.shared.DTO.Media;
import de.tum.os.sa.shared.DTO.PlaybackDevice;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("showcaseService")
public interface IShowcaseService extends RemoteService {

	/*
	 * 
	 * Methods for web clients
	 */

	ArrayList<PlaybackDevice> getAllDevices();

	ArrayList<PlaybackDevice> getAvailableDevices();

	ArrayList<PlaybackDevice> getUnavailableDevices();

	ArrayList<PlaybackDevice> getDevicesInEvent(String eventId);
	
	PlaybackDevice getDevice(String deviceId);

	/**
	 * Searches for events in which the given device is present.
	 * 
	 * @param deviceId
	 *            - The Id of the device.
	 * @return - The first event where this device is found in, null otherwise.
	 */
	Event getEventForDevice(String deviceId);

	Event getEvent(String eventId);

	ArrayList<Event> getAllEvents();

	Boolean addEvent(Event event);

	/**
	 * Deletes the event from the list of events.
	 * 
	 * @param event
	 *            - The event to delete.
	 * @return - True if deleted successfully, false otherwise.
	 */
	Boolean deleteEvent(Event event);

	/**
	 * Deletes the event from the list of events.
	 * 
	 * If possible, use {@link #deleteEvent(Event) deleteEvent(Event)} because it is more efficient.
	 * This method first has to search linearly and if it finds the event it
	 * calls the other method.
	 * 
	 * @param eventId
	 *            - The ID of the event to delete.
	 * @return - True if deleted successfully, false otherwise.
	 */
	Boolean deleteEvent(String eventId);

	Boolean updateEventMediaToDeviceMapping(Event event,
			HashMap<PlaybackDevice,ArrayList<Media>> mediaToDeviceMapping);
	
	Boolean updateEventMediaToDeviceMapping(String eventId,
			HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping);
	Boolean addMediaToEvent(String eventID, ArrayList<Media> newMedia);
	Boolean removeMediaFromEvent(String eventID, ArrayList<Media> mediaToRemove);
	Boolean removeMediaFromEvent(String eventID, String mediaIdToRemove);
	
	Boolean addDevicesToEvent(String eventID, ArrayList<PlaybackDevice> newDevices);
	Boolean removeDevicesFromEvent(String eventID, ArrayList<PlaybackDevice> devicesToRemove);

	Boolean startEvent(Event event);

	Boolean pauseEvent(Event event);

	Boolean stopEvent(Event event);

	Boolean startEvent(String eventID);

	Boolean pauseEvent(String eventID);

	Boolean stopEvent(String eventID);
	
	Boolean addFileToEvent(String eventID, String fileName, String fileLocation);

	/*
	 * 
	 * Methods for Android clients
	 */

	/**
	 * informs the server that a new device has connected.
	 * 
	 * @param device
	 * @return - true if successfully registered, false otherwise.
	 */
	Boolean registerDevice(PlaybackDevice device);

	/**
	 * Informs the server that a device is about to become unavailable.
	 * 
	 * @param device
	 *            - The device that will drop off the network.
	 * @return - True if successfully unregistered, false otherwise.
	 */
	Boolean unregisterDevice(PlaybackDevice device);

	/**
	 * Marks a device as being available to participate in an event.
	 * 
	 * @param device
	 *            - The device to be marked available.
	 * @return
	 */
	Boolean markAvailable(PlaybackDevice device);

	/**
	 * Marks a device({@linkplain PlaybackDevice}) as being unavailable to
	 * participate in an event.
	 * 
	 * @param device
	 *            - The device to be marked unavailable
	 * @return
	 */
	Boolean markUnavailable(PlaybackDevice device);

	/**
	 * Gets an {@link ArrayList} of the {@link Media} the is mapped to a device
	 * in an event.
	 * 
	 * @param deviceID
	 *            - The device to which the media is mapped.
	 * @param eventID
	 *            - The event ID.
	 * @return - An {@code ArrayListy<Media> 
	 */
	List<Media> getMediaForDeviceInEvent(String deviceID, String eventID);
}
