package com.pi.chem.calc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.pi.chem.db.Element;

public class FormulaFromComposition {
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.println("Molar Mass? ");
				float molarMass = scanner.nextFloat();

				Map<Element, Float> percentages = new HashMap<Element, Float>();
				float totalPercentage = 0;

				while (totalPercentage < .99) {
					try {
						System.out.println("Symbol? ");
						Element e = Element.valueOf(scanner.next());
						if (percentages.containsKey(e)) {
							continue;
						}
						System.out.println(e.getFullName() + " % mass? ");
						float pMass = scanner.nextFloat();
						percentages.put(e, pMass / 100f);
						totalPercentage += pMass / 100f;
					} catch (Exception e) {
						continue;
					}
				}

				for (Entry<Element, Float> e : percentages.entrySet()) {
					int count = Math.round((e.getValue() * molarMass)
							/ e.getKey().getWeight());
					System.out.print(e.getKey().name() + count);
				}
				System.out.println();
			} catch (Exception e) {
				break;
			}
		}
		scanner.close();
	}
}
