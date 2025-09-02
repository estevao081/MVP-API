package com.example.neoProjectAPI.infra.security;

import com.example.neoProjectAPI.models.ClientModel;
import com.example.neoProjectAPI.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientModel client = this.clientRepository.findByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
        return new org
                .springframework
                .security
                .core
                .userdetails
                .User(client.getMail(), client.getPassword(), new ArrayList<>());
    }
}
