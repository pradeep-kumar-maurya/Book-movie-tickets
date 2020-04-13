package com.capgemini.dao;

public class Exceptions {

	public void balance() {
		System.out.println("Exception -> your balance is not sufficient\n");
	}

	public void accountNotFound() {
		System.out.println("Exception -> no such account is in exist\n");
	}

	public void seats() {
		System.out.println("Exceptions :");
		System.out.println("-> either seats are fully booked\n-> or you can only book seats within the given range\n-> or no. of selected seats should not be 0\n");
	}

	public void selectSeats() {
		System.out.println("Exception -> your choice for selecting seat is not appropiate\n");
	}

	public void language() {
		System.out.println("Exception -> your choice for selecting the language is not appropiate\n");
	}

	public void show() {
		System.out.println("Exception -> your choice for selecting the show is not appropiate\n");
	}

	public void refund() {
		System.out.println("Exception -> refund process already executed in past\n");
	}

	public void ticketsNotBought() {
		System.out.println("Exception -> sorry you have not bought any tickets\n");
	}
}
