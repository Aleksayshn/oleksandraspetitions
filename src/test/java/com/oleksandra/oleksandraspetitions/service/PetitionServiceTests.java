package com.oleksandra.oleksandraspetitions.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oleksandra.oleksandraspetitions.model.Petition;
import com.oleksandra.oleksandraspetitions.model.Signature;

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

	@Test
	void signPetitionAddsSignatureAndUpdatesCount() {
		Petition petitionBefore = petitionService.findPetitionById(1L).orElseThrow();
		int initialSignatureCount = petitionBefore.getSignatureCount();

		Signature signature = new Signature();
		signature.setName("Cian Walsh");
		signature.setEmail("cian.walsh@example.com");

		Signature createdSignature = petitionService.signPetition(1L, signature);
		Petition petitionAfter = petitionService.findPetitionById(1L).orElseThrow();

		assertThat(createdSignature.getSignedAt()).isNotNull();
		assertThat(petitionAfter.getSignatureCount()).isEqualTo(initialSignatureCount + 1);
		assertThat(petitionAfter.getSignatures().get(petitionAfter.getSignatures().size() - 1).getEmail())
				.isEqualTo("cian.walsh@example.com");
	}

	@Test
	void searchPetitionsMatchesTitleAndDescriptionIgnoringCase() {
		List<Petition> titleMatches = petitionService.searchPetitions("  STUDY  ");
		List<Petition> descriptionMatches = petitionService.searchPetitions("sustainable");

		assertThat(titleMatches).extracting(Petition::getTitle)
				.contains("Improve Campus Study Spaces");
		assertThat(descriptionMatches).extracting(Petition::getTitle)
				.contains("Provide More Bicycle Parking");
	}

	@Test
	void searchPetitionsWithEmptyKeywordReturnsAllPetitions() {
		List<Petition> allPetitions = petitionService.findAllPetitions();
		List<Petition> searchResults = petitionService.searchPetitions("   ");

		assertThat(searchResults).hasSameSizeAs(allPetitions);
	}

}
