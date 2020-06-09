package com.sprint.iice_tests.utilities.thread;

public class ThreadFactory extends GenerateThread {
	
	public ThreadFactory() {
	}
	
	public synchronized static Thread startThread() {
		
		Thread t = new Thread(new GenerateThread());
		t.start();
		return t;
	}
	
	public synchronized static void stopThread(Thread t) {
		
		if(t.isAlive()) {
			t.interrupt();
		}
	}
	
	public synchronized static ThreadGroup getThreadGroup(Thread t) {
		
		if(t.isAlive()) {
			return t.getThreadGroup();
		} else {
			return null;
		}	
	}
	
	public synchronized static ThreadGroup startThreadGroup(String name, Integer threadCount) {
		
		ThreadGroup tGroup = new ThreadGroup(name);
		for(int i=0; i < threadCount; i++) {
			Thread t = new Thread(tGroup, new GenerateThread(), String.valueOf(i));
			if(!t.isAlive()) {
				t.start();
			}
		}
		return tGroup;	
	}
	
	public synchronized static void stopThreadGroup(ThreadGroup tGroup) {
		
		if(Thread.currentThread().getThreadGroup().equals(tGroup)) {
			Thread.currentThread().getThreadGroup().interrupt();
		}
	}
	
	public synchronized static void sleep(Thread t, Integer seconds) {
		
		if(t.isAlive()) {
			try {
				Thread.currentThread();
				Thread.sleep(seconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
