package de.tum.os.sa.client;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

import de.tum.os.sa.client.models.DisplayableEvent;
import de.tum.os.sa.client.models.DisplayableMedia;
import de.tum.os.sa.client.views.MainPage;
import de.tum.os.sa.shared.DTO.Event;
import de.tum.os.sa.shared.DTO.Media;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ShowcaseApp implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final IShowcaseServiceAsync showcareService = GWT
			.create(IShowcaseService.class);
	ServiceDefTarget serviceDef = (ServiceDefTarget) showcareService;
	String addr = GWT.getModuleBaseURL() + "showcaseService";

	MainPage mainPage;
	ListStore<DisplayableEvent> displayableEventsListStore = new ListStore<DisplayableEvent>();
	ListStore<DisplayableMedia> currentEventdisplayableMediaListStore = new ListStore<DisplayableMedia>();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		serviceDef.setServiceEntryPoint(addr);

		mainPage = new MainPage(this, displayableEventsListStore,
				currentEventdisplayableMediaListStore);

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
	 * @return - A DispalyableEvent instance containing the data from the
	 *         supplied parameter.
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

		AsyncCallback<Event> getEventCallback = new AsyncCallback<Event>() {

			@Override
			public void onSuccess(Event result) {
				currentEventdisplayableMediaListStore.removeAll();
				if (result != null) {
					for (Media med : result.getEventMedia()) {
						currentEventdisplayableMediaListStore
								.add(getDispMedFromMed(med));
					}
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};

		showcareService.getEvent(eventID, getEventCallback);
	}

	private DisplayableMedia getDispMedFromMed(Media media) {
		DisplayableMedia dm = new DisplayableMedia(media.getName(),
				media.getId(), media.getDescription(), media.getLocation(),
				media.getType());

		return dm;
	}

	public void removeMediaFromEvent(final String eventID, String mediaIdToRemove,
			final AsyncCallback<Boolean> removeMediaCallback) {
		AsyncCallback<Boolean> realCallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				removeMediaCallback.onSuccess(result);
				// fetch updated media list
				fetchEventMedia(eventID);
			}

			@Override
			public void onFailure(Throwable caught) {
				removeMediaCallback.onFailure(caught);
				// fetch updated media list
				fetchEventMedia(eventID);
			}
		};
		showcareService.removeMediaFromEvent(eventID, mediaIdToRemove,
				realCallback);
	}

}
