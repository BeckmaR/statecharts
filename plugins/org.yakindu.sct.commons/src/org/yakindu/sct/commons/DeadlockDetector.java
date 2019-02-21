package org.yakindu.sct.commons;

public class DeadlockDetector {

	public static DeadlockDetector INSTANCE = new DeadlockDetector();

	public synchronized void wait(String object) {
		System.out.println("waiting for " + object + " FROM " + Thread.currentThread().getName());
		System.out.println("\t" + getMethodName(1));
		System.out.println("\t\t" + getMethodName(2));
		System.out.println("\t\t\t" + getMethodName(3));
	}

	public synchronized void own(String object) {
		System.out.println("owning " + object + " FROM " + Thread.currentThread().getName());
		System.out.println("\t" + getMethodName(1));
		System.out.println("\t\t" + getMethodName(2));
		System.out.println("\t\t\t" + getMethodName(3));
	}

	public synchronized void release(String object) {
		System.out.println("release " + object + " FROM " + Thread.currentThread().getName());
		System.out.println("\t" + getMethodName(1));
		System.out.println("\t\t" + getMethodName(2));
		System.out.println("\t\t\t" + getMethodName(3));
	}

	public synchronized static String getMethodName(int depth) {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[ste.length - depth].getMethodName();
	}

}
