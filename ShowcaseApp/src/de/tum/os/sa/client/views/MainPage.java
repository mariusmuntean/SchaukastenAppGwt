package de.tum.os.sa.client.views;

import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

import de.tum.os.sa.client.ShowcaseApp;
import de.tum.os.sa.client.helpers.UUID;
import de.tum.os.sa.client.models.DisplayableEvent;
import de.tum.os.sa.client.models.DisplayableMedia;
import de.tum.os.sa.client.resources.Showcaseresources;
import de.tum.os.sa.shared.ShowcaseConstants;
import de.tum.os.sa.shared.DTO.Event;

public class MainPage extends Composite {

	private static MainPageUiBinder uiBinder = GWT
			.create(MainPageUiBinder.class);

	/*
	 * Overview controls
	 */
	@UiField(provided = true)
	ListView<DisplayableEvent> lstViewOverviewAvailableEvents;

	@UiField
	DeckPanel deckPanelActualView;

	@UiField
	Image imgEventLogo;

	@UiField
	TextBox txtBoxEventName;

	@UiField
	TextBox txtBoxEventDescription;
	@UiField
	TextBox txtBoxEventLocation;

	@UiField
	TextBox txtBoxEventState;

	@UiField
	Button btnEventStart;

	@UiField
	Button btnEventStop;

	@UiField(provided = true)
	EventWidget eventWidget;

	@UiField
	RadioButton rBtnManageEvents, rBtnOverview;

	/*
	 * Manage Events region
	 */

	String manageEvAdd_CurrentEventName, manageEvAdd_CurrentEventID,
			manageEvAdd_CurrentEventDesc, manageEvAdd_CurrentEventLocation;

	@UiField
	Button btnManageEventUploadFile;

	@UiField
	Button btnManageEventsAdd_NewEvent;

	@UiField
	TextArea txtAreaManageEvents_Add_EvDescription;

	@UiField
	TextBox txtBoxManageEvent_EvTitle, txtBoxManageEvent_EvLocation;

	// FileUpload
	@UiField
	FormPanel formPanelManageEvent;

	@UiField
	FileUpload fileUploadManageEvent;

	@UiField
	DisclosurePanel disclosureManageEventsAdd_EventMedia;

	@UiField(provided = true)
	ListView<DisplayableMedia> lstViewEventMedia, lstViewEventMedia_removed;

	private ListStore<DisplayableMedia> currentEventdisplayableMediaListStore = new ListStore<DisplayableMedia>();
	private ListStore<DisplayableMedia> currentEventdisplayableMediaRemovedListStore = new ListStore<DisplayableMedia>();

	/*
	 * Stores region
	 */
	private ListStore<DisplayableEvent> displayableEventsListStore = new ListStore<DisplayableEvent>();

	/*
	 * Simple templates
	 */
	final String displayableDeviceTemplate = new String(
			"<table>"
					+ "<tr>"
					+ "<td rowspan=\"3\"><img src=\"images/eventLogos/{picture}\" width=\"100\" height=\"100\"></td>"
					+ "<td><font size=\"6\">{name}</font></td></tr>"
					+ "<tr><td><font size=\"4\">{description}</font></td></tr>"
					+ "<tr><td>Status: <big>{state}</big></td></tr>"
					+ "</table>");

	final String displayableMediaTemplate = new String("<table>"
			+ "<tr>"
			// +
			// "<input type=\"image\" src=\"images/button_delete_01.png\" width=\"30\" onclick=\"removeEventMediaFunc()\">"
			+ "<td><img src=\"{imgName}\" height=\"50\"></td>"
			+ "<td><font size=\"5\">{name}</font></td></tr>" + "</table>");

	final String displayableMediaRemovedTemplate = new String("<table>"
			+ "<tr>"
			// +
			// "<input type=\"image\" src=\"images/button_delete_01.png\" width=\"30\" onclick=\"removeEventMediaFunc()\">"
			+ "<td><img src=\"{imgName}\" height=\"20\"></td>"
			+ "<td><font size=\"2\">{name}</font></td></tr>" + "</table>");

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	ShowcaseApp client;

	String uploadedFileName = "";
	String uploadedTempFilePath = "";
	UUID uuidGen = new UUID();

	public MainPage(ShowcaseApp client,
			ListStore<DisplayableEvent> displayableEventsListStore,
			ListStore<DisplayableMedia> currentEventdisplayableMediaListStore) {
		this.client = client;
		this.displayableEventsListStore = displayableEventsListStore;
		this.currentEventdisplayableMediaListStore = currentEventdisplayableMediaListStore;

		exportJsMethods();
		instantiateControls();
		initWidget(uiBinder.createAndBindUi(this));
		wireUpControls();
	}

	private void exportJsMethods() {
		exportRemoveEventMediaHandler();

	}

