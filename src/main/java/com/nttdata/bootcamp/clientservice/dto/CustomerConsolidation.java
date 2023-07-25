package com.nttdata.bootcamp.clientservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerConsolidation {
	private String typeCustomer;
	private String clientId;
	private String clientName;
	private String numberDocument;
	private String openingDate;
	private String accountType;
	private Double accountBalance;
	private String bankAccountNumber;
}
