package com.pi.chem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pi.chem.db.Element;

public class RedoxEquation {
	private static final RedoxMolecule[] IONIC_MOLECULES = { new RedoxMolecule(
			"H2O", 0) };
	private List<RedoxMolecule> leftMolecules;
	private Map<Element, Float> leftCharges;
	private List<String> claimedLeftMolecules = new ArrayList<String>();

	private List<RedoxMolecule> rightMolecules;
	private Map<Element, Float> rightCharges;
	private List<String> claimedRightMolecules = new ArrayList<String>();

	private Element oxidized;
	private Element reduced;

	private HalfEquation oxidation;
	private HalfEquation reduction;

	private List<RedoxMolecule> balancedLeft = new ArrayList<RedoxMolecule>();
	private List<RedoxMolecule> balancedRight = new ArrayList<RedoxMolecule>();
	private int electronsExchanged;

	public static List<RedoxMolecule> createRedoxList(List<Molecule> orig) {
		List<RedoxMolecule> m = new ArrayList<RedoxMolecule>();
		for (Molecule s : orig) {
			m.add(new RedoxMolecule(s.getExplodedEquation(), s
					.getOverallCharge()));
		}
		return m;
	}

	public RedoxEquation(String left, String right) {
		leftMolecules = createRedoxList(EquationParser
				.getMoleculesInEquation(left));
		leftCharges = new HashMap<Element, Float>();
		for (RedoxMolecule s : leftMolecules) {
			MapUtilities.addToMap(leftCharges,
					EquationParser.getChargesInMolecule(s.eqn, s.charge));
		}

		rightMolecules = createRedoxList(EquationParser
				.getMoleculesInEquation(right));
		rightCharges = new HashMap<Element, Float>();
		for (RedoxMolecule s : rightMolecules) {
			MapUtilities.addToMap(rightCharges,
					EquationParser.getChargesInMolecule(s.eqn, s.charge));
		}
		System.out.println("Left Charges: "
				+ Arrays.toString(leftCharges.entrySet().toArray()));
		System.out.println("Right Charges: "
				+ Arrays.toString(rightCharges.entrySet().toArray()));

		for (Entry<Element, Float> e : leftCharges.entrySet()) {
			float lCharge = e.getValue();
			float rCharge = rightCharges.get(e.getKey());
			if (lCharge > rCharge) {
				if (reduced != null) {
					throw new RuntimeException("Lawl we can't do double redox");
				}
				reduced = e.getKey();
			} else if (rCharge > lCharge) {
				if (oxidized != null) {
					throw new RuntimeException("Lawl we can't do double redox");
				}
				oxidized = e.getKey();
			}
		}

		if (oxidized == null || reduced == null) {
			throw new RuntimeException("Not a redox equation!");
		}
		reduction = new HalfEquation(reduced);
		reduction.generate();
		oxidation = new HalfEquation(oxidized);
		oxidation.generate();
	}

	private static class RedoxMolecule {
		private float charge;
		private String eqn;
		private int multiplier = 1;
		private Map<Element, Integer> counts;

		private RedoxMolecule() {
		}

		private RedoxMolecule(String eq, float charge) {
			this(eq, charge, 1);
		}

		private RedoxMolecule(String eq, float charge, int mult) {
			this.eqn = eq;
			this.charge = charge;
			this.multiplier = mult;
			updateCounts();
		}

		private void updateCounts() {
			counts = EquationParser.getElementCounts(EquationParser
					.getElementsInMolecule(eqn));
			for (Entry<Element, Integer> e : counts.entrySet()) {
				e.setValue(e.getValue() * multiplier);
			}
		}

		public String toString() {
			return multiplier + eqn;
		}

		public boolean equals(Object o) {
			if (o instanceof RedoxMolecule) {
				RedoxMolecule rm = (RedoxMolecule) o;
				return rm.eqn.equals(eqn) && rm.multiplier == multiplier
						&& rm.charge == charge;
			}
			return false;
		}
	}

	private class HalfEquation {
		private List<RedoxMolecule> left;
		private List<RedoxMolecule> right;
		private Map<Element, Integer> leftCounts;
		private Map<Element, Integer> rightCounts;
		private Element using;
		private float leftElectrons;

