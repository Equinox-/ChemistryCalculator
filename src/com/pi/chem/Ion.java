package com.pi.chem;

public enum Ion implements OxidationNumber {
	Acetate("CH3COO", -1), Chlorate("ClO3", -1), Chlorite("ClO3", -1), Cyanide(
			"CN", -1), DihydrogenPhosphate("H2PO4", -1), HydrogenCarbonate(
			"BiCarbonate", "HCO3", -1), HydrogenSulfate("BiSulfate", "HSO4", -1), HydrogenSulfide(
			"BiSulfide", "HS", -1), HydrogenSulfite("BiSulfite", "HSO3", -1), Hydroxide(
			"OH", -1), Hypochorite("OCl", -1), Iodate("IO3", -1), Nitrate(
			"NO3", -1), Nitrite("NO2", -1), Perchlorate("ClO4", -1), Permanganate(
			"MnO4", -1), Thiocyanate("SCN", -1), Carbonate("CO3", -2), Chromate(
			"CrO4", -2), Dichromate("Cr2O7", -2), MonohydrogenPhosphate("HPO4",
			-2), Oxalate("Cr2O4", -2), Peroxide("O2", -2), Silicate("SiO3", -2), Sulfate(
			"SO4", -2), Sulfite("SO3", -2), Thiosulfate("S2O3", -2), Phosphate(
			"PO4", -3), Phosphite("PO3", -3), Ammonium("NH4", 1), Hydronium(
			"H3O", 1), Mercury("Hg2", 2);
	public static final String[] NON_IONIC_MOLECULES = { "CO2", "MnO2" };
	private final String equation;
	private final float[] charges;
	private final String[] name;

	public static boolean isNonIonic(String molecule) {
		for (String s : NON_IONIC_MOLECULES) {
			if (s.equals(molecule)) {
				return true;
			}
		}
		return false;
	}

	private Ion(final String eq, final float... charges) {
		this.equation = eq;
		this.charges = charges;
		this.name = new String[] { name() };
	}

	private Ion(final String name, final String eq, final float... charges) {
		this.equation = eq;
		this.charges = charges;
		this.name = new String[] { name, name() };
	}

	public float[] getCharges() {
		return charges;
	}

	public static Ion getIonByEquation(String eq) {
		for (Ion i : values()) {
			if (i.equation.equals(eq)) {
				return i;
			}
		}
		return null;
	}

	public String[] getName() {
		return name;
	}

	public String getEquation() {
		return equation;
	}

	@Override
	public float getOxidationNumber() {
		return charges[0];
	}
}
