package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.repository.ProductRepository;
import com.project.test.ordermanagement.service.base.CRUDBaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private CRUDBaseServiceImpl<Product, Long> productService;

    private Product product;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new CRUDBaseServiceImpl<>(productRepository);

        product = Product.builder()
                .name("Product 1")
                .description("Description of Product")
                .price(BigDecimal.TEN)
                .sku("SKU-" + UUID.randomUUID())
                .measureUnit("UN")
                .build();
    }

    @Test
    void testSave() {
        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.save(product);
        assertEquals("Product 1", saved.getName());
        verify(productRepository).save(product);
    }

    @Test
    void testFindById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> found = productService.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(BigDecimal.TEN, found.get().getPrice());
    }

    @Test
    void testFindById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> found = productService.findById(1L);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> all = productService.findAll();
        assertEquals(1, all.size());
    }

    @Test
    void testUpdate_Success() {
        Product updateData = Product.builder()
                .description("Product desc")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        Optional<Product> updated = productService.update(1L, updateData);

        assertTrue(updated.isPresent());
        verify(productRepository).save(any());
    }

    @Test
    void testUpdate_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> updated = productService.update(99L, product);
        assertFalse(updated.isPresent());
    }

    @Test
    void testDelete_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void testDelete_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            productService.delete(99L);
        });

        assertEquals("Entity not found with id: 99", ex.getMessage());
    }

}
