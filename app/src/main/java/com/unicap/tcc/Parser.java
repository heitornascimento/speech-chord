package com.unicap.tcc;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

	private ArrayList<String> matches;
	private int matchesLength;
	private String lexema;
	private String[] lexemasArray;
	private HashMap<String, String> mapKeys;
	private HashMap<String, String> mapQualities;
	private Chords chords;
	private String resultChord;

	private int index = 0;

	private boolean isChord;

	public Parser(ArrayList<String> matches, Chords chords) {

		this.matches = matches;
		this.chords = chords;

		init();
	}

	public String startAnalysis() throws ChordNotFoundException {

		if (matches != null && matches.size() > 0) {

			matchesLength = matches.size();

			for (int i = 0; i < matchesLength; i++) {

				lexema = matches.get(i);
				lexemasArray = lexema.trim().split(" "); // string
				int lexemasArrayLength = lexemasArray.length;

				if (isKey()) {

					index++;

					if (index < lexemasArrayLength) {

						if (isQuality()) {

							index++;

							if (index == lexemasArrayLength) {
								isChord = true; 
								break; //chord found

							} else {
								isChord = false;
							}
						}

					} else {
						break; //chord not found

					}
				}
			}
		}

		if (isChord) {
			return resultChord;
		} else {
			throw new ChordNotFoundException();
		}

	}

	/*
	 * Se � uma nota natural: A-B-C-D-E-F-G. A primeira ocorrencia sempre � uma
	 * nota
	 */
	private boolean isKey() {

		isChord = false;

		if (mapKeys.containsKey(lexemasArray[index])) {
			resultChord = mapKeys.get(lexemasArray[index]);
			resultChord += " ";
			isChord = true;
		}

		return isChord;
	}

	/*
	 * Se � uma modalidade, nesse caso, Maior ou Menor
	 */
	private boolean isQuality() {

		isChord = false;

		if (mapQualities.containsKey(lexemasArray[index])) {
			resultChord += mapQualities.get(lexemasArray[index]);
			isChord = true;
		}

		return isChord;
	}

	private void init() {

		/* Inicializar os dicionarios */
		mapKeys = chords.getkeys();
		mapQualities = chords.getQualities();

		isChord = false;

	}

}
