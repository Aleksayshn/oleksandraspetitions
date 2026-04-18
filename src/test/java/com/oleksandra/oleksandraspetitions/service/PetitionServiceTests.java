package com.oleksandra.oleksandraspetitions.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oleksandra.oleksandraspetitions.model.Petition;

@SpringBootTest
class PetitionServiceTests {

	@Autowired
	private PetitionService petitionService;

	@Test
	void createPetitionAddsPetitionToList() {
		int initialCount = petitionService.findAllPetitions().size();

		Petition petition = new Petition();
		petition.setTitle("Reduce Printer Queues");
		petition.setDescription("Add one more printer in the library to reduce waiting times during assignment deadlines.");
		petition.setAuthorName("Olivia Daly");

		Petition createdPetition = petitionService.createPetition(petition);
		List<Petition> petitions = petitionService.findAllPetitions();

		assertThat(createdPetition.getId()).isNotNull();
		assertThat(createdPetition.getCreatedAt()).isNotNull();
		assertThat(createdPetition.getSignatures()).isEmpty();
		assertThat(petitions).hasSize(initialCount + 1);
		assertThat(petitions.get(0).getTitle()).isEqualTo("Reduce Printer Queues");
	}

}
