package com.pi.chem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Molecule {
	private static final Pattern IMPLOSION = Pattern
			.compile("\\((.*?)\\)([0-9]+)");
	private String equation;
	private String explodedEquation;
	private float overallCharge;
	private Map<Element, Float> charges = new HashMap<Element, Float>();
	private Map<Element, Integer> counts = new HashMap<Element, Integer>();

	public Molecule(String s) {
		int offset = 0;
		overallCharge = 0;
		if (s.startsWith("+") || s.startsWith("-")) {
			for (int i = 1; i < s.length(); i++) {
				if (!Character.isDigit(s.charAt(i))) {
					offset = i;
					overallCharge = Float.valueOf(s.substring(0, i));
					break;
				}
			}
		}
		explodedEquation = explodeMolecule(s.substring(offset).trim());
		calculateElementCounts();
	}
	
	public void calculateCharges() {
		charges = EquationParser.getChargesInMolecule(explodedEquation, overallCharge);
	}
	
	public float getOverallCharge() {
		return overallCharge;
	}
	
	public String getEquation() {
		return equation;
	}
	
	public String getExplodedEquation() {
		return explodedEquation;
	}
	
	public Map<Element, Integer> getElementCounts() {
		return counts;
	}
	
	public Map<Element, Float> getElementCharges() {
		return charges;
	}

	private static String explodeMolecule(String eq) {
		Matcher m = IMPLOSION.matcher(eq);
		StringBuilder result = new StringBuilder();
		int tail = 0;
		while (m.find()) {
			if (m.start() > tail) {
				result.append(eq.substring(tail, m.start()));
			}
			int count = Integer.valueOf(m.group(2));
			for (int i = 0; i < count; i++) {
				result.append(m.group(1));
			}
			tail = m.end();
		}
		if (tail < eq.length()) {
			result.append(eq.substring(tail));
		}
		return result.toString();
	}

	private void calculateElementCounts() {
		List<Element> elements = new ArrayList<Element>();
		for (int i = 0; i < explodedEquation.length(); i++) {
			for (int j = i; j <= explodedEquation.length(); j++) {
				if (j == explodedEquation.length()
						|| !Character.isDigit(explodedEquation.charAt(j))) {
					if (j > i && elements.size() > 0) {
						int count = Integer.valueOf(explodedEquation.substring(
								i, j));
						for (int k = 1; k < count; k++) {
							elements.add(elements.get(elements.size() - 1));
						}
					}
					break;
				}
			}
			if (i + 1 < explodedEquation.length()) {
				try {
					Element dbl = Element.valueOf(explodedEquation.substring(i,
							i + 2));
					elements.add(dbl);
					i++;
					continue;
				} catch (Exception e) {
				}
			}
			try {
				Element sng = Element.valueOf(Character
						.toString(explodedEquation.charAt(i)));
				elements.add(sng);
			} catch (Exception e) {
			}
		}
		counts.clear();
		for (Element e : elements) {
			Integer cnt = counts.get(e);
			if (cnt == null) {
				cnt = 0;
			}
			cnt++;
			counts.put(e, cnt);
		}
	}
}