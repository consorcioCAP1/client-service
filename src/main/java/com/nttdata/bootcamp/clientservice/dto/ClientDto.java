package com.nttdata.bootcamp.clientservice.dto;

import lombok.Data;


@Data
public class ClientDto {

	private String id;
	private String dni;
	private String name;
	private String typeClient;
	private String createDate;
	private String businessName;
	private String ruc;
	private Integer bankMovementLimit;
	private String typeCustomer;
	private String accountType;
	private Double accountBalance;
	private Double minimumMonthlyAmount;
	private String bankAccountNumber;
	
}
