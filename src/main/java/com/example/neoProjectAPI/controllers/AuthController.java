package com.example.neoProjectAPI.controllers;

import com.example.neoProjectAPI.dtos.ClientRecordDTO;
import com.example.neoProjectAPI.dtos.LoginRequestDTO;
import com.example.neoProjectAPI.dtos.LoginResponseDTO;
import com.example.neoProjectAPI.infra.security.TokenService;
import com.example.neoProjectAPI.models.ClientModel;
import com.example.neoProjectAPI.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(ClientRepository clientRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO dto) {
        ClientModel client = this.clientRepository.findByMail(dto.mail())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        if (passwordEncoder.matches(dto.password(), client.getPassword())) {
            String token = this.tokenService.genereteToken(client);
            return ResponseEntity.ok(new LoginResponseDTO(client.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody ClientRecordDTO dto) {
        Optional<ClientModel> client = this.clientRepository.findByMail(dto.mail());
        if (client.isEmpty()) {
            ClientModel newClient = new ClientModel();
            newClient.setPassword(passwordEncoder.encode(dto.password()));
            newClient.setMail(dto.mail());
            newClient.setName(dto.name());
            newClient.setBirthday(dto.birthday());
            LocalDate today = LocalDate.now();
            newClient.setAge(ChronoUnit.YEARS.between(newClient.getBirthday(), today));
            this.clientRepository.save(newClient);

            String token = this.tokenService.genereteToken(newClient);
            return ResponseEntity.ok(new LoginResponseDTO(newClient.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
