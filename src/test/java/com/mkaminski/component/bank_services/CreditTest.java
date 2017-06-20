package com.mkaminski.component.bank_services;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.mkaminski.component.bank_accounts.BankClient;
import com.mkaminski.component.bank_accounts.Borrower;
import com.mkaminski.component.bank_services.Credit;
import com.mkaminski.component.bank_services.Mortgage;

public class CreditTest {
	
	BankClient bc1;
	BankClient bc2;
	Credit cr1;
	Credit cr2;

	@Before
	public void setUp() {
		bc1 = new Borrower("Pawe³", "Kamiñski");
		bc2 = new Borrower("Konrad", "Ochocza");
		cr1 = new Mortgage("Kredyt hipoteczny - krótkoterminowy", bc1, 200000.0, 120, 4.11);
		cr2 = new Mortgage("Kredyt hipoteczny - d³ugoterminowy", bc2, 300000.0, 360, 3.48);
	}

	@Test
	public void testCreditId() {
		assertEquals(1, cr1.getCreditId());
		assertEquals(2, cr2.getCreditId());
	}

	@Test
	public void testInitCreditStartDate() {
		boolean testSuccess = true;
		
		if (cr1.getCreditStartDate().isAfter(cr1.getInstallmentStartDate())) {
			testSuccess = false;
		}
		if (cr1.getCreditStartDate().isEqual(cr1.getInstallmentStartDate())) {
			testSuccess = false;
		}
		// first installment expected 10 day of next month if day of month >= 10
		// tested for 12.06.2017
		if (cr1.getInstallmentStartDate().isEqual(LocalDate.of(2017, 07, 10))) {
			testSuccess = true;
		}
		
		assertTrue(testSuccess);

		assertEquals(10, cr1.getInstallmentStartDate().getDayOfMonth());
	}
	
}
