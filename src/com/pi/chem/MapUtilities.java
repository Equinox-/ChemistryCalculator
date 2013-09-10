package com.pi.chem;

import java.util.Map;
import java.util.Map.Entry;

public class MapUtilities {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addToMap(Map addTo, Map<?, ?> addIn) {
		for (Entry e : addIn.entrySet()) {
			if (addTo.containsKey(e.getKey())
					&& !e.getValue().equals(addTo.get(e.getKey()))) {
				System.err.println("Mismatch on " + e.getKey() + ": "
						+ addTo.get(e.getKey()) + "->" + e.getValue());
			}
			addTo.put(e.getKey(), e.getValue());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void mapSum(Map addTo, Map<?, ?> addIn) {
		for (Entry e : addIn.entrySet()) {
			if (addTo.containsKey(e.getKey())) {
				addTo.put(e.getKey(), (Integer) addTo.get(e.getKey())
						+ (Integer) e.getValue());
			} else {
				addTo.put(e.getKey(), e.getValue());
			}
		}
	}
}
