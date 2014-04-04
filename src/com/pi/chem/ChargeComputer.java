package com.pi.chem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pi.chem.db.Element;
import com.pi.chem.db.Ion;

public class ChargeComputer {
	private static Map<Element, Float> getChargesInHomoMolecule(
			Molecule molecule) {
		Map<Element, Integer> counts = Collections.unmodifiableMap(molecule
				.getElementCounts());
		HashMap<Element, Integer> idx = new HashMap<Element, Integer>();
		for (Element e : counts.keySet()) {
			idx.put(e, 0);
		}
		Map<Element, Float> charges;
		boolean useRareCharges = false;
		do {
			charges = getChargesInHomoMoleculeInternal(counts,
					molecule.getOverallCharge(), idx, useRareCharges);
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
		} while (invalidChargeConfiguration(charges, counts,
				molecule.getOverallCharge()));
		return charges;
	}

	private static Map<Element, Float> getChargesInHomoMoleculeInternal(
			Map<Element, Integer> counts, float overall,
			Map<Element, Integer> idxs, boolean useRareCharges) {
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
		return getChargesInHomoMolecule(new Molecule(i.getCharges()[0] + ""
				+ i.getEquation()));
	}

	public static Map<Element, Float> getChargesInMolecule(Molecule molecule) {
		List<Ion> ions = EquationParser.getIonsInMolecule(molecule);
		Map<Element, Float> charges = new HashMap<Element, Float>();
		// Find ionic stuff
		String s = new String(molecule.getEquation());
		float iCharge = molecule.getOverallCharge();
		for (Ion i : ions) {
			s = s.replace(i.getEquation(), "");
			iCharge -= i.getCharges()[0];
			for (Entry<Element, Float> e : getChargesInIon(i).entrySet()) {
				charges.put(e.getKey(), e.getValue());
			}
		}
		Molecule noIonMole = new Molecule(s, iCharge, null);
		MapUtilities.addToMap(charges, getChargesInHomoMolecule(noIonMole));
		if (invalidChargeConfiguration(charges, noIonMole.getElementCounts(),
				noIonMole.getOverallCharge())) {
			charges = getChargesInHomoMolecule(noIonMole);
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

	public static Map<Element, Float> getChargesInEquation(
			List<Molecule> molecules) {
		Map<Element, Float> map = new HashMap<Element, Float>();
		for (Molecule s : molecules) {
			MapUtilities.addToMap(map, s.getElementCharges());
		}
		return map;
	}

}
