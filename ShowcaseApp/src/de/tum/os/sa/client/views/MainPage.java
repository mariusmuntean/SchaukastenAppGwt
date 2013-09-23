package de.tum.os.sa.client.views;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.DND.Operation;
import com.extjs.gxt.ui.client.dnd.StatusProxy;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
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
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.TreeStoreEvent;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.Joint;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelView;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelView.TreeViewRenderMode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.PopupPanel;

import de.tum.os.sa.client.ShowcaseApp;
import de.tum.os.sa.client.helpers.UUID;
import de.tum.os.sa.client.models.DisplayableEvent;
import de.tum.os.sa.client.models.DisplayableMedia;
import de.tum.os.sa.client.models.DisplayablePlaybackDevice;
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
	Image imgManageEventAdd_EvLogo;

	@UiField
	FormPanel formPanelManageEvent_evLogo;

	@UiField
	FileUpload fileUploadManageEvent_EvLogo;

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
	VerticalPanel vPanelManageEventsAdd_LayoutPanel;

	@UiField
	FormPanel formPanelManageEvent;

	@UiField
	FileUpload fileUploadManageEvent;

	@UiField
	DisclosurePanel disclosureManageEventsAdd_DevicesAndMedia;

	@UiField(provided = true)
	ListView<DisplayableMedia> lstViewEventMedia;

	private ListStore<DisplayableMedia> currentEventdisplayableMediaListStore = new ListStore<DisplayableMedia>();
	private ListStore<DisplayableMedia> currentEventdisplayableMediaRemovedListStore = new ListStore<DisplayableMedia>();

	@UiField(provided = true)
	ListView<DisplayablePlaybackDevice> lstViewEventDevices_Available;
	// Devices
	ListStore<DisplayablePlaybackDevice> currentEventDisplayablePlaybackDevicesListStore = new ListStore<DisplayablePlaybackDevice>();
	ListStore<DisplayablePlaybackDevice> availableDisplayablePlaybackDevicesListStore = new ListStore<DisplayablePlaybackDevice>();
	ListStore<DisplayablePlaybackDevice> allDisplayablePlaybackDevicesListStore = new ListStore<DisplayablePlaybackDevice>();

	TreeStore<BaseModelData> manageEventsAdd_MappingStore;
	@UiField(provided = true)
	TreePanel<BaseModelData> treePManageEventsAdd_MediaMapping;

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

	final String displayablePlabackDevicesTemplate = new String("<table>"
			+ "<tr>" + "<td><img src=\"{deviceImage}\" height=\"100\"></td>"
			+ "<td><font size=\"5\">{deviceName}</font></td></tr>" + "</table>");

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	ShowcaseApp client;

	String uploadedFileName = "";
	String uploadedTempFilePath = "";
	UUID uuidGen = new UUID();

	public MainPage(
			ShowcaseApp client,
			ListStore<DisplayableEvent> displayableEventsListStore,
			ListStore<DisplayableMedia> currentEventdisplayableMediaListStore,
			ListStore<DisplayablePlaybackDevice> allDisplayablePlaybackDevicesListStore,
			ListStore<DisplayablePlaybackDevice> availableDisplayablePlaybackDevicesListStore,
			ListStore<DisplayablePlaybackDevice> currentEventDisplayablePlaybackDevicesListStore,
			TreeStore<BaseModelData> manageEventsAdd_MediaMapping) {
		this.client = client;
		this.displayableEventsListStore = displayableEventsListStore;
		this.currentEventdisplayableMediaListStore = currentEventdisplayableMediaListStore;
		this.allDisplayablePlaybackDevicesListStore = allDisplayablePlaybackDevicesListStore;
		this.availableDisplayablePlaybackDevicesListStore = availableDisplayablePlaybackDevicesListStore;
		this.currentEventDisplayablePlaybackDevicesListStore = currentEventDisplayablePlaybackDevicesListStore;
		this.manageEventsAdd_MappingStore = manageEventsAdd_MediaMapping;

		exportJsMethods();
		instantiateControls();
		initWidget(uiBinder.createAndBindUi(this));
		wireUpControls();
	}

	private void exportJsMethods() {
		exportRemoveEventMediaHandler();

	}

	private void instantiateControls() {
		/*
		 * Overview
		 */
		lstViewOverviewAvailableEvents = new ListView<DisplayableEvent>();
		lstViewOverviewAvailableEvents
				.setSimpleTemplate(displayableDeviceTemplate);
		lstViewOverviewAvailableEvents.setStore(displayableEventsListStore);

		eventWidget = new EventWidget(displayableEventsListStore);

		/*
		 * Manage Events
		 */
		lstViewEventMedia = new ListView<DisplayableMedia>();
		lstViewEventMedia.setDisplayProperty("name");
		lstViewEventMedia.setSimpleTemplate(displayableMediaTemplate);
		lstViewEventMedia.setStore(currentEventdisplayableMediaListStore);

		lstViewEventDevices_Available = new ListView<DisplayablePlaybackDevice>();
		lstViewEventDevices_Available
				.setStore(availableDisplayablePlaybackDevicesListStore);
		lstViewEventDevices_Available
				.setSimpleTemplate(displayablePlabackDevicesTemplate);
		// to fix dragging only from whitespace use "setItemStelector"

		// Media to device mapping
		treePManageEventsAdd_MediaMapping = new TreePanel<BaseModelData>(
				manageEventsAdd_MappingStore);
		treePManageEventsAdd_MediaMapping.setAutoExpand(true);

	}

	private void generateMediaToDeviceMappingControls() {
		if (vPanelManageEventsAdd_LayoutPanel == null) {
			return;
		}

		// Generate and insert grid w/ two columns
		Grid gridMappingLayout = new Grid(1, 2);
		vPanelManageEventsAdd_LayoutPanel.add(gridMappingLayout);

		// Column 1
		ListView<DisplayableMedia> lstViewCurrentEventMedia = new ListView<DisplayableMedia>(
				currentEventdisplayableMediaListStore);
		lstViewCurrentEventMedia.setSimpleTemplate(displayableMediaTemplate);
		// Enable drag and drop. Dropping results in the element being copied
		// and NOT moved.
		ListViewDragSource ds1 = new ListViewDragSource(
				lstViewCurrentEventMedia) {
			@Override
			protected void onDragDrop(DNDEvent e) {
				e.setOperation(Operation.COPY);
				super.onDragDrop(e);
			}
		};
		gridMappingLayout.setWidget(0, 0, lstViewCurrentEventMedia);

		// Column 2
		int rowCount = currentEventDisplayablePlaybackDevicesListStore
				.getModels().size();
		List<DisplayablePlaybackDevice> deviceList = currentEventDisplayablePlaybackDevicesListStore
				.getModels();
		Grid gridDeviceMap = new Grid(rowCount, 2);
		for (int i = 0; i < rowCount; i++) {
			// Col 1
			VerticalPanel vPanelDevice = new VerticalPanel();
			Image imgDeviceImage = new Image(deviceList.get(i).getDeviceImage());
			imgDeviceImage.setHeight("100px");
			vPanelDevice.add(imgDeviceImage);
			Label lblDeviceName = new Label(deviceList.get(i).getDeviceName());
			vPanelDevice.add(lblDeviceName);
			gridDeviceMap.setWidget(i, 0, vPanelDevice);

			// Col 2
			ListStore<DisplayableMedia> rowLstStore = new ListStore<DisplayableMedia>();
			ListView<DisplayableMedia> rowListView = new ListView<DisplayableMedia>();
			rowListView.setWidth("100%");
			rowListView.setHeight("100px");
			rowListView.setSimpleTemplate(displayableMediaTemplate);
			new ListViewDropTarget(rowListView);
			rowListView.setStore(rowLstStore);
			gridDeviceMap.setWidget(i, 1, rowListView);
		}

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
		formPanelManageEvent_evLogo.setAction(GWT.getModuleBaseURL()
				+ "showcaseService");
		formPanelManageEvent_evLogo.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanelManageEvent_evLogo.setMethod(FormPanel.METHOD_POST);
		formPanelManageEvent_evLogo
				.addSubmitCompleteHandler(new SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						String result = event.getResults();
						result = result.replace("<pre>", "");
						result = result.replace("</pre>", "");
						String r = "\\Q" + ShowcaseConstants.ResponseDelimiter
								+ "\\E";

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
										Info.display(
												"Success",
												"Moved file to: "
														+ manageEvAdd_CurrentEventName);
										// Set picture as eventLogo
										AsyncCallback<String> setAndReturnEventLogoCallback = new AsyncCallback<String>() {
											@Override
											public void onFailure(
													Throwable caught) {
												Info.display("Network Error",
														"Couldn't set event Logo");
											}

											@Override
											public void onSuccess(String result) {
												if (result
														.equals(ShowcaseConstants.ActionFailedMessage)) {
													Info.display("Error",
															"Couldn't set event logo");
												} else {
													String styleName = imgManageEventAdd_EvLogo.getStyleName();
													imgManageEventAdd_EvLogo.setUrl(result);
													imgManageEventAdd_EvLogo.setStyleName(styleName);
												}
											}

										};
										client.setAndReturnEventLogoProxy(
												manageEvAdd_CurrentEventID,
												uploadedFileName,
												setAndReturnEventLogoCallback);

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

		imgManageEventAdd_EvLogo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Thanks to Thomas Broyer fo the following two lines
				fileUploadManageEvent_EvLogo.getElement().<InputElement> cast()
						.setAttribute("accept", "image/*");
				fileUploadManageEvent_EvLogo.getElement().<InputElement> cast()
						.click();
			}
		});
		fileUploadManageEvent_EvLogo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (!fileUploadManageEvent_EvLogo.getFilename()
						.equalsIgnoreCase("")) {
					formPanelManageEvent_evLogo.submit();
				}

			}
		});

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
							enableEventMediaAndDevices();
						} else {
							Info.display("Error!", " Could not create: "
									+ manageEvAdd_CurrentEventName);
							disableEventMediaAndDevices();
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						Info.display("Network Error!",
								"Could not communicate with server.");
						disableEventMediaAndDevices();

					}
				};

				Event ev = new Event(manageEvAdd_CurrentEventName,
						manageEvAdd_CurrentEventID,
						manageEvAdd_CurrentEventDesc,
						manageEvAdd_CurrentEventLocation);

				client.addEventProxy(ev, addEventCallback);
				client.fetchAvailableDevices();

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

		ListViewDragSource lvds_devices = new ListViewDragSource(
				lstViewEventDevices_Available) {
			@Override
			protected void onDragStart(DNDEvent e) {
				e.setOperation(Operation.MOVE);
				super.onDragStart(e);
			}

			@Override
			protected void onDragDrop(DNDEvent e) {
				e.setOperation(Operation.MOVE);
				super.onDragDrop(e);
			}
		};

		ListViewDragSource lvds_media = new ListViewDragSource(
				lstViewEventMedia) {

			@Override
			protected void onDragStart(DNDEvent e) {
				// e.setCancelled(true);
				// e.getStatus().setStatus(false);
				e.setOperation(Operation.COPY);
				super.onDragStart(e);
			}
		};

		// Media to Device mapping
		treePManageEventsAdd_MediaMapping.getStore().addStoreListener(
				new StoreListener<BaseModelData>() {

					@Override
					public void storeAdd(StoreEvent<BaseModelData> se) {
						// super.storeAdd(se);
						if (se instanceof TreeStoreEvent
								&& ((TreeStoreEvent) se).getChildren() != null) {
							BaseModelData child0 = (BaseModelData) ((TreeStoreEvent) se)
									.getChildren().get(0);
							if (child0 != null
									&& child0 instanceof DisplayablePlaybackDevice) {
								treePManageEventsAdd_MediaMapping.setLeaf(
										child0, false);
							}
							if (child0 != null
									&& child0 instanceof DisplayableMedia) {
								treePManageEventsAdd_MediaMapping.setLeaf(
										child0, true);
							}
						}
					}

				});

		TreePanelDropTarget tpdt = new TreePanelDropTarget(
				treePManageEventsAdd_MediaMapping) {

			@Override
			protected void handleAppendDrop(DNDEvent event, TreeNode item) {
				// If DisplayablePlaybackDevices are dropped MOVE them, if Media
				// is dropped COPY it.
				ArrayList dataAsList = event.getData();
				if (dataAsList.size() > 0) {
					BaseModelData bmd = (BaseModelData) dataAsList.get(0);
					if (bmd instanceof DisplayablePlaybackDevice) {
						// event.setOperation(Operation.MOVE);
						// treePManageEventsAdd_MediaMapping.getStore().add(bmd,
						// false);
						// treePManageEventsAdd_MediaMapping.setLeaf(
						// (BaseModelData) dataAsList.get(0), false);
						addPlaybackDeviceToEvent(manageEvAdd_CurrentEventID,
								(DisplayablePlaybackDevice) bmd);

					}
					if (bmd instanceof DisplayableMedia && item != null) {
						// event.setOperation(Operation.COPY);
						DisplayableMedia oldDm = (DisplayableMedia) bmd;
						mapMediaToDevice(
								(DisplayablePlaybackDevice) item.getModel(),
								oldDm);
					}
				}
			}
		};
		tpdt.setOperation(Operation.COPY);
		// tpdt.setFeedback(Feedback.INSERT);

		// Add context menu to media mapping tree
		Menu mediaMappingTreeContextMenu = new Menu();
		mediaMappingTreeContextMenu.setWidth(100);
		mediaMappingTreeContextMenu.setHeight(40);
		MenuItem removeSelectedItem_MenuItem = new MenuItem();
		removeSelectedItem_MenuItem.setText("Unmap media");
		removeSelectedItem_MenuItem.setIcon(AbstractImagePrototype
				.create(Showcaseresources.INSTANCE.deleteIconSmall()));
		removeSelectedItem_MenuItem
				.addSelectionListener(new SelectionListener<MenuEvent>() {
					public void componentSelected(MenuEvent ce) {

						BaseModelData selectedItem = treePManageEventsAdd_MediaMapping
								.getSelectionModel().getSelectedItem();
						if (selectedItem != null) {
							if (selectedItem instanceof DisplayablePlaybackDevice) {
								final DisplayablePlaybackDevice selectedDevice = (DisplayablePlaybackDevice) selectedItem;
								AsyncCallback<Boolean> removeDevicecallback = new AsyncCallback<Boolean>() {

									@Override
									public void onSuccess(Boolean result) {
										if (result) {
											Info.display(
													"Success!",
													"Removed "
															+ selectedDevice
																	.getDeviceName());
										} else {
											Info.display(
													"Error!",
													"Couldn'd remove "
															+ selectedDevice
																	.getDeviceName());
										}

									}

									@Override
									public void onFailure(Throwable caught) {
										Info.display("Network Error!",
												"Couldn'd communicate with the server.");

									}
								};
								client.removeDeviceFromEvent(
										manageEvAdd_CurrentEventID,
										selectedDevice, removeDevicecallback);

							}
							if (selectedItem instanceof DisplayableMedia) {
								final DisplayableMedia selectedMedia = (DisplayableMedia) selectedItem;
								BaseModelData selectionParent = treePManageEventsAdd_MediaMapping
										.getStore().getParent(selectedMedia);
								if (selectionParent != null
										&& selectionParent instanceof DisplayablePlaybackDevice) {
									AsyncCallback<Boolean> unmapMediaCallback = new AsyncCallback<Boolean>() {
										@Override
										public void onSuccess(Boolean result) {
											if (result) {
												Info.display(
														"Success!",
														"Unmapped "
																+ selectedMedia
																		.getName());
											} else {
												Info.display(
														"Error!",
														"Couldn'd remove "
																+ selectedMedia
																		.getName());
											}
										}

										@Override
										public void onFailure(Throwable caught) {
											Info.display("Network Error!",
													"Couldn'd communicate with the server.");
										}
									};
									client.unmapMediaFromDeviceInEvent(
											manageEvAdd_CurrentEventID,
											((DisplayablePlaybackDevice) selectionParent)
													.getDeviceId(),
											selectedMedia.getID(),
											unmapMediaCallback);
								}

							}
						}
					};
				});
		mediaMappingTreeContextMenu.add(removeSelectedItem_MenuItem);
		treePManageEventsAdd_MediaMapping
				.setContextMenu(mediaMappingTreeContextMenu);

		treePManageEventsAdd_MediaMapping
				.setIconProvider(new ModelIconProvider<BaseModelData>() {

					@Override
					public AbstractImagePrototype getIcon(BaseModelData model) {
						if (model != null
								&& model instanceof DisplayablePlaybackDevice) {
							return AbstractImagePrototype
									.create(Showcaseresources.INSTANCE
											.androidLogoSmall());
						}
						if (model != null && model instanceof DisplayableMedia) {
							return AbstractImagePrototype
									.create(((DisplayableMedia) model)
											.getImageSmall());
						}
						return null;
					}
				});

		treePManageEventsAdd_MediaMapping
				.setLabelProvider(new ModelStringProvider<BaseModelData>() {

					@Override
					public String getStringValue(BaseModelData model,
							String property) {
						if (model instanceof DisplayablePlaybackDevice) {
							return "<b>"
									+ ((DisplayablePlaybackDevice) model)
											.getDeviceName()
									+ "</b> - <i>"
									+ ((DisplayablePlaybackDevice) model)
											.getDeviceId() + "</i>";

						}
						if (model instanceof DisplayableMedia) {
							return ((DisplayableMedia) model).getName();
						}
						return null;
					}
				});
		// treePManageEventsAdd_MediaMapping.getStyle().setNodeCloseIcon(AbstractImagePrototype.create(Showcaseresources.INSTANCE.deleteIcon()));

	}

	/**
	 * 
	 * @param eventID
	 * @param playbackDevice
	 */
	protected void addPlaybackDeviceToEvent(String eventID,
			final DisplayablePlaybackDevice playbackDevice) {
		// Sanitize inputs
		if (eventID == null || eventID.isEmpty() || playbackDevice == null) {
			return;
		}

		AsyncCallback<Boolean> addDeviceToEventCallback = new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				client.fetchEventMapping(manageEvAdd_CurrentEventID);
				if (result) {
					Info.display("Success",
							"Added " + playbackDevice.getDeviceName() + " to "
									+ manageEvAdd_CurrentEventName);
				} else {
					Info.display("Error!",
							"Error adding device to current event!");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Network error!", "Network problems!");
			}
		};

		client.addDeviceToEventProxy(manageEvAdd_CurrentEventID,
				playbackDevice, addDeviceToEventCallback);

	}

	protected void mapMediaToDevice(DisplayablePlaybackDevice device,
			DisplayableMedia media) {
		// Sanitize inputs
		if (device == null || media == null) {
			return;
		}
		AsyncCallback<Boolean> mapMediaToDeviceInEventCallback = new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					Info.display("Success!", "Media mapping was successful.");
				} else {
					Info.display("Error", "Couldn't map media to device!");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Network error!", "Network troubles!");
			}
		};

		client.mapMediaToDeviceInEventProxy(manageEvAdd_CurrentEventID,
				device.getDeviceId(), media.getID(),
				mapMediaToDeviceInEventCallback);
	}

	public static native void exportRemoveEventMediaHandler()/*-{
		$wnd.removeEventMediaFunc = $entry(@de.tum.os.sa.client.views.MainPage::removeEventMediaBtn_OnClick());
	}-*/;

	public static void removeEventMediaBtn_OnClick() {
		Info.display("Yay!", "It works!");
	}

	private void enableEventMediaAndDevices() {
		disclosureManageEventsAdd_DevicesAndMedia.setOpen(true);
		// disclosureManageEventsAdd_EventDevices.setOpen(true);
	}

	private void disableEventMediaAndDevices() {
		disclosureManageEventsAdd_DevicesAndMedia.setOpen(false);
		// disclosureManageEventsAdd_EventDevices.setOpen(false);
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
