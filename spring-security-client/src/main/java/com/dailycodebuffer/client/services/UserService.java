package com.dailycodebuffer.client.services;

import java.util.Optional;

import com.dailycodebuffer.client.entities.User;
import com.dailycodebuffer.client.entities.VerificationToken;
import com.dailycodebuffer.client.models.UserModel;

public interface UserService {

	User registerUser(UserModel userModel);

	void saveVerificationTokenForUser(String token, User user);

	String validateVerificationToken(String token);

	VerificationToken generateNewVerificationToken(String oldToken);

	User findUserByEmail(String email);

	void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

	void changeUserPassword(User user, String newPassword);

}
