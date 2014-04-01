package com.pi.chem.calc;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Scanner;

import com.pi.chem.MetricConversion;
import com.pi.chem.Molecule;
import com.pi.chem.db.Element;

public class MolsFromMassFormula {
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		in.useDelimiter("\n");
		while (true) {
			try {
				System.out.println("Molecule? ");
				Molecule m = new Molecule(in.next());
				float wt = 0;
				for (Entry<Element, Integer> ent : m.getElementCounts()
						.entrySet()) {
					wt += (ent.getKey().getWeight() * (float) ent.getValue());
				}
				System.out.println("\tMolar Mass: " + wt + " amu");
				System.out.println("Mass Amount (g)? ");
				float count = (float) MetricConversion.parseToGeneric(in.next())
						/ wt;
				for (Entry<Element, Integer> ent : m.getElementCounts()
						.entrySet()) {
					System.out.println("\t" + ent.getKey().name() + " "
							+ (ent.getValue() * count));
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		in.close();
	}
}
