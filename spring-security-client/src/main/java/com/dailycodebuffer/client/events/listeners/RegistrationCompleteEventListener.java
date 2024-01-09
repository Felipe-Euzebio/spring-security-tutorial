package com.dailycodebuffer.client.events.listeners;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.dailycodebuffer.client.entities.User;
import com.dailycodebuffer.client.events.RegistrationCompleteEvent;
import com.dailycodebuffer.client.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the Verification Token for the User with Link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        
        //Send Mail to user (mimicking)
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;
        
        log.info("Click the link to verify your account: {}", url);
    }
}
