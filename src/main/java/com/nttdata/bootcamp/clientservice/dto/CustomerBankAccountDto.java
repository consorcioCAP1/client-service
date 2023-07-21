package com.nttdata.bootcamp.clientservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerBankAccountDto {

	private String id;
	private String typeCustomer;
	private String clientId;
	private String name;
	private String dni;
	private String businessName;
	private String ruc;
	private String openingDate;
	private Integer bankMovementLimit;
	private String accountType;
	private Double accountBalance;
	private String bankAccountNumber;
	private Double minimumMonthlyAmount;
}


