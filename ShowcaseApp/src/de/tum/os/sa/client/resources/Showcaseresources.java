package de.tum.os.sa.client.resources;

import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Showcaseresources extends ClientBundle {

	public static final Showcaseresources INSTANCE = GWT.create(Showcaseresources.class);
	public static final HashMap<String, ImageResource> deviceNameToImageMap = new HashMap<String, ImageResource>(){
		{
			put("android question", INSTANCE.androidQuestion());
			put("android question.jpg", INSTANCE.androidQuestion());
			put("", INSTANCE.androidQuestion());
			put("galaxy nexus", INSTANCE.galaxyNexus());
			put("galaxy nexus.jpg", INSTANCE.galaxyNexus());
			put("htc one x", INSTANCE.htcOneX());
			put("htc one x.jpg", INSTANCE.htcOneX());
			put("htc one x+", INSTANCE.htcOneXPlus());
			put("htc one x+.jpg", INSTANCE.htcOneXPlus());
			put("htc one", INSTANCE.htcOne());
			put("htc one.jpg", INSTANCE.htcOne());
			put("nexus 10", INSTANCE.nexus10());
			put("nexus 10.jpg", INSTANCE.nexus10());
			put("nexus 4", INSTANCE.nexus4());
			put("nexus 4.jpg", INSTANCE.nexus4());
			put("nexus 7", INSTANCE.nexus7());
			put("nexus 7.jpg", INSTANCE.nexus7());
			put("nexus one", INSTANCE.nexusOne());
			put("nexus one.jpg", INSTANCE.nexusOne());
			put("nexus s", INSTANCE.nexusS());
			put("nexus s.jpg", INSTANCE.nexusS());
		}
	};
	
	
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


	@Source("button_delete_01.png")
	ImageResource deleteIcon();


	@Source("button_delete_01_small.png")
	ImageResource deleteIconSmall();


	@Source("de/tum/os/sa/client/resources/devices/android question.jpg")
	ImageResource androidQuestion();


	@Source("de/tum/os/sa/client/resources/devices/galaxy nexus.jpg")
	ImageResource galaxyNexus();


	@Source("de/tum/os/sa/client/resources/devices/htc one x.jpg")
	ImageResource htcOneX();


	@Source("de/tum/os/sa/client/resources/devices/htc one x+.jpg")
	ImageResource htcOneXPlus();


	@Source("de/tum/os/sa/client/resources/devices/htc one.jpg")
	ImageResource htcOne();


	@Source("de/tum/os/sa/client/resources/devices/nexus 10.jpg")
	ImageResource nexus10();


	@Source("de/tum/os/sa/client/resources/devices/nexus 4.jpg")
	ImageResource nexus4();


	@Source("de/tum/os/sa/client/resources/devices/nexus 7.jpg")
	ImageResource nexus7();


	@Source("de/tum/os/sa/client/resources/devices/nexus one.jpg")
	ImageResource nexusOne();


	@Source("de/tum/os/sa/client/resources/devices/nexus s.jpg")
	ImageResource nexusS();


	ImageResource androidLogoSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/_blankSmall.png")
	ImageResource _blankSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/_pageSmall.png")
	ImageResource _pageSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/aacSmall.png")
	ImageResource aacSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/aviSmall.png")
	ImageResource aviSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/bmpSmall.png")
	ImageResource bmpSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/flvSmall.png")
	ImageResource flvSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/gifSmall.png")
	ImageResource gifSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/htmlSmall.png")
	ImageResource htmlSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/jpgSmall.png")
	ImageResource jpgSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/mp3Small.png")
	ImageResource mp3Small();


	@Source("de/tum/os/sa/client/resources/icons/Small/mp4Small.png")
	ImageResource mp4Small();


	@Source("de/tum/os/sa/client/resources/icons/Small/mpgSmall.png")
	ImageResource mpgSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/pdfSmall.png")
	ImageResource pdfSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/pngSmall.png")
	ImageResource pngSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/txtSmall.png")
	ImageResource txtSmall();


	@Source("de/tum/os/sa/client/resources/icons/Small/wavSmall.png")
	ImageResource wavSmall();


}
