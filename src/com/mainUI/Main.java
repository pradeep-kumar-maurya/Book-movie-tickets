package com.mainUI;

import java.sql.SQLException;


import java.util.InputMismatchException;
import java.util.Scanner;

import com.capgemini.service.MovieService;

public class Main {
	 
	 static MovieService service = new MovieService();
	public static void main(String[] args) throws SQLException {
		// this application is only for booking and canceling movie tickets
		while(true)
		{
			Scanner sc = new Scanner(System.in);
			System.out.println("please enter your choice");
			System.out.println("Press 1 -> book movie tickets");
			System.out.println("Press 2 -> cancel booking");
			System.out.println("Press 3 -> exit from the application");
			try{
				int choice = sc.nextInt();
			if( choice > 0 && choice < 4)
			{
				switch(choice)
				{
				case 1 : 
						System.out.println("**Book your tickets now**");
				service.bookTickets();
				break;
				
				case 2 : System.out.println("**Cancel your booked tickets**");
				service.cancelTickets();
				break;
				
				case 3 : System.out.println("thanks for using the application");
				System.out.println("HAVE A NICE DAY :)");
				System.exit(0);
				}
			}
			else
				System.out.println("Exception -> your choice is not appropiate\n");
		}
			catch(InputMismatchException e) {
				System.out.println("Exception -> your input should be an integer value\n");
			}
	}
}
}
