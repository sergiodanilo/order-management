package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.model.Resale;
import com.project.test.ordermanagement.repository.ResaleRepository;
import com.project.test.ordermanagement.service.base.CRUDBaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResaleServiceTest {

    private ResaleRepository resaleRepository;
    private CRUDBaseServiceImpl<Resale, Long> resaleService;

    private Resale resale;

    String taxId = UUID.randomUUID().toString().substring(0, 13);

    @BeforeEach
    void setUp() {
        resaleRepository = mock(ResaleRepository.class);
        resaleService = new CRUDBaseServiceImpl<>(resaleRepository);

        resale = Resale.builder()
                .businessName("Comercial Exemplo LTDA")
                .contractName("João da Silva")
                .deliveryAddress("Rua da Entrega, 456")
                .email("contato@exemplo.com")
                .phone("11999887766")
                .taxId(taxId)
                .tradeName("Comercial Exemplo")
                .build();
    }

    @Test
    void testSave() {
        when(resaleRepository.save(resale)).thenReturn(resale);

        Resale saved = resaleService.save(resale);
        assertEquals("Comercial Exemplo LTDA", saved.getBusinessName());
        verify(resaleRepository).save(resale);
    }

    @Test
    void testFindById_Found() {
        when(resaleRepository.findById(1L)).thenReturn(Optional.of(resale));

        Optional<Resale> found = resaleService.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(taxId, found.get().getTaxId());
    }

    @Test
    void testFindById_NotFound() {
        when(resaleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Resale> found = resaleService.findById(1L);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        when(resaleRepository.findAll()).thenReturn(List.of(resale));

        List<Resale> all = resaleService.findAll();
        assertEquals(1, all.size());
    }

    @Test
    void testUpdate_Success() {
        Resale updateData = Resale.builder()
                .taxId("5464654")
                .build();

        when(resaleRepository.findById(1L)).thenReturn(Optional.of(resale));
        when(resaleRepository.save(any())).thenReturn(resale);

        Optional<Resale> updated = resaleService.update(1L, updateData);

        assertTrue(updated.isPresent());
        verify(resaleRepository).save(any());
    }

    @Test
    void testUpdate_NotFound() {
        when(resaleRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Resale> updated = resaleService.update(99L, resale);
        assertFalse(updated.isPresent());
    }

    @Test
    void testDelete_Success() {
        when(resaleRepository.findById(1L)).thenReturn(Optional.of(resale));

        resaleService.delete(1L);

        verify(resaleRepository).delete(resale);
    }

    @Test
    void testDelete_NotFound() {
        when(resaleRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            resaleService.delete(99L);
        });

        assertEquals("Entity not found with id: 99", ex.getMessage());
    }

}
