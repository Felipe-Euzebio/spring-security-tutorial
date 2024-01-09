package com.dailycodebuffer.client.services;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dailycodebuffer.client.entities.User;
import com.dailycodebuffer.client.entities.VerificationToken;
import com.dailycodebuffer.client.models.UserModel;
import com.dailycodebuffer.client.repositories.UserRepository;
import com.dailycodebuffer.client.repositories.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

		return userRepository.save(user);
	}

	@Override
	public void saveVerificationTokenForUser(String token, User user) {
		VerificationToken verificationToken = new VerificationToken(user, token);
		
		verificationTokenRepository.save(verificationToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		
		if (verificationToken == null) return "invalid";
		
		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		
		if((verificationToken.getExpirationTime().getTime() 
				- cal.getTime().getTime()) <= 0) {		
			verificationTokenRepository.delete(verificationToken);
			
			return "expired";	
		}
		
		user.setEnabled(true);
		userRepository.save(user);
		
		return "valid";
	}

	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        
		verificationToken.setToken(UUID.randomUUID().toString());
		
		verificationTokenRepository.save(verificationToken);
		
		return verificationToken;
	}

}
