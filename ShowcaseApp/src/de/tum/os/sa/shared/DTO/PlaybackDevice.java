package de.tum.os.sa.shared.DTO;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.tum.os.sa.shared.DeviceType;

/**
 * DTO representing a device that can participate in an Event and play back media.
 * 
 * @author Marius
 * 
 */
public class PlaybackDevice implements IsSerializable, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2109990224769622746L;
	private String deviceId;
	private String deviceIp;
	private Float screenSize;
	private boolean isAvailable;
	private String deviceName;
//	private DeviceType deviceType;
	private Event parentEvent;

	public PlaybackDevice(){
		
	}
	
	/**
	 * Creates a new instance of PlaybackDevice
	 * 
	 * @param deviceId
	 *            - a unique device identified.
	 * @param screenSize
	 *            - the screen size in inches. May be considered when assigned to an event.
	 */
	public PlaybackDevice(String deviceId, String deviceName, DeviceType deviceType,
			Float screenSize) {
		this.deviceId = deviceId;
		this.screenSize = screenSize;
//		this.deviceType = deviceType;
		this.deviceName = deviceName;

		this.isAvailable = true;
	}
	


	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public Event getParentEvent() {
		return parentEvent;
	}

	public void setParentEvent(Event parentEvent) {
		this.parentEvent = parentEvent;
	}

	public DeviceType getDeviceType() {
//		return deviceType;
		return null;
	}

	public void setDeviceType(DeviceType deviceType) {
//		this.deviceType = deviceType;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the screenSize
	 */
	public Float getScreenSize() {
		return screenSize;
	}

	/**
	 * @param screenSize
	 *            the screenSize to set
	 */
	public void setScreenSize(Float screenSize) {
		this.screenSize = screenSize;
	}

	/**
	 * @return the isAvailable
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * @param isAvailable
	 *            the isAvailable to set
	 */
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PlaybackDevice, name:"+this.getDeviceName()+" ID: "+this.getDeviceId();
	}
	
	
}
