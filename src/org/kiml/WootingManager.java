package org.kiml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;

import processing.core.PApplet;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package
 * 'template' to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder
 * 'examples' will automatically include the example in the javadoc.)
 *
 * @author Shinhoo Park
 *
 */

public class WootingManager {
	private boolean Debug = false;
	// myParent is a reference to the parent sketch
	public final static String VERSION = "1.0.0";

	private PApplet myParent;
	private final int BUFSIZE = 2048;

	private Vector<HIDDevice> hidDevices = new Vector<HIDDevice>();
	private Vector<HIDDeviceInfo> hidDeviceInfos = new Vector<HIDDeviceInfo>();
	private HashMap<HIDDevice, Integer> hidDeviceIDHashMap = new HashMap<HIDDevice, Integer>();
	private HashMap<HIDDevice, byte[]> deviceBufferHashMap = new HashMap<HIDDevice, byte[]>();

	private WootingReadManager readManager;

	private HashMap<KeyValue, Boolean> keyMap = new HashMap<KeyValue, Boolean>(); // Default is "All"

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @param theParent
	 */
	public WootingManager(PApplet myParent) {
		this.myParent = myParent;
		ClassPathLibraryLoader.loadNativeHIDLibrary();
		getWootingDevices();

		myParent.registerMethod("dispose", this);
		setAllMode();
	}

	/**
	 * Get IO of the keyboard devices connected to the computer
	 */
	public void getWootingDevices() {
		try {
			HIDManager manager = HIDManager.getInstance();
			HIDDeviceInfo[] deviceInfos = manager.listDevices();
			for (int i = 0; i < deviceInfos.length; i++) {
				// Wooting keyboard
				if (deviceInfos[i].getVendor_id() == 0x03EB && deviceInfos[i].getProduct_id() == 0xFF01
						&& deviceInfos[i].getUsage() == 1) {
					if (Debug) {
						System.out.println(i + " " + deviceInfos[i].getUsage() + " " + deviceInfos[i].getVendor_id()
								+ " " + deviceInfos[i].getProduct_id() + " " + deviceInfos[i].getSerial_number());
					}
					hidDeviceInfos.add(deviceInfos[i]);
				}
			}
			System.gc();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Close the IO of the keyboard devices connected to the computer
	 */
	public void closeWootingDevices() {
		// close devices
		for (HIDDevice hidDevice : hidDevices) {
			try {
				hidDevice.close();
				HIDManager.getInstance().release();
				System.gc();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		if (readManager != null)
			readManager.stop();
		readManager = null;
	}

	/**
	 * Open the IO input from the keyboard devices
	 */
	public void openWootingDevices() {
		int i = 0;

		// open devices
		for (HIDDeviceInfo hidDeviceInfo : hidDeviceInfos) {
			try {
				HIDManager manager = HIDManager.getInstance();
				HIDDevice hidDevice = manager.openByPath(hidDeviceInfo.getPath());
				if (hidDevice != null) {
					hidDevices.add(hidDevice);
					if (Debug) {
						System.out.println(i + " " + hidDeviceInfo.getUsage() + " " + hidDeviceInfo.getVendor_id() + " "
								+ hidDeviceInfo.getProduct_id() + " " + hidDeviceInfo.getSerial_number() + " "
								+ hidDeviceInfo.getPath());
						System.out.println(
								i + " " + hidDevice.getManufacturerString() + " " + hidDevice.getProductString());
					}
					hidDeviceIDHashMap.put(hidDevice, i);

					hidDevice.disableBlocking();

					// this is for reading real time buffer
					deviceBufferHashMap.put(hidDevice, new byte[BUFSIZE]);
				}
			} catch (Exception e) {
				System.err.println(e);
			}

			i++;
		}
		readManager = new WootingReadManager(myParent, hidDevices, deviceBufferHashMap);
		readManager.start();
	}

	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * Method used by the main Processing applet to clear up any resources used by
	 * the timer when closing the applet.
	 */
	public void dispose() {
		closeWootingDevices();
		if (Debug)
			System.out.println("Quit: WootingManager");
	}

	public void setAllMode() {
		for (KeyValue s : KeyValue.values()) {
			keyMap.put(s, true);
		}
	}

	public void setTextMode() {
		for (KeyValue s : KeyValue.values()) {
			if (s.getValue() < 16 || s.getValue() > 85)
				keyMap.put(s, false);
			else
				keyMap.put(s, true);
		}
	}

	public void setCustomMode(ArrayList<KeyValue> list) {
		for (KeyValue s : KeyValue.values()) {
			keyMap.put(s, false);
		}
		for (KeyValue s : list) {
			keyMap.put(s, true);
		}
	}

	public String parseLog(byte[] data) {
		String result = "";
		for (int i = 0; i < data.length; i = i + 2) {
			int key = ubyteToInt(data[i]);
			int depth = ubyteToInt(data[i + 1]);

			if (depth != 0) {
				KeyValue kv = KeyValue.getKey(key);
				result += String.format("%s: %03d / ", kv.toString(), depth);
			}
		}
		return result;
	}

	public HashMap<KeyValue, Integer> parseData(byte[] data) {
		HashMap<KeyValue, Integer> result = new HashMap<KeyValue, Integer>();
		for (KeyValue s : KeyValue.values()) {
			if (keyMap.get(s)) {
				result.put(s, 0);
			}
		}

		for (int i = 0; i < data.length; i = i + 2) {
			int key = ubyteToInt(data[i]);
			int depth = ubyteToInt(data[i + 1]);

			if (depth != 0) {
				KeyValue kv = KeyValue.getKey(key);
				result.put(kv, depth);
			}
		}

		return result;
	}

	private int ubyteToInt(byte b) {
		if (b < 0)
			return b + 256;
		else
			return b;
	}
}
