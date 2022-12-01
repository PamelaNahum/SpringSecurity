package com.security.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.security.model.Authority;
import com.security.security.model.User;
import com.security.security.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //Buscar el usuario con el repository y si no existe vamos a lanzar una exepcion
        //NOSOTROS GENERAMOS LA EXCEPCION
        //vamos a guardar en una variable el usuario buscado segun username, si no lo encuentra CREAMOS UN ERROR el cual
        //al momento de generarse nos devolverá el mensaje "no existe el usuario"
        User appUser = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(("no existe el usuario")));

        //Vamos a mappear nuestra lista de Authority con las propiedades de spring security

        List gantList = new ArrayList();

        for(Authority authority: appUser.getAuthority()){
            //ROL_USER, ROL_ADMIN, ROL_CLIENT
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
            gantList.add(grantedAuthority);
        }

        //Vamos a craer el objeto UserDetails que va a ir en sesion y tambien nos lo va a retornar

        UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getPassword(), gantList);
        return user;
        
    }
    
}
