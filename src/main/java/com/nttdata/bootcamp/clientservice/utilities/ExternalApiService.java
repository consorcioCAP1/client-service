package com.nttdata.bootcamp.clientservice.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.bootcamp.clientservice.dto.ClientDto;
import com.nttdata.bootcamp.clientservice.dto.CreditBankAccountDto;
import com.nttdata.bootcamp.clientservice.dto.CustomerBankAccountDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ExternalApiService {
   @Value("${credit-bank-account.api.url}")
    private String creditAccountBankUrl;
   
   @Value("${customer-bank-account.api.url}")
   private String customerBankAccountBankUrl;

   //metodo para obtener document creditBankAccount
   public Mono<CreditBankAccountDto> getCreditAccountByDniType(String dni, String type) {
		WebClient webClient = WebClient.create(creditAccountBankUrl); 
		 return webClient.get()
	                .uri("/findByDniAndTypeAccount/{dni}/{type}", dni, type)
	                .retrieve()
	                .bodyToMono(CreditBankAccountDto.class);
	}

   public Flux<CreditBankAccountDto> getCreditAccountByRucType(String ruc, String type) {
		WebClient webClient = WebClient.create(creditAccountBankUrl); 
		 return webClient.get()
	                .uri("/findByRucAndTypeAccount/{ruc}/{type}", ruc, type)
	                .retrieve()
	                .bodyToFlux(CreditBankAccountDto.class);
	}

   public Mono<Long> getBankAccountByRucType(String ruc, String type) {
		WebClient webClient = WebClient.create(customerBankAccountBankUrl); 
		 return webClient.get()
	                .uri("/getBankAccountByRucAndType/{ruc}/{type}", ruc, type)
	                .retrieve()
	                .bodyToMono(Long.class);
	}

	//metodo para el consumo de la api de creacion de deudas de credito
	public void createBankAccount(ClientDto client) throws JsonProcessingException {
		LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		CustomerBankAccountDto bankAccountDto = CustomerBankAccountDto.builder()
				.dni(client.getDni())
				.name(client.getName())
				.openingDate(currentDate.format(formatter))
				.bankMovementLimit(client.getBankMovementLimit())
				.typeCustomer(client.getTypeCustomer())
				.accountType(client.getAccountType())
				.accountBalance(client.getAccountBalance())
				.bankAccountNumber(client.getBankAccountNumber())
				.minimumMonthlyAmount(client.getMinimumMonthlyAmount())
				.build();

		String objectToJson;
		objectToJson = ConvertJson.toJson(bankAccountDto);
		WebClient webClient = WebClient.create(customerBankAccountBankUrl);
		webClient.post()
	        .uri("/createCustomerPersonalAccount")
	        .contentType(MediaType.APPLICATION_JSON)
	        .body(BodyInserters.fromValue(objectToJson))
	        .retrieve()
	        .bodyToMono(String.class)
	        .subscribe();				
	}
}
