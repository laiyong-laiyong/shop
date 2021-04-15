package com.sobey.util.business.event;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class EventUtil {

	public static ArrayList<String> getEvents(String head, String middle, String tail) {
		String rs = "";
		if (StringUtils.isNotEmpty(head)) {
			rs = rs + head;
		}
		if (StringUtils.isNotEmpty(middle)) {
			rs = rs + "." + middle;
		}

		ArrayList<String> events = new ArrayList<String>();
		if (StringUtils.isNotEmpty(tail)) {
			String[] list = tail.split(",");
			for (String item : list) {
				String event = rs + "." + item;
				events.add(event);
			}
		}

		return events;
	}

}
