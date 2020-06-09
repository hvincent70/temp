package com.sprint.iice_tests.lib.dal.services;

import java.io.IOException;

import com.sprint.iice_tests.lib.dao.vo.Account;
import com.sprint.iice_tests.utilities.parser.JSONParser;

public class AccountService {
	
	Account account = new Account();
	
	public AccountService() {
	}
	
	public Account getAccount(String path) {
		try {
			return (Account)JSONParser.parseJson(path, Account.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return account;
	}

}
