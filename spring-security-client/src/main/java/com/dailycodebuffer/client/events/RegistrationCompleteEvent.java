package com.dailycodebuffer.client.events;

import org.springframework.context.ApplicationEvent;

import com.dailycodebuffer.client.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
	
	private User user;
	private String applicationUrl;

	public RegistrationCompleteEvent(User user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}

}
