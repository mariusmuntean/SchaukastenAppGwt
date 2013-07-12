package de.tum.os.sa.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import de.tum.os.sa.client.IShowcaseService;
import de.tum.os.sa.shared.Command;
import de.tum.os.sa.shared.CommandType;
import de.tum.os.sa.shared.DeviceType;
import de.tum.os.sa.shared.MediaTypes;
import de.tum.os.sa.shared.DTO.Event;
import de.tum.os.sa.shared.DTO.Media;
import de.tum.os.sa.shared.DTO.PlaybackDevice;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
public class ShowcaseServiceImpl extends RemoteServiceServlet implements IShowcaseService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2798669587684922962L;
	private ArrayList<PlaybackDevice> registeredDevices = new ArrayList<PlaybackDevice>();
	private ArrayList<PlaybackDevice> availableDevices = new ArrayList<PlaybackDevice>();
	private ArrayList<PlaybackDevice> unavailableDevices = new ArrayList<PlaybackDevice>();
	private ArrayList<Media> media = new ArrayList<Media>();
	private ArrayList<Event> events = new ArrayList<Event>();

	private ClientListener clientListen;
	private ConnectionManager conManager;
	private ConcurrentHashMap<String, Socket> clientIDToSocketMap;
	private ConcurrentHashMap<String, PlaybackDevice> clientIDToDeviceMap;

	public ShowcaseServiceImpl() {
		super();

		// My stuff here
		generateDummyDevices();
		generateDummyMedia();
		generateDummyEvents();

		clientListen = new ClientListener();
		this.clientIDToSocketMap = clientListen.startAndGetClientIdsMap();
		conManager = new ConnectionManager(this.clientIDToSocketMap);
		clientIDToDeviceMap = new ConcurrentHashMap<String, PlaybackDevice>();

	}

	private void generateDummyDevices() {
		PlaybackDevice pd1 = new PlaybackDevice(UUID.randomUUID().toString(), "Nexus S",
				DeviceType.Smartphone, 3.7f);
		PlaybackDevice pd2 = new PlaybackDevice(UUID.randomUUID().toString(), "Nexus S",
				DeviceType.Smartphone, 3.7f);
		PlaybackDevice pd3 = new PlaybackDevice(UUID.randomUUID().toString(), "Nexus 4",
				DeviceType.Smartphone, 4.5f);
		PlaybackDevice pd4 = new PlaybackDevice(UUID.randomUUID().toString(),
				"Galaxy Nexus", DeviceType.Smartphone, 4.5f);
		registeredDevices.add(pd1);
		registeredDevices.add(pd2);
		registeredDevices.add(pd3);
		registeredDevices.add(pd4);
	}

	private void generateDummyMedia() {
		Media me1 = new Media(
				"Career TUM Logo",
				"3524637854",
				"The logo of the Career TUM",
				"http://3.bp.blogspot.com/-u4VkoAchhvw/TcrDIHhxaxI/AAAAAAAAAWk/RmBZ9jY73Fg/s1600/20110511_tum_career_week.png",
				MediaTypes.image);
		Media me2 = new Media(
				"Career TUM Video",
				"9621582846",
				"Promo video of the Career TUM",
				"http://download.wavetlan.com/SVV/Media/HTTP/MP4/ConvertedFiles/Media-Convert/Unsupported/test7.mp4",
				MediaTypes.video);
		Media me3 = new Media(
				"Elephants at TUM",
				"3524637854",
				"The logo of the Career TUM",
				"http://download.wavetlan.com/SVV/Media/HTTP/H264/Other_Media/H264_test5_voice_mp4_480x360.mp4",
				MediaTypes.video);
		Media me4 = new Media(
				"Career TUM Logo",
				"3524637854",
				"The logo of the Career TUM",
				"http://download.wavetlan.com/SVV/Media/HTTP/MP4/ConvertedFiles/Media-Convert/Unsupported/test7.mp4",
				MediaTypes.video);
		Media me5 = new Media(
				"Career TUM Logo",
				"3524637854",
				"The logo of the Career TUM",
				"http://3.bp.blogspot.com/-u4VkoAchhvw/TcrDIHhxaxI/AAAAAAAAAWk/RmBZ9jY73Fg/s1600/20110511_tum_career_week.png",
				MediaTypes.image);
		media.add(me1);
		media.add(me2);
		media.add(me3);
		media.add(me4);
		media.add(me5);
	}

	private void generateDummyEvents() {
		/*
		 * Event 1
		 */
		// Media in this event
		final ArrayList<Media> careerTumMediaList = new ArrayList<Media>(media.subList(0,
				2));
		// Map media to devices in this event
		HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
		// mediaToDeviceMapping.put(registeredDevices.get(0), new ArrayList<Media>(
		// careerTumMediaList.subList(0, 0)));
		// mediaToDeviceMapping.put(registeredDevices.get(1), new ArrayList<Media>(
		// careerTumMediaList.subList(1, 1)));
		// mediaToDeviceMapping.put(registeredDevices.get(2), new ArrayList<Media>(
		// careerTumMediaList.subList(2, 2)));

		// Create the event
		Event ev1 = new Event("Career TUM", "14616453728542",
				"Get your future career started now!",
				"Garching Forschungszentrum, Bolzman Str 3");
		// Add mapping
		ev1.setEventMediaToDeviceMapping(mediaToDeviceMapping);
		// Add logo
		ev1.setEventPictureUrl("eventLogo.gif");

		// Add the event to the list
		events.add(ev1);

		/*
		 * Event 2
		 */
		// Media in this event
		final ArrayList<Media> demoTumMediaList = new ArrayList<Media>(
				media.subList(3, 4));
		// Map media to devices in this event
		HashMap<PlaybackDevice, ArrayList<Media>> demoMediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
		demoMediaToDeviceMapping.put(registeredDevices.get(2), new ArrayList<Media>(
				demoTumMediaList.subList(0, 0)));
		demoMediaToDeviceMapping.put(registeredDevices.get(3), new ArrayList<Media>(
				demoTumMediaList.subList(1, 1)));

		// Create the event
		Event ev2 = new Event("Demo TUM", "14716433728342", "It's a demo event!",
				"Garching Forschungszentrum, Bolzman Str 3");
		// Add mapping
		ev2.setEventMediaToDeviceMapping(demoMediaToDeviceMapping);
		// Add logo
		ev2.setEventPictureUrl("theDemoLogo.gif");

		// Add the event to the list
		events.add(ev2);

	}

	/**
	 * Escape an html string. Escaping data received from the client helps to prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public ArrayList<PlaybackDevice> getAllDevices() {
		return this.registeredDevices;
	}

	@Override
	public ArrayList<PlaybackDevice> getAvailableDevices() {
		return null;
	}

	@Override
	public ArrayList<PlaybackDevice> getUnavailableDevices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<PlaybackDevice> getDevicesInEvent(String eventId) {
		Event event = getEvent(eventId);
		if (event == null) {
			return null;
		}
		ArrayList<PlaybackDevice> result = new ArrayList<PlaybackDevice>(event
				.getEventMediaToDeviceMapping().keySet());
		// May still be null or empty
		return result;
	}

	@Override
	public Event getEventForDevice(String deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean addEvent(Event event) {
		// TODO Auto-generated method stub
		if (event != null) {
			events.add(event);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean deleteEvent(Event event) {
		if (event != null && events.contains(event)) {
			events.remove(event);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean deleteEvent(String eventId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean registerDevice(PlaybackDevice device) {
		/*
		 * Protocol: first the device connects to the serverSocket, if that is successful the device is added to the
		 * clientIDToSocketMap and it tries to call this method. Here I'm adding devices to the clientIDToDeviceMap but only those that
		 * have successfully been added to clientIDToSocketMap
		 */
		if (clientIDToSocketMap.containsKey(device.getDeviceId())) {
			clientIDToDeviceMap.put(device.getDeviceId(), device);
			System.out.println("Registered: " + device.toString());
			return new Boolean(true);
		} else {
			System.out.println("Unauthorized call to registerDevice: "
					+ device.toString());
			return new Boolean(false);
		}
	}

	@Override
	public Boolean unregisterDevice(PlaybackDevice device) {
		/*
		 * Protocol: when a device has to be removed from the Schaukasten it has the option to inform the server that it will become
		 * unavailable(to avoid confusion). First I check if the device that want's to leave the event was registered in the first
		 * place.
		 */
		if (clientIDToSocketMap.containsKey(device.getDeviceId())) {
			clientIDToSocketMap.remove(device.getDeviceId());
			clientIDToDeviceMap.remove(device.getDeviceId());
			System.out.println("Deregistered: " + device.toString());
			return new Boolean(true);
		} else {
			System.out.println("Unauthorized call to unregisterDevice: "
					+ device.toString());
			return new Boolean(false);
		}

	}

	@Override
	public Boolean markAvailable(PlaybackDevice device) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean markUnavailable(PlaybackDevice device) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Event getEvent(String eventId) {
		Event ev = null;
		for (Event e : events) {
			if (e.getEventId().equals(eventId)) {
				ev = e;
				break;
			}
		}
		return ev;
	}

	@Override
	public ArrayList<Event> getAllEvents() {
		// TODO Auto-generated method stub
		return events;
	}

	@Override
	public Boolean mapMediaToDevicesForEvent(Event event,
			HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean startEvent(Event event) {
		return startEvent(event.getEventId());
	}

	@Override
	public Boolean startEvent(String eventID) {
		final Command playCommand = new Command(CommandType.play);
		for (final String clientID : this.clientIDToSocketMap.keySet()) {
			conManager.sendCommandAsync(playCommand, clientID);
		}
		return true;
	}

	@Override
	public Boolean pauseEvent(Event event) {
		return pauseEvent(event.getEventId());
	}

	@Override
	public Boolean pauseEvent(String eventID) {
		final Command pauseCommand = new Command(CommandType.pause);
		for (final String clientID : this.clientIDToSocketMap.keySet()) {
			conManager.sendCommandAsync(pauseCommand, clientID);
		}
		return true;
	}

	@Override
	public Boolean stopEvent(Event event) {
		return stopEvent(event.getEventId());
	}

	@Override
	public Boolean stopEvent(String eventID) {
		final Command stopCommand = new Command(CommandType.stop);
		for (final String clientID : this.clientIDToSocketMap.keySet()) {
			conManager.sendCommandAsync(stopCommand, clientID);
		}
		return true;
	}

}
