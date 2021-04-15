/**
 * 
 */
package com.sobey.module.topic.model.request;

import java.util.List;

/**
 * @author lgc
 * @date 2020年10月13日 下午6:29:51
 */
public class Topic {
	
	private String topic_name;
	private String show_name;
	private Object tags;
	
	private List<Event> events;
	private List<Subscription> subscriptions;
	/**
	 * @return the topic_name
	 */
	public String getTopic_name() {
		return topic_name;
	}
	/**
	 * @param topic_name the topic_name to set
	 */
	public void setTopic_name(String topic_name) {
		this.topic_name = topic_name;
	}
	/**
	 * @return the show_name
	 */
	public String getShow_name() {
		return show_name;
	}
	/**
	 * @param show_name the show_name to set
	 */
	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}
	/**
	 * @return the tags
	 */
	public Object getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(Object tags) {
		this.tags = tags;
	}
	/**
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	/**
	 * @return the subscriptions
	 */
	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}
	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	
	
	
	
	

}