		private HalfEquation(Element e) {
			this.using = e;
		}

		private void generate() {
			left = new ArrayList<RedoxMolecule>();
			leftCounts = new HashMap<Element, Integer>();
			right = new ArrayList<RedoxMolecule>();
			rightCounts = new HashMap<Element, Integer>();
			for (RedoxMolecule e : leftMolecules) {
				if (e.counts.containsKey(using)
						&& !claimedLeftMolecules.contains(e.eqn)) {
					claimedLeftMolecules.add(e.eqn);
					left.add(new RedoxMolecule(e.eqn, e.charge));
					MapUtilities.mapSum(leftCounts, (EquationParser
							.getElementCounts(EquationParser
									.getElementsInMolecule(e.eqn))));
				}
			}
			for (RedoxMolecule e : rightMolecules) {
				if (e.counts.containsKey(using)
						&& !claimedRightMolecules.contains(e.eqn)) {
					claimedRightMolecules.add(e.eqn);
					right.add(new RedoxMolecule(e.eqn, e.charge));
					MapUtilities.mapSum(rightCounts, EquationParser
							.getElementCounts(EquationParser
									.getElementsInMolecule(e.eqn)));
				}
			}
		}

		private void updateCounts() {
			leftCounts.clear();
			rightCounts.clear();
			for (RedoxMolecule m : left) {
				MapUtilities.mapSum(leftCounts, m.counts);
			}
			for (RedoxMolecule m : right) {
				MapUtilities.mapSum(rightCounts, m.counts);
			}
		}

		public void balance() {
			Element[] els = leftCounts.keySet().toArray(new Element[0]);
			for (Element e : els) {
				if (e != using) {
					continue;
				}
				Integer i2 = rightCounts.get(e);
				Integer i = leftCounts.get(e);
				if (i2 != null) {
					if (i2 != i) {
						// well balance these first
						int bTot = (int) OMath.lcm(i.floatValue(),
								i2.floatValue());
						for (RedoxMolecule m : left) {
							if (m.counts.containsKey(e)) {
								m.multiplier = bTot / m.counts.get(e);
								m.updateCounts();
							}
						}
						for (RedoxMolecule m : right) {
							if (m.counts.containsKey(e)) {
								m.multiplier = bTot / m.counts.get(e);
								m.updateCounts();
							}
						}
						updateCounts();
					}
				}
			}
			for (int tryy = 0; tryy < 10 && !isBalanced(); tryy++) {
				balanceIncr();
			}
			// Lets just calc the electrons and gtfo
			float leftCharge = 0;
			float rightCharge = 0;
			for (RedoxMolecule m : left) {
				leftCharge += m.charge;
			}
			for (RedoxMolecule m : right) {
				rightCharge += m.charge;
			}
			leftElectrons = leftCharge - rightCharge;

			System.out.print(Arrays.toString(left.toArray()));
			System.out.print("\t->\t");
			System.out.println(Arrays.toString(right.toArray()));
		}

		private boolean isBalanced() {
			if (leftCounts.size() == rightCounts.size()) {
				for (Entry<Element, Integer> e : leftCounts.entrySet()) {
					if (rightCounts.get(e.getKey()) != e.getValue()) {
						return false;
					}
				}
				return true;
			}
			return false;
		}

		private boolean tryBalanceLeft(Element e) {
			if ((!rightCounts.containsKey(e) && leftCounts.containsKey(e))
					|| (leftCounts.containsKey(e) && rightCounts.containsKey(e) && rightCounts
							.get(e) < leftCounts.get(e))) {
				if (!rightCounts.containsKey(e)) {
					rightCounts.put(e, 0);
				}
				// First try to use other molecules
				for (RedoxMolecule s : rightMolecules) {
					if (s.counts.containsKey(e)
							&& !claimedRightMolecules.contains(s.eqn)) {
						int count = (leftCounts.get(e) - rightCounts.get(e))
								/ s.counts.get(e);
						right.add(new RedoxMolecule(s.eqn, s.charge * count,
								count));
						claimedRightMolecules.add(s.eqn);
						updateCounts();
						return true;
					}
				}
				right.add(new RedoxMolecule(e.name(), leftCharges.get(e)
						* (leftCounts.get(e) - rightCounts.get(e)), leftCounts
						.get(e) - rightCounts.get(e)));
				updateCounts();
				return true;
			}
			return false;
		}

