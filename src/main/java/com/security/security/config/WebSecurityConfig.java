package com.security.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.security.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    //Necesario para evitar que la seguridad se aplique a los resources
    //Como los css, imagenes y javascripts
    String[] resources = new String[]{
        "/include/**","/css/**","/icons/**","/img/**","/js/**","/layer/**"
};

//Crea el encriptador de contraseñas
BCryptPasswordEncoder bCryptPasswordEncoder;
@Bean
public BCryptPasswordEncoder passwordEncoder() {
    bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
//El numero 4 representa que tan fuerte quieres la encriptacion.
//Se puede en un rango entre 1 y 31. 
//Si no pones un numero el programa utilizara uno aleatoriamente cada vez
//que inicies la aplicacion, por lo cual tus contrasenas encriptadas no funcionaran bien
    return bCryptPasswordEncoder;
}
@Autowired
UserDetailsServiceImpl userDetailsService;

@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http
        .authorizeRequests()
        .antMatchers(resources).permitAll()  
        .antMatchers("/","/index").permitAll()
        .antMatchers("/admin*").access("hasRole('ADMIN')")
        //.antMatchers("/user*").access("hasRole('USER') or hasRole('ADMIN')")
        .antMatchers("/user*").access("hasRole('USER')")
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/menu")
            .failureUrl("/login?error=true")
            .usernameParameter("username")
            .passwordParameter("password")
            .and()
        .logout()
            .permitAll()
            .logoutSuccessUrl("/login?logout");
return http.build();
}
    
}
