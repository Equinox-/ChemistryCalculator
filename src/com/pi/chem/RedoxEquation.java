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
			"H2O", 0, null) };
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
					.getOverallCharge(), null));
		}
		return m;
	}

	public RedoxEquation(String left, String right) {
		GenericEquation equation = new GenericEquation(left + "->" + right);
		leftMolecules = createRedoxList(equation.leftMolecules);
		leftCharges = new HashMap<Element, Float>();
		for (RedoxMolecule s : leftMolecules) {
			MapUtilities.addToMap(leftCharges,
					ChargeComputer.getChargesInMolecule(s));
		}

		rightMolecules = createRedoxList(equation.rightMolecules);
		rightCharges = new HashMap<Element, Float>();
		for (RedoxMolecule s : rightMolecules) {
			MapUtilities.addToMap(rightCharges,
					ChargeComputer.getChargesInMolecule(s));
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

	private static class RedoxMolecule extends Molecule {
		private int multiplier;

		private RedoxMolecule(String eq, float charge, Phase phase) {
			this(eq, charge, phase, 1);
		}

		private RedoxMolecule(String eq, float charge, Phase phase, int mult) {
			super(eq, charge, phase);
		}

		public String toString() {
			return multiplier + super.getEquation();
		}

		public boolean equals(Object o) {
			if (o instanceof RedoxMolecule) {
				RedoxMolecule rm = (RedoxMolecule) o;
				return super.equals(o) && rm.multiplier == multiplier;
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
				if (e.getElementCounts().containsKey(using)
						&& !claimedLeftMolecules.contains(e.getEquation())) {
					claimedLeftMolecules.add(e.getEquation());
					left.add(new RedoxMolecule(e.getEquation(), e
							.getOverallCharge(), null));
					MapUtilities.mapSum(leftCounts, e.getElementCounts(),
							e.multiplier);
				}
			}
			for (RedoxMolecule e : rightMolecules) {
				if (e.getElementCounts().containsKey(using)
						&& !claimedRightMolecules.contains(e.getEquation())) {
					claimedRightMolecules.add(e.getEquation());
					right.add(new RedoxMolecule(e.getEquation(), e
							.getOverallCharge(), null));
					MapUtilities.mapSum(rightCounts, e.getElementCounts(),
							e.multiplier);
				}
			}
		}

		private void updateCounts() {
			leftCounts.clear();
			rightCounts.clear();
			for (RedoxMolecule m : left) {
				MapUtilities.mapSum(leftCounts, m.getElementCounts(),
						m.multiplier);
			}
			for (RedoxMolecule m : right) {
				MapUtilities.mapSum(rightCounts, m.getElementCounts(),
						m.multiplier);
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
							if (m.getElementCounts().containsKey(e)) {
								m.multiplier = bTot
										/ m.getElementCounts().get(e);
							}
						}
						for (RedoxMolecule m : right) {
							if (m.getElementCounts().containsKey(e)) {
								m.multiplier = bTot
										/ m.getElementCounts().get(e);
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
				leftCharge += m.getOverallCharge() * m.multiplier;
			}
			for (RedoxMolecule m : right) {
				rightCharge += m.getOverallCharge() * m.multiplier;
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
					if (s.getElementCounts().containsKey(e)
							&& !claimedRightMolecules.contains(s.getEquation())) {
						int count = (leftCounts.get(e) - rightCounts.get(e))
								/ s.getElementCounts().get(e);
						right.add(new RedoxMolecule(s.getEquation(), s
								.getOverallCharge(), null, count));
						claimedRightMolecules.add(s.getEquation());
						updateCounts();
						return true;
					}
				}
				right.add(new RedoxMolecule(e.name(), leftCharges.get(e), null,
						leftCounts.get(e) - rightCounts.get(e)));
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
					if (s.getElementCounts().containsKey(e)
							&& !claimedLeftMolecules.contains(s.getEquation())) {
						int count = (rightCounts.get(e) - leftCounts.get(e))
								/ s.getElementCounts().get(e);
						left.add(new RedoxMolecule(s.getEquation(), s
								.getOverallCharge(), null, count));
						claimedLeftMolecules.add(s.getEquation());
						updateCounts();
						return true;
					}
				}
				left.add(new RedoxMolecule(e.name(), rightCharges.get(e), null,
						rightCounts.get(e) - leftCounts.get(e)));
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
				if (m2.getEquation().equals(m.getEquation())) {
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedLeft().add(
					new RedoxMolecule(m.getEquation(), m.getOverallCharge(),
							null, m.multiplier * oMult));
		}
		out: for (RedoxMolecule m : oxidation.right) {
			for (RedoxMolecule m2 : getBalancedRight()) {
				if (m2.getEquation().equals(m.getEquation())) {
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedRight().add(
					new RedoxMolecule(m.getEquation(), m.getOverallCharge(),
							null, m.multiplier * oMult));
		}
		out: for (RedoxMolecule m : reduction.left) {
			for (RedoxMolecule m2 : getBalancedLeft()) {
				if (m2.getEquation().equals(m.getEquation())) {
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedLeft().add(
					new RedoxMolecule(m.getEquation(), m.getOverallCharge(),
							null, m.multiplier * rMult));
		}
		out: for (RedoxMolecule m : reduction.right) {
			for (RedoxMolecule m2 : getBalancedRight()) {
				if (m2.getEquation().equals(m.getEquation())) {
					m2.multiplier += m.multiplier;
					continue out;
				}
			}
			getBalancedRight().add(
					new RedoxMolecule(m.getEquation(), m.getOverallCharge(),
							null, m.multiplier * rMult));
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
				} else if (m2.getEquation().equals(m.getEquation())) {
					m.multiplier += m2.multiplier;
					m2.multiplier = 0;
				}
			}
		}
	}

	private void cancelEquals() {
		for (RedoxMolecule m : getBalancedLeft()) {
			for (RedoxMolecule m2 : getBalancedRight()) {
				if (m.getEquation().equals(m2.getEquation())
						&& m.getOverallCharge() * m.multiplier == m2
								.getOverallCharge() * m2.multiplier) {
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
						Element e = Element.valueOf(rm.getEquation());
						if (e != null && m.getElementCounts().containsKey(e)
								&& m.getElementCounts().get(e) > 0
								&& rm.multiplier > 0) {
							claiming.add(rm);
							count = Math.min(count, rm.multiplier
									/ m.getElementCounts().get(e));
						}
					} catch (Exception e) {
					}
				}
				if (count != Integer.MAX_VALUE
						&& claiming.size() == m.getElementCounts().keySet()
								.size()) {
					// Ok drop those we are using and add the new one
					getBalancedLeft().add(
							new RedoxMolecule(m.getEquation(), 0, null, count));
					for (RedoxMolecule mod : claiming) {
						mod.multiplier -= count
								* m.getElementCounts().get(
										Element.valueOf(mod.getEquation()));
					}
				}
			}
			{
				List<RedoxMolecule> claiming = new ArrayList<RedoxMolecule>();
				int count = Integer.MAX_VALUE;
				for (RedoxMolecule rm : getBalancedRight()) {
					try {
						Element e = Element.valueOf(rm.getEquation());
						if (e != null && m.getElementCounts().containsKey(e)
								&& m.getElementCounts().get(e) > 0
								&& rm.multiplier > 0) {
							claiming.add(rm);
							count = Math.min(count, rm.multiplier
									/ m.getElementCounts().get(e));
						}
					} catch (Exception e) {
					}
				}
				if (count != Integer.MAX_VALUE
						&& claiming.size() == m.getElementCounts().keySet()
								.size()) {
					// Ok drop those we are using and add the new one
					getBalancedRight().add(
							new RedoxMolecule(m.getEquation(), 0, null, count));
					for (RedoxMolecule mod : claiming) {
						mod.multiplier -= count
								* m.getElementCounts().get(
										Element.valueOf(mod.getEquation()));
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
