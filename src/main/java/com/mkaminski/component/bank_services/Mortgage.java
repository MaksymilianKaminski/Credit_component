package com.mkaminski.component.bank_services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;

import com.mkaminski.component.bank_accounts.BankClient;

public class Mortgage extends Credit {

	private Double wibor3M = 1.73;
	private Double overpayment = 0.0;
	private Double totalInterest = (super.bankMargin + this.wibor3M) / 100;
	private HashMap<LocalDate, Double> changeWibor3MList;
	private HashMap<LocalDate, Double> changeOverpaymentList;
	private boolean dateValid = true;
	private LocalDate finalPayMonthlyDate = super.installmentStartDate;
	private int installmentCount = 0;

	
	public Mortgage(String creditName, BankClient borrower, Double borrowedCapital, int payMonthlyTerm,
			Double bankMargin) {
		super(creditName, borrower, borrowedCapital, payMonthlyTerm, bankMargin);
		super.installment = calculateInstallment();
		
		changeWibor3MList = new HashMap<LocalDate, Double>();
		changeOverpaymentList = new HashMap<LocalDate, Double>();
	}

	
	@Override
	public Double calculateInstallment() {
		int interestPeriod = 12;
		Double interestRatio = 1 + (totalInterest / interestPeriod);

		Double installment = super.borrowedCapital * Math.pow(interestRatio, super.payMonthlyTerm) * (interestRatio - 1)
				/ ((Math.pow(interestRatio, super.payMonthlyTerm) - 1));

		return installment;
	}
	

