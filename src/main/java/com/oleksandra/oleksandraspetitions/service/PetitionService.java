package com.oleksandra.oleksandraspetitions.service;

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

}
