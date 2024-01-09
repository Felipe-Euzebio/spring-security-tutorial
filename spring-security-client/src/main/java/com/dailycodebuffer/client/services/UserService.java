package com.dailycodebuffer.client.services;

import com.dailycodebuffer.client.entities.User;
import com.dailycodebuffer.client.entities.VerificationToken;
import com.dailycodebuffer.client.models.UserModel;

public interface UserService {

	User registerUser(UserModel userModel);

	void saveVerificationTokenForUser(String token, User user);

	String validateVerificationToken(String token);

	VerificationToken generateNewVerificationToken(String oldToken);

}
