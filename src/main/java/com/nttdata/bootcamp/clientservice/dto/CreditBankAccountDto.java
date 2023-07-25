package com.nttdata.bootcamp.clientservice.dto;

import lombok.Data;

@Data
public class CreditBankAccountDto {

	private String id;
	private String typeCustomer;
	private String clientId;
	private String clientName;
	private String numberDocument;
	private int paymentBankFee;
	private String paymentStartDate;
	private Double paymentAmountBankFee;
	private Double accountBalance;
	private String bankAccountNumber;
	private String accountType;
	private String openingDate;
}


