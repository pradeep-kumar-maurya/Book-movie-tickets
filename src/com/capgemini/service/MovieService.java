package com.capgemini.service;

import java.sql.SQLException;


import com.capgemini.dao.MovieDao;

public class MovieService implements MovieServiceInterface{
	static MovieDao dao = new MovieDao();
	
	@Override
	public void bookTickets() throws SQLException {
		dao.bookTickets();
	}
	@Override
	public void cancelTickets() throws SQLException{
		dao.cancelTickets();
	}
	
	private void secretMethod() {
		System.out.println("Secret");
	}
}
