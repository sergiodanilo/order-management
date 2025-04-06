package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.repository.ClientRepository;
import com.project.test.ordermanagement.service.base.CRUDBaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private ClientRepository clientRepository;
    private CRUDBaseServiceImpl<Client, Long> clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        clientService = new CRUDBaseServiceImpl<>(clientRepository);

        client = Client.builder()
                .id(1L)
                .name("Test Client")
                .email("test@email.com")
                .address("123 Test Street")
                .taxId("12345678901234")
                .build();
    }

    @Test
    void testSave() {
        when(clientRepository.save(client)).thenReturn(client);

        Client saved = clientService.save(client);
        assertEquals("Test Client", saved.getName());
        verify(clientRepository).save(client);
    }

    @Test
    void testFindById_Found() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> found = clientService.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("Test Client", found.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Client> found = clientService.findById(1L);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<Client> all = clientService.findAll();
        assertEquals(1, all.size());
    }

    @Test
    void testUpdate_Success() {
        Client updateData = Client.builder()
                .name("Updated Name")
                .build();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any())).thenReturn(client);

        Optional<Client> updated = clientService.update(1L, updateData);

        assertTrue(updated.isPresent());
        verify(clientRepository).save(any());
    }

    @Test
    void testUpdate_NotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Client> updated = clientService.update(99L, client);
        assertFalse(updated.isPresent());
    }

    @Test
    void testDelete_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.delete(1L);

        verify(clientRepository).delete(client);
    }

    @Test
    void testDelete_NotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            clientService.delete(99L);
        });

        assertEquals("Entity not found with id: 99", ex.getMessage());
    }

}
