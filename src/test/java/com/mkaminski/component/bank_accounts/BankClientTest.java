package com.mkaminski.component.bank_accounts;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.mkaminski.component.bank_accounts.BankClient;
import com.mkaminski.component.bank_accounts.Borrower;

public class BankClientTest {
	BankClient bc1;
	BankClient bc2;
	
	@Before
	public void setUp() {
	bc1 = new Borrower("Pawe³", "Kamiñski");
	bc2 = new Borrower("Konrad", "Ochocza");
	}
	
	@Test
	public void testClientId(){
		assertEquals(1,bc1.getClientId());
		assertEquals(2,bc2.getClientId());	
	}

}
