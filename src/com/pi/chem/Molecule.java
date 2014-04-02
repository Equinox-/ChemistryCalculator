package com.pi.chem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pi.chem.db.Element;

public class Molecule {
	public enum MoleculeType {
		MOLECULAR, IONIC;
	}

	private static final Pattern IMPLOSION = Pattern
			.compile("\\((.*?)\\)([0-9]+)");
	private String equation;
	private String explodedEquation;
	private float overallCharge;
	private Map<Element, Float> charges = new HashMap<Element, Float>();
	private Map<Element, Integer> counts = new HashMap<Element, Integer>();
	private Phase phase;

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
		s = s.substring(offset).trim();
		phase = Phase.findPhase(s);
		if (phase != null) {
			s = s.replace("(" + phase.getAbbreviation() + ")", "").trim();
		}
		equation = s;
		explodedEquation = explodeMolecule(s);
		calculateElementCounts();
	}

	public Phase getPhase() {
		return phase;
	}

	public void calculateCharges() {
		charges = ChargeComputer.getChargesInMolecule(explodedEquation,
				overallCharge);
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

	private void calculateMoleculeType() {

	}

	public String toString() {
		StringBuilder content = new StringBuilder();
		for (Entry<Element, Integer> i : counts.entrySet()) {
			content.append(i.getKey().name());
			int count = i.getValue().intValue();
			if (count > 1) {
				content.append(count);
			}
		}
		return content.toString();
	}

	public int hashCode() {
		int i = 0;
		for (Entry<Element, Integer> cc : counts.entrySet()) {
			i += cc.getKey().ordinal() ^ cc.getValue().intValue();
		}
		return i;
	}

	public boolean equals(Object o) {
		if (o instanceof Molecule) {
			Molecule m = (Molecule) o;
			return m.getPhase() == getPhase()
					&& m.getElementCounts().equals(getElementCounts())
					&& overallCharge == m.getOverallCharge();
		}
		return false;
	}
}
