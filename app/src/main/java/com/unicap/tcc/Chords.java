package com.unicap.tcc;

import java.util.HashMap;

public class Chords {

	private HashMap<String, String> keys;
	private HashMap<String, String> qualities;

	public Chords() {
		loadDicionary();
	}

	private void loadDicionary() {
		keys = new HashMap<String, String>();
		qualities = new HashMap<String, String>();

		//keys
		keys.put("Lá", "A");
		keys.put("lá", "A");
		keys.put("Si", "B");
		keys.put("si", "B");
		keys.put("Dó", "C");
		keys.put("dó", "C");
		keys.put("do", "C");
		keys.put("dol", "C"); //
		keys.put("Ré", "D");
		keys.put("ré", "D");
		keys.put("red", "D"); //
		keys.put("rer", "D"); //
		keys.put("mi", "E");
		keys.put("Mi", "E");
		keys.put("fá", "F");
		keys.put("Fá", "F");
		keys.put("fa", "F");
		keys.put("Fa", "F");
		keys.put("Sol", "G");
		keys.put("sol", "G");
		
		//modal
		qualities.put("maior", "major");
		qualities.put("Maior", "major");
		qualities.put("Menor", "minor");
		qualities.put("menor", "minor");
		
		
		
	}

	public HashMap<String, String> getkeys() {
		return keys;
	}

	public HashMap<String, String> getQualities() {
		return qualities;
	}
}
