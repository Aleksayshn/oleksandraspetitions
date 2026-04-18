package com.oleksandra.oleksandraspetitions.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.oleksandra.oleksandraspetitions.model.Petition;
import com.oleksandra.oleksandraspetitions.model.Signature;

@Repository
public class PetitionRepository {

	private final List<Petition> petitions = new ArrayList<>();

	@PostConstruct
	void seedData() {
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
	}

	public List<Petition> findAll() {
		return List.copyOf(petitions);
	}

}
