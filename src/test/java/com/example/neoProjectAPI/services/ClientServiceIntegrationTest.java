package com.example.neoProjectAPI.services;

import com.example.neoProjectAPI.dtos.ClientRecordDTO;
import com.example.neoProjectAPI.models.ClientModel;
import com.example.neoProjectAPI.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ClientModel client;

    @BeforeEach
    @Commit
    void setUp() {
        client = new ClientModel();
        client.setName("Thiago");
        client.setMail("thiago@test.com");
        client.setPassword(passwordEncoder.encode("123"));
        client.setBirthday(LocalDate.of(2000, 9, 2));

        clientRepository.saveAndFlush(client);
    }

    @Test
    void updateClient_ShouldPersistChanges() {
        ClientRecordDTO dto = new ClientRecordDTO(
                "Novo Nome",
                "novo@mail.com",
                "novaSenha",
                LocalDate.of(2000, 9, 2)
        );

        ClientModel updated = clientService.updateClient(client.getId(), dto);

        assertEquals("Novo Nome", updated.getName());
        assertEquals("novo@mail.com", updated.getMail());
        assertTrue(passwordEncoder.matches("novaSenha", updated.getPassword()));
        assertEquals(LocalDate.of(2000, 9, 2), updated.getBirthday());
    }

    @Test
    void deleteClient_ShouldRemoveFromDatabase() {
        clientService.deleteClient(client.getId());

        Optional<ClientModel> result = clientRepository.findById(client.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllClients_ShouldReturnPage() {
        var page = clientService.getAllClients(0, 10);

        assertEquals(1, page.getTotalElements());
        assertEquals(client.getMail(), page.getContent().get(0).getMail());
    }

    @Test
    void findById_ShouldReturnClient() {
        Optional<ClientModel> result = clientService.findById(client.getId());

        assertTrue(result.isPresent());
        assertEquals(client.getMail(), result.get().getMail());
    }

    @Test
    void findByName_ShouldReturnClient() {
        List<ClientModel> result = clientService.findByName("Thiago");

        assertFalse(result.isEmpty());
        assertEquals(client.getMail(), result.get(0).getMail());
    }

    @Test
    void filterByAge_ShouldReturnClient_WhenAgeMatches() {
        LocalDate today = LocalDate.now();
        int calculatedAge = Period.between(client.getBirthday(), today).getYears();

        client.setAge((long) calculatedAge);
        clientRepository.save(client);

        Optional<ClientModel> result = clientService.filterByAge(calculatedAge);

        assertTrue(result.isPresent());
        assertEquals(calculatedAge, result.get().getAge());
    }

    @Test
    void findByMail_ShouldReturnClient() {
        Optional<ClientModel> result = clientService.findByMail("thiago@test.com");

        assertTrue(result.isPresent());
        assertEquals(client.getName(), result.get().getName());
    }
}
