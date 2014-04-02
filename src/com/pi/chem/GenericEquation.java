package com.pi.chem;

import java.util.List;

import com.pi.chem.db.ThermodynamicQuantities;

public class GenericEquation {
	private List<Molecule> leftMolecules;
	private List<Molecule> rightMolecules;

	private float totalFormation = 0;
	private float totalEntropy = 0;

	public GenericEquation(String eq) {
		String[] parts = eq.split("(->|→|⇌)");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Must be an equation...");
		}
		leftMolecules = EquationParser.getMoleculesInEquation(parts[0]);
		rightMolecules = EquationParser.getMoleculesInEquation(parts[1]);
	}

	public void calculateThermodynamics() {
		totalFormation = 0;
		totalEntropy = 0;
		for (Molecule m : rightMolecules) {
			String mole = m.getEquation() + "("
					+ m.getPhase().getAbbreviation() + ")";
			float heat = ThermodynamicQuantities.getHeatOfFormation(mole);
			float entropy = ThermodynamicQuantities.getStandardEntropy(mole);
			totalFormation += heat;
			totalEntropy += entropy;

		}
		for (Molecule m : leftMolecules) {
			String mole = m.getEquation() + "("
					+ m.getPhase().getAbbreviation() + ")";
			float heat = ThermodynamicQuantities.getHeatOfFormation(mole);
			float entropy = ThermodynamicQuantities.getStandardEntropy(mole);
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
}
