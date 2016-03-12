package SmartGrid;

public interface iMessageManager {
	void register(iMessageClient sender,String messageType);
	void sendMessage(String topic, Object message);
}
