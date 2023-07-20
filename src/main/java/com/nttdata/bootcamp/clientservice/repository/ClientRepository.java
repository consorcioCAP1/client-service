package com.nttdata.bootcamp.clientservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.bootcamp.clientservice.documents.Client;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String>{
}
