package de.tum.os.sa.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.tum.os.sa.shared.DTO.Event;
import de.tum.os.sa.shared.DTO.Media;
import de.tum.os.sa.shared.DTO.PlaybackDevice;

/**
 * The async counterpart of <code>ShowcaseService</code>.
 */
public interface IShowcaseServiceAsync {


	void getAllDevices(AsyncCallback<ArrayList<PlaybackDevice>> callback);

	void deleteEvent(String eventId, AsyncCallback<Boolean> callback);

	void getUnavailableDevices(AsyncCallback<ArrayList<PlaybackDevice>> callback);

	void getDevicesInEvent(String eventId,
			AsyncCallback<ArrayList<PlaybackDevice>> callback);

	void addEvent(Event event, AsyncCallback<Boolean> callback);

	void markAvailable(PlaybackDevice device, AsyncCallback<Boolean> callback);

	void markUnavailable(PlaybackDevice device, AsyncCallback<Boolean> callback);

	void registerDevice(PlaybackDevice device, AsyncCallback<Boolean> callback);

	void unregisterDevice(PlaybackDevice device, AsyncCallback<Boolean> callback);

	void getAvailableDevices(AsyncCallback<ArrayList<PlaybackDevice>> callback);

	void getEventForDevice(String deviceId, AsyncCallback<Event> callback);

	void deleteEvent(Event event, AsyncCallback<Boolean> callback);

	void getAllEvents(AsyncCallback<ArrayList<Event>> callback);

	void getEvent(String eventId, AsyncCallback<Event> callback);

	void mapMediaToDevicesForEvent(Event event,
			HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping,
			AsyncCallback<Boolean> callback);

	void startEvent(Event event, AsyncCallback<Boolean> callback);

	void pauseEvent(Event event, AsyncCallback<Boolean> callback);

	void stopEvent(Event event, AsyncCallback<Boolean> callback);

	void stopEvent(String eventID, AsyncCallback<Boolean> callback);

	void pauseEvent(String eventID, AsyncCallback<Boolean> callback);

	void startEvent(String eventID, AsyncCallback<Boolean> callback);
}
