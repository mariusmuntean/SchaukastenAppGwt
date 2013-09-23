package de.tum.os.sa.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

import de.tum.os.sa.client.helpers.PlaybackDeviceComparator;
import de.tum.os.sa.client.models.DisplayableEvent;
import de.tum.os.sa.client.models.DisplayableMedia;
import de.tum.os.sa.client.models.DisplayablePlaybackDevice;
import de.tum.os.sa.client.views.MainPage;
import de.tum.os.sa.shared.DTO.Event;
import de.tum.os.sa.shared.DTO.Media;
import de.tum.os.sa.shared.DTO.PlaybackDevice;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ShowcaseApp implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final IShowcaseServiceAsync showcareService = GWT
			.create(IShowcaseService.class);
	ServiceDefTarget serviceDef = (ServiceDefTarget) showcareService;
	String addr = GWT.getModuleBaseURL() + "showcaseService";

	MainPage mainPage;
	ListStore<DisplayableEvent> displayableEventsListStore = new ListStore<DisplayableEvent>();
	ListStore<DisplayablePlaybackDevice> allDisplayablePlaybackDevicesListStore = new ListStore<DisplayablePlaybackDevice>();
	ListStore<DisplayablePlaybackDevice> availableDisplayablePlaybackDevicesListStore = new ListStore<DisplayablePlaybackDevice>();
	ListStore<DisplayableMedia> currentEventdisplayableMediaListStore = new ListStore<DisplayableMedia>();
	ListStore<DisplayablePlaybackDevice> currentEventDisplayablePlaybackDevicesListStore = new ListStore<DisplayablePlaybackDevice>();
	TreeStore<BaseModelData> manageEventsAdd_MediaMapping = new TreeStore<BaseModelData>();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		serviceDef.setServiceEntryPoint(addr);

		mainPage = new MainPage(this, displayableEventsListStore,
				currentEventdisplayableMediaListStore,
				allDisplayablePlaybackDevicesListStore,
				availableDisplayablePlaybackDevicesListStore,
				currentEventDisplayablePlaybackDevicesListStore,
				manageEventsAdd_MediaMapping);

		fetchInitialData();

		RootPanel.get().add(mainPage);

	}

	private void fetchInitialData() {
		AsyncCallback<ArrayList<Event>> getEventscallback = new AsyncCallback<ArrayList<Event>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<Event> result) {
				displayableEventsListStore.removeAll();
				if (result != null && result.size() > 0) {
					// Convert the result from Event to DisplayableEvent
					// The ListStore is "observed" so changing it here is
					// reflected by any observer.
					for (Event event : result) {
						displayableEventsListStore.add(getDEfromEvent(event));
					}
				}
			}
		};
		showcareService.getAllEvents(getEventscallback);
	}

	/**
	 * Creates and returns a DisplayableEvent from a provided Event.
	 * 
	 * @param event
	 *            - The Event to be converted
	 * @return - A DispalyableEvent instance containing the data from the supplied parameter.
	 */
	private DisplayableEvent getDEfromEvent(Event event) {
		if (event == null)
			return null;

		DisplayableEvent de = new DisplayableEvent(event.getEventName(),
				event.getEventId(), event.getEventDescription(),
				event.getEventLocation(), event.getEventPictureUrl());
		de.setMediaToDeviceMapping(event.getEventMediaToDeviceMapping());
		de.setState(event.getEventState());
		de.setDevices(event.getEventDevices());
		de.setMedia(event.getEventMedia());

		return de;
	}

	public void startEvent(String eventID,
			final AsyncCallback<Boolean> startEventResultCallback) {
		if (eventID != null && !eventID.isEmpty()
				&& startEventResultCallback != null) {

			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					startEventResultCallback.onSuccess(result);
					fetchInitialData();
				}

				@Override
				public void onFailure(Throwable caught) {
					startEventResultCallback.onFailure(caught);
					fetchInitialData();

				}
			};

			showcareService.startEvent(eventID, callback);

		}
	}

	public void stopEvent(String eventID,
			final AsyncCallback<Boolean> stopEventResultCallback) {
		if (eventID != null && !eventID.isEmpty()
				&& stopEventResultCallback != null) {

			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					stopEventResultCallback.onSuccess(result);
					fetchInitialData();
				}

				@Override
				public void onFailure(Throwable caught) {
					stopEventResultCallback.onFailure(caught);
					fetchInitialData();

				}
			};

			showcareService.stopEvent(eventID, callback);

		}
	}

	public void pauseEvent(String eventID,
			final AsyncCallback<Boolean> pauseEventResultCallback) {
		if (eventID != null && !eventID.isEmpty()
				&& pauseEventResultCallback != null) {

			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					pauseEventResultCallback.onSuccess(result);
					fetchInitialData();
				}

				@Override
				public void onFailure(Throwable caught) {
					pauseEventResultCallback.onFailure(caught);
					fetchInitialData();

				}
			};

			showcareService.pauseEvent(eventID, callback);

		}
	}

	public void addEventProxy(Event event,
			final AsyncCallback<Boolean> addEventCallback) {
		AsyncCallback<Boolean> realAddEventCallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				addEventCallback.onSuccess(result);

			}

			@Override
			public void onFailure(Throwable caught) {
				addEventCallback.onFailure(caught);

			}
		};
		showcareService.addEvent(event, realAddEventCallback);
	}

	public void addFileToEventProxy(final String eventID, String fileLocation,
			String newFileName, final AsyncCallback<Boolean> addFileCallback) {

		AsyncCallback<Boolean> realAddFileCallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				addFileCallback.onSuccess(result);
				// fetch updated media list
				fetchEventMedia(eventID);

			}

			@Override
			public void onFailure(Throwable caught) {
				addFileCallback.onFailure(caught);
				// fetch updated media list
				fetchEventMedia(eventID);

			}
		};
		showcareService.addFileToEvent(eventID, newFileName, fileLocation,
				realAddFileCallback);

	}

	public void fetchEventMedia(String eventID) {

		AsyncCallback<ArrayList<Media>> getEventMediaCallback = new AsyncCallback<ArrayList<Media>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ArrayList<Media> result) {
				currentEventdisplayableMediaListStore.removeAll();
				if (result != null) {
					for (Media med : result) {
						currentEventdisplayableMediaListStore
								.add(getDispMedFromMed(med));
					}
				}

			}
		};

		showcareService.getEventMedia(eventID, getEventMediaCallback);
	}

	private DisplayableMedia getDispMedFromMed(Media media) {
		DisplayableMedia dm = new DisplayableMedia(media.getName(),
				media.getId(), media.getDescription(), media.getLocation(),
				media.getType());

		return dm;
	}

	public void unmapMediaFromDeviceInEvent(final String eventID,
			String deviceID, String mediaID,
			final AsyncCallback<Boolean> unmapCallback) {
		// Input sanitization
		if (eventID == null || eventID.isEmpty() || deviceID == null
				|| deviceID.isEmpty() || mediaID == null || mediaID.isEmpty()) {
			return;
		}

		AsyncCallback<Boolean> realUnmapCallback = new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				unmapCallback.onSuccess(result);
				fetchEventMapping(eventID);
				if (result) {
					Info.display("Succes!!", "Removed file from device");
				} else {
					Info.display("Error", "Couldn't remove file from device!");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				unmapCallback.onFailure(caught);
				fetchEventMapping(eventID);
				Info.display("Network Error",
						"Cannot communicate w/ the server.");
			}
		};

		showcareService.unmapMediaFromDeviceInEvent(eventID, deviceID, mediaID,
				realUnmapCallback);
	}

	public void removeMediaFromEvent(final String eventID,
			String mediaIdToRemove,
			final AsyncCallback<Boolean> removeMediaCallback) {
		AsyncCallback<Boolean> realCallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				removeMediaCallback.onSuccess(result);
				// fetch updated media list
				fetchEventMedia(eventID);
				fetchEventMapping(eventID);
			}

			@Override
			public void onFailure(Throwable caught) {
				removeMediaCallback.onFailure(caught);
				// fetch updated media list
				fetchEventMedia(eventID);
				fetchEventMapping(eventID);
			}
		};
		showcareService.removeMediaFromEvent(eventID, mediaIdToRemove,
				realCallback);
	}

	/**
	 * Fetches the most up-to-date available devices and updates "availableDisplayablePlaybackDevicesListStore".
	 */
	public void fetchAvailableDevices() {

		AsyncCallback<ArrayList<PlaybackDevice>> getAvailableDevicesCallback = new AsyncCallback<ArrayList<PlaybackDevice>>() {
			@Override
			public void onSuccess(ArrayList<PlaybackDevice> result) {
				if (ShowcaseApp.this.availableDisplayablePlaybackDevicesListStore != null) {
					ShowcaseApp.this.availableDisplayablePlaybackDevicesListStore
							.removeAll();
				}
				if (result != null && result.size() > 0) {
					for (PlaybackDevice pd : result) {
						ShowcaseApp.this.availableDisplayablePlaybackDevicesListStore
								.add(getDPDfromPD(pd));
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				if (ShowcaseApp.this.availableDisplayablePlaybackDevicesListStore != null) {
					ShowcaseApp.this.availableDisplayablePlaybackDevicesListStore
							.removeAll();
				}
			}
		};

		showcareService.getAvailableDevices(getAvailableDevicesCallback);
	}

	/**
	 * Returns an equivalent {@link DisplayablePlaybackDevice} instance from a provided {@link PlaybackDevice} instance.
	 * 
	 * @param pd
	 *            - A {@link PlaybackDevice} instance to be converted.
	 * @return - A {@link DisplayablePlaybackDevice} instance w/ the same data as the provide {@link PlaybackDevice}
	 */
	private DisplayablePlaybackDevice getDPDfromPD(PlaybackDevice pd) {
		if (pd == null) {
			return null;
		}

		DisplayablePlaybackDevice dpd = new DisplayablePlaybackDevice(
				pd.getDeviceName(), pd.getDeviceId(), pd.getScreenSize(),
				pd.getDeviceType());

		return dpd;

	}

	private PlaybackDevice getPDfromDPD(DisplayablePlaybackDevice dpd) {
		if (dpd == null) {
			return null;
		}

		PlaybackDevice pd = new PlaybackDevice(dpd.getDeviceId(),
				dpd.getDeviceName(), dpd.getDeviceType(), dpd.getScreenSize());

		return pd;
	}

	public void addDeviceToEventProxy(final String eventID,
			DisplayablePlaybackDevice device,
			final AsyncCallback<Boolean> addDevicesCallbak) {
		// Some parameter sanitization.
		if (eventID == null || eventID.isEmpty() || device == null
				|| addDevicesCallbak == null) {
			return;
		}

		// Generate list of converted objects
		ArrayList<PlaybackDevice> convertedDevices = new ArrayList<PlaybackDevice>(
				1);
		convertedDevices.add(getPDfromDPD(device));

		// This callback forwards the result to the one provided as param but
		// also allows to execute some logic when the results appear.
		AsyncCallback<Boolean> realAddDevicescallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				addDevicesCallbak.onSuccess(result);
				fetchEventMapping(eventID);
			}

			@Override
			public void onFailure(Throwable caught) {
				addDevicesCallbak.onFailure(caught);
				fetchEventMapping(eventID);
			}
		};

		showcareService.addDevicesToEvent(eventID, convertedDevices,
				realAddDevicescallback);

	}

	public void addDevicesToEventProxy(String eventID,
			ArrayList<DisplayablePlaybackDevice> devices,
			final AsyncCallback<Boolean> addDevicesCallbak) {
		// Some parameter sanitization.
		if (eventID == null || eventID.isEmpty() || devices == null
				|| devices.size() < 1 || addDevicesCallbak == null) {
			return;
		}

		// Generate list of converted objects
		ArrayList<PlaybackDevice> convertedDevices = new ArrayList<PlaybackDevice>(
				devices.size());
		for (DisplayablePlaybackDevice dpd : devices) {
			convertedDevices.add(getPDfromDPD(dpd));
		}

		// This callback forwards the result to the one provided as param but
		// also allows to execute some logic when the results appear.
		AsyncCallback<Boolean> realAddDevicescallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				addDevicesCallbak.onSuccess(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				addDevicesCallbak.onFailure(caught);
			}
		};

		showcareService.addDevicesToEvent(eventID, convertedDevices,
				realAddDevicescallback);

	}

	public void removeDeviceFromEvent(final String eventID,
			DisplayablePlaybackDevice deviceToRemove,
			final AsyncCallback<Boolean> removeDeviceCallback) {
		// Some parameter sanitization.
		if (eventID == null || eventID.isEmpty() || deviceToRemove == null
				|| removeDeviceCallback == null) {
			return;
		}

		ArrayList<String> deviceIDsToremove = new ArrayList<String>(1);
		deviceIDsToremove.add(deviceToRemove.getDeviceId());

		AsyncCallback<Boolean> realRemoveDevicecallback = new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				removeDeviceCallback.onSuccess(result);
				fetchAvailableDevices();
				fetchEventMapping(eventID);
			}

			@Override
			public void onFailure(Throwable caught) {
				removeDeviceCallback.onFailure(caught);
				fetchAvailableDevices();
				fetchEventMapping(eventID);
			}
		};
		showcareService.removeDevicesByIdFromEvent(eventID, deviceIDsToremove,
				realRemoveDevicecallback);
	}

	public void removeDevicesFromEvent(String eventID,
			ArrayList<DisplayablePlaybackDevice> devicesToRemove,
			final AsyncCallback<Boolean> removeDevicescallback) {

		// Some parameter sanitization.
		if (eventID == null || eventID.isEmpty() || devicesToRemove == null
				|| devicesToRemove.size() < 1 || removeDevicescallback == null) {
			return;
		}

		ArrayList<String> deviceIDsToremove = new ArrayList<String>(
				devicesToRemove.size());
		for (DisplayablePlaybackDevice dpd : devicesToRemove) {
			deviceIDsToremove.add(dpd.getDeviceId());
		}

		AsyncCallback<Boolean> realRemoveDevicecallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				removeDevicescallback.onSuccess(result);
				fetchAvailableDevices();
			}

			@Override
			public void onFailure(Throwable caught) {
				removeDevicescallback.onFailure(caught);

			}
		};

		showcareService.removeDevicesByIdFromEvent(eventID, deviceIDsToremove,
				realRemoveDevicecallback);
	}

	public void mapMediaToDeviceInEventProxy(final String eventID,
			String deviceID, String mediaID,
			final AsyncCallback<Boolean> mapMediacallback) {
		if (eventID == null || eventID.isEmpty() || deviceID == null
				|| deviceID.isEmpty() || mediaID == null || mediaID.isEmpty()) {
			return;
		}
		AsyncCallback<Boolean> realMapMediaCallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				mapMediacallback.onSuccess(result);
				fetchEventMapping(eventID);
				fetchEventMedia(eventID);

			}

			@Override
			public void onFailure(Throwable caught) {
				mapMediacallback.onFailure(caught);
				fetchEventMapping(eventID);
				fetchEventMedia(eventID);

			}
		};

		showcareService.mapMediaToDeviceInevent(eventID, deviceID, mediaID,
				realMapMediaCallback);

	}

	public void fetchEventMapping(String eventID) {
		if (eventID == null || eventID.isEmpty()) {
			return;
		}

		AsyncCallback<HashMap<PlaybackDevice, ArrayList<Media>>> getMappingcallback = new AsyncCallback<HashMap<PlaybackDevice, ArrayList<Media>>>() {

			@Override
			public void onSuccess(
					HashMap<PlaybackDevice, ArrayList<Media>> result) {
				ShowcaseApp.this.manageEventsAdd_MediaMapping.removeAll();
				if (result != null) {
					ArrayList<PlaybackDevice> sortedKeys = new ArrayList<PlaybackDevice>(
							result.keySet());
					Collections
							.sort(sortedKeys, new PlaybackDeviceComparator());
					for (PlaybackDevice pd : sortedKeys) {
						DisplayablePlaybackDevice localPD = getDPDfromPD(pd);
						if (result.get(pd) != null) {
							ShowcaseApp.this.manageEventsAdd_MediaMapping.add(
									localPD, false);
							for (Media md : result.get(pd)) {
								DisplayableMedia localMD = getDispMedFromMed(md);
								ShowcaseApp.this.manageEventsAdd_MediaMapping
										.add(localPD, localMD, false);
							}
						} else {
							ShowcaseApp.this.manageEventsAdd_MediaMapping.add(
									localPD, false);
						}
					}
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};

		showcareService.getEventMapping(eventID, getMappingcallback);

	}

	public void setAndReturnEventLogoProxy(String eventID, String pictureName, final AsyncCallback<String> evLogoCallback){
		// Filter input
		if(eventID==null || eventID.isEmpty() || pictureName==null || pictureName.isEmpty()||evLogoCallback==null){
			return;
		}
		AsyncCallback<String> realSetAndRetEvLogocallback = new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				evLogoCallback.onSuccess(result);
			}
			@Override
			public void onFailure(Throwable caught) {
				evLogoCallback.onFailure(caught);
			}
		};
		
		showcareService.setAndReturnEventLogo(eventID, pictureName, realSetAndRetEvLogocallback);
	}
}
