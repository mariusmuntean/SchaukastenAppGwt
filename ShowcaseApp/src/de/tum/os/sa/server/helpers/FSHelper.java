package de.tum.os.sa.server.helpers;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import de.tum.os.sa.shared.MediaTypes;

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

	private static final HashMap<String, MediaTypes> extensionToTypeMapping = new HashMap<String, MediaTypes>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6206021343568823680L;

		{
			// Images
			put("jpg", MediaTypes.image);
			put("jpeg", MediaTypes.image);
			put("png", MediaTypes.image);
			put("gif", MediaTypes.image);

			// Text
			put("txt", MediaTypes.text);
			put("pdf", MediaTypes.pdf);

			// Video
			put("avi", MediaTypes.video);
			put("mpg", MediaTypes.video);
			put("mkv", MediaTypes.video);
			put("mp4", MediaTypes.video);
			put("flv", MediaTypes.video);

			// Audio
			put("mp3", MediaTypes.audio);
			put("ogg", MediaTypes.audio);
			put("aiff", MediaTypes.audio);
			put("flac", MediaTypes.audio);

			// Web
			put("rss", MediaTypes.feed);
			put("html", MediaTypes.html);
		}
	};

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

	public Boolean createEventFolder(String eventName) {
		if (!isEventNameAvailable(eventName)) {
			return false;
		}

		Boolean result = true;
		try {
			String newEventFolderPath = getEventsFolderPath() + File.separator
					+ eventName;
			File newEventFolder = new File(newEventFolderPath);
			result = newEventFolder.mkdir();
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
		File f = new File(getEventsFolderPath() + File.separator + eventName);

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

	/**
	 * Moves a file from a specified location to an Event's folder and renames
	 * the file.
	 * 
	 * @param eventName
	 *            - The name of the event to hold the file.
	 * @param fileLocation
	 *            - The initial location of the file.
	 * @param newFileName
	 *            - The new file name.
	 * @return - True if the operation succeeded, false otherwise.
	 */
	public Boolean moveFileToEvent(String eventName, String fileLocation,
			String newFileName) {
		// Sanity checks
		if (eventName == null || eventName.isEmpty() || fileLocation == null
				|| fileLocation.isEmpty() || newFileName == null
				|| newFileName.isEmpty()) {
			return false;
		}

		// First move the file
		Path source = Paths.get(fileLocation);
		Path destination = Paths.get(getEventsFolderPath() + File.separator
				+ eventName + File.separator + newFileName);
		Boolean operationStatus = true;
		try {
			Files.move(source, destination);
		} catch (Exception ex) {
			operationStatus = false;
		}
		if (!operationStatus) {
			return false;
		}

		// If moving worked then rename the file.
		try {
			Files.move(destination, destination.resolveSibling(newFileName));
		} catch (Exception ex) {
			operationStatus = false;
		}
		if (!operationStatus) {
			return false;
		}

		return operationStatus;
	}

	/**
	 * Returns the best-fitting {@link MediaTypes} for the provided file name.
	 * If no match is found {@link MediaTypes#other} is returned.
	 * @param fileName - File name or file path.
	 * @return - A {@link MediaTypes} describing the file.
	 */
	public MediaTypes getFileTypeFromName(String fileName) {
		String ext = FilenameUtils.getExtension(fileName);

		MediaTypes result = extensionToTypeMapping.get(ext.toLowerCase());
		if (result == null) {
			result = MediaTypes.other;
		}

		return result;
	}
}
