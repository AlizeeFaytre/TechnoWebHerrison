package com.grille;

import com.grille.dao.RoleRepository;
import com.grille.dao.UserRepository;
import com.grille.dto.LDAPObject;
import com.grille.entities.Role;
import com.grille.entities.User;
import com.grille.service.LDAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

/**
 * Created by alizeefaytre on 03/05/2017.
 */
public class AuthenticationConfig implements AuthenticationManager {

    @Autowired
    private LDAPService ldapService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public AuthenticationConfig(LDAPService ldapService, UserRepository userRepository, RoleRepository roleRepository) {
        this.ldapService = ldapService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String pw = authentication.getCredentials().toString();



        if ((username.equals("admin")) && (pw.equals("admin"))){
            return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }
        else if ((username.equals("eleve")) && (pw.equals("eleve"))){
            return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_eleve")));
        }
        else if ((username.equals("prof")) && (pw.equals("prof"))){
            return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_prof")));
        }
        else if ((username.equals("respoModule")) && (pw.equals("respoModule"))){
            return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_respoModule")));
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



                Role role = roleRepository.findByName(userIdentified.getType());
                if(role == null){
                    Role newRole = new Role(userIdentified.getType());
                    roleRepository.save(newRole);
                    role = newRole;
                }

                Set<Role> listRole = new HashSet<>();
                listRole.add(role);

                user.setRoles(listRole);

                userRepository.save(user);
                if (role.getName().equals("eleve")){
                    return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_eleve")));
                }

                else if (role.getName().equals("prof")){
                    return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_prof")));
                }

                else if (role.getName().equals("respoModule")){
                    return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_respoModule")));
                }

                else if (role.getName().equals("administration")){
                    return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_administration")));
                }

            }
            else {
                Set<Role> setRole = identifiedUser.getRoles();
                ArrayList<SimpleGrantedAuthority> roles = new ArrayList<>();
                for (Role role:setRole) {
                    roles.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                }

                return new UsernamePasswordAuthenticationToken(username, pw, roles);
            }

        } catch (Exception e) {
            throw new BadCredentialsException("User not found");
            //throw new RuntimeException("LDAP ne repond pas, enfin peut etre ...", e);
        }

        /*
        User actual = userRepository.findByIdentifiant(username);
        Set<Role> roles = actual.getRoles();
        Role userRole = (Role) roles.toArray()[0];
        String roleName = userRole.getName();
        */

        return new UsernamePasswordAuthenticationToken(username, pw, Collections.singletonList(new SimpleGrantedAuthority("ROLE_eleve")));
    }
}
