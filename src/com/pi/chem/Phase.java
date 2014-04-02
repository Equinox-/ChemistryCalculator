package com.pi.chem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Phase {
	LIQUID("l"), AQUEOUS("aq"), SOLID("s"), GASEOUS("g");
	private static Pattern PHASE_MATCHER = Pattern.compile("\\((.+)\\)");
	private String abbr;

	private Phase(String abbr) {
		this.abbr = abbr;
	}

	public String getAbbreviation() {
		return abbr;
	}

	public static Phase findPhase(String s) {
		Matcher m = PHASE_MATCHER.matcher(s);
		if (m.find()) {
			String value = m.group(1);
			return getPhaseByAbbreviation(value);
		}
		return null;
	}

	public static Phase getPhaseByAbbreviation(String value) {
		for (Phase p : Phase.values()) {
			if (value.equalsIgnoreCase(p.abbr)) {
				return p;
			}
		}
		return null;
	}
}