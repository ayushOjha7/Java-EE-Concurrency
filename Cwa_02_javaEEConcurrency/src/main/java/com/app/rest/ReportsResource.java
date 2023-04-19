package com.app.rest;

import java.beans.PropertyVetoException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.app.beans.BankAccount;
import com.app.dao.BankAccountDao;
import com.app.runnnables.ReportsProcessor;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Path("reports")
public class ReportsResource {

	private BankAccountDao dao;

//	public ReportsResource() throws Exception {
//
//		InitialContext context = new InitialContext();
//		ManagedExecutorService service = (ManagedExecutorService) context.lookup("java:comp/DefaultManagedExecutorService");
//	}

	@Resource(lookup = "java:comp/DefaultManagedExecutorService")
	private ManagedExecutorService service;

	public ReportsResource() {

		try {
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/multithreading");
			dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
			dataSource.setUser("root");
			dataSource.setPassword("root");
			dao = new BankAccountDao(dataSource);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	@GET
	public String generateReports() {

		List<BankAccount> accounts = dao.getAllBankAccount();
		for (BankAccount account : accounts) {
			try {
				Future<Boolean> future = service.submit(new ReportsProcessor(account, dao));
				System.out.println("report generated ? " + future.get());
			} catch (InterruptedException e) {
				Logger.getLogger(ReportsResource.class.getName()).log(Level.SEVERE, null, e);
			} catch (ExecutionException e) {
				Logger.getLogger(ReportsResource.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return "report generation task submitted";
	}

}
