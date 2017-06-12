package com.mk.ailleron.bank_services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.mk.ailleron.bank_accounts.BankClient;
import com.mk.ailleron.bank_accounts.Borrower;

public class MortgageTest {

	BankClient bc1;
	BankClient bc2;
	Mortgage m1;
	Credit cr1;
	Credit cr2;
	Credit cr3;
	Credit cr4;

	// Assumed that the input data are correct in the constructor
	@Before
	public void setUp() {
		bc1 = new Borrower("Pawe³", "Kamiñski");
		bc2 = new Borrower("Konrad", "Ochocza");
		m1 = new Mortgage("Kredyt hipoteczny - krótkoterminowy", bc1, 200000.0, 120, 4.11);
		cr1 = new Mortgage("Kredyt hipoteczny - krótkoterminowy", bc1, 200000.0, 120, 4.11);
		cr2 = new Mortgage("Kredyt hipoteczny - d³ugoterminowy", bc2, 300000.0, 360, 3.48);
		cr3 = new Mortgage("Kredyt hipoteczny - krótkoterminowy", bc1, 200000.0, 1, 4.11);
		cr4 = new Mortgage("Kredyt hipoteczny - krótkoterminowy", bc2, 1.0, 120, 4.11);
	}

	@Test
	public void testCalculateInstallment() {
		// based on http://www.kalkulatorkredytowy.jakzrobicwexcelu.pl/
		// http://jakkupicmieszkanie.com/2016/08/19/raty-rowne-malejace-najlepiej-splacac-kredyt-hipoteczny/

		assertEquals(2204.37, cr1.calculateInstallment(), 1);

		// when run repaymentSchedule() it must be constant
		cr1.repaymentSchedule();
		assertEquals(2204.37, cr1.calculateInstallment(), 1);

		// when run repaymentSchedule() and change wibor and overpayment
		cr1.changeOverpayment(2017, 7, 10, 2000.0);
		cr1.changeWibor3M(2017, 10, 8, 2.0);
		cr1.repaymentSchedule();
		assertEquals(2204.37, cr1.calculateInstallment(), 1);
	}

	@Test
	public void testFinalCreditCost() {
		boolean testSuccess = true;

		// without changed wibor and overpayment
		cr1.repaymentSchedule();
		assertEquals(264524.40, cr1.finalCreditCost, 1);
		cr2.repaymentSchedule();
		assertEquals(593707.10, cr2.finalCreditCost, 1);

		// only 1 payMonthlyTerm
		cr3.repaymentSchedule();
		assertEquals(200973.33, cr3.finalCreditCost, 1);

		// when changed wibor and overpayment
		cr1.changeOverpayment(2017, 7, 10, 2000.0);
		cr1.changeWibor3M(2017, 10, 8, 2.0);
		cr1.repaymentSchedule();
		if (264524.40 == cr1.finalCreditCost) {
			testSuccess = false;
		}

		// when changed wibor
		cr1.changeWibor3M(2017, 10, 8, 2.0);
		cr1.repaymentSchedule();
		if (264524.40 == cr1.finalCreditCost) {
			testSuccess = false;
		}

		// when changed overpayment
		cr1.changeOverpayment(2017, 7, 10, 2000.0);
		cr1.repaymentSchedule();
		if (264524.40 == cr1.finalCreditCost) {
			testSuccess = false;
		}
		assertTrue(testSuccess);
	}

	@Test
	public void testFinalPayMonthlyDate() {

		// tested when creditStartDate is 12.06.2017
		cr1.repaymentSchedule();
		if (!(LocalDate.of(2027, 06, 10).isEqual(cr1.finalPayMonthlyDate()))) {
			fail();
		}

		// only 1 payMonthlyTerm
		cr3.repaymentSchedule();
		if (!(cr3.installmentStartDate.isEqual(cr3.finalPayMonthlyDate()))) {
			fail();
		}

		// when changed huge overpayment
		cr1.changeOverpayment(2017, 7, 10, 6000.0);
		cr1.repaymentSchedule();
		if ((LocalDate.of(2027, 06, 10).isEqual(cr1.finalPayMonthlyDate()))) {
			fail();
		}
	}

	@Test
	public void testValidDate() {
		
		// invalid date
		m1.changeOverpayment(2017, 13, 32, 1000.0);
		m1.repaymentSchedule();
		assertTrue(m1.isDateValid() == false);
	}

	@Test
	// changeOverpaymentAction has same structure
	public void testChangeWibor3MAction() {

		// date out of credit term range
		m1.changeOverpayment(3017, 7, 10, 6000.0);
		m1.repaymentSchedule();
		assertEquals(264524.40, m1.finalCreditCost, 1);

		// last installment, if indicate no changes are included
		m1.changeOverpayment(2027, 6, 10, 6000.0);
		m1.repaymentSchedule();
		assertEquals(0.0, m1.getOverpayment(), 0);

		// before last installment - last installment contain 0.0 overpayment
		m1.changeOverpayment(2027, 5, 10, 6000.0);
		m1.repaymentSchedule();
		assertEquals(0.0, m1.getOverpayment(), 0);

		// wibor without changing
		m1.repaymentSchedule();
		assertEquals(0.00486, m1.getWibor3M(), 2);

		// changed wibor and repaymentSchedule() reaction
		// The entered order is more important than date order
		m1.changeWibor3M(2024, 4, 9, 3.0);
		m1.changeWibor3M(2024, 5, 10, 2.0);
		m1.repaymentSchedule();
		assertEquals(0.00509, m1.getWibor3M(), 2);

		// the same date
		m1.changeWibor3M(2024, 5, 10, 3.0);
		m1.changeWibor3M(2024, 5, 10, 2.0);
		m1.repaymentSchedule();
		Double fc1 = m1.finalCreditCost;
		assertEquals(0.00509, m1.getWibor3M(), 2);

		// the same date and additionally changeOverpayment()
		m1.changeOverpayment(2024, 5, 10, 3000.0);
		m1.changeWibor3M(2024, 5, 10, 2.0);
		m1.repaymentSchedule();
		Double fc2 = m1.finalCreditCost;
		if (fc1 == fc2) {
			fail();
		}
		assertEquals(0.00509, m1.getWibor3M(), 2);
	}

	// effect in changed wibor/overpayment for installments I checked on console

}
