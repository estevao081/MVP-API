package com.example.neoProjectAPI.controllers;

import com.example.neoProjectAPI.dtos.ClientRecordDTO;
import com.example.neoProjectAPI.models.ClientModel;
import com.example.neoProjectAPI.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PutMapping("/{id}")
    public ResponseEntity<ClientModel> updateClient(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ClientRecordDTO dto) {
        if (clientService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ClientModel updatedClient = clientService.updateClient(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable(value = "id") UUID id) {
        if(clientService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<Page<ClientModel>> getAllClients(@RequestParam int page,
                                                           @RequestParam int items) {
        Page<ClientModel> products = clientService.getAllClients(page, items);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientModel> findById(@PathVariable UUID id) {
        Optional<ClientModel> clientOpt = clientService.findById(id);
        if(clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ClientModel client = clientOpt.get();
        return ResponseEntity.ok(client);
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<ClientModel>> findByName(@RequestParam String name) {
        List<ClientModel> clientsFound = clientService.findByName(name);
        if (clientsFound.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clientsFound);
    }

    @GetMapping("/filterByAge")
    public ResponseEntity<Optional<ClientModel>> filterByAge(@RequestParam Integer age) {
        Optional<ClientModel> clientsFound = clientService.filterByAge(age);
        if (clientsFound.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clientsFound);
    }

    @GetMapping("/findByMail")
    public ResponseEntity<Optional<ClientModel>> findByMail(@RequestParam String mail) {
        Optional<ClientModel> clientsFound = clientService.findByMail(mail);
        if (clientsFound.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clientsFound);
    }
}
