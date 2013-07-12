package de.tum.os.sa.client.views;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.tum.os.sa.client.ShowcaseApp;
import de.tum.os.sa.client.models.DisplayableEvent;

public class MainPage extends Composite {

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);

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
					+ "<tr><td>Status: <big>{state}</big></td></tr>" + "</table>");

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	ShowcaseApp client;

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
		lstViewOverviewAvailableEvents.setSimpleTemplate(displayableDeviceTemplate);
		lstViewOverviewAvailableEvents.setStore(displayableEventsListStore);

	}

	private void wireUpControls() {

		deckPanelActualView.showWidget(0);
		lstViewOverviewAvailableEvents.getSelectionModel().addSelectionChangedListener(
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

		displayableEventsListStore.addListener(Store.Add, new Listener<StoreEvent<DisplayableEvent>>() {
			@Override
			public void handleEvent(StoreEvent<DisplayableEvent> be) {
				if(be!=null && be.getModels()!=null && be.getModels().size()>0)
				MainPage.this.showDisplayableEventInfo(be.getModels().get(0));	
			}
		});
		
		btnEventStart.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (lstViewOverviewAvailableEvents.getSelectionModel().getSelectedItem() != null) {
					DisplayableEvent de = lstViewOverviewAvailableEvents
							.getSelectionModel().getSelectedItem();
					AsyncCallback<Boolean> startEventResultCallback = new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								Info.display("Success!", "Event started!");
							} else {
								Info.display("Server error!", "Could not start event!");
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Info.display("Network error!", "Event status is unknown!");
						}
					};

					client.startEvent(de.getID(), startEventResultCallback);
				}

			}
		});

		btnEventStop.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (lstViewOverviewAvailableEvents.getSelectionModel().getSelectedItem() != null) {
					DisplayableEvent de = lstViewOverviewAvailableEvents
							.getSelectionModel().getSelectedItem();
					AsyncCallback<Boolean> stopEventResultCallback = new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								Info.display("Success!", "Event stoped!");
							} else {
								Info.display("Server error!", "Could not stop event!");
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Info.display("Network error!", "Event status is unknown!");
						}
					};

					client.stopEvent(de.getID(), stopEventResultCallback);
				}

			}
		});

	}

	private void showDisplayableEventInfo(DisplayableEvent displayableEvent) {
		txtBoxEventDescription.setText(displayableEvent.getDescription());
		txtBoxEventLocation.setText(displayableEvent.getLocation());
		txtBoxEventName.setText(displayableEvent.getName());
		txtBoxEventState.setText(displayableEvent.getState().toString());
		imgEventLogo.setUrl("images/eventLogos/" + displayableEvent.getPictureName());
	}
	// @UiHandler("button")
	// void onClick(ClickEvent e) {
	// Window.alert("Hello!");
	// }

}
