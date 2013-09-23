package de.tum.os.sa.client.helpers;

import java.util.Comparator;

import de.tum.os.sa.shared.DTO.PlaybackDevice;

public class PlaybackDeviceComparator implements Comparator<PlaybackDevice> {

	@Override
	public int compare(PlaybackDevice arg0, PlaybackDevice arg1) {
		if (arg0 == null && arg1 == null) {
			return 0;
		}

		if (arg0 != null && arg1 == null) {
			return 1;
		}

		if (arg0 == null && arg1 != null) {
			return -1;
		}

		// if(arg0 != null && arg1 != null)
		if (arg0.getDeviceName().compareTo(arg1.getDeviceName()) != 0) {
			return arg0.getDeviceName().compareTo(arg1.getDeviceName());
		} else {
			return arg0.getDeviceId().compareTo(arg1.getDeviceId());
		}
	}
}
