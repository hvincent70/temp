package com.sprint.iice_tests.utilities.thread;

public class GenerateThread implements Runnable {

	@Override
	public synchronized void run() {
		try {
			System.out.println("The thread name is " + Thread.currentThread().getName());
			System.out.println("The thread id is " + Thread.currentThread().getId());
			System.out.println("The thread state is " + Thread.currentThread().getState());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}

}
