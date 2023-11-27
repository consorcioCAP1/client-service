package com.nttdata.bootcamp.clientservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.bootcamp.clientservice.documents.Client;

import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String>{

	Mono<Client> findByNumberDocument(String numberDocument);
}
