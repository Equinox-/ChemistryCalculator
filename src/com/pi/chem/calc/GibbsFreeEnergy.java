package com.pi.chem.calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pi.chem.GenericEquation;
import com.pi.chem.db.ThermodynamicQuantities;

public class GibbsFreeEnergy {

	public static void main(String[] args) throws IOException {
		while (true) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			String eq = reader.readLine();
			GenericEquation equation = new GenericEquation(eq);
			/*
			 * String[] parts = eq.split("(->|→|⇌)"); if (parts.length != 2) {
			 * throw new IllegalArgumentException("Must be an equation..."); }
			 * float eqFormation = 0; float eqEntropy = 0; for (int i = 0; i <
			 * parts.length; i++) { String[] chunks =
			 * parts[i].trim().split("( |->|\\+|→)"); float totalFormation = 0;
			 * float totalEntropy = 0; for (String s : chunks) { if
			 * (s.trim().length() > 0) { Matcher m =
			 * MOLECULE_COEFF.matcher(s.trim()); if (m.find()) { String coeffS =
			 * m.group(1).trim(); String mole = m.group(2); float coeff = 1; if
			 * (coeffS.length() > 0) { coeff = Float.valueOf(coeffS); } float
			 * heat = ThermodynamicQuantities .getHeatOfFormation(mole); float
			 * entropy = ThermodynamicQuantities .getStandardEntropy(mole);
			 * totalFormation += coeff * heat; totalEntropy += coeff * entropy;
			 * } } } eqEntropy -= totalEntropy * (i == 0 ? 1 : -1); eqFormation
			 * -= totalFormation * (i == 0 ? 1 : -1); }
			 */
			System.out.println("Standard Entropic Change (ΔS°):\t"
					+ equation.getEntropy() * 1000 + "\tJ / (mol ⋅ K)");
			System.out.println("Heat of Formation (ΔHf°):\t"
					+ equation.getReactionHeat() + "\tkJ / mol");
			System.out.println("Gibbs Standard Free Energy (ΔG°):\t"
					+ equation.getGibbsFreeEnergy() + "\tkJ/mol");
			System.out
					.println("Standard Total Entropy (ΔHt°):\t"
							+ (equation.getEntropy() + (-equation
									.getReactionHeat() / 298.15)) * 1000.0
							+ "\tJ / (mol ⋅ K)");
			System.out.println("Standard Reaction Constant (K°):\t"
					+ equation.getStandardReactionConstant());
			/*System.out.println();
			while (true) {
				try {
					System.out.print("Temperature (K)?\t");
					float temp = Float.valueOf(reader.readLine());
					System.out.println("Gibbs Free Energy (ΔG):\t"
							+ (eqFormation - eqEntropy * temp) + "\tkJ/mol");
					System.out.println("Total Entropy (ΔHt):\t"
							+ (eqEntropy + (-eqFormation / temp)) * 1000.0
							+ "\tJ / (mol ⋅ K)");
					System.out.println("Reaction Constant (K):\t"
							+ Math.exp((eqFormation - eqEntropy * temp)
									* 1000.0 / (-8.314 * temp)));
				} catch (Exception e) {
					break;
				}
			}*/
		}
	}
}
