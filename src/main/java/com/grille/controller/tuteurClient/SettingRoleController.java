package com.grille.controller.tuteurClient;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.grille.entities.User;
import com.grille.service.UserService;

@Controller
public class SettingRoleController {
	
	@Autowired
    private UserService userService;
	
	@GetMapping("/setting-role")
	public String index (Model model, HttpSession session){
		User currentUser = userService.getLogedUser(session);
		model.addAttribute("currentUser", currentUser);
					return "tuteur-client/setting-role";
	}
}
	


    