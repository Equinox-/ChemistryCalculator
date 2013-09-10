package com.pi.chem;

public class MetricConversion {
	enum Modifier {
		yotta('Y', 1000000000000000000000000.0), zetta('Z',
				1000000000000000000000.0), exa('E', 1000000000000000000.0), peta(
				'P', 1000000000000000.0), tera('T', 1000000000000.0), giga('G',
				1000000000.0), mega('M', 1000000.0), kilo('k', 1000.0), hecto(
				'h', 100.0), deca("d", 10.0), deci('d', 0.1), centi('c', 0.01), milli(
				'm', 0.001), micro('µ', 0.000001), nano('n', 0.000000001), pico(
				'p', 0.000000000001), femto('f', 0.000000000000001), atto('a',
				0.000000000000000001), zepto('z', 0.000000000000000000001), yocto(
				'y', 0.000000000000000000000001);
		private double multiplier;
		private String sym;

		private Modifier(String sym, double mult) {
			this.sym = sym;
			this.multiplier = mult;
		}

		private Modifier(char sym, double mult) {
			this.sym = Character.toString(sym);
			this.multiplier = mult;
		}

		public static Modifier getModifier(String sym) {
			for (Modifier m : values()) {
				if (m.sym.equals(sym)) {
					return m;
				}
			}
			return null;
		}
	}

	public static double parseToGeneric(String s) {
		int numericEnd = -1;
		for (int i = s.length() - 1; i >= 0; i--) {
			if (Character.isDigit(s.charAt(i))
					|| Character.isWhitespace(s.charAt(i))) {
				numericEnd = i + 1;
				break;
			}
		}
		if (numericEnd == -1) {
			throw new IllegalArgumentException("Not a numeric string.");
		}
		double val = Double.valueOf(s.substring(0, numericEnd).trim());
		String mod = s.substring(numericEnd).trim();
		if (mod.length() != 0) {
			Modifier m = Modifier.getModifier(mod);
			if (m == null) {
				mod = mod.substring(0, mod.length() - 1);
				m = Modifier.getModifier(mod);
			}
			if (m != null) {
				val *= m.multiplier;
			}
		}
		return val;
	}
}
