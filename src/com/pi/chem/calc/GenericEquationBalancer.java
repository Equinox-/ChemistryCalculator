package com.pi.chem.calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.pi.chem.RedoxEquation;

public class GenericEquationBalancer {
	public static void main(String[] args) {
		if (args.length != 2) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(System.in));
				System.out.println("Input left side. ");
				args = new String[2];
				args[0] = reader.readLine();
				System.out.println("Input right side. ");
				args[1] = reader.readLine();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		RedoxEquation e = new RedoxEquation(args[0], args[1]);
	}
}
