
package com.aed.demo.cluster;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The Class Cluster.
 */
public class ClusterDimoi {

	/** The map. */
	HashMap<String, ArrayList<Boundary>> map;

	HashMap<String, String> translate;

	/** The instance. */
	private static ClusterDimoi instance = new ClusterDimoi();

	/**
	 * Instantiates a new cluster.
	 */
	private ClusterDimoi() {
		System.out.println("Cluster Dimoi Created");
		//translate = new HashMap();
//		translate.put("Π. ΑΝΑΤΟΛΙΚΗΣ ΜΑΚΕΔΟΝΙΑΣ - ΘΡΑΚΗΣ", "P-Anatolikis-Makedonias-Thrakis");
//		translate.put("Π. ΚΕΝΤΡΙΚΗΣ ΜΑΚΕΔΟΝΙΑΣ", "P-Kentrikis-Makedonias");
//		translate.put("Π. ΔΥΤΙΚΗΣ ΜΑΚΕΔΟΝΙΑΣ", "P-Dytikis-Makedonias");
//		translate.put("Π. ΗΠΕΙΡΟΥ", "P-Hpeirou");
//		translate.put("Π. ΘΕΣΣΑΛΙΑΣ", "P-Thessalias");
//		translate.put("Π. ΒΟΡΕΙΟΥ ΑΙΓΑΙΟΥ", "P-Boreiou-Aigaiou");
//		translate.put("Π. ΝΟΤΙΟΥ ΑΙΓΑΙΟΥ", "P-Notiou-Aigaiou");
//		translate.put("Π. ΣΤΕΡΕΑΣ ΕΛΛΑΔΑΣ", "P-Stereas-Elladas");
//		translate.put("Π. ΔΥΤΙΚΗΣ ΕΛΛΑΔΑΣ", "P-Dytikis-Elladas");
//		translate.put("Π. ΠΕΛΟΠΟΝΝΗΣΟΥ", "P-Peloponnisou");
//		translate.put("Π. ΙΟΝΙΩΝ ΝΗΣΩΝ", "P-Ionion-Nison");
//		translate.put("Π. ΚΡΗΤΗΣ", "P-Kritis");
//		translate.put("Π. ΑΤΤΙΚΗΣ", "P-Attikis");
	}

	/**
	 * Gets the single instance of Cluster.
	 *
	 * @return single instance of Cluster
	 */
	public static ClusterDimoi getInstance() {
		return instance;
	}

	/**
	 * First step is to load the Cluster.
	 *
	 * @return true, if successful
	 */
	public boolean loadCluster() {
		String path = "dimoi-LatLong.json";
		JSONObject allData;
		try {
			InputStream inputStream = null;
			Scanner sc = null;
			StringBuilder data = new StringBuilder();
			try {
				inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
				sc = new Scanner(inputStream, "UTF-8");
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					data.append(line);
					// System.out.println(line);
				}
				// note that Scanner suppresses exceptions
				if (sc.ioException() != null) {
					throw sc.ioException();
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (sc != null) {
					sc.close();
				}
				System.out.println("Cluster Dimoi ready to be used");
				allData = new JSONObject(data.toString());

			}
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}

		// JSONObject jsonData = new JSONObject(data);

		JSONArray features = (JSONArray) allData.get("features");

		map = new HashMap<String,ArrayList<Boundary>>();

		for (Object periferies : features) {
			JSONObject periferia = (JSONObject) (periferies);

			JSONObject properties = periferia.getJSONObject("properties");

			String onomasiaPeriferias = (String) properties.get("namegr");

			JSONObject geometry = periferia.getJSONObject("geometry");

			JSONArray coordinates = geometry.getJSONArray("coordinates");
			ArrayList<Boundary> b = new ArrayList<Boundary>();

			for (Object insideCoord : coordinates) {

				JSONArray incoord = (JSONArray) insideCoord;
				
			//	for (int i = 0; i > incoord.length(); i++) {
		
					JSONArray zeroCoord = (JSONArray) incoord.get(0);

					Boundary boundary = new Boundary();
					boundary.initPointSize(zeroCoord.length());
					
					for (Object boun : zeroCoord) {
						JSONArray bound =null;
						 bound = (JSONArray) boun;
						 
						 double x = (Double) bound.get(1);
						 double y = (Double) bound.get(0);
						 Point p = new Point(x, y);
						 boundary.loadPoint(p);
						 
						}
						
					b.add(boundary);
				//}
			}
			map.put(onomasiaPeriferias, b);
		}
		
		
		//System.out.println(map);
		return true;
	}

	/**
	 * Returns the String where the Point p was found.
	 *
	 * @param p    the p
	 * @param lang , if the values is en, the returned string will be in English
	 *             language
	 * @return String
	 */
	public String getClusterFeature(Point p, String lang) {
		System.out.println("\n\n\n\nOutSide LOOP\n\n\n\n");

		Iterator<Entry<String,ArrayList<Boundary>>> it = map.entrySet().iterator();

		System.out.println("\n\n\n\nOutSide LOOP\n\n\n\n");

		while (it.hasNext()) {
			Map.Entry<String,ArrayList<Boundary>> pair = (Map.Entry<String,ArrayList<Boundary>>) it.next();
			System.out.println("\n\n\n\nIN LOOP\n\n\n\n");

			ArrayList<Boundary> temp = (ArrayList<Boundary>) pair.getValue();

			for (Boundary bound : temp) {
				if (bound.contains(p)) {

					if (lang.equalsIgnoreCase("en")) {
						return (String) pair.getKey();
					} else {
						return (String) pair.getKey();
					}
				}

			}

		}
		return null;
	}

}
