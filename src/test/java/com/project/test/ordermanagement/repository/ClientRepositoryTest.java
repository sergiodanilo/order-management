package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldSaveAndFindClientByTaxId() {
        Client client = Client.builder()
                .name("ACME Corp.")
                .taxId("123456789")
                .email("email@server.com")
                .phone("123456789")
                .address("123 Street Name")
                .build();

        clientRepository.save(client);

        Optional<Client> result = clientRepository.findByTaxId("123456789");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("ACME Corp.");
        assertThat(result.get().getTaxId()).isEqualTo("123456789");
    }

    @Test
    void shouldReturnEmptyWhenTaxIdNotFound() {
        Optional<Client> result = clientRepository.findByTaxId("000000000");

        assertThat(result).isEmpty();
    }

}
