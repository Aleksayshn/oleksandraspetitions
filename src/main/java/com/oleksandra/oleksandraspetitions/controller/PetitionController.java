package com.oleksandra.oleksandraspetitions.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.oleksandra.oleksandraspetitions.model.Petition;
import com.oleksandra.oleksandraspetitions.model.Signature;
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

	@GetMapping("/petitions/search")
	public String showSearchPage(@RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
		model.addAttribute("keyword", keyword);
		return "search";
	}

	@GetMapping("/petitions/search/results")
	public String viewSearchResults(@RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
		String trimmedKeyword = keyword.trim();
		model.addAttribute("keyword", trimmedKeyword);
		model.addAttribute("keywordProvided", !trimmedKeyword.isEmpty());
		model.addAttribute("petitions", petitionService.searchPetitions(trimmedKeyword));
		return "search-results";
	}

	@PostMapping("/petitions")
	public String createPetition(@Valid @ModelAttribute("petition") Petition petition, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "create-petition";
		}

		petitionService.createPetition(petition);
		return "redirect:/petitions";
	}

	@GetMapping("/petitions/{id}")
	public String viewPetitionDetails(@PathVariable Long id, Model model) {
		Petition petition = getPetitionOrThrow(id);
		model.addAttribute("petition", petition);
		model.addAttribute("signature", new Signature());
		return "petition-details";
	}

	@PostMapping("/petitions/{id}/sign")
	public String signPetition(@PathVariable Long id, @Valid @ModelAttribute("signature") Signature signature,
			BindingResult bindingResult, Model model) {
		Petition petition = getPetitionOrThrow(id);

		if (bindingResult.hasErrors()) {
			model.addAttribute("petition", petition);
			return "petition-details";
		}

		petitionService.signPetition(id, signature);
		return "redirect:/petitions/" + id;
	}

	private Petition getPetitionOrThrow(Long id) {
		return petitionService.findPetitionById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Petition not found"));
	}

}
