package com.mk.ailleron.bank_services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mk.ailleron.bank_accounts.BankClient;

public abstract class Credit implements CreditTools {

	protected String creditName;
	protected BankClient borrower;
	protected Double borrowedCapital;
	protected int payMonthlyTerm;
	protected Double bankMargin;
	protected Double installment;
	protected Double finalCreditCost;
	protected LocalDate creditStartDate;
	protected LocalDate installmentStartDate;
	protected int creditId;
	protected static int id = 0;

	
	public Credit(String creditName, BankClient borrower, Double borrowedCapital, int payMonthlyTerm,
			Double bankMargin) {
		this.creditName = creditName;
		this.borrower = borrower;
		this.borrowedCapital = borrowedCapital;
		this.payMonthlyTerm = payMonthlyTerm;
		this.bankMargin = bankMargin;
		
		initCreditStartDate();
		creditId = ++id;
	}

	
	public void initCreditStartDate() {
		ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
		
		creditStartDate = LocalDate.from(date.toLocalDate());
		installmentStartDate = LocalDate.of(creditStartDate.getYear(), creditStartDate.getMonth(), 10);
		
		if (creditStartDate.isAfter(installmentStartDate.minusDays(1))) {
			installmentStartDate = installmentStartDate.plusMonths(1);
		} else {
			installmentStartDate = LocalDate.of(creditStartDate.getYear(), creditStartDate.getMonth(), 10);
		}
	}

	public abstract Double calculateInstallment();

	public abstract void repaymentSchedule();
	
	public abstract Double finalInterestCost();

	public abstract Double finalCreditCost();

	public abstract LocalDate finalPayMonthlyDate();

	public abstract int finalPayMonthlyTerm();

	public String creditInfo(){
		return "Credit [creditName=" + creditName + ", creditId=" + creditId + ", borrower=" + borrower + "]";
	}

	
	public String getCreditName() {
		return creditName;
	}
	public void setCreditName(String creditName) {
		this.creditName = creditName;
	}
	public BankClient getBorrower() {
		return borrower;
	}
	public void setBorrower(BankClient borrower) {
		this.borrower = borrower;
	}
	public Double getBorrowedCapital() {
		return borrowedCapital;
	}
	public void setBorrowedCapital(Double borrowedCapital) {
		this.borrowedCapital = borrowedCapital;
	}
	public int getPayMonthlyTerm() {
		return payMonthlyTerm;
	}
	public void setPayMonthlyTerm(int payMonthlyTerm) {
		this.payMonthlyTerm = payMonthlyTerm;
	}
	public Double getBankMargin() {
		return bankMargin;
	}
	public void setBankMargin(Double bankMargin) {
		this.bankMargin = bankMargin;
	}
	public Double getInstallment() {
		return installment;
	}
	public void setInstallment(Double installment) {
		this.installment = installment;
	}
	public LocalDate getCreditStartDate() {
		return creditStartDate;
	}
	public void setCreditStartDate(LocalDate creditStartDate) {
		this.creditStartDate = creditStartDate;
	}
	public LocalDate getInstallmentStartDate() {
		return installmentStartDate;
	}
	public void setInstallmentStartDate(LocalDate installmentStartDate) {
		this.installmentStartDate = installmentStartDate;
	}
	public int getCreditId() {
		return creditId;
	}
	public void setCreditId(int creditId) {
		this.creditId = creditId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankMargin == null) ? 0 : bankMargin.hashCode());
		result = prime * result + ((borrowedCapital == null) ? 0 : borrowedCapital.hashCode());
		result = prime * result + ((borrower == null) ? 0 : borrower.hashCode());
		result = prime * result + creditId;
		result = prime * result + ((creditName == null) ? 0 : creditName.hashCode());
		result = prime * result + ((creditStartDate == null) ? 0 : creditStartDate.hashCode());
		result = prime * result + ((finalCreditCost == null) ? 0 : finalCreditCost.hashCode());
		result = prime * result + ((installment == null) ? 0 : installment.hashCode());
		result = prime * result + ((installmentStartDate == null) ? 0 : installmentStartDate.hashCode());
		result = prime * result + payMonthlyTerm;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credit other = (Credit) obj;
		if (bankMargin == null) {
			if (other.bankMargin != null)
				return false;
		} else if (!bankMargin.equals(other.bankMargin))
			return false;
		if (borrowedCapital == null) {
			if (other.borrowedCapital != null)
				return false;
		} else if (!borrowedCapital.equals(other.borrowedCapital))
			return false;
		if (borrower == null) {
			if (other.borrower != null)
				return false;
		} else if (!borrower.equals(other.borrower))
			return false;
		if (creditId != other.creditId)
			return false;
		if (creditName == null) {
			if (other.creditName != null)
				return false;
		} else if (!creditName.equals(other.creditName))
			return false;
		if (creditStartDate == null) {
			if (other.creditStartDate != null)
				return false;
		} else if (!creditStartDate.equals(other.creditStartDate))
			return false;
		if (finalCreditCost == null) {
			if (other.finalCreditCost != null)
				return false;
		} else if (!finalCreditCost.equals(other.finalCreditCost))
			return false;
		if (installment == null) {
			if (other.installment != null)
				return false;
		} else if (!installment.equals(other.installment))
			return false;
		if (installmentStartDate == null) {
			if (other.installmentStartDate != null)
				return false;
		} else if (!installmentStartDate.equals(other.installmentStartDate))
			return false;
		if (payMonthlyTerm != other.payMonthlyTerm)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Credit [creditName=" + creditName + ", borrower=" + borrower + ", borrowedCapital=" + borrowedCapital
				+ ", payMonthlyTerm=" + payMonthlyTerm + ", bankMargin=" + bankMargin + ", installment=" + installment
				+ ", finalCreditCost=" + finalCreditCost + ", creditStartDate=" + creditStartDate
				+ ", installmentStartDate=" + installmentStartDate + ", creditId=" + creditId + "]";
	}
	
}
