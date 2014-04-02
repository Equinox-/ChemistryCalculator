package com.pi.chem.db;

import java.util.HashMap;
import java.util.Map;

import com.pi.chem.Molecule;

public class ThermodynamicQuantities {
	private static class ThermodynamicValue {
		private float heatFormation;
		private float gibbsFreeEnergy;
		private float stdEntropy;
	}

	private static Map<String, ThermodynamicValue> data = new HashMap<String, ThermodynamicValue>();

	public static float getHeatOfFormation(String name) {
		ThermodynamicValue val = data.get(name);
		if (val != null) {
			return val.heatFormation;
		}
		return Float.NaN;
	}

	public static float getGibbsFreeEnergy(String name) {
		ThermodynamicValue val = data.get(name);
		if (val != null) {
			return val.gibbsFreeEnergy;
		}
		return Float.NaN;
	}

	public static float getStandardEntropy(String name) {
		ThermodynamicValue val = data.get(name);
		if (val != null) {
			return val.stdEntropy / 1000.0f;// J -> kJ
		}
		return Float.NaN;
	}

	private static void put(String name, double heatFormation,
			double gibbsFree, double stdEntropy) {
		ThermodynamicValue value = new ThermodynamicValue();
		value.heatFormation = (float) heatFormation;
		value.gibbsFreeEnergy = (float) gibbsFree;
		value.stdEntropy = (float) stdEntropy;
		data.put(name, value);
	}

