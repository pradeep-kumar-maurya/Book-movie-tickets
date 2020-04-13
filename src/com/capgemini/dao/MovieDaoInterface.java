package com.capgemini.dao;

import java.sql.SQLException;

public interface MovieDaoInterface {

	int bookTickets() throws SQLException;

	void cancelTickets() throws SQLException;

}
