package com.pi.chem.calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import com.pi.chem.Element;
import com.pi.chem.Molecule;

public class MassCompositionFromFormula {
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String s = in.readLine();
			if (s == null || s.equalsIgnoreCase("exit")) {
				break;
			}
			Molecule m = new Molecule(s);
			float wt = 0;
			for (Entry<Element, Integer> ent : m.getElementCounts().entrySet()) {
				wt += (ent.getKey().getWeight() * (float) ent.getValue());
			}
			System.out.println("Weight: " + wt + " amu");
			for (Entry<Element, Integer> ent : m.getElementCounts().entrySet()) {
				System.out.println("\t"
						+ ent.getKey().name()
						+ " "
						+ (100f * (ent.getKey().getWeight() * (float) ent
								.getValue()) / wt) + "% ("
						+ (ent.getKey().getWeight() * (float) ent.getValue())
						+ " amu)");
			}
		}
		in.close();
	}
}
