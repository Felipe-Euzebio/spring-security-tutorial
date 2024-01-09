package com.dailycodebuffer.client.controllers;

import java.util.Optional;
import java.util.UUID;

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
import com.dailycodebuffer.client.models.PasswordModel;
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
		
		if(!result.equalsIgnoreCase("valid")) return "Bad user";
			
		return "User verified successfully";
	}
	
	@GetMapping("resendVerifyToken")
	public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
		VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
		
		User user = verificationToken.getUser();
		
		resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
		
		return "Verification link sent";			
	}
	
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
		User user = userService.findUserByEmail(passwordModel.getEmail());

		String url = "";
		
		if (user != null) {
			String token = UUID.randomUUID().toString();
			userService.createPasswordResetTokenForUser(user, token);
			url = passwordResetTokenMail(user, applicationUrl(request), token);
		}
		
		return url;
	}

	@PostMapping("/savePassword")
	public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
		String result = userService.validatePasswordResetToken(token);

		if(!result.equalsIgnoreCase("valid")) return "Invalid/Expired token";

		Optional<User> user = userService.getUserByPasswordResetToken(token);

		if(!user.isPresent()) return "Invalid token";

		userService.changeUserPassword(user.get(), passwordModel.getNewPassword());

		return "Password reset successfully";
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

	private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        //Send Mail to user (mimicking)
        String url = applicationUrl + "/savePassword?token=" + token;
	
        log.info("Click the link below to reset your password: {}", url);

		return url;
	}
	
}
