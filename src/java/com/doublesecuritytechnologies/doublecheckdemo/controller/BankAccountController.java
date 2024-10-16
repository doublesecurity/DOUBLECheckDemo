package com.doublesecuritytechnologies.doublecheckdemo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.doublesecuritytechnologies.doublecheckdemo.model.BankAccount;

@Named
@ViewScoped
public class BankAccountController implements Serializable {
	
	private List<BankAccount> bankAccounts;
	private List<SelectItem> bankAccountSelectionItems;
	private List<SelectItem> bankDepositAccountSelectionItems;
	private List<SelectItem> bankCreditCardAccountSelectionItems;
	
	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void init() {
		bankAccounts = new ArrayList<>();
		bankAccounts.add(new BankAccount("Cuentas","ES06 7355 8567 8573 6693 8635", 836.74));
		bankAccounts.add(new BankAccount("Cuentas", "ES75 8572 8562 6933 0567 7693", 9353.67));
		bankAccounts.add(new BankAccount("Tarjetas de Crédito", "5482 8593 5955 9525", 126.54));
		bankAccounts.add(new BankAccount("Tarjetas de Crédito", "5429 3857 2759 7250", 75.20));
		
		bankAccountSelectionItems = new ArrayList<>();
		SelectItemGroup depositAccounts = new SelectItemGroup("Cuentas");
		depositAccounts.setSelectItems(new SelectItem[] {
				new SelectItem(bankAccounts.get(0).getIdfr()),
				new SelectItem(bankAccounts.get(1).getIdfr())
		});
		bankAccountSelectionItems.add(depositAccounts);
		bankDepositAccountSelectionItems = new ArrayList<>();
		bankDepositAccountSelectionItems.add(depositAccounts);
		
		SelectItemGroup creditCardAccounts = new SelectItemGroup("Tarjetas de Crédito");
		creditCardAccounts.setSelectItems(new SelectItem[] {
				new SelectItem(bankAccounts.get(2).getIdfr()),
				new SelectItem(bankAccounts.get(3).getIdfr())
		});
		bankAccountSelectionItems.add(creditCardAccounts);
		bankCreditCardAccountSelectionItems = new ArrayList<>();
		bankCreditCardAccountSelectionItems.add(creditCardAccounts);
	}
	
	public List<BankAccount> getBankAccounts() {
		if (bankAccounts == null) {
			init();
		}
		return bankAccounts;
	}
	
	public List<SelectItem> getBankAccountSelectionItems() {
		if (bankAccountSelectionItems == null) {
			init();
		}
		return bankAccountSelectionItems;
	}
	
	public List<SelectItem> getBankDepositAccountSelectionItems() {
		if (bankDepositAccountSelectionItems == null) {
			init();
		}
		return bankDepositAccountSelectionItems;
	}
	
	public List<SelectItem> getBankCreditCardAccountSelectionItems() {
		if (bankCreditCardAccountSelectionItems == null) {
			init();
		}
		return bankCreditCardAccountSelectionItems;
	}
	
}
