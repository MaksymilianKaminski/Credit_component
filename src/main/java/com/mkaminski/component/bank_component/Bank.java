package com.mkaminski.component.bank_component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.mkaminski.component.bank_accounts.BankClient;
import com.mkaminski.component.bank_accounts.Borrower;
import com.mkaminski.component.bank_services.Credit;
import com.mkaminski.component.bank_services.Mortgage;

public class Bank {

	public static void main(String[] args) {

		BankClient bc1 = new Borrower("Pawe�", "Kami�ski");
		BankClient bc2 = new Borrower("Konrad", "Ochocza");

		Credit cr1 = new Mortgage("Kredyt hipoteczny - kr�tkoterminowy", bc1, 200000.0, 120, 4.11);
		Credit cr2 = new Mortgage("Kredyt hipoteczny - d�ugoterminowy", bc2, 300000.0, 360, 3.48);

		System.out.println("| Operacja                                        | Data                     Warto��");

//		cr1.changeOverpayment(2017, 11, 11, 2000.0);
//		cr1.changeWibor3M(2017, 10, 8, 2.3);
//		cr1.changeOverpayment(2018, 11, 16, 1000.0);
//		cr1.changeWibor3M(2017, 12, 31, 2.5);

		cr1.repaymentSchedule();

		System.out
				.println("\nPodsumowanie: \n" + cr1.creditInfo() + "\n| Ca�kowity planowany koszt kredytu: " + cr1.finalCreditCost()
						+ "    | Warto�� odsetek: " + cr1.finalInterestCost() + "\n| Rata sta�a: "
						+ new BigDecimal(cr1.calculateInstallment().toString()).setScale(2, RoundingMode.HALF_UP)
								.doubleValue()
						+ "      | Ilo�� rat: " + cr1.finalPayMonthlyTerm() + "       | Data sp�acenia: "
						+ cr1.finalPayMonthlyDate() + "\n");
	}

}
