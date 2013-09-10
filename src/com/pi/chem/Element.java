package com.pi.chem;

public enum Element implements OxidationNumber {
	H("hydrogen", 1.008f, new float[] { 1.0f, -1.0f }, new float[] { -1.0f,
			1.0f }), He("helium", 4.002602f, new float[] {}, new float[] {}), Li(
			"lithium", 6.94f, new float[] { 1.0f }, new float[] { 1.0f }), Be(
			"beryllium", 9.012182f, new float[] { 2.0f }, new float[] { 1.0f,
					2.0f }), B("boron", 10.81f, new float[] {}, new float[] {
			1.0f, 2.0f, 3.0f }), C("carbon", 12.011f,
			new float[] { 4.0f, -4.0f }, new float[] { -4.0f, -3.0f, -2.0f,
					-1.0f, 1.0f, 2.0f, 3.0f, 4.0f }), N("nitrogen", 14.007f,
			new float[] { -3.0f }, new float[] { -3.0f, -2.0f, -1.0f, 1.0f,
					2.0f, 3.0f, 4.0f, 5.0f }), O("oxygen", 15.999f,
			new float[] { -2.0f }, new float[] { -2.0f, -1.0f, 1.0f, 2.0f }), F(
			"fluorine", 18.998404f, new float[] { -1.0f },
			new float[] { -1.0f }), Ne("neon", 20.1797f, new float[] {},
			new float[] {}), Na("sodium", 22.989769f, new float[] { 1.0f },
			new float[] { -1.0f, 1.0f }), Mg("magnesium", 24.305f,
			new float[] { 2.0f }, new float[] { 1.0f, 2.0f }), Al("aluminium",
			26.981539f, new float[] { 3.0f }, new float[] { 1.0f, 2.0f, 3.0f }), Si(
			"silicon", 28.085f, new float[] { 4.0f }, new float[] { -4.0f,
					-3.0f, -2.0f, -1.0f, 1.0f, 2.0f, 3.0f, 4.0f }), P(
			"phosphorus", 30.973763f, new float[] { -3.0f }, new float[] {
					-3.0f, -2.0f, -1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f }), S(
			"sulfur", 32.06f, new float[] { -2.0f }, new float[] { -2.0f,
					-1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }), Cl(
			"chlorine", 35.45f, new float[] { -1.0f }, new float[] { -1.0f,
					1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f }), Ar("argon",
			39.948f, new float[] {}, new float[] {}), K("potassium", 39.0983f,
			new float[] { 1.0f }, new float[] { -1.0f, 1.0f }), Ca("calcium",
			40.078f, new float[] { 2.0f }, new float[] { 1.0f, 2.0f }), Sc(
			"scandium", 44.955914f, new float[] {}, new float[] { 1.0f, 2.0f,
					3.0f }), Ti("titanium", 47.867f, new float[] {},
			new float[] { -1.0f, 1.0f, 2.0f, 3.0f, 4.0f }), V("vanadium",
			50.9415f, new float[] {}, new float[] { -1.0f, 1.0f, 2.0f, 3.0f,
					4.0f, 5.0f }), Cr("chromium", 51.9961f, new float[] {},
			new float[] { -2.0f, -1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }), Mn(
			"manganese", 54.938046f, new float[] {}, new float[] { -3.0f,
					-2.0f, -1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f }), Fe(
			"iron", 55.845f, new float[] {}, new float[] { -2.0f, -1.0f, 1.0f,
					2.0f, 3.0f, 4.0f, 5.0f, 6.0f }), Co("cobalt", 58.933193f,
			new float[] {}, new float[] { -1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f }), Ni(
			"nickel", 58.6934f, new float[] {}, new float[] { -1.0f, 1.0f,
					2.0f, 3.0f, 4.0f }), Cu("copper", 63.546f, new float[] {},
			new float[] { 1.0f, 2.0f, 3.0f, 4.0f }), Zn("zinc", 65.38f,
			new float[] { 2.0f }, new float[] { 1.0f, 2.0f }), Ga("gallium",
			69.723f, new float[] { 3.0f }, new float[] { 1.0f, 2.0f, 3.0f }), Ge(
			"germanium", 72.63f, new float[] {}, new float[] { -4.0f, -3.0f,
					-2.0f, -1.0f, 1.0f, 2.0f, 3.0f, 4.0f }), As("arsenic",
			74.9216f, new float[] {}, new float[] { -3.0f, 1.0f, 2.0f, 3.0f,
					5.0f }), Se("selenium", 78.96f, new float[] {},
			new float[] { -2.0f, 1.0f, 2.0f, 4.0f, 6.0f }), Br("bromine",
			79.904f, new float[] { -1.0f }, new float[] { -1.0f, 1.0f, 2.0f,
					3.0f, 4.0f, 5.0f, 7.0f }), Kr("krypton", 83.798f,
			new float[] {}, new float[] { 2.0f }), Rb("rubidium", 85.4678f,
			new float[] { 1.0f }, new float[] { -1.0f, 1.0f }), Sr("strontium",
			87.62f, new float[] { 2.0f }, new float[] { 1.0f, 2.0f }), Y(
			"yttrium", 88.90585f, new float[] {}, new float[] { 1.0f, 2.0f,
					3.0f }), Zr("zirconium", 91.224f, new float[] {},
			new float[] { 1.0f, 2.0f, 3.0f, 4.0f }), Nb("niobium", 92.90638f,
			new float[] {}, new float[] { -1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f }), Mo(
			"molybdenum", 95.96f, new float[] {}, new float[] { -2.0f, -1.0f,
					1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }), Tc("technetium",
			97.0f, new float[] {}, new float[] { -3.0f, -1.0f, 1.0f, 2.0f,
					3.0f, 4.0f, 5.0f, 6.0f, 7.0f }), Ru("ruthenium", 101.07f,
			new float[] {}, new float[] { -2.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f,
					6.0f, 7.0f, 8.0f }), Rh("rhodium", 102.9055f,
			new float[] {}, new float[] { -1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f,
					6.0f }), Pd("palladium", 106.42f, new float[] {},
			new float[] { 1.0f, 2.0f, 4.0f }), Ag("silver", 107.8682f,
			new float[] {}, new float[] { 1.0f, 2.0f, 3.0f, 4.0f }), Cd(
			"cadmium", 112.411f, new float[] { 2.0f },
			new float[] { 1.0f, 2.0f }), In("indium", 114.818f, new float[] {},
			new float[] { 1.0f, 2.0f, 3.0f }), Sn("tin", 118.71f,
			new float[] {}, new float[] { -4.0f, 2.0f, 4.0f }), Sb("antimony",
			121.76f, new float[] {}, new float[] { -3.0f, 3.0f, 5.0f }), Te(
			"tellurium", 127.6f, new float[] { -2.0f }, new float[] { -2.0f,
					2.0f, 4.0f, 5.0f, 6.0f }), I("iodine", 126.90447f,
			new float[] { -1.0f }, new float[] { -1.0f, 1.0f, 3.0f, 4.0f, 5.0f,
					7.0f }), Xe("xenon", 131.293f, new float[] {}, new float[] {
			1.0f, 2.0f, 4.0f, 6.0f, 8.0f }), Cs("cesium", 132.90546f,
			new float[] { 1.0f }, new float[] { -1.0f, 1.0f }), Ba("barium",
			137.327f, new float[] { 2.0f }, new float[] { 2.0f }), La(
			"lanthanum", 138.90547f, new float[] {}, new float[] { 2.0f, 3.0f }), Ce(
			"cerium", 140.116f, new float[] {},
			new float[] { 2.0f, 3.0f, 4.0f }), Pr("praseodymium", 140.90765f,
			new float[] {}, new float[] { 2.0f, 3.0f, 4.0f }), Nd("neodymium",
			144.242f, new float[] {}, new float[] { 2.0f, 3.0f, 4.0f }), Pm(
			"promethium", 145.0f, new float[] {}, new float[] { 2.0f, 3.0f }), Sm(
			"samarium", 150.36f, new float[] {}, new float[] { 2.0f, 3.0f }), Eu(
			"europium", 151.964f, new float[] {}, new float[] { 2.0f, 3.0f }), Gd(
			"gadolinium", 157.25f, new float[] {}, new float[] { 1.0f, 2.0f,
					3.0f }), Tb("terbium", 158.92535f, new float[] {},
			new float[] { 1.0f, 2.0f, 3.0f, 4.0f }), Dy("dysprosium", 162.5f,
			new float[] {}, new float[] { 2.0f, 3.0f, 4.0f }), Ho("holmium",
			164.93031f, new float[] {}, new float[] { 2.0f, 3.0f }), Er(
			"erbium", 167.259f, new float[] {}, new float[] { 2.0f, 3.0f }), Tm(
			"thulium", 168.9342f, new float[] {}, new float[] { 2.0f, 3.0f,
					4.0f }), Yb("ytterbium", 173.054f, new float[] {},
			new float[] { 2.0f, 3.0f }), Lu("lutetium", 174.9668f,
			new float[] {}, new float[] { 3.0f }), Hf("hafnium", 178.49f,
			new float[] {}, new float[] { 2.0f, 3.0f, 4.0f }), Ta("tantalum",
			180.94788f, new float[] {}, new float[] { -1.0f, 2.0f, 3.0f, 4.0f,
					5.0f }), W("tungsten", 183.84f, new float[] {},
			new float[] { -2.0f, -1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }), Re(
			"rhenium", 186.207f, new float[] {}, new float[] { -3.0f, -1.0f,
					1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f }), Os("osmium",
			190.23f, new float[] {}, new float[] { -2.0f, -1.0f, 1.0f, 2.0f,
					3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f }), Ir("iridium",
			192.217f, new float[] {}, new float[] { -3.0f, -1.0f, 1.0f, 2.0f,
					3.0f, 4.0f, 5.0f, 6.0f, 8.0f }), Pt("platinum", 195.084f,
			new float[] {}, new float[] { -3.0f, -2.0f, -1.0f, 1.0f, 2.0f,
					3.0f, 4.0f, 5.0f, 6.0f }), Au("gold", 196.96657f,
			new float[] {}, new float[] { -1.0f, 1.0f, 2.0f, 3.0f, 5.0f }), Hg(
			"mercury", 200.592f, new float[] { 2.0f }, new float[] { 1.0f,
					2.0f, 4.0f }), Tl("thallium", 204.38f, new float[] {},
			new float[] { 1.0f, 3.0f }), Pb("lead", 207.2f, new float[] {},
			new float[] { -4.0f, 2.0f, 4.0f }), Bi("bismuth", 208.9804f,
			new float[] {}, new float[] { -3.0f, 1.0f, 3.0f, 5.0f }), Po(
			"polonium", 209.0f, new float[] {}, new float[] { -2.0f, 2.0f,
					4.0f, 6.0f }), At("astatine", 210.0f, new float[] {},
			new float[] { -1.0f, 1.0f, 3.0f, 5.0f, 7.0f }), Rn("radon", 222.0f,
			new float[] {}, new float[] { 2.0f, 6.0f }), Fr("francium", 223.0f,
			new float[] {}, new float[] { 1.0f }), Ra("radium", 226.0f,
			new float[] {}, new float[] { 2.0f }), Ac("actinium", 227.0f,
			new float[] {}, new float[] { 2.0f, 3.0f }), Th("thorium",
			232.03806f, new float[] {}, new float[] { 2.0f, 3.0f, 4.0f }), Pa(
			"protactinium", 231.03587f, new float[] {}, new float[] { 2.0f,
					3.0f, 4.0f, 5.0f }), U("uranium", 238.02892f,
			new float[] {}, new float[] { 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }), Np(
			"neptunium", 237.0f, new float[] {}, new float[] { 3.0f, 4.0f,
					5.0f, 6.0f, 7.0f }), Pu("plutonium", 244.0f,
			new float[] {}, new float[] { 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f }), Am(
			"americium", 243.0f, new float[] {}, new float[] { 2.0f, 3.0f,
					4.0f, 5.0f, 6.0f, 7.0f }), Cm("curium", 247.0f,
			new float[] {}, new float[] { 3.0f, 4.0f }), Bk("berkelium",
			247.0f, new float[] {}, new float[] { 2.0f, 3.0f, 4.0f }), Cf(
			"californium", 251.0f, new float[] {}, new float[] { 2.0f, 3.0f,
					4.0f }), Es("einsteinium", 252.0f, new float[] {},
			new float[] { 2.0f, 3.0f }), Fm("fermium", 257.0f, new float[] {},
			new float[] { 2.0f, 3.0f }), Md("mendelevium", 258.0f,
			new float[] {}, new float[] { 2.0f, 3.0f }), No("nobelium", 259.0f,
			new float[] {}, new float[] { 2.0f, 3.0f }), Lr("lawrencium",
			262.0f, new float[] {}, new float[] { 3.0f }), Rf("rutherfordium",
			267.0f, new float[] {}, new float[] { 4.0f }), Db("dubnium",
			270.0f, new float[] {}, new float[] { 5.0f }), Sg("seaborgium",
			271.0f, new float[] {}, new float[] { 6.0f }), Bh("bohrium",
			270.0f, new float[] {}, new float[] { 7.0f }), Hs("hassium",
			277.0f, new float[] {}, new float[] { 8.0f });
	private final String symbol;
	private final String name;
	private final float weight;
	private final float[] commonOxidation;
	private final float[] oxidation;

	private Element(String name, float weight, float[] commonOxidation,
			float[] oxidation) {
		this.symbol = name();
		this.name = name;
		this.weight = weight;
		this.commonOxidation = commonOxidation;
		this.oxidation = oxidation;
	}

	private Element(String name, float weight) {
		this(name, weight, new float[] {}, new float[] {});
	}

	public float[] getCommonOxidationNumbers() {
		return commonOxidation;
	}

	public String getFullName() {
		return name.substring(0, 1).toUpperCase()
				.concat(name.substring(1).toLowerCase());
	}

	public float[] getAllOxidationNumbers() {
		return oxidation;
	}

	@Override
	public float getOxidationNumber() {
		return commonOxidation[0];
	}

	public float getWeight() {
		return weight;
	}
}
