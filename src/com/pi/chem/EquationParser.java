package com.pi.chem;

import java.util.ArrayList;
import java.util.List;

import com.pi.chem.db.Ion;

public class EquationParser {
//	private static final Pattern IMPLOSION = Pattern
//			.compile("\\((.*?)\\)([0-9]+)");
//
//	public static String explodeMolecule(String eq) {
//		Matcher m = IMPLOSION.matcher(eq);
//		StringBuilder result = new StringBuilder();
//		int tail = 0;
//		while (m.find()) {
//			if (m.start() > tail) {
//				result.append(eq.substring(tail, m.start()));
//			}
//			int count = Integer.valueOf(m.group(2));
//			for (int i = 0; i < count; i++) {
//				result.append(m.group(1));
//			}
//			tail = m.end();
//		}
//		if (tail < eq.length()) {
//			result.append(eq.substring(tail));
//		}
//		return result.toString();
//	}

	public static List<Ion> getIonsInMolecule(Molecule molecule) {
		List<Ion> ions = new ArrayList<Ion>();
		if (Ion.isNonIonic(molecule.getEquation())) {
			return ions;
		}
		int head = 0;
		main: while (head < molecule.getEquation().length()) {
			for (int tail = molecule.getEquation().length() - 1; tail >= head + 1; tail--) {
				if (tail + 1 < molecule.getEquation().length()
						&& Character.isDigit(molecule.getEquation().charAt(tail + 1))) {
					continue;
				}
				Ion i = Ion.getIonByEquation(molecule.getEquation().substring(head, tail + 1));
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
//
//	public static List<Element> getElementsInMolecule(String eq) {
//		List<Element> elements = new ArrayList<Element>();
//		for (int i = 0; i < eq.length(); i++) {
//			for (int j = i; j <= eq.length(); j++) {
//				if (j == eq.length() || !Character.isDigit(eq.charAt(j))) {
//					if (j > i && elements.size() > 0) {
//						int count = Integer.valueOf(eq.substring(i, j));
//						for (int k = 1; k < count; k++) {
//							elements.add(elements.get(elements.size() - 1));
//						}
//					}
//					i = j;
//					break;
//				}
//			}
//			if (i + 1 < eq.length()) {
//				try {
//					Element dbl = Element.valueOf(eq.substring(i, i + 2));
//					elements.add(dbl);
//					i++;
//					continue;
//				} catch (Exception e) {
//				}
//			}
//			try {
//				Element sng = Element.valueOf(Character.toString(eq.charAt(i)));
//				elements.add(sng);
//			} catch (Exception e) {
//			}
//		}
//		return elements;
//	}
//
//	public static Map<Element, Integer> getElementCounts(List<Element> elements) {
//		Map<Element, Integer> counts = new HashMap<Element, Integer>();
//		for (Element e : elements) {
//			Integer cnt = counts.get(e);
//			if (cnt == null) {
//				cnt = 0;
//			}
//			cnt++;
//			counts.put(e, cnt);
//		}
//		return counts;
//	}
}
