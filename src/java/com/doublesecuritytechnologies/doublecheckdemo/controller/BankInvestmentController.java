package com.doublesecuritytechnologies.doublecheckdemo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.doublesecuritytechnologies.doublecheckdemo.model.User;
import com.doublesecuritytechnologies.doublecheckdemo.worker.DCVerifyPassword;

@Named("bankInvestment")
@ViewScoped
public class BankInvestmentController implements Serializable {
	
	private String operationMode;
	private String relatedAccount;
	private String ammount;
	private String dcPassword;
	private BankTransferStatus status = BankTransferStatus.dataEntry;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.controller");

	public enum BankTransferStatus {
		dataEntry,
		authorization,
		finished
	}
	
	
	public BankInvestmentController() {
		super();
		log.finest("DCDemo: BankInvestmentController (backing bean) instantiated.");
	}
	
	
	  /////////////////
	 ///  Actions  ///
	/////////////////
	
	public String submit() {
		log.fine("DCDemo: Bank-Investment Data-Entry complete, go to Authorization...");
		
		status = BankTransferStatus.authorization;
		return null;
	}
	
	public String back() {
		log.fine("DCDemo: Bank-Investment backing Authorization, go to Data-Entry...");
		
		status = BankTransferStatus.dataEntry;
		return null;
	}
	
	public String authorize() {
		FacesContext context = FacesContext.getCurrentInstance();
		String username = ((User)(context.getExternalContext().getSessionMap().get("user"))).getIdfr();
		
		if ((new DCVerifyPassword().verifyDoubleCheckPassword(username, getDcPassword()))) {
			log.fine("DCDemo: Bank-Investment Authorization complete, go to Finish...");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success:", "Transaction complete."));
			status = BankTransferStatus.finished;
		} else {
			log.fine("DCDemo: Bank-Investment authorization failed. (DOUBLECheck verification)");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rejected:", "Credentials are not valid."));
		}
		return null;
	}
	
	
	  ////////////////////
	 ///  Properties  ///
	////////////////////
	
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String[] getOperationModes() {
		String[] operationModes = {
			"Inversión",
			"Retiro de inversión"
		};
		return operationModes;
	}
	
	public String getRelatedAccount() {
		return relatedAccount;
	}
	public void setRelatedAccount(String relatedAccount) {
		this.relatedAccount = relatedAccount;
	}
	
	public String getAmmount() {
		return ammount;
	}
	public void setAmmount(String ammount) {
		this.ammount = ammount;
	}
	
	public String getDcPassword() {
		return dcPassword;
	}
	public void setDcPassword(String dcPassword) {
		this.dcPassword = dcPassword;
	}
	
	public BankTransferStatus getStatus() {
		return status;
	}
	public void setStatus(BankTransferStatus status) {
		this.status = status;
	}
	public boolean isDataEntry() {
		return status == BankTransferStatus.dataEntry;
	}
	public boolean isAuthorization() {
		return status == BankTransferStatus.authorization;
	}
	public boolean isFinished() {
		return status == BankTransferStatus.finished;
	}
	public List<BankTransactionStatus> getLifeCycle() {
		List<BankTransactionStatus> lifeCycle = new ArrayList<>();
		switch (getStatus()) {
		case dataEntry:
			lifeCycle.add(new BankTransactionStatus("Datos", "#2196F3", "pi pi-arrow-down"));
			lifeCycle.add(new BankTransactionStatus("Autorización", "#AAAAAA", "pi pi-circle-off"));
			lifeCycle.add(new BankTransactionStatus("Comprobante", "#AAAAAA", "pi pi-circle-off"));
			break;
		case authorization:
			lifeCycle.add(new BankTransactionStatus("Datos", "#689F38", "pi pi-check"));
			lifeCycle.add(new BankTransactionStatus("Autorización", "#2196F3", "pi pi-arrow-down"));
			lifeCycle.add(new BankTransactionStatus("Comprobante", "#AAAAAA", "pi pi-circle-off"));
			break;
		case finished:
			lifeCycle.add(new BankTransactionStatus("Datos", "#689F38", "pi pi-check"));
			lifeCycle.add(new BankTransactionStatus("Autorización", "#689F38", "pi pi-check"));
			lifeCycle.add(new BankTransactionStatus("Comprobante", "#2196F3", "pi pi-arrow-down"));
			break;
		}
		return lifeCycle;
	}
	
}
