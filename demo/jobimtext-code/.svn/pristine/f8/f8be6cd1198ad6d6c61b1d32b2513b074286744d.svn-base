package com.ibm.bluej.util.common;

public class LogLinear {
	public static double logit(double x) {
		return Math.log(x / (1-x));
	}
	public static double logistic(double x) {
		return 1.0/(1.0+Math.exp(-x));
	}
	public static double smooth(double x, double smoothby) {
		return (1-2*smoothby)*x+smoothby;
	}
}
