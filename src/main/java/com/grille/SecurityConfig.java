package com.grille;

import com.grille.dao.RoleRepository;
import com.grille.dao.UserRepository;
import com.grille.service.LDAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by alizeefaytre on 01/05/2017.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LDAPService ldapService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public void globalConfig(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/index.html", true);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationConfig(ldapService, userRepository, roleRepository);
    }
}
