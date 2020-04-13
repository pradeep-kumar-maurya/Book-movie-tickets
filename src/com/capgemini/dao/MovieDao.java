package com.capgemini.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import com.capgemini.moviedb.MovieDb;

public class MovieDao implements MovieDaoInterface {

	static Connection conn = null;
	static PreparedStatement city = null;
	static PreparedStatement theatre = null;
	static PreparedStatement movie = null;
	static PreparedStatement show = null;
	static PreparedStatement language = null;
	static PreparedStatement seats = null;
	static PreparedStatement payment = null;
	static PreparedStatement customers = null;
	static PreparedStatement refunds = null;
	static PreparedStatement transaction = null;
	static Exceptions exception = new Exceptions();
	int totalPrice;
	int temp;
	int newSeats;
	int updatedBalance;
	Random random = new Random(); // GENERATES RANDOM NOS. FOR BOOKING_ID

	// BOOKING OF THE MOVIE TICKETS
	@Override
	public int bookTickets() throws SQLException{
		try {
			// **SELECT THE CITY**
			conn = MovieDb.getConnection();
			System.out.println("please select a city");
			city = conn.prepareStatement("select * from city");
			ResultSet cityResultSet = city.executeQuery();
			System.out.println("*CityNames*");
			while (cityResultSet.next()) {
				System.out.println(cityResultSet.getString(2));
			}
			Scanner sc = new Scanner(System.in);
			String chosenCity = sc.next();

			// **SELECT THE THEATRE**
			System.out.println("please select a theatre");
			theatre = conn.prepareStatement("select theatres from theatre where citynames='" + chosenCity + "'");
			ResultSet theatreResultSet = theatre.executeQuery();
			if (theatreResultSet.next()) {
				System.out.println(theatreResultSet.getString("theatres"));
			}
			Scanner sc1 = new Scanner(System.in);
			String chosenTheatre = sc1.next();

			// **SELECT THE MOVIE**
			movie = conn.prepareStatement("select movies from movie where theatres='" + chosenTheatre + "'");
			ResultSet movieResultSet = movie.executeQuery();
			System.out.println("please select a movie");
			if (movieResultSet.next()) {
				System.out.println(movieResultSet.getString("movies"));
			}
			@SuppressWarnings("unused")
			Scanner sc2 = new Scanner(System.in);
			String chosenMovie = sc2.next();

			// **SELECT THE SHOW**
			show = conn.prepareStatement("select * from show");
			ResultSet showResultSet = show.executeQuery();
			System.out.println("please select a show by choosing its SNo.");
			System.out.println("SNo  Shows    Timings");
			while (showResultSet.next()) {
				System.out.print(showResultSet.getString("sno") + "  ");
				System.out.print(showResultSet.getString("show") + "  ");
				System.out.println(showResultSet.getString("timings"));
			}

			Scanner sc3 = new Scanner(System.in);
			int chosenShow = sc3.nextInt();
			if (chosenShow > 0 && chosenShow < 5) // CHECK IF CHOICE IS CORRECT
			{
				// **SELECT THE LANGUAGE**
				language = conn.prepareStatement("select * from languages");
				ResultSet languageResultSet = language.executeQuery();
				System.out.println("please select a language by choosing its SNo.");
				System.out.println("SNo Language");
				while (languageResultSet.next()) {
					System.out.print(languageResultSet.getInt("sno") + "  ");
					System.out.println(languageResultSet.getString("language"));
				}
				Scanner sc4 = new Scanner(System.in);
				int chosenlanguage = sc4.nextInt();
				if (chosenlanguage > 0 && chosenlanguage < 4) // CHECK IF CHOICE IS CORRECT
				{
					seats = conn.prepareStatement("select * from seats");
					ResultSet seatsResultSet = seats.executeQuery();

					// **SELECT THE SEAT**
					System.out.println("please select a seat by choosing its SNo.");
					System.out.println("SNo  SeatType   AvailableSeats        Price");
					while (seatsResultSet.next()) {
						System.out.println(
								seatsResultSet.getInt(1) + "      " + seatsResultSet.getString(2) + "           "
										+ seatsResultSet.getInt(3) + "               " + seatsResultSet.getInt(4));
					}

					Scanner sc5 = new Scanner(System.in);
					int chosenSeat = sc5.nextInt();
					if (chosenSeat > 0 && chosenSeat < 4) // CHECK IF CHOICE IS CORRECT
					{
						// **SELECT NO. OF TICKETS TO BE BOOKED**
						System.out.println("how many tickets to be booked");
						int numberOfTickets = sc.nextInt();
						int updatedSeats;
						seats = conn.prepareStatement("select * from seats where sno='" + chosenSeat + "'");
						ResultSet seatsAvailable = seats.executeQuery();
						if (seatsAvailable.next()) {
							if (seatsAvailable.getInt(3) > 0 && seatsAvailable.getInt(3) >= numberOfTickets
									&& numberOfTickets > 0) // CHECK AVAILABILITY OF SEATS
							{
								totalPrice = seatsAvailable.getInt(4) * numberOfTickets; // CALCULATING TOTAL PRICE OF
								// THE TICKETS
								System.out.println("your total amount is = " + totalPrice);
								System.out.println("enter your account number"); // ENTER THE ACCOUNT NO.
								int accountNumber = sc.nextInt();

								customers = conn.prepareStatement(
										"select currentbalance from customer where accountno='" + accountNumber + "'");
								ResultSet customersResultSet = customers.executeQuery();
								if (customersResultSet.next()) {
									updatedBalance = customersResultSet.getInt("currentbalance") - totalPrice; 

									// DEDUCTING MONEY FROM THE ACCOUNT	

									if (updatedBalance >= 0) // CHECK IF SUFFICIENT BALANCE IS THERE IN THE ACCOUNT
									{
										customers = conn.prepareStatement("update customer set currentbalance='"
												+ updatedBalance + "' where accountno='" + accountNumber + "'");
										customers.executeUpdate();
										// UPDATING THE PAYMENT TABLE
										payment = conn.prepareStatement(
												"insert into payment values(?,?,?,?,?,?,sysdate,to_char(sysdate,'hh24:mi:ss'))");
										payment.setInt(1, accountNumber);
										payment.setInt(2, totalPrice);
										payment.setInt(3, numberOfTickets);
										payment.setString(4, seatsAvailable.getString(2));
										payment.setInt(5, 0);
										int randomNumber = random.nextInt(1000); // GENERATES RANDOM NOS. FROM 0 to 1000
										payment.setInt(6, randomNumber);
										payment.executeUpdate();
										updatedSeats = seatsAvailable.getInt(3) - numberOfTickets; // UPDATING THE SEATS
										seats = conn.prepareStatement("update seats set seatsavailable='" + updatedSeats
												+ "' where sno='" + chosenSeat + "'");
										seats.executeUpdate();
										// UPDATING THE TRANSACTION TABLE
										transaction = conn.prepareStatement(
												"insert into transactions values(?,?,?,?,sysdate,to_char(sysdate,'hh24:mi:ss'))");
										transaction.setInt(1, accountNumber);
										transaction.setInt(2, 0);
										transaction.setInt(3, totalPrice);
										transaction.setInt(4, updatedBalance);
										transaction.executeUpdate();
										System.out.println("your tickets are booked"); // TICKETS ARE BOOKED
										System.out.println("your Booking Id is = " + randomNumber); // BOOKING_ID FOR
										// YOUR TRANSACTIONS
										System.out.println();
									} else
										exception.balance();;
								} else
									exception.accountNotFound();
							} else
								exception.seats();
						}
					} else
						exception.selectSeats();
				}
				else
					exception.language();
			}
			else
				exception.show();

		}catch(InputMismatchException e) {
			System.out.println("your input should be an integer value");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return updatedBalance;
	}

	// REFUNDING OF THE MOVIE TICKETS
	@Override
	public void cancelTickets() throws SQLException {
		Scanner sc = new Scanner(System.in);
		try {
			conn = MovieDb.getConnection();
			System.out.println("please enter your account number"); // ENTER THE ACCOUNT NO.
			int accountNumber1 = sc.nextInt();
			System.out.println("please enter your Booking Id"); // ENTER THE BOOKING_ID
			int bookingId = sc.nextInt();
			payment = conn.prepareStatement(
					"select * from payment where accountno='" + accountNumber1 + "' and bookingid='" + bookingId + "'");
			ResultSet paymentResultSet = payment.executeQuery();
			temp = 1;
			if (paymentResultSet.next()) {
				if (paymentResultSet.getInt("moneycollected") > 0) // CHECK IF REFUNDED PREVIOUSLY
				{
					// UPDATING THE REFUND TABLE
					refunds = conn
							.prepareStatement("insert into refund values(?,?,sysdate,to_char(sysdate,'hh24:mi:ss'),?)");
					refunds.setInt(1, accountNumber1);
					refunds.setInt(2, paymentResultSet.getInt(2));
					refunds.setInt(3, bookingId);
					refunds.executeUpdate();

					// UPDATING PAYMENT TABLE
					int updatedBalanceForPayment = paymentResultSet.getInt(2) - paymentResultSet.getInt(2);
					payment = conn.prepareStatement("update payment set moneycollected='" + updatedBalanceForPayment
							+ "' where accountno='" + accountNumber1 + "' and bookingid='" + bookingId + "'");
					payment.executeUpdate();
					seats = conn.prepareStatement(
							"select seatsavailable from seats where seattype='" + paymentResultSet.getString(4) + "'");
					ResultSet availableSeats = seats.executeQuery();
					if (availableSeats.next()) {
						System.out.println("number of seats booked by you = " + paymentResultSet.getInt("seatsbooked"));
						System.out.println("number of seats canceled =" + paymentResultSet.getInt("seatsbooked"));

						// UPDATING SEATS TABLE
						newSeats = availableSeats.getInt("seatsavailable") + paymentResultSet.getInt("seatsbooked");
					}
					seats = conn.prepareStatement("update seats set seatsavailable='" + newSeats + "' where seattype='"
							+ paymentResultSet.getString(4) + "'");
					seats.executeUpdate();
					customers = conn.prepareStatement(
							"select currentbalance from customer where accountno='" + accountNumber1 + "'");
					ResultSet customerBalance = customers.executeQuery();
					{
						if (customerBalance.next()) {
							// UPDATING THE CUSTOMER's CURRENT BALANCE IN THE CUSTOMER TABLE
							int updatedBalanceForCustomer = customerBalance.getInt("currentbalance")
									+ paymentResultSet.getInt(2);
							customers = conn.prepareStatement("update customer set currentbalance='"
									+ updatedBalanceForCustomer + "' where accountno='" + accountNumber1 + "'");
							customers.executeUpdate();
							// UPDATING THE REFUND COLUMN IN THE PAYMENT TABLE
							payment = conn.prepareStatement("update payment set refund='" + paymentResultSet.getInt(2)
							+ "' where accountno='" + accountNumber1 + "' and bookingid='" + bookingId + "'");
							payment.executeUpdate();
							// UPDATING THE TRANSACTION TABLE
							transaction = conn.prepareStatement(
									"insert into transactions values(?,?,?,?,sysdate,to_char(sysdate,'hh24:mi:ss'))");
							transaction.setInt(1, accountNumber1);
							transaction.setInt(2, paymentResultSet.getInt(2));
							transaction.setInt(3, 0);
							transaction.setInt(4, updatedBalanceForCustomer);
							transaction.executeUpdate();
						}
					}
					temp = 0;
				} else
					exception.refund();
			} else
				exception.ticketsNotBought();
			if (temp == 0) {
				System.out.println("refunded successfully back to your account\n");
			}
		} catch(InputMismatchException e) {
			System.out.println("your input should be an integer value");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
