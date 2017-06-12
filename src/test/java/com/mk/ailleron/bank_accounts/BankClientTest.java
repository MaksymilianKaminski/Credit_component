package com.mk.ailleron.bank_accounts;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankClientTest {
	BankClient bc1;
	BankClient bc2;
	
	@Before
	public void setUp() {
	bc1 = new Borrower("Pawe�", "Kami�ski");
	bc2 = new Borrower("Konrad", "Ochocza");
	}
	
	@Test
	public void testClientId(){
		assertEquals(1,bc1.getClientId());
		assertEquals(2,bc2.getClientId());	
	}

}
