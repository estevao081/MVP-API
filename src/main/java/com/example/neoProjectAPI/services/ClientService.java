package com.example.neoProjectAPI.services;

import com.example.neoProjectAPI.dtos.ClientRecordDTO;
import com.example.neoProjectAPI.models.ClientModel;
import com.example.neoProjectAPI.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    public ClientModel updateClient(UUID id, ClientRecordDTO dto) {
        Optional<ClientModel> client = clientRepository.findById(id);
        var productModel = client.get();
        String password = productModel.getPassword();
        BeanUtils.copyProperties(dto, productModel);
        if(dto.password().isBlank()) {
            productModel.setPassword(password);
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            productModel.setPassword(passwordEncoder.encode(dto.password()));
        }
        return clientRepository.save(productModel);
    }

    public void deleteClient(UUID id) {
        Optional<ClientModel> client = clientRepository.findById(id);
        client.ifPresent(clientModel -> clientRepository.delete(clientModel));
    }

    public Page<ClientModel> getAllClients(int page, int items) {
        return clientRepository.findAll(PageRequest.of(page, items));
    }

    public Optional<ClientModel> findById(UUID id) {
        return clientRepository.findById(id);
    }

    public List<ClientModel> findByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<ClientModel> filterByAge(Integer age) {
        return clientRepository.filterByAge(age);
    }

    public Optional<ClientModel> findByMail(String mail) {
        return clientRepository.findByMail(mail);
    }

}
