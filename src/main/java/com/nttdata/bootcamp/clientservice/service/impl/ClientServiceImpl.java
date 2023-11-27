package com.nttdata.bootcamp.clientservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.bootcamp.clientservice.documents.Client;
import com.nttdata.bootcamp.clientservice.dto.ClientDto;
import com.nttdata.bootcamp.clientservice.dto.CreditBankAccountDto;
import com.nttdata.bootcamp.clientservice.dto.CustomerBankDto;
import com.nttdata.bootcamp.clientservice.dto.CustomerConsolidation;
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
		 return repository.findByNumberDocument(clientDto.getNumberDocument())
            .flatMap(client -> externalApiService.getCreditAccountByDniType(client.getNumberDocument(),
            			TYPE_CREDIT_ACCOUNT)
                .switchIfEmpty(
                		Mono.error(
                            	new RuntimeException("Cliente debe tener Tarjeta de crédito")))
                .flatMap(creditAccount -> {
                   try {
	            	    client.setTypeClient(TYPE_CLIENT_VIP);
	            	    clientDto.setClientName(client.getClientName());
	            	    clientDto.setNumberDocument(client.getNumberDocument());
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
		 return repository.findByNumberDocument(clientDto.getNumberDocument())
	        .flatMap(client -> externalApiService.getCreditAccountByRucType(client.getNumberDocument(),
	        					TYPE_CREDIT_ACCOUNT)
	            .count()
	            .flatMap(countCreditAccount -> {
	                if (countCreditAccount == 0) {
	                    return Mono.error(new RuntimeException("Cliente debe tener Tarjeta de crédito"));
	                } else {
	                    return externalApiService.getBankAccountByRucType(client.getBankAccountNumber(),
	                    					TYPE_ACCOUNT_BANK_CURRENT)
	                		.flatMap(countBankAccount -> {
	                        if (countBankAccount == 0) {
	                            return Mono.error(new RuntimeException("Cliente debe poseer un Cuenta Corriente"));
	                        } else {
	                            client.setTypeClient(TYPE_CLIENT_BUSINESS_PYME);
	                            return repository.save(client);
	                        }
	                    });
	                }
	            })
	        );
    }

    //metodo para buscar el consolidado del cliente de sus productos
    @Override
    public Flux<CustomerConsolidation> consolidateCustomerData(String numberDocument){
        Flux<CreditBankAccountDto> creditAccountFlux = externalApiService
        							.getCreditAccountByNumberDocument(numberDocument);
        Flux<CustomerBankDto> bankAccountFlux = externalApiService
        							.getBankAccountByNumberDocument(numberDocument);
        
        //busco productos de credito
        Flux<CustomerConsolidation> creditConsolidationFlux = creditAccountFlux
                .map(credit -> CustomerConsolidation.builder()
                        .typeCustomer(credit.getTypeCustomer())
                        .clientId(credit.getClientId())
                        .clientName(credit.getClientName())
                        .numberDocument(credit.getNumberDocument())
                        .openingDate(credit.getOpeningDate())
                        .accountType(credit.getAccountType())
                        .accountBalance(credit.getAccountBalance())
                        .bankAccountNumber(credit.getBankAccountNumber())
                        .build());

        //busco productos de bancarios
        Flux<CustomerConsolidation> bankConsolidationFlux = bankAccountFlux
                .map(bankAccount -> CustomerConsolidation.builder()
                        .typeCustomer(bankAccount.getTypeCustomer())
                        .clientId(bankAccount.getClientId())
                        .clientName(bankAccount.getClientName())
                        .numberDocument(bankAccount.getNumberDocument())
                        .openingDate(bankAccount.getOpeningDate())
                        .accountType(bankAccount.getAccountType())
                        .accountBalance(bankAccount.getAccountBalance())
                        .bankAccountNumber(bankAccount.getBankAccountNumber())
                        .build());
        //los combino ambos en un mismo flux
        return Flux.concat(creditConsolidationFlux, bankConsolidationFlux);
    }

}
