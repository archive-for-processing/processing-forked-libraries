package SmartGrid;

import java.util.ArrayList;

public class SimpleMessageManager implements iMessageManager {
	private static ArrayList<MessageTopic> topics=new ArrayList<MessageTopic>();
	static SimpleMessageManager _instance;
	public static SimpleMessageManager getInstance()
	{
		if(_instance==null)
		{
			_instance=new SimpleMessageManager();
		}
		return _instance;
	}
	
	public void register(iMessageClient sender, String title) {
		MessageTopic target=getByTitle(title);
    	if(target==null)
    	{
    		target=new MessageTopic();
    		target.title=title;
    	}
    	if (!target.subscribers.contains(sender)) {
    		target.subscribers.add(sender);
		}
    	topics.add(target);

	}
	private MessageTopic getByTitle(String title)
    {
    	MessageTopic result = null;
    	for(MessageTopic topic :topics)
    	{
    		if(topic.title==title)
    		{
    			result=topic;
    			break;
    		}
    	}
    	return result;
    }
	public void sendMessage(String title,Object message)
    {
    	MessageTopic target=getByTitle(title);
    	if(target==null)
    	{
    		for (iMessageClient item:target.subscribers)
    		{
    			item.messageReceived(title,message);
    		}
    	}
    }
	class MessageTopic
	{
		String title;
		ArrayList<iMessageClient> subscribers=new ArrayList<iMessageClient>();
	}
}
