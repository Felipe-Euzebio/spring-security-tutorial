package com.dailycodebuffer.client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodebuffer.client.entities.User;
import com.dailycodebuffer.client.entities.VerificationToken;
import com.dailycodebuffer.client.events.RegistrationCompleteEvent;
import com.dailycodebuffer.client.models.UserModel;
import com.dailycodebuffer.client.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PostMapping("/register")
	public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
		User user = userService.registerUser(userModel);
		
		publisher.publishEvent(new RegistrationCompleteEvent(
			user, 
			applicationUrl(request)
		));
		
		return "Success";	
	}
	
	@GetMapping("/verifyRegistration")
	public String verifyRegistration(@RequestParam("token") String token) {
		String result = userService.validateVerificationToken(token);
		
		if(result.equalsIgnoreCase("valid")) {
			return "User verified successfully";
		} else {
			return "Bad user";
		}
	}
	
	@GetMapping("resendVerifyToken")
	public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
		VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
		
		User user = verificationToken.getUser();
		
		resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
		
		return "Verification link sent";			
	}

	private String applicationUrl(HttpServletRequest request) {
		return "http://" + 
			request.getServerName() + 
			":" + 
			request.getServerPort() + 
			request.getContextPath();
	}
	
	private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        //Send Mail to user (mimicking)
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
	
        log.info("Click the link to verify your account: {}", url);
	}
	
}
