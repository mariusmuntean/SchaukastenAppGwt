package de.tum.os.sa.client;

import java.util.ArrayList;
import java.util.HashMap;

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
	 * Methods for web clients
	 */

	ArrayList<PlaybackDevice> getAllDevices();

	ArrayList<PlaybackDevice> getAvailableDevices();

	ArrayList<PlaybackDevice> getUnavailableDevices();

	ArrayList<PlaybackDevice> getDevicesInEvent(String eventId);

	Event getEventForDevice(String deviceId);
	
	Event getEvent(String eventId);
	
	ArrayList<Event> getAllEvents();

	Boolean addEvent(Event event);

	Boolean deleteEvent(Event event);

	Boolean deleteEvent(String eventId);
	
	Boolean mapMediaToDevicesForEvent(Event event, HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping);
	
	Boolean startEvent(Event event);
	Boolean pauseEvent(Event event);
	Boolean stopEvent(Event event);
	
	Boolean startEvent(String eventID);
	Boolean pauseEvent(String eventID);
	Boolean stopEvent(String eventID);
	
	/*
	 * Methods for Android clients
	 */
	
	Boolean registerDevice(PlaybackDevice device);
	Boolean unregisterDevice(PlaybackDevice device);
	Boolean markAvailable(PlaybackDevice device);
	Boolean markUnavailable(PlaybackDevice device);
}

