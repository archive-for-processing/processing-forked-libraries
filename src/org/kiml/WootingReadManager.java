package org.kiml;

import java.io.IOException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import com.codeminders.hidapi.HIDDevice;

import processing.core.PApplet;

/**
 * @author Shinhoo Park @ KAIST Interactive Media Lab
 */

public class WootingReadManager {
	private boolean Debug = false;
	private timerThread mtimer;
	private boolean isStart;
	private boolean isPause;

	private PApplet myParent;

	private Vector<HIDDevice> hidDevices = new Vector<HIDDevice>();
	private HashMap<HIDDevice, byte[]> deviceBufferHashMap = new HashMap<HIDDevice, byte[]>();

	private static final String ON_READ_EVENT_NAME = "onReadEvent";
	private Method onReadEvent;

	public WootingReadManager(PApplet myParent, Vector<HIDDevice> hidDevices,
			HashMap<HIDDevice, byte[]> deviceBufferHashMap) {
		this.myParent = myParent;
		this.hidDevices = hidDevices;
		this.deviceBufferHashMap = deviceBufferHashMap;

		try {
			onReadEvent = myParent.getClass().getMethod(ON_READ_EVENT_NAME, byte[].class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public synchronized void start() {
		if (mtimer != null) {
			mtimer.cancel();
			mtimer = null;
		}

		isStart = true;
		isPause = false;
		mtimer = new timerThread();
		mtimer.start();
	}

	public synchronized void stop() {
		if (mtimer != null) {
			mtimer.cancel();
			mtimer = null;
		}
		isStart = false;
		isPause = false;
		if (Debug)
			System.out.println("Quit: WootingReadManager");
	}

	public synchronized void pause() {
		if (!isPause) {
			isPause = true;
		}
	}

	public synchronized void unpause() {
		if (isPause) {
			isPause = false;
		}
	}

	private void connectionLost() {
		this.start();
	}

	public boolean isStart() {
		return isStart;
	}

	public boolean isPause() {
		return isPause;
	}

	private void invokeMethod(Method method, Object... args) {
		if (method != null) {
			try {
				method.invoke(myParent, args);
			} catch (Exception e) {
				if (Debug) {
					System.err.println("failed to call method " + method.toString() + " inside main app");
					e.printStackTrace();
				}
			}
		}
	}

	private class timerThread extends Thread {
		private boolean isQuit;

		public timerThread() {
			isQuit = false;
		}

		public void run() {
			while (!isQuit) {
				if (!isPause) {
					for (HIDDevice hidDevice : hidDevices) {
						try {
							// the byte array is called input report,which only works when there is an input
							// event;
							// n is the number of bytes
							int n = hidDevice.read(deviceBufferHashMap.get(hidDevice));
							byte[] buffer = deviceBufferHashMap.get(hidDevice);

							if (n != 0 && buffer != null) {
								byte[] returnArray = new byte[n];
								for (int i = 0; i < n; i++) {
									returnArray[i] = buffer[i];
								}
								invokeMethod(onReadEvent, returnArray);
								// System.out.println(n);
								//
								// for (int i = 0; i < n; i++) {
								// int current = buffer[i];
								//
								// if (current < 0) {
								// current = current + 256;
								// }
								//
								// System.out.print(current + " ");
								// if (i % 2 == 0 && buffer[i + 1] != 0) {
								// String a = keyArray.get(current);
								// System.out.print(a + " ");
								// }
								// }
								// System.out.println();
							}
						} catch (IOException e) {
							if (Debug)
								System.err.println(e);
						}
					}
				}
			}
		}

		public void cancel() {
			isQuit = true;
		}
	}
}