		private boolean tryBalanceRight(Element e) {
			if ((!leftCounts.containsKey(e) && rightCounts.containsKey(e))
					|| (leftCounts.containsKey(e) && rightCounts.containsKey(e) && leftCounts
							.get(e) < rightCounts.get(e))) {
				if (!leftCounts.containsKey(e)) {
					leftCounts.put(e, 0);
				}
				// First try to use other molecules
				for (RedoxMolecule s : leftMolecules) {
					if (s.counts.containsKey(e)
							&& !claimedLeftMolecules.contains(s.eqn)) {
						int count = (rightCounts.get(e) - leftCounts.get(e))
								/ s.counts.get(e);
						left.add(new RedoxMolecule(s.eqn, s.charge * count,
								count));
						claimedLeftMolecules.add(s.eqn);
						updateCounts();
						return true;
					}
				}
				left.add(new RedoxMolecule(e.name(), rightCharges.get(e)
						* (rightCounts.get(e) - leftCounts.get(e)), rightCounts
						.get(e) - leftCounts.get(e)));
				updateCounts();
				return true;
			}
			return false;
		}

		private void balanceIncr() {
			if (tryBalanceLeft(Element.O)) {
				return;
			}
			if (tryBalanceLeft(Element.H)) {
				return;
			}
			for (Element e : leftCounts.keySet()) {
				if (e == Element.O || e == Element.H) {
					continue;
				}
				if (tryBalanceLeft(e)) {
					return;
				}
			}

			if (tryBalanceRight(Element.O)) {
				return;
			}
			if (tryBalanceRight(Element.H)) {
				return;
			}
			for (Element e : rightCounts.keySet()) {
				if (e == Element.O || e == Element.H) {
					continue;
				}
				if (tryBalanceRight(e)) {
					return;
				}
			}
		}
	}

