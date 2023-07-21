package com.nttdata.bootcamp.clientservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.bootcamp.clientservice.documents.Client;
import com.nttdata.bootcamp.clientservice.dto.ClientDto;
import com.nttdata.bootcamp.clientservice.repository.ClientRepository;
import com.nttdata.bootcamp.clientservice.service.ClientService;
import com.nttdata.bootcamp.clientservice.utilities.ExternalApiService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements ClientService{
	
	@Autowired
    private ClientRepository repository;

	@Autowired
	ExternalApiService externalApiService;

	private static final String TYPE_CLIENT_VIP = "VYP_PERSONAL";
	private static final String TYPE_CLIENT_BUSINESS_PYME = "PYME_BUSSINES";
	private static final double MINIMUN_MONTHLY_AMOUNT= 500.00;
	private static final String TYPE_CREDIT_ACCOUNT = "CREDITCARD";
	private static final String TYPE_ACCOUNT_BANK_SAVING = "SAVING";
	private static final String TYPE_ACCOUNT_BANK_CURRENT = "CURRENT";
	
	@Override
    public Mono<Client> createClient(Client client) {
        return repository.save(client);
    }

    @Override
    public Mono<Client> getClientById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Client> getAllClients() {
        return repository.findAll();
    }

    @Override
    public Mono<Client> updateClient(Client client) {
        return repository.save(client);
    }

    @Override
    public Mono<Void> deleteClientById(String id) {
        return repository.deleteById(id);
    }
    
    //metodo para registrar cliente vip validando que tenga al menos una CREDITCARD
    @Override
    public Mono<Client> updateClientProfileVip(ClientDto clientDto){
		 return repository.findById(clientDto.getId())
            .flatMap(client -> externalApiService.getCreditAccountByDniType(client.getDni(), TYPE_CREDIT_ACCOUNT)
                .switchIfEmpty(
                		Mono.error(
                            	new RuntimeException("Cliente debe tener Tarjeta de crédito")))
                .flatMap(creditAccount -> {
                   try {
	            	    
	            	    client.setTypeClient(TYPE_CLIENT_VIP);
	            	    clientDto.setDni(client.getDni());
	            	    clientDto.setName(client.getName());
	            	    clientDto.setAccountType(TYPE_ACCOUNT_BANK_SAVING);
	                	clientDto.setMinimumMonthlyAmount(MINIMUN_MONTHLY_AMOUNT);
	                	clientDto.setTypeCustomer(TYPE_CLIENT_VIP);
						externalApiService.createBankAccount(clientDto);
						return repository.save(client);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;          
                    
                })
            );
    }

    //metodo para registrar cliente empresario MYPE
    @Override
    public Mono<Client> updateClientProfilePyme(ClientDto clientDto){
    	 return repository.findById(clientDto.getId())
            .flatMap(client -> externalApiService.getCreditAccountByRucType(client.getRuc(), TYPE_CREDIT_ACCOUNT)
                .count()
                .flatMap(countCreditAccount -> {
                    if (countCreditAccount == 0) {
                        return Mono.error(new RuntimeException("Cliente debe tener Tarjeta de crédito"));
                    } else {
                        return externalApiService.getBankAccountByRucType(client.getRuc(), TYPE_ACCOUNT_BANK_CURRENT)
                    		.flatMap(countBankAccount -> {
                            if (countBankAccount == 0) {
                                return Mono.error(new RuntimeException("Cliente debe tener Cuenta Corriente"));
                            } else {
                                client.setTypeClient(TYPE_CLIENT_BUSINESS_PYME);
                                return repository.save(client);
                            }
                        });
                    }
                })
            );
    }
}
