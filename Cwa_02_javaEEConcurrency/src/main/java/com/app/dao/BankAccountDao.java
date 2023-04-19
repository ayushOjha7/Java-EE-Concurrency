package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.app.beans.BankAccount;
import com.app.beans.BankAccountTransaction;

public class BankAccountDao {

	private DataSource dataSource;

	public BankAccountDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<BankAccount> getAllBankAccount() {

		List<BankAccount> accounts = new ArrayList<>();
		BankAccount account = null;

		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery("select * from bank_accounts");
			while (set.next()) {
				account = new BankAccount();
				account.setAccNumber(set.getInt("acc_number"));
				account.setName(set.getString("acc_holder_name"));
				account.setEmail(set.getString("acc_email"));
				account.setAccType(set.getString("acc_type"));
				accounts.add(account);
			}
			return accounts;
		} catch (SQLException e) {
			Logger.getLogger(BankAccountDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return accounts;
	}

	public List<BankAccountTransaction> getTransactionForAccount(BankAccount account) {

		BankAccountTransaction transaction = null;
		List<BankAccountTransaction> transactions = new ArrayList<>();

		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("select * from from bank_accounts_transaction where acc = ?");
			statement.setInt(1, account.getAccNumber());

			ResultSet set = statement.executeQuery();

			while (set.next()) {
				transaction = new BankAccountTransaction();
				transaction.setAccountNumber(set.getInt("acc_number"));
				transaction.setAmount(set.getDouble("amount"));
				transaction.setTxDate(new Date(set.getDate("transaction_date").getTime()));
				transaction.setTxId(set.getInt("tx_id"));
				transaction.setTxType(set.getString("transaction_type"));
				transactions.add(transaction);
			}

		} catch (SQLException e) {
			Logger.getLogger(BankAccountDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return transactions;
	}
}