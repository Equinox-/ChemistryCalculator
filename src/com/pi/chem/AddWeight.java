package com.pi.chem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddWeight {
	public static void main(String[] args) throws IOException {
		Map<Integer, Float> values = new HashMap<Integer, Float>();
		BufferedReader db = new BufferedReader(new FileReader(new File("wt")));
		while (true) {
			String s = db.readLine();
			if (s == null) {
				break;
			}
			String[] data = s.split("\t");
			String wt = data[3];
			wt = wt.replace("[", "").replace("]", "");
			int idx = wt.indexOf('(');
			if (idx >= 0) {
				wt = wt.substring(0, idx);
			}
			System.out.println(Float.valueOf(wt) + "\t" + wt);
			values.put(Integer.valueOf(data[0]), Float.valueOf(wt));
		}
		db.close();

		BufferedWriter out = new BufferedWriter(new FileWriter(new File("out")));
		Element[] eles = Element.values();
		for (int i = 0; i < eles.length; i++) {
			Element e = eles[i];
			out.write(e.name() + "(\"" + e.getFullName() + "\","
					+ values.get(i + 1) + "f,new float[]{");
			boolean flag = false;
			for (float f : e.getCommonOxidationNumbers()) {
				if (flag) {
					out.write("," + f + "f");
				} else {
					out.write(String.valueOf(f) + "f");
				}
				flag = true;
			}
			out.write("},new float[]{");
			flag = false;
			for (float f : e.getAllOxidationNumbers()) {
				if (flag) {
					out.write("," + f + "f");
				} else {
					out.write(String.valueOf(f) + "f");
				}
				flag = true;
			}
			out.write("}),");
			out.newLine();
		}
		out.close();
	}
}
