package com.app.runnnables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import com.app.beans.BankAccount;
import com.app.beans.BankAccountTransaction;
import com.app.dao.BankAccountDao;

public class ReportsProcessor implements Callable<Boolean> {

	private BankAccount account;
	private BankAccountDao dao;

	public ReportsProcessor(BankAccount account, BankAccountDao dao) {
		this.account = account;
		this.dao = dao;
	}

	@Override
	public Boolean call() {

		Boolean reportGenerated = false;
		List<BankAccountTransaction> transactions = dao.getTransactionForAccount(account);
		File file = new File("D:/reports/" + account.getAccNumber() + "_tx_report.txt");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (BankAccountTransaction transaction : transactions) {
				writer.write("Account number : " + transaction.getAccountNumber());
				writer.write("Transaction Date : " + transaction.getTxDate());
				writer.write("Transaction Type : " + transaction.getTxType());
				writer.write("Transaction Id : " + transaction.getTxId());
				writer.write("Amount : " + transaction.getAmount());
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reportGenerated = true;
		return reportGenerated;
	}

}
