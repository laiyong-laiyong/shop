package com.sobey.module.topic.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sobey.module.topic.model.request.Event;
import com.sobey.module.topic.model.request.Subscription;
import com.sobey.module.topic.model.request.Topic;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class TopicService {

	@Value("${callback}")
	private String callback;

	public Topic buildTopic(String site) {
		Event started = new Event();
		started.setEvent("SFLOW.FLOW.*.STARTED");
		Event succeed = new Event();
		succeed.setEvent("SFLOW.FLOW.*.SUCCEED");
		Event failed = new Event();
		failed.setEvent("SFLOW.FLOW.*.FAILED");
		Event redirected= new Event();
		redirected.setEvent("SFLOW.FLOW.*.REDIRECTED");
		Event cancel= new Event();
		cancel.setEvent("SFLOW.FLOW.*.CANCELED");
		List<Event> events = new ArrayList<Event>();
		events.add(started);
		events.add(succeed);
		events.add(failed);
		events.add(redirected);
		events.add(cancel);

		Subscription sp = new Subscription();
		sp.setProtocol("REST");
		sp.setTerminal(callback);
		List<Subscription> sps = new ArrayList<Subscription>();
		sps.add(sp);

		Topic tp = new Topic();
		tp.setEvents(events);
		tp.setSubscriptions(sps);
		tp.setShow_name(site + "主题订阅");
		tp.setTopic_name("taskMonitor_" + RandomStringUtils.randomAlphabetic(10));

		return tp;
	}

}
