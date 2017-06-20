package com.mkaminski.component.bank_accounts;

public abstract class BankClient {

	protected String name;
	protected String surname;
	protected int clientId;
	protected static int id = 0;

	
	public BankClient(String name, String surname) {
		this.name = name;
		this.surname = surname;
		clientId = ++id;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clientId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
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
		BankClient other = (BankClient) obj;
		if (clientId != other.clientId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "BankClient [name=" + name + ", surname=" + surname + ", clientId=" + clientId + "]";
	}
	
}
