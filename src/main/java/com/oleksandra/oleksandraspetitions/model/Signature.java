package com.oleksandra.oleksandraspetitions.model;

import java.time.LocalDateTime;

public class Signature {

	private String name;
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
