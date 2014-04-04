package com.pi.chem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pi.chem.db.ThermodynamicQuantities;

public class GenericEquation {
	public List<Molecule> leftMolecules;
	public List<Molecule> rightMolecules;

	private float totalFormation = 0;
	private float totalEntropy = 0;

	public GenericEquation(String eq) {
		String[] parts = eq.split("(->|→|⇌)");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Must be an equation...");
		}
		leftMolecules = getMoleculesInEquation(parts[0]);
		rightMolecules = getMoleculesInEquation(parts[1]);
	}

	public void calculateThermodynamics() {
		totalFormation = 0;
		totalEntropy = 0;
		for (Molecule m : rightMolecules) {
			float heat = ThermodynamicQuantities.getHeatOfFormation(m);
			float entropy = ThermodynamicQuantities.getStandardEntropy(m);
			totalFormation += heat;
			totalEntropy += entropy;

		}
		for (Molecule m : leftMolecules) {
			float heat = ThermodynamicQuantities.getHeatOfFormation(m);
			float entropy = ThermodynamicQuantities.getStandardEntropy(m);
			totalFormation -= heat;
			totalEntropy -= entropy;
		}
	}

	public float getReactionHeat() {
		if (totalFormation == 0) {
			calculateThermodynamics();
		}
		return totalFormation;
	}

	public float getEntropy() {
		if (totalEntropy == 0) {
			calculateThermodynamics();
		}
		return totalEntropy;
	}

	public float getGibbsFreeEnergy(float temperature) {
		return (float) (getReactionHeat() - (getEntropy() * temperature));
	}

	public float getStandardReactionConstant(float temperature) {
		return (float) Math.exp(getGibbsFreeEnergy(temperature)
				/ (.008314 * temperature));
	}

	public float getTotalEntropy(float temperature) {
		return (getEntropy() - (getReactionHeat() / temperature));
	}

	private static final Pattern MOLECULE_COEFF = Pattern
			.compile("^([0-9]*)(.*)");

	private static List<Molecule> getMoleculesInEquation(String eq) {
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
}
