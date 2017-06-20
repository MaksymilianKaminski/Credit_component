package com.mkaminski.component.bank_services;

public interface CreditTools {

	public void changeWibor3M(int year, int month, int day, Double changing);
	public void changeOverpayment(int year, int month, int day, Double changing);

}