	static {
		put("Al(s)", 0, 0, 28.32);
		put("AlCl3(s)", -705.6, -630, 109.3);
		put("Al2O3(s)", -1669.8, -1576.5, 51);
		put("Ba(s)", 0, 0, 63.2);
		put("BaCO3(s)", -1216.3, -1137.6, 112.1);
		put("BaO(s)", -553.5, -525.1, 70.42);
		put("Be(s)", 0, 0, 9.44);
		put("BeO(s)", -608.4, -579.1, 13.77);
		put("Be(OH)2(s)", -905.8, -817.9, 50.21);
		put("Br(g)", 111.8, 82.38, 174.9);
		put("Br-(aq)", -120.9, -102.8, 80.71);
		put("Br2(g)", 30.71, 3.14, 245.3);
		put("Br2(l)", 0, 0, 152.3);
		put("HBr(g)", -36.23, -53.22, 198.49);
		put("Ca(g)", 179.3, 145.5, 154.8);
		put("Ca(s)", 0, 0, 41.4);
		put("CaCO3(s, calcite)", -1207.1, -1128.76, 92.88);
		put("CaCl2(s)", -795.8, -748.1, 104.6);
		put("CaF2(s)", -1219.6, -1167.3, 68.87);
		put("CaO(s)", -635.5, -604.17, 39.75);
		put("Ca(OH)2(s)", -986.2, -898.5, 83.4);
		put("CaSO4(s)", -1434, -1321.8, 106.7);
		put("C(g)", 718.4, 672.9, 158);
		put("C(s, diamond)", 1.88, 2.84, 2.43);
		put("C(s, graphite)", 0, 0, 5.69);
		put("CCl4(g)", -106.7, -64, 309.4);
		put("CCl4(l)", -139.3, -68.6, 214.4);
		put("CF4(g)", -679.9, -635.1, 262.3);
		put("CH4(g)", -74.8, -50.8, 186.3);
		put("C2H2(g)", 226.77, 209.2, 200.8);
		put("C2H4(g)", 52.3, 68.11, 219.4);
		put("C2H6(g)", -84.68, -32.89, 229.5);
		put("C3H8(g)", -103.85, -23.47, 269.9);
		put("C4H10(g)", -124.73, -15.71, 310);
		put("C4H10(l)", -147.6, -15, 231);
		put("C6H6(g)", 82.9, 129.7, 269.2);
		put("C6H6(l)", 49, 124.5, 172.8);
		put("CH3OH(g)", -201.2, -161.9, 237.6);
		put("CH3OH(l)", -238.6, -166.23, 126.8);
		put("C2H5OH(g)", -235.1, -168.5, 282.7);
		put("C2H5OH(l)", -277.7, -174.76, 160.7);
		put("C6H12O6(s)", -1273.02, -910.4, 212.1);
		put("CO(g)", -110.5, -137.2, 197.9);
		put("CO2(g)", -393.5, -394.4, 213.6);
		put("CH3COOH(l)", -487, -392.4, 159.8);
		put("Cs(g)", 76.5, 49.53, 175.6);
		put("Cs(l)", 2.09, 0.03, 92.07);
		put("Cs(s)", 0, 0, 85.15);
		put("CsCl(s)", -442.8, -414.4, 101.2);
		put("Cl(g)", 121.7, 105.7, 165.2);
		put("Cl(aq)", -167.2, -131.2, 56.5);
		put("Cl2(g)", 0, 0, 222.96);
		put("HCl(aq)", -167.2, -131.2, 56.5);
		put("HCl(g)", -92.3, -95.27, 186.69);
		put("Cr(g)", 397.5, 352.6, 174.2);
		put("Cr(s)", 0, 0, 23.6);
		put("Cr2O3(s)", -1139.7, -1058.1, 81.2);
		put("Co(g)", 439, 393, 179);
		put("Co(s)", 0, 0, 28.4);
		put("Cu(g)", 338.4, 298.6, 166.3);
		put("Cu(s)", 0, 0, 33.3);
		put("CuCl2(s)", -205.9, -161.7, 108.1);
		put("CuO(s)", -156.1, -128.3, 42.59);
		put("Cu2O(s)", -170.7, -147.9, 92.36);
		put("MgO(s)", -601.8, -569.6, 26.8);
		put("Mg(OH)2(s)", -924.7, -833.7, 63.24);
		put("Mn(g)", 280.7, 238.5, 173.6);
		put("Mn(s)", 0, 0, 32);
		put("MnO(s)", -385.2, -362.9, 59.7);
		put("MnO2(s)", -519.6, -464.8, 53.14);
		put("MnO4-(aq)", -541.4, -447.2, 191.2);
		put("Hg(g)", 60.83, 31.76, 174.89);
		put("Hg(l)", 0, 0, 77.4);
		put("HgCl2(s)", -230.1, -184, 144.5);
		put("Hg2Cl2(s)", -264.9, -210.5, 192.5);
		put("Ni(g)", 429.7, 384.5, 182.1);
		put("Ni(s)", 0, 0, 29.9);
		put("NiCl2(s)", -305.3, -259, 97.65);
		put("NiO(s)", -239.7, -211.7, 37.99);
		put("NiS(s)", Float.NaN, Float.NaN, 53.0);
		put("Pb(s)", 0, 0, 68.85);
		put("PbBr2(s)", -277.4, -260.7, 161);
		put("PbCO3(s)", -699.1, -625.5, 131);
		put("Pb(NO3)2(aq)", -421.3, -246.9, 303.3);
		put("Pb(NO3)2(s)", -451.9, Float.NaN, Float.NaN);
		put("PbO(s)", -217.3, -187.9, 68.7);
		put("N(g)", 472.7, 455.5, 153.3);
		put("N2(g)", 0, 0, 191.5);
		put("NH3(aq)", -80.29, -26.5, 111.3);
		put("NH3(g)", -46.19, -16.66, 192.5);
		put("NH4 + 1aq2", -132.5, -79.31, 113.4);
		put("N2H4(g)", 95.4, 159.4, 238.5);
		put("NH4CN(s)", 0, Float.NaN, Float.NaN);
		put("NH4Cl(s)", -314.4, -203, 94.6);
		put("NH4NO3(s)", -365.6, -184, 151);
		put("NO(g)", 90.37, 86.71, 210.62);
		put("NO2(g)", 33.84, 51.84, 240.45);
		put("N2O(g)", 81.6, 103.59, 220);
		put("N2O4(g)", 9.66, 98.28, 304.3);
		put("NOCl(g)", 52.6, 66.3, 264);
		put("HNO3(aq)", -206.6, -110.5, 146);
		put("HNO3(g)", -134.3, -73.94, 266.4);
		put("Li(g)", 159.3, 126.6, 138.8);
		put("Li(s)", 0, 0, 29.09);
		put("Li+(aq)", -278.5, -273.4, 12.2);
		put("Li+(g)", 685.7, 648.5, 133);
		put("LiCl(s)", -408.3, -384, 59.3);
		put("Mg(g)", 147.1, 112.5, 148.6);
		put("Mg(s)", 0, 0, 32.51);
		put("MgCl2(s)", -641.6, -592.1, 89.6);
		put("O(g)", 247.5, 230.1, 161);
		put("O2(g)", 0, 0, 205);
		put("O3(g)", 142.3, 163.4, 237.6);
		put("OH-1aq2", -230, -157.3, -10.7);
		put("H2O(g)", -241.82, -228.57, 188.83);
		put("H2O(l)", -285.83, -237.13, 69.91);
		put("H2O2(g)", -136.1, -105.48, 232.9);
		put("H2O2(l)", -187.8, -120.4, 109.6);
		put("P(g)", 316.4, 280, 163.2);
		put("P2(g)", 144.3, 103.7, 218.1);
		put("F(g)", 80, 61.9, 158.7);
		put("F(aq)", -332.6, -278.8, -13.8);
		put("F2(g)", 0, 0, 202.7);
		put("HF(g)", -268.61, -270.7, 173.51);
		put("H(g)", 217.94, 203.26, 114.6);
		put("H+(aq)", 0, 0, 0);
		put("H+(g)", 1536.2, 1517, 108.9);
		put("H2(g)", 0, 0, 130.58);
		put("I(g)", 106.6, 70.16, 180.66);
		put("I-(aq)", -55.19, -51.57, 111.3);
		put("I2(g)", 62.25, 19.37, 260.57);
		put("I2(s)", 0, 0, 116.73);
		put("HI(g)", 25.94, 1.3, 206.3);
		put("Fe(g)", 415.5, 369.8, 180.5);
		put("Fe(s)", 0, 0, 27.15);
		put("Fe2+(aq)", -87.86, -84.93, 113.4);
		put("Fe3+(aq)", -47.69, -10.54, 293.3);
		put("FeCl2(s)", -341.8, -302.3, 117.9);
		put("FeCl3(s)", -400, -334, 142.3);
		put("FeO(s)", -271.9, -255.2, 60.75);
		put("Fe2O3(s)", -822.16, -740.98, 89.96);
		put("Fe3O4(s)", -1117.1, -1014.2, 146.4);
		put("FeS2(s)", -171.5, -160.1, 52.92);
		put("P4(g)", 58.9, 24.4, 280);
		put("P4(s, red)", -17.46, -12.03, 22.85);
		put("P4(s, white)", 0, 0, 41.08);
		put("PCl3(g)", -288.07, -269.6, 311.7);
		put("PCl3(l)", -319.6, -272.4, 217);
		put("PF5(g)", -1594.4, -1520.7, 300.8);
		put("PH3(g)", 5.4, 13.4, 210.2);
		put("P4O6(s)", -1640.1, Float.NaN, Float.NaN);
		put("P4O10(s)", -2940.1, -2675.2, 228.9);
		put("POCl3(g)", -542.2, -502.5, 325);
		put("POCl3(l)", -597, -520.9, 222);
		put("H3PO4(aq)", -1288.3, -1142.6, 158.2);
		put("K(g)", 89.99, 61.17, 160.2);
		put("K(s)", 0, 0, 64.67);
		put("KCl(s)", -435.9, -408.3, 82.7);
		put("KClO3(s)", -391.2, -289.9, 143);
		put("KClO3(aq)", -349.5, -284.9, 265.7);
		put("K2CO3(s)", -1150.18, -1064.58, 155.44);
		put("KNO3(s)", -492.7, -393.13, 132.9);
		put("K2O(s)", -363.2, -322.1, 94.14);
		put("KO2(s)", -284.5, -240.6, 122.5);
		put("K2O2(s)", -495.8, -429.8, 113);
		put("KOH(s)", -424.7, -378.9, 78.91);
		put("KOH(aq)", -482.4, -440.5, 91.6);
		put("Rb(g)", 85.8, 55.8, 170);
		put("Rb(s)", 0, 0, 76.78);
		put("RbCl(s)", -430.5, -412, 92);
		put("RbClO3(s)", -392.4, -292, 152);
		put("Sc(g)", 377.8, 336.1, 174.7);
		put("Sc(s)", 0, 0, 34.6);
		put("H2Se(g)", 29.7, 15.9, 219);
		put("Si(g)", 368.2, 323.9, 167.8);
		put("Si(s)", 0, 0, 18.7);
		put("SiC(s)", -73.22, -70.85, 16.61);
		put("SiCl4(l)", -640.1, -572.8, 239.3);
		put("SiO2(s, quartz)", -910.9, -856.5, 41.84);
		put("Ag(s)", 0, 0, 42.55);
		put("Ag + 1aq2", 105.9, 77.11, 73.93);
		put("AgCl(s)", -127, -109.7, 96.11);
		put("Ag2O(s)", -31.05, -11.2, 121.3);
		put("AgNO3(s)", -124.4, -33.41, 140.9);
		put("Na(g)", 107.7, 77.3, 153.7);
		put("Na(s)", 0, 0, 51.45);
		put("Na+(aq)", -240.1, -261.9, 59);
		put("Na+(g)", 609.3, 574.3, 148);
		put("NaBr(aq)", -360.6, -364.7, 141);
		put("NaBr(s)", -361.4, -349.3, 86.82);
		put("Na2CO3(s)", -1130.9, -1047.7, 136);
		put("NaCl(aq)", -407.1, -393, 115.5);
		put("NaCl(g)", -181.4, -201.3, 229.8);
		put("NaCl(s)", -410.9, -384, 72.33);
		put("NaHCO3(s)", -947.7, -851.8, 102.1);
		put("NaNO3(aq)", -446.2, -372.4, 207);
		put("NaNO3(s)", -467.9, -367, 116.5);
		put("NaOH(aq)", -469.6, -419.2, 49.8);
		put("NaOH(s)", -425.6, -379.5, 64.46);
		put("Na2SO4(s)", -1387.1, -1270.2, 149.6);
		put("SrO(s)", -592, -561.9, 54.9);
		put("Sr(g)", 164.4, 110, 164.6);
		put("S(s, rhombic)", 0, 0, 31.88);
		put("S(s)", 0, 0, 31.88);
		put("S8(g)", 102.3, 49.7, 430.9);
		put("SO2(g)", -296.9, -300.4, 248.5);
		put("SO3(g)", -395.2, -370.4, 256.2);
		put("SO4 2- (aq)", -909.3, -744.5, 20.1);
		put("SOCl2(l)", -245.6, Float.NaN, Float.NaN);
		put("H2S(g)", -20.17, -33.01, 205.6);
		put("H2SO4(aq)", -909.3, -744.5, 20.1);
		put("H2SO4(l)", -814, -689.9, 156.1);
		put("Ti(g)", 468, 422, 180.3);
		put("Ti(s)", 0, 0, 30.76);
		put("TiCl4(g)", -763.2, -726.8, 354.9);
		put("TiCl4(l)", -804.2, -728.1, 221.9);
		put("TiO2(s)", -944.7, -889.4, 50.29);
		put("V(g)", 514.2, 453.1, 182.2);
		put("V(s)", 0, 0, 28.9);
		put("Zn(g)", 130.7, 95.2, 160.9);
		put("Zn(s)", 0, 0, 41.63);
		put("ZnCl2(s)", -415.1, -369.4, 111.5);
		put("ZnO(s)", -348, -318.2, 43.9);
	}
}
