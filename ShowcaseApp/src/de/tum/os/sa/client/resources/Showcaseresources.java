package de.tum.os.sa.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Showcaseresources extends ClientBundle {

	public static final Showcaseresources INSTANCE = GWT.create(Showcaseresources.class);
	
	
	ImageResource genericEvent();


	ImageResource genericEventLogo();

}
