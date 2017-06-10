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
	

	
	@GetMapping("/setting-role")
	public String index (Model model, HttpSession session){
		
					return "tuteur-client/setting-role";
	}
}
	


    