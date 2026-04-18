package com.oleksandra.oleksandraspetitions.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oleksandra.oleksandraspetitions.model.Petition;
import com.oleksandra.oleksandraspetitions.model.Signature;
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

	public List<Petition> searchPetitions(String keyword) {
		return petitionRepository.search(normalize(keyword)).stream()
				.sorted(Comparator.comparing(Petition::getCreatedAt).reversed())
				.toList();
	}

	public Optional<Petition> findPetitionById(Long id) {
		return petitionRepository.findById(id);
	}

	public Petition createPetition(Petition petition) {
		Petition newPetition = new Petition();
		newPetition.setTitle(normalize(petition.getTitle()));
		newPetition.setDescription(normalize(petition.getDescription()));
		newPetition.setAuthorName(normalize(petition.getAuthorName()));
		newPetition.setCreatedAt(LocalDateTime.now());
		newPetition.setSignatures(List.of());
		return petitionRepository.save(newPetition);
	}

	public Signature signPetition(Long petitionId, Signature signature) {
		Signature newSignature = new Signature();
		newSignature.setName(normalize(signature.getName()));
		newSignature.setEmail(normalize(signature.getEmail()));
		newSignature.setSignedAt(LocalDateTime.now());
		return petitionRepository.addSignature(petitionId, newSignature);
	}

	private String normalize(String value) {
		return value == null ? "" : value.trim();
	}

}
