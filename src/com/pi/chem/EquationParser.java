package com.pi.chem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pi.chem.db.Element;
import com.pi.chem.db.Ion;

public class EquationParser {
	private static final Pattern MOLECULE_COEFF = Pattern
			.compile("^([0-9]*)(.*)");

	public static List<Molecule> getMoleculesInEquation(String eq) {
		String[] parts = eq.split("\\+");
		List<Molecule> eqn = new ArrayList<Molecule>();
		for (String s : parts) {
			Matcher m = MOLECULE_COEFF.matcher(s.trim());
			if (m.find()) {
				Molecule mm = new Molecule(m.group(2));
				int count = 1;
				if (m.group(1).length() > 0) {
					try {
						count = Integer.valueOf(m.group(1));
					} catch (Exception e) {
					}
				}
				for (int i = 0; i < count; i++) {
					eqn.add(mm);
				}
			}
		}
		return eqn;
	}

	private static final Pattern IMPLOSION = Pattern
			.compile("\\((.*?)\\)([0-9]+)");

	public static String explodeMolecule(String eq) {
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

	public static List<Ion> getIonsInMolecule(String eq) {
		List<Ion> ions = new ArrayList<Ion>();
		if (Ion.isNonIonic(eq)) {
			return ions;
		}
		int head = 0;
		main: while (head < eq.length()) {
			for (int tail = eq.length() - 1; tail >= head + 1; tail--) {
				if (tail + 1 < eq.length()
						&& Character.isDigit(eq.charAt(tail + 1))) {
					continue;
				}
				Ion i = Ion.getIonByEquation(eq.substring(head, tail + 1));
				if (i != null) {
					ions.add(i);
					head = tail + 1;
					continue main;
				}
			}
			head++;
		}
		return ions;
	}

	public static List<Element> getElementsInMolecule(String eq) {
		List<Element> elements = new ArrayList<Element>();
		for (int i = 0; i < eq.length(); i++) {
			for (int j = i; j <= eq.length(); j++) {
				if (j == eq.length() || !Character.isDigit(eq.charAt(j))) {
					if (j > i && elements.size() > 0) {
						int count = Integer.valueOf(eq.substring(i, j));
						for (int k = 1; k < count; k++) {
							elements.add(elements.get(elements.size() - 1));
						}
					}
					i = j;
					break;
				}
			}
			if (i + 1 < eq.length()) {
				try {
					Element dbl = Element.valueOf(eq.substring(i, i + 2));
					elements.add(dbl);
					i++;
					continue;
				} catch (Exception e) {
				}
			}
			try {
				Element sng = Element.valueOf(Character.toString(eq.charAt(i)));
				elements.add(sng);
			} catch (Exception e) {
			}
		}
		return elements;
	}

	public static Map<Element, Integer> getElementCounts(List<Element> elements) {
		Map<Element, Integer> counts = new HashMap<Element, Integer>();
		for (Element e : elements) {
			Integer cnt = counts.get(e);
			if (cnt == null) {
				cnt = 0;
			}
			cnt++;
			counts.put(e, cnt);
		}
		return counts;
	}
}
