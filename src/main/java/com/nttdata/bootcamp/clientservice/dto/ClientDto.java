package com.nttdata.bootcamp.clientservice.dto;

import lombok.Data;


@Data
public class ClientDto {

	private String id;
	private String clientName;
	private String numberDocument;
	private String typeClient;
	private String createDate;
	private Integer bankMovementLimit;
	private String typeCustomer;
	private String accountType;
	private Double accountBalance;
	private Double minimumMonthlyAmount;
	private String bankAccountNumber;
	
}
