package com.nttdata.bootcamp.clientservice.service;

import com.nttdata.bootcamp.clientservice.documents.Client;
import com.nttdata.bootcamp.clientservice.dto.ClientDto;
import com.nttdata.bootcamp.clientservice.dto.CustomerConsolidation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {

    public Mono<Client> createClient(Client client);
    public Mono<Client> getClientById(String id);
    public Flux<Client> getAllClients();
    public Mono<Client> updateClient(Client client);
    public Mono<Void> deleteClientById(String id);
    public Mono<Client> updateClientProfileVip(ClientDto clientDto);
    public Mono<Client> updateClientProfilePyme(ClientDto clientDto);
    public Flux<CustomerConsolidation> consolidateCustomerData(String numberDocument);
}

