package com.oleksandra.oleksandraspetitions.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Signature {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;
	private LocalDateTime signedAt;

	public Signature() {
	}

	public Signature(String name, String email, LocalDateTime signedAt) {
		this.name = name;
		this.email = email;
		this.signedAt = signedAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getSignedAt() {
		return signedAt;
	}

	public void setSignedAt(LocalDateTime signedAt) {
		this.signedAt = signedAt;
	}

}
