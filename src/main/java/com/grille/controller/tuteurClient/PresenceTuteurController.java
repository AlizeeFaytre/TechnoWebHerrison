package com.grille.controller.tuteurClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PresenceTuteurController {
	@GetMapping("/presence-tuteur")
	public String index (Model model){
					return "tuteur-client/presence-tuteur";
	}
}
	


    