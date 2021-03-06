package com.grille.controller.tuteurClient;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.grille.entities.User;
import com.grille.service.UserService;

@Controller
public class PresenceTuteurController {
	
	@Autowired
    private UserService userService;

	@Secured({"ROLE_ADMIN", "ROLE_prof"})
	@GetMapping("/presence-tuteur")
	public String index (Model model, HttpSession session){
		User currentUser = userService.getLogedUser(session);
		model.addAttribute("currentUser", currentUser);
					return "tuteur-client/presence-tuteur";
	}
}
	


    