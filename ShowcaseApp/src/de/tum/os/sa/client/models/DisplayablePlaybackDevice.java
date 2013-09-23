package de.tum.os.sa.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.resources.client.ImageResource;

import de.tum.os.sa.client.resources.Showcaseresources;
import de.tum.os.sa.shared.DeviceType;
import de.tum.os.sa.shared.DTO.PlaybackDevice;

/**
 * 
 * A surrogate class for {@link PlaybackDevice}. Used in the client.
 * 
 * @author Marius
 * 
 */
public class DisplayablePlaybackDevice extends BaseModelData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7044488175269528936L;

	private String deviceId = "deviceID";
	private String deviceIp = "deviceIP";
	private String screenSize = "screenSize";
	private String isAvailable = "isAvailable";
	private String deviceName = "deviceName";
	private String deviceType = "deviceType";
	private String parentEventID = "parentID";
	private String deviceImage = "deviceImage";

	/**
	 * Empty-/Parameterless constructor for serialization
	 */
	public DisplayablePlaybackDevice() {

	}

	/**
	 * 
	 * @param name
	 *            - Name of the device
	 * @param id
	 *            - A unique ID for the device.
	 * @param screenSize
	 *            - A screen size in inches.
	 */
	public DisplayablePlaybackDevice(String name, String id, Float screenSize) {
		setDeviceAvailability(true);
		setDeviceName(name);
		setdeviceId(id);
		setScreenSize(screenSize);
	}

	public DisplayablePlaybackDevice(String name, String id, Float screenSize,
			DeviceType deviceType) {
		setDeviceAvailability(true);
		setDeviceName(name);
		setdeviceId(id);
		setScreenSize(screenSize);
		setDeviceType(deviceType);
	}

	public void setDeviceImage(String newDeviceImage) {
		set(deviceImage, newDeviceImage);
	}

	public String getDeviceImage() {
		return get(deviceImage);
	}

	public void setParentId(String newParentId) {
		set(parentEventID, newParentId);
	}

	public String getParentId() {
		return get(parentEventID);
	}

	public void setDeviceType(DeviceType newDeviceType) {
		set(deviceType, newDeviceType);
	}

	public DeviceType getDeviceType() {
		return get(deviceType);
	}

	public void setDeviceName(String newDeviceName) {
		set(deviceName, newDeviceName);
		ImageResource imgRes = Showcaseresources.deviceNameToImageMap
				.get(newDeviceName.toLowerCase().trim());
		String imgName = "";
		if (imgRes != null) {
			imgName = imgRes.getURL();
		} else {
			imgName = Showcaseresources.deviceNameToImageMap.get("").getURL();
		}

		set(deviceImage, imgName);

	}

	public String getDeviceName() {
		return get(deviceName);
	}

	public void setDeviceAvailability(Boolean newAvailability) {
		set(isAvailable, newAvailability);
	}

	public String getDeviceAvailability() {
		return get(isAvailable);
	}

	public void setdeviceId(String newDeviceId) {
		set(deviceId, newDeviceId);
	}

	public String getDeviceId() {
		return get(deviceId);
	}

	public void setdeviceIp(String newDeviceIp) {
		set(deviceIp, newDeviceIp);
	}

	public String getDeviceIp() {
		return get(deviceIp);
	}

	public void setScreenSize(Float newScreenSize) {
		set(screenSize, newScreenSize);
	}

	public Float getScreenSize() {
		return get(screenSize);
	}
}
