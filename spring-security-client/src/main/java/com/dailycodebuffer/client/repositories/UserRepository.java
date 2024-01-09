package com.dailycodebuffer.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodebuffer.client.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

}
