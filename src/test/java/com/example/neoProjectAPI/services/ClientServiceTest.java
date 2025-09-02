package com.example.neoProjectAPI.services;

import com.example.neoProjectAPI.dtos.ClientRecordDTO;
import com.example.neoProjectAPI.models.ClientModel;
import com.example.neoProjectAPI.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    private UUID clientId;
    private ClientModel clientModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientId = UUID.randomUUID();
        clientModel = new ClientModel();
        clientModel.setId(clientId);
        clientModel.setName("Thiago");
        clientModel.setMail("test@mail.com");
        clientModel.setPassword("123");
    }

    @Test
    void updateClient_ShouldUpdateAndEncodePassword_WhenPasswordIsProvided() {
        ClientRecordDTO dto = new ClientRecordDTO(
                "New Name",
                "new@mail.com",
                "newpass",
                LocalDate.parse("2000-09-02"));

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientModel));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");
        when(clientRepository.save(any(ClientModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientModel updated = clientService.updateClient(clientId, dto);

        assertEquals("New Name", updated.getName());
        assertEquals("new@mail.com", updated.getMail());
        assertEquals("encodedPass", updated.getPassword());
    }

    @Test
    void updateClient_ShouldUpdateWithoutChangingPassword_WhenPasswordIsBlank() {
        ClientRecordDTO dto = new ClientRecordDTO(
                "Another Name",
                "mail@mail.com",
                "",
                LocalDate.parse("2000-09-02")
);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientModel));
        when(clientRepository.save(any(ClientModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientModel updated = clientService.updateClient(clientId, dto);

        assertEquals("Another Name", updated.getName());
        assertEquals("mail@mail.com", updated.getMail());
        assertEquals("123", updated.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deleteClient_ShouldDelete_WhenClientExists() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientModel));

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).delete(clientModel);
    }

    @Test
    void deleteClient_ShouldDoNothing_WhenClientDoesNotExist() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        clientService.deleteClient(clientId);

        verify(clientRepository, never()).delete(any());
    }

    @Test
    void getAllClients_ShouldReturnPage() {
        Page<ClientModel> page = new PageImpl<>(List.of(clientModel));
        when(clientRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<ClientModel> result = clientService.getAllClients(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(clientModel, result.getContent().get(0));
    }

    @Test
    void findById_ShouldReturnClient_WhenExists() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientModel));

        Optional<ClientModel> result = clientService.findById(clientId);

        assertTrue(result.isPresent());
        assertEquals(clientModel, result.get());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        Optional<ClientModel> result = clientService.findById(clientId);

        assertFalse(result.isPresent());
    }

    @Test
    void findByName_ShouldReturnList() {
        when(clientRepository.findByNameContainingIgnoreCase("Thiago"))
                .thenReturn(List.of(clientModel));

        List<ClientModel> result = clientService.findByName("Thiago");

        assertEquals(1, result.size());
        assertEquals(clientModel, result.get(0));
    }

    @Test
    void filterByAge_ShouldReturnClient_WhenExists() {
        when(clientRepository.filterByAge(25)).thenReturn(Optional.of(clientModel));

        Optional<ClientModel> result = clientService.filterByAge(25);

        assertTrue(result.isPresent());
        assertEquals(clientModel, result.get());
    }

    @Test
    void filterByAge_ShouldReturnEmpty_WhenNotExists() {
        when(clientRepository.filterByAge(25)).thenReturn(Optional.empty());

        Optional<ClientModel> result = clientService.filterByAge(25);

        assertFalse(result.isPresent());
    }

    @Test
    void findByMail_ShouldReturnClient_WhenExists() {
        when(clientRepository.findByMail("test@mail.com")).thenReturn(Optional.of(clientModel));

        Optional<ClientModel> result = clientService.findByMail("test@mail.com");

        assertTrue(result.isPresent());
        assertEquals(clientModel, result.get());
    }

    @Test
    void findByMail_ShouldReturnEmpty_WhenNotExists() {
        when(clientRepository.findByMail("notfound@mail.com")).thenReturn(Optional.empty());

        Optional<ClientModel> result = clientService.findByMail("notfound@mail.com");

        assertFalse(result.isPresent());
    }
}
