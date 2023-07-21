package com.nttdata.bootcamp.clientservice.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Data
@Builder
@Document(collection = "client")
public class Client {

	@Id
	private String id;
	private String dni;
	private String name;
	private String typeClient;
	private String createDate;
	private String businessName;
	private String ruc;
	private String bankAccountNumber;
	
}
