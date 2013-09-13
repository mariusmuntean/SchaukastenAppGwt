package de.tum.os.sa.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tum.os.sa.client.IShowcaseService;
import de.tum.os.sa.server.helpers.FSHelper;
import de.tum.os.sa.shared.CommandType;
import de.tum.os.sa.shared.DeviceType;
import de.tum.os.sa.shared.MediaTypes;
import de.tum.os.sa.shared.ShowcaseConstants;
import de.tum.os.sa.shared.DTO.Event;
import de.tum.os.sa.shared.DTO.Media;
import de.tum.os.sa.shared.DTO.PlaybackDevice;
import de.tum.os.sa.shared.commands.Command;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

// Needed for the file upload part
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

/**
 * The server side implementation of the RPC service.
 */
public class ShowcaseServiceImpl extends RemoteServiceServlet implements
		IShowcaseService {

	private static final long serialVersionUID = -2798669587684922962L;
	private ArrayList<PlaybackDevice> registeredDevices = new ArrayList<PlaybackDevice>();
	private ArrayList<PlaybackDevice> availableDevices = new ArrayList<PlaybackDevice>();
	private ArrayList<PlaybackDevice> unavailableDevices = new ArrayList<PlaybackDevice>();
	private ArrayList<Media> media = new ArrayList<Media>();
	private List<Event> events = new ArrayList<Event>();

	private ClientListener clientListen;
	private ConnectionManager conManager;
	private ConcurrentHashMap<String, Socket> clientIDToSocketMap;
	private ConcurrentHashMap<String, PlaybackDevice> clientIDToDeviceMap;

	/*
	 * Persistence stuff here
	 */
	public static final String persistence_unit_name = "showcase";
	EntityManagerFactory emf;
	EntityManager em;

	FSHelper fsHelper = new FSHelper();

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

		/*
		 * Persistence stuff here
		 */
		emf = Persistence.createEntityManagerFactory(persistence_unit_name);
		em = emf.createEntityManager();

		// Dummy persist here
		em.getTransaction().begin();

		em.persist(events.get(0));
		em.persist(events.get(1));

		em.getTransaction().commit();

		events.clear();
		events = getAllEvents();

		fsHelper.createFolderStructure(true);
	}

	@Override
	protected void checkPermutationStrongName() throws SecurityException {
		return;
	}

	/*
	 * Overriding the service method to hook myself in. I need this to intercept
	 * uploaded files.
	 */
	private FileItem fi;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload
				.isMultipartContent(new ServletRequestContext(request));

		if (isMultipart) {
			FileItemFactory fif = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fif);
			try {
				List items = upload.parseRequest(request);
				fi = (FileItem) items.get(0); // I upload only one file, for
												// now.

				if (fi == null) {
					super.service(request, response);
					return;
				} else if (fi.getFieldName().equalsIgnoreCase(
						"FileUploadWidget")) {
					String fileName = fi.getName();
					String tempFileLocation = ((org.apache.commons.fileupload.disk.DiskFileItem) fi)
							.getStoreLocation().getPath();
					response.setStatus(HttpServletResponse.SC_CREATED);
					response.getWriter().print(
							ShowcaseConstants.FileUploadOkMessage
									+ ShowcaseConstants.ResponseDelimiter
									+ fileName
									+ ShowcaseConstants.ResponseDelimiter
									+ tempFileLocation);
					response.flushBuffer();
				}
			} catch (FileUploadException ex) {
				System.out.println(ex);
			}
		} else {
			super.service(request, response);
			return;
		}
	}

	private String persistObject(Object obj) {
		String result = "ok";
		try {
			em.getTransaction().begin();

			em.persist(obj);

			em.getTransaction().commit();
		} catch (Exception e) {
			result = e.getMessage();
		}
		System.out.println("SERVER persistObject() result: " + result);
		em.clear();
		return result;
	}

	private String updateObject(Object obj) {
		String result = "ok";
		try {
			em.getTransaction().begin();

			em.merge(obj);

			em.getTransaction().commit();
		} catch (Exception e) {
			result = e.getMessage();
		}
		System.out.println("SERVER updateObject() result: " + result);
		em.clear();
		return result;
	}

	private String deleteObject(Object obj) {
		String result = "ok";
		try {
			em.getTransaction().begin();

			em.remove(obj);

			em.getTransaction().commit();
		} catch (Exception e) {
			result = e.getMessage();
		}
		System.out.println("SERVER persistObject() result: " + result);
		return result;
	}

	private void generateDummyDevices() {
		PlaybackDevice pd1 = new PlaybackDevice(UUID.randomUUID().toString(),
				"Nexus S", DeviceType.Smartphone, 3.7f);
		PlaybackDevice pd2 = new PlaybackDevice(UUID.randomUUID().toString(),
				"Nexus S", DeviceType.Smartphone, 3.7f);
		PlaybackDevice pd3 = new PlaybackDevice(UUID.randomUUID().toString(),
				"Nexus 4", DeviceType.Smartphone, 4.5f);
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
		final ArrayList<Media> careerTumMediaList = new ArrayList<Media>(
				media.subList(0, 2));
		// Map media to devices in this event
		HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping = new HashMap<PlaybackDevice, ArrayList<Media>>();
		// mediaToDeviceMapping.put(registeredDevices.get(0), new
		// ArrayList<Media>(
		// careerTumMediaList.subList(0, 0)));
		// mediaToDeviceMapping.put(registeredDevices.get(1), new
		// ArrayList<Media>(
		// careerTumMediaList.subList(1, 1)));
		// mediaToDeviceMapping.put(registeredDevices.get(2), new
		// ArrayList<Media>(
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

		demoMediaToDeviceMapping.put(registeredDevices.get(3),
				new ArrayList<Media>(demoTumMediaList.subList(1, 1)));
		demoMediaToDeviceMapping.put(registeredDevices.get(2),
				new ArrayList<Media>(demoTumMediaList.subList(0, 0)));
		// Create the event
		Event ev2 = new Event("Demo TUM", "14716446728347",
				"It's a demo event!",
				"Garching Forschungszentrum, Bolzman Str 3");
		// Add mapping
		ev2.setEventMediaToDeviceMapping(demoMediaToDeviceMapping);
		// Add logo
		ev2.setEventPictureUrl("theDemoLogo.gif");

		// Add the event to the list
		events.add(ev2);

	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
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

	/*
	 * Methods for web clients
	 */

	@Override
	public ArrayList<PlaybackDevice> getAllDevices() {
		return this.registeredDevices;
	}

	@Override
	public ArrayList<PlaybackDevice> getAvailableDevices() {
		ArrayList<PlaybackDevice> result = new ArrayList<PlaybackDevice>();
		for (PlaybackDevice pd : registeredDevices) {
			if (pd.isAvailable()) {
				result.add(pd);
			}
		}

		return result;
	}

	@Override
	public ArrayList<PlaybackDevice> getUnavailableDevices() {
		ArrayList<PlaybackDevice> result = new ArrayList<PlaybackDevice>();
		for (PlaybackDevice pd : registeredDevices) {
			if (!pd.isAvailable()) {
				result.add(pd);
			}
		}

		return result;
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
		Event result = null;
		events = getAllEvents();
		for (Event ev : events) {
			if (ev.containsDeviceId(deviceId)) {
				result = ev;
				break;
			}
		}
		return result;
	}

	@Override
	public Boolean addEvent(Event event) {
		if (event == null || event.getEventName() == null
				|| event.getEventName().isEmpty()
				|| !fsHelper.isEventNameAvailable(event.getEventName())) {
			return false;

		} else {
			// Create a folder to hold event media
			Boolean createEventFolderResult = fsHelper.createEventFolder(event
					.getEventName());
			if (!createEventFolderResult) {
				return false;
			}

			// Save event to database
			String result = persistObject(event);
			if (result.equals("ok")) {
				// events = getAllEvents();
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public Boolean deleteEvent(Event event) {
		if (event == null) {
			return false;
		}

		return deleteEvent(event.getEventId());
	}

	@Override
	public Boolean deleteEvent(String eventId) {
		if (eventId == null || eventId.isEmpty()) {
			return false;
		}
		String result = "ok";
		try {
			Event eventToRemove = getEvent(eventId);
			result = deleteObject(eventToRemove);
		} catch (Exception e) {
			result = e.getMessage();
		}

		if (result.equals("ok")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Event getEvent(String eventId) {
		Event ev = null;
		try {
			Query getEventQuery = em
					.createQuery("select ev from Event ev where ev.eventId = :eID");
			getEventQuery.setParameter("eID", eventId);

			ev = (Event) getEventQuery.getSingleResult();
		} catch (Exception e) {
			// System.out.println();
			e.printStackTrace();
		}
		return ev;
	}

	@Override
	public ArrayList<Event> getAllEvents() {
		Query getEventsQuery = em.createQuery("select ev from Event ev");

		ArrayList<Event> eventsResult = new ArrayList<Event>(
				getEventsQuery.getResultList());

		return eventsResult;
	}

	@Override
	public Boolean updateEventMediaToDeviceMapping(Event event,
			HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping) {
		if (event == null || mediaToDeviceMapping == null) {
			return false;
		}

		return updateEventMediaToDeviceMapping(event.getEventId(),
				mediaToDeviceMapping);
	}

	@Override
	public Boolean updateEventMediaToDeviceMapping(String eventId,
			HashMap<PlaybackDevice, ArrayList<Media>> mediaToDeviceMapping) {
		if (eventId == null || eventId.isEmpty()
				|| mediaToDeviceMapping == null) {
			return false;
		}

		Event dbEvent = getEvent(eventId);
		if (dbEvent == null) {
			return false;
		}

		dbEvent.setEventMediaToDeviceMapping(mediaToDeviceMapping);
		if (updateObject(dbEvent).equals("ok")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean startEvent(Event event) {
		return startEvent(event.getEventId());

		/*
		 * Send Play command including the EventID, the Android Clients will ask
		 * for the media in that particular event that is theirs to display.
		 * 
		 * Similarly to getEventForDevice make a method
		 * getMediaForDeviceInEvent(deviceID, eventId) that returns all the
		 * media that is mapped to a device in a certain event.
		 * 
		 * This will be useful in the long term when a device could be used in
		 * multiple events only one of which is active at one moment in time
		 */

	}

	@Override
	public Boolean startEvent(String eventID) {
		final Command playCommand = new Command(CommandType.play, "");
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
		final Command pauseCommand = new Command(CommandType.pause, "");
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
		final Command stopCommand = new Command(CommandType.stop, "");
		for (final String clientID : this.clientIDToSocketMap.keySet()) {
			conManager.sendCommandAsync(stopCommand, clientID);
		}
		return true;
	}

	/*
	 * Methods for Android Clients
	 */

	@Override
	public Boolean registerDevice(PlaybackDevice device) {
		/*
		 * Protocol: first the device connects to the serverSocket, if that is
		 * successful the device is added to the clientIDToSocketMap and it
		 * tries to call this method. Here I'm adding devices to the
		 * clientIDToDeviceMap but only those that have successfully been added
		 * to clientIDToSocketMap
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
		 * Protocol: when a device has to be removed from the Schaukasten it has
		 * the option to inform the server that it will become unavailable(to
		 * avoid confusion). First I check if the device that wants to leave the
		 * event was registered in the first place.
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
	public PlaybackDevice getDevice(String deviceId) {
		if (deviceId == null || deviceId.isEmpty()) {
			return null;
		}

		PlaybackDevice result = null;
		for (PlaybackDevice pd : registeredDevices) {
			if (pd.getDeviceId().equals(deviceId)) {
				result = pd;
				break;
			}
		}
		return result;
	}

	@Override
	public Boolean markAvailable(PlaybackDevice device) {
		// Make sure the device is actually registered
		if (getDevice(device.getDeviceId()) == null) {
			return false;
		}
		device.setAvailable(true);
		return true;
	}

	@Override
	public Boolean markUnavailable(PlaybackDevice device) {
		// Make sure the device is actually registered
		if (getDevice(device.getDeviceId()) == null) {
			return false;
		}
		device.setAvailable(false);
		return true;
	}

	@Override
	public List<Media> getMediaForDeviceInEvent(String deviceID, String eventID) {
		// Sanitize code
		if (deviceID == null || deviceID.isEmpty() || eventID == null
				|| eventID.isEmpty()) {
			return null;
		}

		// Search for an event with the given event ID

		Event event = getEvent(eventID);
		if (event == null) {
			return null;
		}

		// Search for a device in the event that has the given device ID
		List<PlaybackDevice> eventDevices = event.getEventDevices();
		if (eventDevices == null || eventDevices.size() == 0) {
			return null;
		}

		PlaybackDevice playbackDevice = null;
		for (PlaybackDevice pb : eventDevices) {
			if (pb.getDeviceId().equals(deviceID)) {
				playbackDevice = pb;
				break;
			}
		}
		if (playbackDevice == null) {
			return null;
		}

		List<Media> result = event.getEventMediaToDeviceMapping().get(
				playbackDevice);

		return result; // result could still be null so when calling this method
						// check the result.
	}

	@Override
	public Boolean addMediaToEvent(String eventID, ArrayList<Media> newMedia) {
		if (eventID == null) {
			return false;
		}

		if (newMedia == null || newMedia.size() < 1) {
			return true;
		}

		Event ev = getEvent(eventID);
		if (ev != null) {
			ev.addMedia(newMedia);
			if (updateObject(ev).equals("ok")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Boolean removeMediaFromEvent(String eventID,
			ArrayList<Media> mediaToRemove) {
		if (eventID == null) {
			return false;
		}

		if (mediaToRemove == null || mediaToRemove.size() < 1) {
			return true;
		}

		Event ev = getEvent(eventID);
		if (ev != null) {
			ev.removeMedia(mediaToRemove);
			if (updateObject(ev).equals("ok")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Boolean removeMediaFromEvent(String eventID,
			String mediaIdToRemove) {
		if (eventID == null) {
			return false;
		}

		if (mediaIdToRemove == null || mediaIdToRemove.isEmpty()) {
			return true;
		}

		Event ev = getEvent(eventID);
		if (ev != null) {
			// Search for a Media item with the given ID in the found Event
			Media mediaToDelete = null;
			for(Media m:ev.getEventMedia()){
				if(m.getId().equals(mediaIdToRemove)){
					mediaToDelete = m;
					break;
				}
			}
			// If no Media item exists in the Event ...
			if(mediaToDelete==null){
				return false;
			}
			Boolean deleteStatus = fsHelper.deleteFileFromEvent(mediaToDelete.getName(), ev.getEventName());
			ev.removeMedia(mediaIdToRemove);
			Boolean updateStatus = updateObject(ev).equals("ok");
			if (deleteStatus && updateStatus) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Boolean addDevicesToEvent(String eventID,
			ArrayList<PlaybackDevice> newDevices) {
		if (eventID == null) {
			return false;
		}

		if (newDevices == null || newDevices.size() < 1) {
			return true;
		}

		Event ev = getEvent(eventID);
		if (ev != null) {
			ev.addDevices(newDevices);
			if (updateObject(ev).equals("ok")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Boolean removeDevicesFromEvent(String eventID,
			ArrayList<PlaybackDevice> devicesToRemove) {
		if (eventID == null) {
			return false;
		}

		if (devicesToRemove == null || devicesToRemove.size() < 1) {
			return true;
		}

		Event ev = getEvent(eventID);
		if (ev != null) {
			ev.removeDeviceList(devicesToRemove);
			if (updateObject(ev).equals("ok")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Boolean addFileToEvent(String eventID, String fileName,
			String fileLocation) {
		if (eventID == null || eventID.isEmpty() || fileLocation == null
				|| fileLocation.isEmpty() || fileName == null
				|| fileName.isEmpty()) {
			return false;
		}
		Event ev = getEvent(eventID);
		String eventName = ev.getEventName();
		if (eventName == null || eventName.isEmpty()) {
			return false;
		}

		Boolean result = true;
		// Add file to the event folder
		result = fsHelper.moveFileToEvent(eventName, fileLocation, fileName);
		if (!result) {
			return false;
		}

		// Add a Media object to the event
		Media newMedia = new Media(fileName, UUID.randomUUID().toString(), "",
				"", fsHelper.getFileTypeFromName(fileName));
		ev.addMedia(newMedia);

		// Persist event
		String persistResult = persistObject(ev);
		if (persistResult.equals("ok")) {
			result = true;
		} else {
			result = false;
		}

		return result;
	}

}
