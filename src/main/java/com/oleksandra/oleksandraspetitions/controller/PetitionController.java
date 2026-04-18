package com.oleksandra.oleksandraspetitions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
