package com.example.user_management_service;

import com.s2s.S2SVerification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);

		S2SVerification.addToTrustedStore("userManagement","userKey");
		System.out.println(S2SVerification.verifyRequest("product-catalog",""));

	}

}