	@Override
	public void repaymentSchedule() {
		this.installmentCount = 1;
		LocalDate currentInstallmentDate = super.installmentStartDate;
		LocalDate afterDate = super.installmentStartDate.plusMonths(1);

		super.finalCreditCost = super.borrowedCapital;
		Double capital = super.borrowedCapital;
		Double capitalBefore;
		Double monthlyInterest = this.totalInterest / 12;

		System.out.println("\nPlan sp³aty: ");
		
		if (payMonthlyTerm!=1) {				
		while (capital >= super.installment) {

			monthlyInterest = changeWibor3MAction(currentInstallmentDate, monthlyInterest);
			this.overpayment = changeOverpaymentAction(currentInstallmentDate, this.overpayment);

			if (currentInstallmentDate == super.installmentStartDate) {

				capital = (super.borrowedCapital - this.overpayment) * (1 + monthlyInterest) - super.installment;
				
				super.finalCreditCost = super.finalCreditCost + super.borrowedCapital * monthlyInterest
						+ this.overpayment;

				System.out.println("~~~~~~~~" + "\nNumer Raty: " + this.installmentCount++ + "\nData: "
						+ currentInstallmentDate + "\nSp³acony kapita³: " + capital + "\nOdsetki: "
						+ super.borrowedCapital * monthlyInterest + "\nOprocentowanie miesiêczne: " + monthlyInterest
						+ "\nNadp³ata: " + this.overpayment);
				
				currentInstallmentDate = currentInstallmentDate.plusMonths(1);
			}

			if (currentInstallmentDate.isEqual(afterDate)) {

				monthlyInterest = changeWibor3MAction(currentInstallmentDate, monthlyInterest);
				this.overpayment = changeOverpaymentAction(currentInstallmentDate, this.overpayment);
			}

			capitalBefore = capital;
			capital = (capital - this.overpayment) * (1 + monthlyInterest) - super.installment;
			
			super.finalCreditCost = super.finalCreditCost + capitalBefore * monthlyInterest + this.overpayment;

			System.out.println(
					"~~~~~~~~" + "\nNumer Raty: " + this.installmentCount++ + "\nData: " + currentInstallmentDate
							+ "\nSp³acony kapita³: " + capital + "\nOdsetki: " + capitalBefore * monthlyInterest
							+ "\nOprocentowanie miesiêczne: " + monthlyInterest + "\nNadp³ata: " + this.overpayment);
			
			currentInstallmentDate = currentInstallmentDate.plusMonths(1);

			if (capital - overpayment <= installment) {

				capitalBefore = capital;
				overpayment = 0.0;
				capital = 0.0;
				
				super.finalCreditCost = super.finalCreditCost + capitalBefore * monthlyInterest;
				this.finalPayMonthlyDate = currentInstallmentDate;
				
				System.out.println("~~~~~~~~" + "\nNumer Raty: " + this.installmentCount + "\nData: "
						+ currentInstallmentDate + "\nSp³acony kapita³: " + capital + "\nOdsetki: "
						+ capitalBefore * monthlyInterest + "\nOprocentowanie miesiêczne: " + monthlyInterest
						+ "\nNadp³ata: " + this.overpayment);
			}
		}
		} else {
			super.finalCreditCost = super.finalCreditCost + super.borrowedCapital * monthlyInterest;
		}
	}

	
	@Override
	public void changeWibor3M(int year, int month, int day, Double changing) {
		LocalDate changedInstallment = validDate(year, month, day);
		
		if (dateValid == true) {
		changeWibor3MList.put(changedInstallment, changing);
		
		System.out.println("| Zmiana WIBOR3M                                  | "
				+ String.format("%d-%d-%d", year, month, day) + "                " + changing + "% ");
	    }
	}
	
	
	@Override
	public void changeOverpayment(int year, int month, int day, Double changing) {
		LocalDate changedInstallment = validDate(year, month, day);
		
		if (dateValid == true) {
			changeOverpaymentList.put(changedInstallment, changing);
			
			System.out.println("| Od teraz bêdê nadp³acaæ kredyt w kwocie         | "
					+ String.format("%d-%d-%d", year, month, day) + "                " + changing + " PLN");
		}
	}

	
	public Double changeWibor3MAction(LocalDate currentInstallmentDate, Double monthlyInterest) {
		
		if (changeWibor3MList.containsKey(currentInstallmentDate)) {

			System.out.println("\nOprocentowanie Wibor3M zmienione!  Obowi¹zuje: " + currentInstallmentDate
					+ "  Zaktualizowana wartoœæ: " + this.changeWibor3MList.get(currentInstallmentDate) + "%");
			
			return (super.bankMargin + this.changeWibor3MList.get(currentInstallmentDate)) / (12*100);
		}
		return monthlyInterest;
	}

	
	public Double changeOverpaymentAction(LocalDate currentInstallmentDate, Double overpayment) {
		
		if (changeOverpaymentList.containsKey(currentInstallmentDate)) {
			
			System.out.println("\nKwota nadp³aty zmieniona!  Obowi¹zuje: " + currentInstallmentDate
					+ "  Zaktualizowana wartoœæ: " + this.changeOverpaymentList.get(currentInstallmentDate) + " PLN");
			
			return this.changeOverpaymentList.get(currentInstallmentDate);
		}
		return overpayment;
	}

	
	public LocalDate validDate(int year, int month, int day) {
		LocalDate date = LocalDate.MIN;
		
		try {
			date = LocalDate.of(year, month, day);
		} catch (DateTimeException e) {
			System.out.println("Nieprawid³owa data! " + String.format("%d-%d-%d", year, month, day));
			this.dateValid = false;
			return date;
		}
		if (day <= 10) {
			date = LocalDate.of(year, month, 10);
			this.dateValid = true;
		} else {
			date = date.plusMonths(1);
			date = LocalDate.of(date.getYear(), date.getMonth(), 10);
			this.dateValid = true;
		}
		return date;
	}

	
	@Override
	public Double finalCreditCost() {
		return new BigDecimal(this.finalCreditCost.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	@Override
	public LocalDate finalPayMonthlyDate() {
		return this.finalPayMonthlyDate;
	}

	@Override
	public int finalPayMonthlyTerm() {
		return this.installmentCount;
	}

	@Override
	public Double finalInterestCost() {
		Double finalInterestCost = this.finalCreditCost - this.borrowedCapital;
		return new BigDecimal(finalInterestCost.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}


	public Double getWibor3M() {
		return wibor3M;
	}
	public void setWibor3M(Double wibor3m) {
		wibor3M = wibor3m;
	}
	public Double getOverpayment() {
		return overpayment;
	}
	public void setOverpayment(Double overpayment) {
		this.overpayment = overpayment;
	}
	public HashMap<LocalDate, Double> getChangeWibor3MList() {
		return changeWibor3MList;
	}
	public void setChangeWibor3MList(HashMap<LocalDate, Double> changeWibor3MList) {
		this.changeWibor3MList = changeWibor3MList;
	}
	public HashMap<LocalDate, Double> getChangeOverpaymentList() {
		return changeOverpaymentList;
	}
	public void setChangeOverpaymentList(HashMap<LocalDate, Double> changeOverpaymentList) {
		this.changeOverpaymentList = changeOverpaymentList;
	}
	public int getInstallmentCount() {
		return installmentCount;
	}
	public void setInstallmentCount(int installmentCount) {
		this.installmentCount = installmentCount;
	}
	public boolean isDateValid() {
		return dateValid;
	}
	public void setDateValid(boolean dateValid) {
		this.dateValid = dateValid;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((changeOverpaymentList == null) ? 0 : changeOverpaymentList.hashCode());
		result = prime * result + ((changeWibor3MList == null) ? 0 : changeWibor3MList.hashCode());
		result = prime * result + (dateValid ? 1231 : 1237);
		result = prime * result + ((finalPayMonthlyDate == null) ? 0 : finalPayMonthlyDate.hashCode());
		result = prime * result + installmentCount;
		result = prime * result + ((overpayment == null) ? 0 : overpayment.hashCode());
		result = prime * result + ((totalInterest == null) ? 0 : totalInterest.hashCode());
		result = prime * result + ((wibor3M == null) ? 0 : wibor3M.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mortgage other = (Mortgage) obj;
		if (changeOverpaymentList == null) {
			if (other.changeOverpaymentList != null)
				return false;
		} else if (!changeOverpaymentList.equals(other.changeOverpaymentList))
			return false;
		if (changeWibor3MList == null) {
			if (other.changeWibor3MList != null)
				return false;
		} else if (!changeWibor3MList.equals(other.changeWibor3MList))
			return false;
		if (dateValid != other.dateValid)
			return false;
		if (finalPayMonthlyDate == null) {
			if (other.finalPayMonthlyDate != null)
				return false;
		} else if (!finalPayMonthlyDate.equals(other.finalPayMonthlyDate))
			return false;
		if (installmentCount != other.installmentCount)
			return false;
		if (overpayment == null) {
			if (other.overpayment != null)
				return false;
		} else if (!overpayment.equals(other.overpayment))
			return false;
		if (totalInterest == null) {
			if (other.totalInterest != null)
				return false;
		} else if (!totalInterest.equals(other.totalInterest))
			return false;
		if (wibor3M == null) {
			if (other.wibor3M != null)
				return false;
		} else if (!wibor3M.equals(other.wibor3M))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Mortgage [wibor3M=" + wibor3M + ", overpayment=" + overpayment + ", totalInterest=" + totalInterest
				+ ", changeWibor3MList=" + changeWibor3MList + ", changeOverpaymentList=" + changeOverpaymentList
				+ ", dateValid=" + dateValid + ", finalPayMonthlyDate=" + finalPayMonthlyDate + ", installmentCount="
				+ installmentCount + "]";
	}

}