	public void balance() {
		System.out.println("Balancing half equations: ");
		System.out.println("Reduction: ");
		reduction.balance();
		System.out.println("Oxidation: ");
		oxidation.balance();

		if (reduction.leftElectrons * oxidation.leftElectrons >= 0) {
			throw new RuntimeException("Electron inbalance!");
		}
		electronsExchanged = (int) OMath.lcm(Math.abs(reduction.leftElectrons),
				Math.abs(oxidation.leftElectrons));
		int rMult = (int) (getElectronsExchanged() / Math
				.abs(reduction.leftElectrons));
		int oMult = (int) (getElectronsExchanged() / Math
				.abs(oxidation.leftElectrons));
		System.out.println("Reduction multiplier: " + rMult
				+ ", Oxidation multiplier: " + oMult);
		getBalancedLeft().clear();
		getBalancedRight().clear();
		out: for (RedoxMolecule m : oxidation.left) {
			for (RedoxMolecule m2 : getBalancedLeft()) {
				if (m2.eqn.equals(m.eqn)) {
					m2.charge += m.charge;
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedLeft().add(
					new RedoxMolecule(m.eqn, m.charge * oMult, m.multiplier
							* oMult));
		}
		out: for (RedoxMolecule m : oxidation.right) {
			for (RedoxMolecule m2 : getBalancedRight()) {
				if (m2.eqn.equals(m.eqn)) {
					m2.charge += m.charge;
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedRight().add(
					new RedoxMolecule(m.eqn, m.charge * oMult, m.multiplier
							* oMult));
		}
		out: for (RedoxMolecule m : reduction.left) {
			for (RedoxMolecule m2 : getBalancedLeft()) {
				if (m2.eqn.equals(m.eqn)) {
					m2.charge += m.charge;
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedLeft().add(
					new RedoxMolecule(m.eqn, m.charge * rMult, m.multiplier
							* rMult));
		}
		out: for (RedoxMolecule m : reduction.right) {
			for (RedoxMolecule m2 : getBalancedRight()) {
				if (m2.eqn.equals(m.eqn)) {
					m2.charge += m.charge;
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedRight().add(
					new RedoxMolecule(m.eqn, m.charge * rMult, m.multiplier
							* rMult));
		}

		// Combine those equivalent pieces
		combineEquals(getBalancedLeft());
		combineEquals(getBalancedRight());

		// We could now like... combine ionic
		combineIonicChunks();

		// Cancel those equivalent pieces
		cancelEquals();

		// Drop 0s
		Iterator<RedoxMolecule> itr = getBalancedLeft().listIterator();
		while (itr.hasNext()) {
			if (itr.next().multiplier == 0) {
				itr.remove();
			}
		}
		itr = getBalancedRight().listIterator();
		while (itr.hasNext()) {
			if (itr.next().multiplier == 0) {
				itr.remove();
			}
		}
	}

	private void combineEquals(List<RedoxMolecule> list) {
		for (RedoxMolecule m : list) {
			for (RedoxMolecule m2 : list) {
				if (m == m2) {
					continue;
				} else if (m2.eqn.equals(m.eqn)) {
					m.multiplier += m2.multiplier;
					m.updateCounts();
					m2.multiplier = 0;
				}
			}
		}
	}

	private void cancelEquals() {
		for (RedoxMolecule m : getBalancedLeft()) {
			for (RedoxMolecule m2 : getBalancedRight()) {
				if (m.eqn.equals(m2.eqn) && m.charge == m2.charge) {
					if (m2.multiplier >= m.multiplier) {
						m2.multiplier -= m.multiplier;
						m.multiplier = 0;
					} else {
						m.multiplier -= m2.multiplier;
						m2.multiplier = 0;
					}
				}
			}
		}
	}

	private void combineIonicChunks() {
		for (RedoxMolecule m : IONIC_MOLECULES) {
			{
				List<RedoxMolecule> claiming = new ArrayList<RedoxMolecule>();
				int count = Integer.MAX_VALUE;
				for (RedoxMolecule rm : getBalancedLeft()) {
					try {
						Element e = Element.valueOf(rm.eqn);
						if (e != null && m.counts.containsKey(e)
								&& m.counts.get(e) > 0 && rm.multiplier > 0) {
							claiming.add(rm);
							count = Math.min(count,
									rm.multiplier / m.counts.get(e));
						}
					} catch (Exception e) {
					}
				}
				if (count != Integer.MAX_VALUE
						&& claiming.size() == m.counts.keySet().size()) {
					// Ok drop those we are using and add the new one
					getBalancedLeft().add(new RedoxMolecule(m.eqn, 0, count));
					for (RedoxMolecule mod : claiming) {
						mod.multiplier -= count
								* m.counts.get(Element.valueOf(mod.eqn));
					}
				}
			}
			{
				List<RedoxMolecule> claiming = new ArrayList<RedoxMolecule>();
				int count = Integer.MAX_VALUE;
				for (RedoxMolecule rm : getBalancedRight()) {
					try {
						Element e = Element.valueOf(rm.eqn);
						if (e != null && m.counts.containsKey(e)
								&& m.counts.get(e) > 0 && rm.multiplier > 0) {
							claiming.add(rm);
							count = Math.min(count,
									rm.multiplier / m.counts.get(e));
						}
					} catch (Exception e) {
					}
				}
				if (count != Integer.MAX_VALUE
						&& claiming.size() == m.counts.keySet().size()) {
					// Ok drop those we are using and add the new one
					getBalancedRight().add(new RedoxMolecule(m.eqn, 0, count));
					for (RedoxMolecule mod : claiming) {
						mod.multiplier -= count
								* m.counts.get(Element.valueOf(mod.eqn));
					}
				}
			}
		}
	}

	public Element getReducedElement() {
		return reduced;
	}

	public Element getOxidizedElement() {
		return oxidized;
	}

	public List<RedoxMolecule> getBalancedLeft() {
		return balancedLeft;
	}

	public List<RedoxMolecule> getBalancedRight() {
		return balancedRight;
	}

	public int getElectronsExchanged() {
		return electronsExchanged;
	}
}
