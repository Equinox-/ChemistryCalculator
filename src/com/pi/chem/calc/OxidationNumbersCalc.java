package com.pi.chem.calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import com.pi.chem.Molecule;
import com.pi.chem.db.Element;

public class OxidationNumbersCalc {
	public static void main(String[] args) {
		BufferedReader reader = null;
		while (true) {
			if (args.length != 1) {
				try {
					if (reader == null) {
						reader = new BufferedReader(new InputStreamReader(
								System.in));
					}
					System.out.println("Input molecule. ");
					args = new String[1];
					args[0] = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			Molecule m = new Molecule(args[0]);
			System.out.println(m);
			m.calculateCharges();
			for (Entry<Element, Float> e : m.getElementCharges().entrySet()) {
				System.out.println(e.getKey().name() + "\t" + e.getValue());
			}
			args = new String[0];
		}
	}
}
