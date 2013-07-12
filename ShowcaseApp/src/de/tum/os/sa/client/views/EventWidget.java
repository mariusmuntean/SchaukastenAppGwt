package de.tum.os.sa.client.views;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import de.tum.os.sa.client.models.DisplayableEvent;

public class EventWidget extends Composite {

	private static EventWidgetUiBinder uiBinder = GWT.create(EventWidgetUiBinder.class);

	interface EventWidgetUiBinder extends UiBinder<Widget, EventWidget> {
	}

	// UI
	
	@UiField(provided = true)
	ListView<DisplayableEvent> lstViewEventCovers;
	
	// Logic
	StringBuilder sbEventCoverTemplate = new StringBuilder();
	ListStore<DisplayableEvent> displayableEventsListStore;

	public EventWidget(ListStore<DisplayableEvent> displayableEventsListStore) {
		this.displayableEventsListStore = displayableEventsListStore;
		
		instantiateControls();
		initWidget(uiBinder.createAndBindUi(this));
		wireUpControls();
	}

	private void instantiateControls() {
		// Templates
		sbEventCoverTemplate.append("<table>\n");
		sbEventCoverTemplate.append("<tr>\n");
		sbEventCoverTemplate.append("<td><img src=\"images/eventLogos/{picture}\" width=\"80\" height=\"80\"></td>\n");
		sbEventCoverTemplate.append("</tr>\n");
		sbEventCoverTemplate.append("<tr>\n");
		sbEventCoverTemplate.append("<td><p style=\"font-size:20px\"><b>{name}</b></p></td>\n");
		sbEventCoverTemplate.append("</tr>\n");
		sbEventCoverTemplate.append("</table>\n");
		
		// Event cover
		lstViewEventCovers = new ListView<DisplayableEvent>(displayableEventsListStore);
		lstViewEventCovers.setSimpleTemplate(sbEventCoverTemplate.toString());

	}
	
	public void displayEvent(DisplayableEvent displayableEvent){
		
	}

	private void wireUpControls() {
		// TODO Auto-generated method stub

	}

	// @UiHandler("button")
	// void onClick(ClickEvent e) {
	// Window.alert("Hello!");
	// }

}
