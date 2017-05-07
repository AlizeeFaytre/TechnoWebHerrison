package com.grille;

import com.grille.dao.UserRepository;
import com.grille.entities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.jws.soap.SOAPBinding;
import java.util.Calendar;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		ApplicationContext ctx=SpringApplication.run(DemoApplication.class, args);
		/*UserRepository dao=ctx.getBean(UserRepository.class);


		System.out.println("Voici la liste des users");
		List<User> list = dao.findAll();
		for (User u: list) {
			System.out.println(u.getNom() + "-" + u.getPrenom());
		}

		User myUser = dao.findById(2);
		System.out.println(myUser.getNom());*/


	}
}
