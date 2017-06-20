package com.mkaminski.component.bank_accounts;

public class Borrower extends BankClient {

	public Borrower(String name, String surname) {
		super(name, surname);
	}


	@Override
	public String toString() {
		return "Borrower [name=" + name + ", surname=" + surname + ", clientId=" + clientId + "]";
	}
	
}
