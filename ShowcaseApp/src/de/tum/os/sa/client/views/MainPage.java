package de.tum.os.sa.client.views;

import java.util.regex.Pattern;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

import de.tum.os.sa.client.ShowcaseApp;
import de.tum.os.sa.client.models.DisplayableEvent;
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

	@UiField
	Button btnManageEventNewEvent, btnManageEventUploadFile;

	// FileUpload
	@UiField
	FormPanel formPanelManageEvent;

	@UiField
	FileUpload fileUploadManageEvent;

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

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	ShowcaseApp client;

	String uploadedFileName = "";
	String uploadedTempFilePath = "";

	public MainPage(ShowcaseApp client,
			ListStore<DisplayableEvent> displayableEventsListStore) {
		this.client = client;
		this.displayableEventsListStore = displayableEventsListStore;

		instantiateControls();
		initWidget(uiBinder.createAndBindUi(this));
		wireUpControls();
	}

	private void instantiateControls() {
		// Overview
		lstViewOverviewAvailableEvents = new ListView<DisplayableEvent>();
		lstViewOverviewAvailableEvents
				.setSimpleTemplate(displayableDeviceTemplate);
		lstViewOverviewAvailableEvents.setStore(displayableEventsListStore);

		eventWidget = new EventWidget(displayableEventsListStore);

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
		btnManageEventNewEvent.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AsyncCallback<Boolean> addEventCallback = new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				};

				Event ev = new Event("dummyEvent1",
						"26D917B7-B1F4-4C70-BF80-A2512A7DEC10",
						"some description 1", "Freimann");
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
						String r = "\\Q"+ShowcaseConstants.ResponseDelimiter+"\\E";
						
//						String splitPattern = Pattern
//								.quote(ShowcaseConstants.ResponseDelimiter);
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
							client.addFileToEventProxy("dummyEvent1",
									uploadedTempFilePath, uploadedFileName,
									addFileCallback);
						} else {
							Info.display("Error", "Result: " + result);
							Window.alert("Result: "+result);
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
