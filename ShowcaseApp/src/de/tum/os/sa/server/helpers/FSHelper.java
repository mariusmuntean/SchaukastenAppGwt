package de.tum.os.sa.server.helpers;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * 
 * Helper class for operations with the File System.
 * 
 * 
 * @author marius
 * 
 */
public class FSHelper {

	private String dataFolderPath = "";

	private static final String dataFolderName = "Showcasedata";
	private static final String eventsFolderName = "Events";
	private static final String otherFolderName = "Other";

	public FSHelper() {

		dataFolderPath = System.getProperty("user.home") + File.separator
				+ dataFolderName;

	}

	/**
	 * <br>
	 * Creates the folder structure needed to store application files.</br>
	 * 
	 * @param preserve
	 *            - Indicates whether to preserve an existing folder
	 *            structure(true) or to create an empty structure(false).
	 * @return - true if creation succeeded, false otherwise.
	 */
	public Boolean createFolderStructure(Boolean preserve) {
		Boolean result = true;
		try {
			// Create main folder
			File dataFolder = new File(dataFolderPath);
			if (dataFolder.exists()) {
				if (preserve == false) {
					FileUtils.deleteDirectory(dataFolder);
					result = dataFolder.mkdir();
				}
			} else {
				result = dataFolder.mkdir();
			}

			// Create Events folder
			File eventsFolder = new File(getEventsFolderPath());
			if (eventsFolder.exists()) {
				if (preserve == false) {
					FileUtils.deleteDirectory(eventsFolder);
					result = eventsFolder.mkdir();
				}
			} else {
				result = eventsFolder.mkdir();
			}

			// Create Other folder
			File otherFolder = new File(getOtherFolderPath());
			if (otherFolder.exists()) {
				if (preserve == false) {
					FileUtils.deleteDirectory(otherFolder);
					result = otherFolder.mkdir();
				}
			} else {
				result = otherFolder.mkdir();
			}
		} catch (Exception ex) {
			result = false;
		}

		return result;
	}

	public String getEventsFolderName() {
		return eventsFolderName;
	}

	public String getEventsFolderPath() {
		return dataFolderPath + File.separator + eventsFolderName;
	}

	public String getOtherFolderName() {
		return otherFolderName;
	}

	public String getOtherFolderPath() {
		return dataFolderPath + File.separator + otherFolderName;
	}

	/**
	 * @param eventName
	 *            - The event name to check for.
	 * @return - True if no folder with the provided name exists in the Events
	 *         folder.
	 */
	public Boolean isEventNameAvailable(String eventName) {
		File f = new File(getEventsFolderPath() + File.separator
				+ eventName);
		
		if (!f.exists()) {
			return true;
		} else {
			if (f.isFile()) {
				return true;
			} else {
				return false;
			}
		}
	}
}
