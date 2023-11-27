package com.nttdata.bootcamp.clientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);

		//comentario agregado para probar el primer commit 
		//comentario numero 2 para probar el segundo commit 
		//borramos una linea y agregamos el comentario numero 3
		//cuarto comentario para 4 commits 
		//con todo esto estamos prvando
		//probando commit numero 5 con cambios en lineas 
		//esto es un commit nuevo
		//para despues hacer el stash
		//cambios en el commit 5  
	}

}
