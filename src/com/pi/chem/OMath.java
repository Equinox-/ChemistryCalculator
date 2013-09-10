package com.pi.chem;

public class OMath {
	public static float gcf(float a, float b) {
		return (b == 0) ? (a) : (gcf(b, a % b));
	}

	public static float lcm(float a, float b) {
		return (a / gcf(a, b)) * b;
	}
}
