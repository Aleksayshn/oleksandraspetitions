package com.oleksandra.oleksandraspetitions.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.oleksandra.oleksandraspetitions.model.Petition;
import com.oleksandra.oleksandraspetitions.repository.PetitionRepository;

@Service
public class PetitionService {

	private final PetitionRepository petitionRepository;

	public PetitionService(PetitionRepository petitionRepository) {
		this.petitionRepository = petitionRepository;
	}

	public List<Petition> findAllPetitions() {
		return petitionRepository.findAll().stream()
				.sorted(Comparator.comparing(Petition::getCreatedAt).reversed())
				.toList();
	}

	public Petition createPetition(Petition petition) {
		Petition newPetition = new Petition();
		newPetition.setTitle(petition.getTitle().trim());
		newPetition.setDescription(petition.getDescription().trim());
		newPetition.setAuthorName(petition.getAuthorName().trim());
		newPetition.setCreatedAt(LocalDateTime.now());
		newPetition.setSignatures(List.of());
		return petitionRepository.save(newPetition);
	}

}
