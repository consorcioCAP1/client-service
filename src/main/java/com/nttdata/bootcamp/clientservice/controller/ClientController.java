package com.nttdata.bootcamp.clientservice.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nttdata.bootcamp.clientservice.documents.Client;
import com.nttdata.bootcamp.clientservice.dto.ClientDto;
import com.nttdata.bootcamp.clientservice.dto.CustomerConsolidation;
import com.nttdata.bootcamp.clientservice.service.ClientService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class ClientController {

	@Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public Mono<Client> createClient(@RequestBody Client client) {
    	LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        client.setCreateDate(currentDate.format(formatter));
    	return clientService.createClient(client);
    }

    @GetMapping("/{id}")
    public Mono<Client> getClientById(@PathVariable String id) {
        return clientService.getClientById(id);
    }

    @GetMapping
    public Flux<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @PutMapping("/{id}")
    public Mono<Client> updateClient(@PathVariable String id, @RequestBody Client client) {
        client.setId(id);
        return clientService.updateClient(client);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteClientById(@PathVariable String id) {
        return clientService.deleteClientById(id);
    }
    //metodo creado para agregar perfil VIP
    @PostMapping("/updateClientProfileVip")
    public Mono<ResponseEntity<Object>> updateClientProfileVip(@RequestBody ClientDto clientDto) {
    	
    	return clientService.updateClientProfileVip(clientDto)
				.flatMap(objResponse -> {
			        ResponseEntity<Object> responseEntity = ResponseEntity.ok(objResponse);
			        return Mono.just(responseEntity);
		    })
		    .onErrorResume(error -> {
		        ResponseEntity<Object> responseEntity = ResponseEntity
		        		.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
		        return Mono.just(responseEntity);
		    });	
    }

    //metodo creado para agregar perfil PYME
    @PostMapping("/updateClientProfilePyme")
    public Mono<ResponseEntity<Object>> updateClientProfilePyme(@RequestBody ClientDto clientDto) {
    	
    	return clientService.updateClientProfilePyme(clientDto)
				.flatMap(objResponse -> {
			        ResponseEntity<Object> responseEntity = ResponseEntity.ok(objResponse);
			        return Mono.just(responseEntity);
		    })
		    .onErrorResume(error -> {
		        ResponseEntity<Object> responseEntity = ResponseEntity
		        		.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
		        return Mono.just(responseEntity);
		    });
    }

    @GetMapping("/getConsolidateCustomerData/{numberDocument}")
    public Flux<CustomerConsolidation> getConsolidateCustomerData(@PathVariable String numberDocument) {
        return clientService.consolidateCustomerData(numberDocument);
    }
    
}
