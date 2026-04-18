package com.oleksandra.oleksandraspetitions.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oleksandra.oleksandraspetitions.model.Petition;
import com.oleksandra.oleksandraspetitions.service.PetitionService;

@Controller
public class PetitionController {

	private final PetitionService petitionService;

	public PetitionController(PetitionService petitionService) {
		this.petitionService = petitionService;
	}

	@GetMapping("/")
	public String redirectToPetitions() {
		return "redirect:/petitions";
	}

	@GetMapping("/petitions")
	public String viewAllPetitions(Model model) {
		model.addAttribute("petitions", petitionService.findAllPetitions());
		return "petitions";
	}

	@GetMapping("/petitions/new")
	public String showCreatePetitionForm(Model model) {
		model.addAttribute("petition", new Petition());
		return "create-petition";
	}

	@PostMapping("/petitions")
	public String createPetition(@Valid @ModelAttribute("petition") Petition petition, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "create-petition";
		}

		petitionService.createPetition(petition);
		return "redirect:/petitions";
	}

}
