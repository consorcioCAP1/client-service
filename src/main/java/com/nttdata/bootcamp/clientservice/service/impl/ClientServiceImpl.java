package com.nttdata.bootcamp.clientservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nttdata.bootcamp.clientservice.documents.Client;
import com.nttdata.bootcamp.clientservice.repository.ClientRepository;
import com.nttdata.bootcamp.clientservice.service.ClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements ClientService{
	
	@Autowired
    private ClientRepository clientRepository;

    public Mono<Client> createClient(Client client) {
        return clientRepository.save(client);
    }

    public Mono<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    public Flux<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Mono<Client> updateClient(Client client) {
        return clientRepository.save(client);
    }

    public Mono<Void> deleteClientById(String id) {
        return clientRepository.deleteById(id);
    }
}
