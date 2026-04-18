package com.oleksandra.oleksandraspetitions.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.oleksandra.oleksandraspetitions.model.Petition;
import com.oleksandra.oleksandraspetitions.model.Signature;

@Repository
public class PetitionRepository {

	private final List<Petition> petitions = new ArrayList<>();
	private long nextId = 1L;

	public PetitionRepository() {
		seedData();
	}

	private void seedData() {
		if (!petitions.isEmpty()) {
			return;
		}

		petitions.add(new Petition(
				1L,
				"Improve Campus Study Spaces",
				"Add more quiet study rooms, better lighting, and extra power sockets in the main library for evening study sessions.",
				"Alice Murphy",
				LocalDateTime.now().minusDays(7),
				List.of(
						new Signature("Daniel Walsh", "daniel.walsh@example.com", LocalDateTime.now().minusDays(6)),
						new Signature("Emma Byrne", "emma.byrne@example.com", LocalDateTime.now().minusDays(5)))));

		petitions.add(new Petition(
				2L,
				"Extend Cafeteria Vegetarian Options",
				"Increase the number of affordable vegetarian meals available on campus during lunch hours to support healthier choices.",
				"Niamh Kelly",
				LocalDateTime.now().minusDays(4),
				List.of(
						new Signature("Sean Doyle", "sean.doyle@example.com", LocalDateTime.now().minusDays(3)),
						new Signature("Laura Ryan", "laura.ryan@example.com", LocalDateTime.now().minusDays(2)),
						new Signature("Mark Finn", "mark.finn@example.com", LocalDateTime.now().minusDays(1)))));

		petitions.add(new Petition(
				3L,
				"Provide More Bicycle Parking",
				"Install additional covered bicycle parking near the engineering and business buildings to encourage sustainable travel.",
				"Conor Hayes",
				LocalDateTime.now().minusDays(2),
				List.of(
						new Signature("Aoife Nolan", "aoife.nolan@example.com", LocalDateTime.now().minusDays(1)))));

		nextId = petitions.stream()
				.mapToLong(Petition::getId)
				.max()
				.orElse(0L) + 1;
	}

	public List<Petition> findAll() {
		return List.copyOf(petitions);
	}

	public List<Petition> search(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return findAll();
		}

		String normalizedKeyword = keyword.toLowerCase();
		return petitions.stream()
				.filter(petition -> containsIgnoreCase(petition.getTitle(), normalizedKeyword)
						|| containsIgnoreCase(petition.getDescription(), normalizedKeyword))
				.toList();
	}

	public Optional<Petition> findById(Long id) {
		return petitions.stream()
				.filter(petition -> petition.getId().equals(id))
				.findFirst();
	}

	public Petition save(Petition petition) {
		Petition storedPetition = new Petition(
				nextId++,
				petition.getTitle(),
				petition.getDescription(),
				petition.getAuthorName(),
				petition.getCreatedAt(),
				petition.getSignatures());
		petitions.add(storedPetition);
		return storedPetition;
	}

	public Signature addSignature(Long petitionId, Signature signature) {
		Petition petition = findById(petitionId)
				.orElseThrow(() -> new IllegalArgumentException("Petition not found: " + petitionId));

		Signature storedSignature = new Signature(
				signature.getName(),
				signature.getEmail(),
				signature.getSignedAt());
		petition.getSignatures().add(storedSignature);
		return storedSignature;
	}

	private boolean containsIgnoreCase(String source, String keyword) {
		return source != null && source.toLowerCase().contains(keyword);
	}

}
