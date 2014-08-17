package ZstProcessing;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import examples.TestNode;
import ZST.*;


public class Showtime extends ZstNode{

	/**
	 * Create a new stage node that will reply on a fixed port
	 * @param nodeId Unique name identifying this stage node
	 * @param stagePort Port to run stage on
	 */
	public Showtime(String nodeId, int stagePort) {
		super(nodeId, stagePort);
		start();
	}
	
	/**
	 * Create a new node
	 * @param nodeId Unique name identifying this node
	 * @param stageAddress Address of the stage in the format 'address:port' (eg: 127.0.0.1:6000)
	 */
	public Showtime(String nodeId, String stageAddress) {
		super(nodeId, stageAddress);
		requestRegisterNode();
		start();
	}
	
	/**
	 * Register a new method for this node
	 * @param methodName Name of the method
	 * @param accessMode Access mode of this method
	 * @param owner Object that owns this method
	 */
	public void registerMethod(String methodName, String accessMode, Object owner){
		registerMethod(methodName, accessMode, owner, new String[0]);
	}
	
	/**
	 * Register a new method for this node with arguments
	 * @param methodName Name of the method
	 * @param accessMode Access mode of this method
	 * @param owner Object that owns this method
	 * @param args Arguments this method requires
	 */
	public void registerMethod(String methodName, String accessMode, Object callbackOwner, String[] args){
		Map<String, Object> nodeArgs = new HashMap<String, Object>();
		
		for(String arg : args)
			nodeArgs.put(arg, "");
		
		Method callback = null;
		try {
			callback = callbackOwner.getClass().getDeclaredMethod(methodName, new Class[]{ZstMethod.class});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        requestRegisterMethod(methodName, accessMode, nodeArgs, callbackOwner, callback);
	}
	
	/**
	 * Subscribe to updates from a remote method
	 * @param remoteMethod		Remote method to subscribe to 
	 * @param callbackName		Callback to run
	 * @param callbackOwner		Object to run callback on
	 */
	public void subscribe(ZstMethod remoteMethod, String callbackName, Object callbackOwner){
		Method callback = null;
		try {
			callback = callbackOwner.getClass().getDeclaredMethod(callbackName, new Class[]{ZstMethod.class});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		subscribeToMethod(remoteMethod, callbackOwner, callback);
	}

}