	private void instantiateControls() {
		// Overview
		lstViewOverviewAvailableEvents = new ListView<DisplayableEvent>();
		lstViewOverviewAvailableEvents
				.setSimpleTemplate(displayableDeviceTemplate);
		lstViewOverviewAvailableEvents.setStore(displayableEventsListStore);

		eventWidget = new EventWidget(displayableEventsListStore);

		// Manage Events
		lstViewEventMedia = new ListView<DisplayableMedia>();
		lstViewEventMedia.setDisplayProperty("name");
		lstViewEventMedia.setSimpleTemplate(displayableMediaTemplate);
		lstViewEventMedia.setStore(currentEventdisplayableMediaListStore);

		lstViewEventMedia_removed = new ListView<DisplayableMedia>();
		lstViewEventMedia.setDisplayProperty("name");
		lstViewEventMedia_removed
				.setSimpleTemplate(displayableMediaRemovedTemplate);
		lstViewEventMedia_removed
				.setStore(currentEventdisplayableMediaRemovedListStore);

	}

	private void wireUpControls() {

		/*
		 * Page selection radio buttons
		 */

		rBtnManageEvents.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deckPanelActualView.showWidget(1);
			}
		});

		rBtnOverview.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deckPanelActualView.showWidget(0);
			}
		});

		deckPanelActualView.showWidget(0);

		/*
		 * Overview page
		 */
		lstViewOverviewAvailableEvents.getSelectionModel()
				.addSelectionChangedListener(
						new SelectionChangedListener<DisplayableEvent>() {

							@Override
							public void selectionChanged(
									SelectionChangedEvent<DisplayableEvent> se) {
								if (se != null && se.getSelectedItem() != null) {
									DisplayableEvent de = se.getSelectedItem();
									showDisplayableEventInfo(de);
									btnEventStart.setEnabled(true);
									btnEventStop.setEnabled(true);
								}

							}
						});

		displayableEventsListStore.addListener(Store.Add,
				new Listener<StoreEvent<DisplayableEvent>>() {
					@Override
					public void handleEvent(StoreEvent<DisplayableEvent> be) {
						if (be != null && be.getModels() != null
								&& be.getModels().size() > 0)
							MainPage.this.showDisplayableEventInfo(be
									.getModels().get(0));
					}
				});

		btnEventStart.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (lstViewOverviewAvailableEvents.getSelectionModel()
						.getSelectedItem() != null) {
					DisplayableEvent de = lstViewOverviewAvailableEvents
							.getSelectionModel().getSelectedItem();
					AsyncCallback<Boolean> startEventResultCallback = new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								Info.display("Success!", "Event started!");
							} else {
								Info.display("Server error!",
										"Could not start event!");
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Info.display("Network error!",
									"Event status is unknown!");
						}
					};

					client.startEvent(de.getID(), startEventResultCallback);
				}

			}
		});

		btnEventStop.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (lstViewOverviewAvailableEvents.getSelectionModel()
						.getSelectedItem() != null) {
					DisplayableEvent de = lstViewOverviewAvailableEvents
							.getSelectionModel().getSelectedItem();
					AsyncCallback<Boolean> stopEventResultCallback = new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								Info.display("Success!", "Event stoped!");
							} else {
								Info.display("Server error!",
										"Could not stop event!");
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Info.display("Network error!",
									"Event status is unknown!");
						}
					};

					client.stopEvent(de.getID(), stopEventResultCallback);
				}

			}
		});

		/*
		 * Manage Events page
		 */
		btnManageEventsAdd_NewEvent.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				manageEvAdd_CurrentEventName = txtBoxManageEvent_EvTitle
						.getText();
				manageEvAdd_CurrentEventID = UUID.uuid();
				manageEvAdd_CurrentEventLocation = txtBoxManageEvent_EvLocation
						.getText();
				manageEvAdd_CurrentEventDesc = txtAreaManageEvents_Add_EvDescription
						.getText();

				if (manageEvAdd_CurrentEventName == null
						|| manageEvAdd_CurrentEventName.isEmpty()) {
					return;
				}

				AsyncCallback<Boolean> addEventCallback = new AsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Info.display("Success!",
									manageEvAdd_CurrentEventName + " created!");
							enableEventMediaUpload();
						} else {
							Info.display("Error!", " Could not create: "
									+ manageEvAdd_CurrentEventName);
							disableEventMediaUpload();
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						Info.display("Network Error!",
								"Could not communicate with server.");
						disableEventMediaUpload();

					}
				};

				Event ev = new Event(manageEvAdd_CurrentEventName,
						manageEvAdd_CurrentEventID,
						manageEvAdd_CurrentEventDesc,
						manageEvAdd_CurrentEventLocation);

				client.addEventProxy(ev, addEventCallback);
			}
		});

		formPanelManageEvent.setAction(GWT.getModuleBaseURL()
				+ "showcaseService");
		formPanelManageEvent.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanelManageEvent.setMethod(FormPanel.METHOD_POST);
		formPanelManageEvent
				.addSubmitCompleteHandler(new SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						String result = event.getResults();
						result = result.replace("<pre>", "");
						result = result.replace("</pre>", "");
						String r = "\\Q" + ShowcaseConstants.ResponseDelimiter
								+ "\\E";

						// String splitPattern = Pattern
						// .quote(ShowcaseConstants.ResponseDelimiter);
						String[] parsedResult = result.split(r);
						String message = parsedResult[0];
						uploadedFileName = parsedResult[1];
						uploadedTempFilePath = parsedResult[2];
						if (message.toLowerCase().contains(
								ShowcaseConstants.FileUploadOkMessage
										.toLowerCase())) {
							Info.display("Success", "Upload worked!");
							// Move the file to the proper event
							AsyncCallback<Boolean> addFileCallback = new AsyncCallback<Boolean>() {

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										Info.display("Success",
												"Moved file to: dummyEvent1");
									} else {
										Info.display("Error",
												"Could not move file!");
									}

								}

								@Override
								public void onFailure(Throwable caught) {
									Info.display("Error",
											"Network error while moving file!");

								}
							};
							client.addFileToEventProxy(
									manageEvAdd_CurrentEventID,
									uploadedTempFilePath, uploadedFileName,
									addFileCallback);
						} else {
							Info.display("Error", "Result: " + result);
							Window.alert("Result: " + result);
						}

					}
				});

		btnManageEventUploadFile.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!fileUploadManageEvent.getFilename().equalsIgnoreCase("")) {
					formPanelManageEvent.submit();
				}

			}
		});

		ListViewDragSource eventMediaDragSource = new ListViewDragSource(
				lstViewEventMedia);
		eventMediaDragSource.setGroup("eventMediaGroup1");
		ListViewDragSource eventMediaDragSource_Removed = new ListViewDragSource(
				lstViewEventMedia_removed);
		eventMediaDragSource_Removed.setGroup("eventMediaGroup1");

		ListViewDropTarget eventMediaDragTarget = new ListViewDropTarget(
				lstViewEventMedia);
		eventMediaDragTarget.setGroup("eventMediaGroup1");
		eventMediaDragTarget.setFeedback(Feedback.INSERT);
		ListViewDropTarget eventMediaRemovedDragTarget = new ListViewDropTarget(
				lstViewEventMedia_removed);
		eventMediaRemovedDragTarget.setGroup("eventMediaGroup1");
		eventMediaRemovedDragTarget.setFeedback(Feedback.APPEND);

		// Add context menu to event media list
		Menu mediaListContextMenu = new Menu();
		mediaListContextMenu.setWidth(100);
		mediaListContextMenu.setHeight(40);
		MenuItem removeSelectedMedia_MenuItem = new MenuItem();
		removeSelectedMedia_MenuItem.setText("delete");
		removeSelectedMedia_MenuItem.setIcon(AbstractImagePrototype
				.create(Showcaseresources.INSTANCE.deleteIconSmall()));
		removeSelectedMedia_MenuItem
				.addSelectionListener(new SelectionListener<MenuEvent>() {
					public void componentSelected(MenuEvent ce) {
						final DisplayableMedia dm = lstViewEventMedia
								.getSelectionModel().getSelectedItem();
						if (dm != null) {
							// currentEventdisplayableMediaListStore.remove(dm);
							AsyncCallback<Boolean> removeMediaCallback = new AsyncCallback<Boolean>() {

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										Info.display("Success!", "Removed "
												+ dm.getName());
									} else {
										Info.display(
												"Error!",
												"Couldn'd remove "
														+ dm.getName());
									}
								}

								@Override
								public void onFailure(Throwable caught) {
									Info.display("Network Error!",
											"Couldn'd communicate with the server.");

								}
							};
							client.removeMediaFromEvent(
									manageEvAdd_CurrentEventID, dm.getID(),
									removeMediaCallback);
						}
					};
				});
		mediaListContextMenu.add(removeSelectedMedia_MenuItem);
		lstViewEventMedia.setContextMenu(mediaListContextMenu);

	}

	public static native void exportRemoveEventMediaHandler()/*-{
		$wnd.removeEventMediaFunc = $entry(@de.tum.os.sa.client.views.MainPage::removeEventMediaBtn_OnClick());
	}-*/;

	public static void removeEventMediaBtn_OnClick() {
		Info.display("Yay!", "It works!");
	}

	private void enableEventMediaUpload() {
		disclosureManageEventsAdd_EventMedia.setOpen(true);
	}

	private void disableEventMediaUpload() {
		disclosureManageEventsAdd_EventMedia.setOpen(false);
	}

	private void showDisplayableEventInfo(DisplayableEvent displayableEvent) {
		txtBoxEventDescription.setText(displayableEvent.getDescription());
		txtBoxEventLocation.setText(displayableEvent.getLocation());
		txtBoxEventName.setText(displayableEvent.getName());
		txtBoxEventState.setText(displayableEvent.getState().toString());
		imgEventLogo.setUrl("images/eventLogos/"
				+ displayableEvent.getPictureName());
	}
	// @UiHandler("button")
	// void onClick(ClickEvent e) {
	// Window.alert("Hello!");
	// }

}
