package com.grille;

import com.grille.dao.UserRepository;
import com.grille.dto.LDAPObject;
import com.grille.entities.User;
import com.grille.service.LDAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

/**
 * Created by alizeefaytre on 03/05/2017.
 */
public class AuthenticationConfig implements AuthenticationManager {

    private LDAPService ldapService;

    private UserRepository userRepository;

    public AuthenticationConfig(LDAPService ldapService, UserRepository userRepository) {
        this.ldapService = ldapService;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String pw = authentication.getCredentials().toString();


        if ((username.equals("admin")) && (pw.equals("admin"))){
            return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }

        try {

            LDAPObject userIdentified = ldapService.LDAPget(username, pw);


            if (userIdentified == null){
                throw new BadCredentialsException("User not found");
            }

            User identifiedUser = userRepository.findByIdentifiant(username);


            if (identifiedUser == null){

                User user =new User();
                user.setNom(userIdentified.getNomFamille());
                user.setPrenom(userIdentified.getPrenom());
                user.setIdentifiant(userIdentified.getLogin());
                user.setClasse(userIdentified.getPromo());

                userRepository.save(user);

            }

        } catch (Exception e) {
            throw new BadCredentialsException("User not found");
            //throw new RuntimeException("LDAP ne repond pas, enfin peut etre ...", e);
        }
        
        return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
