package com.oleksandra.oleksandraspetitions.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Petition {

	private Long id;
	private String title;
	private String description;
	private String authorName;
	private LocalDateTime createdAt;
	private List<Signature> signatures = new ArrayList<>();

	public Petition() {
	}

	public Petition(Long id, String title, String description, String authorName, LocalDateTime createdAt,
			List<Signature> signatures) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.authorName = authorName;
		this.createdAt = createdAt;
		setSignatures(signatures);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<Signature> getSignatures() {
		return signatures;
	}

	public void setSignatures(List<Signature> signatures) {
		this.signatures = signatures == null ? new ArrayList<>() : new ArrayList<>(signatures);
	}

	public int getSignatureCount() {
		return signatures.size();
	}

	public String getShortDescription() {
		if (description == null || description.isBlank()) {
			return "";
		}
		if (description.length() <= 140) {
			return description;
		}
		return description.substring(0, 137) + "...";
	}

}
