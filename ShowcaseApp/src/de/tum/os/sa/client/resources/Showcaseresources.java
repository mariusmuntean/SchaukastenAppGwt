package de.tum.os.sa.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Showcaseresources extends ClientBundle {

	public static final Showcaseresources INSTANCE = GWT.create(Showcaseresources.class);
	
	
	ImageResource genericEvent();


	ImageResource genericEventLogo();


	@Source("de/tum/os/sa/client/views/res/EventMediaIcon.png")
	ImageResource eventMediaIcon();


	@Source("de/tum/os/sa/client/resources/icons/_blank.png")
	ImageResource blank();


	@Source("de/tum/os/sa/client/resources/icons/_page.png")
	ImageResource page();


	@Source("de/tum/os/sa/client/resources/icons/aac.png")
	ImageResource aac();


	@Source("de/tum/os/sa/client/resources/icons/avi.png")
	ImageResource avi();


	@Source("de/tum/os/sa/client/resources/icons/bmp.png")
	ImageResource bmp();


	@Source("de/tum/os/sa/client/resources/icons/flv.png")
	ImageResource flv();


	@Source("de/tum/os/sa/client/resources/icons/gif.png")
	ImageResource gif();


	@Source("de/tum/os/sa/client/resources/icons/html.png")
	ImageResource html();


	@Source("de/tum/os/sa/client/resources/icons/jpg.png")
	ImageResource jpg();


	@Source("de/tum/os/sa/client/resources/icons/mp3.png")
	ImageResource mp3();


	@Source("de/tum/os/sa/client/resources/icons/mp4.png")
	ImageResource mp4();


	@Source("de/tum/os/sa/client/resources/icons/mpg.png")
	ImageResource mpg();


	@Source("de/tum/os/sa/client/resources/icons/pdf.png")
	ImageResource pdf();


	@Source("de/tum/os/sa/client/resources/icons/png.png")
	ImageResource png();


	@Source("de/tum/os/sa/client/resources/icons/txt.png")
	ImageResource txt();


	@Source("de/tum/os/sa/client/resources/icons/wav.png")
	ImageResource wav();

}
