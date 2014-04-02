package com.pi.chem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pi.chem.db.Element;
import com.pi.chem.db.Ion;

public class ChargeComputer {

	private static Map<Element, Float> getChargesInHomoMolecule(String eq,
			float overall) {
		List<Element> elements = EquationParser.getElementsInMolecule(eq);
		Map<Element, Integer> counts = EquationParser
				.getElementCounts(elements);
		HashMap<Element, Integer> idx = new HashMap<Element, Integer>();
		for (Element e : elements) {
			idx.put(e, 0);
		}
		Map<Element, Float> charges;
		boolean useRareCharges = false;
		do {
			charges = getChargesInHomoMoleculeInternal(elements, counts,
					overall, idx, useRareCharges);
			// Advance by 1
			boolean broke = false;
			for (Entry<Element, Integer> o : idx.entrySet()) {
				if (o.getValue() + 1 < (useRareCharges ? o.getKey()
						.getAllOxidationNumbers().length : o.getKey()
						.getCommonOxidationNumbers().length)) {
					o.setValue(o.getValue() + 1);
					broke = true;
					break;
				} else {
					o.setValue(0);
				}
			}
			if (!broke) {
				if (useRareCharges) {
					break;
				} else {
					useRareCharges = true;
				}
			}
		} while (invalidChargeConfiguration(charges, counts, overall));
		return charges;
	}

	private static Map<Element, Float> getChargesInHomoMoleculeInternal(
			List<Element> elements, Map<Element, Integer> counts,
			float overall, Map<Element, Integer> idxs, boolean useRareCharges) {
		Map<Element, Float> charges = new HashMap<Element, Float>();
		float missingCharge = overall;
		if (counts.containsKey(Element.O)) {
			float[] standard = useRareCharges ? Element.O
					.getAllOxidationNumbers() : Element.O
					.getCommonOxidationNumbers();
			if (standard.length > 0) {
				Integer i = idxs.get(Element.O);
				int idx = i != null ? i.intValue() : 0;
				float charge = standard[idx];
				missingCharge -= charge * counts.get(Element.O).floatValue();
				charges.put(Element.O, charge);
			}
		}
		if (counts.containsKey(Element.H)) {
			float[] standard = useRareCharges ? Element.H
					.getAllOxidationNumbers() : Element.H
					.getCommonOxidationNumbers();
			if (standard.length > 0) {
				Integer i = idxs.get(Element.H);
				int idx = i != null ? i.intValue() : 0;
				float charge = standard[idx];
				missingCharge -= charge * counts.get(Element.H).floatValue();
				charges.put(Element.H, charge);
			}
		}
		for (Entry<Element, Integer> obj : counts.entrySet()) {
			if (obj.getKey() == Element.O || obj.getKey() == Element.H
					|| counts.size() - charges.size() <= 1) {
				continue;
			}
			float[] standard = useRareCharges ? obj.getKey()
					.getAllOxidationNumbers() : obj.getKey()
					.getCommonOxidationNumbers();
			if (standard.length > 0) {
				Integer i = idxs.get(obj.getKey());
				int idx = i != null ? i.intValue() : 0;
				float charge = standard[idx];
				missingCharge -= charge * obj.getValue().floatValue();
				charges.put(obj.getKey(), charge);
			}
		}
		for (Entry<Element, Integer> e : counts.entrySet()) {
			if (!charges.containsKey(e.getKey())) {
				float possibleCharge = missingCharge
						/ e.getValue().floatValue();
				if (useRareCharges || possibleCharge == 0f
						|| e.getKey().isCommonOxidationNumber(possibleCharge)) {
					charges.put(e.getKey(), possibleCharge);
					break;
				}
			}
		}
		return charges;
	}

	public static Map<Element, Float> getChargesInIon(Ion i) {
		return getChargesInHomoMolecule(i.getEquation(), i.getCharges()[0]);
	}

	public static Map<Element, Float> getChargesInMolecule(String eq,
			float overall) {
		List<Ion> ions = EquationParser.getIonsInMolecule(eq);
		Map<Element, Float> charges = new HashMap<Element, Float>();
		// Find ionic stuff
		String s = new String(eq);
		float iCharge = overall;
		for (Ion i : ions) {
			s = s.replace(i.getEquation(), "");
			iCharge -= i.getCharges()[0];
			for (Entry<Element, Float> e : getChargesInIon(i).entrySet()) {
				charges.put(e.getKey(), e.getValue());
			}
		}
		MapUtilities.addToMap(charges, getChargesInHomoMolecule(s, iCharge));
		List<Element> elements = EquationParser.getElementsInMolecule(eq);
		Map<Element, Integer> counts = EquationParser.getElementCounts(elements);
		if (invalidChargeConfiguration(charges, counts, overall)) {
			charges = getChargesInHomoMolecule(eq, overall);
		}
		return charges;
	}

	public static boolean invalidChargeConfiguration(
			Map<Element, Float> charges, Map<Element, Integer> counts,
			float overall) {
		float totalCharge = 0;
		for (Entry<Element, Float> e : charges.entrySet()) {
			// Check for non integer charges
			if (Math.abs(e.getValue().floatValue() - e.getValue().intValue()) > 0.0) {
				return true;
			}
			if (!counts.containsKey(e.getKey())) {
				continue;
			}
			totalCharge += e.getValue().floatValue()
					* counts.get(e.getKey()).floatValue();
		}
		return overall != totalCharge;
	}

	public static Map<Element, Float> getChargesInEquation(String eq) {
		List<Molecule> molecules = EquationParser.getMoleculesInEquation(eq);
		Map<Element, Float> map = new HashMap<Element, Float>();
		for (Molecule s : molecules) {
			MapUtilities.addToMap(map, s.getElementCharges());
		}
		return map;
	}

}
