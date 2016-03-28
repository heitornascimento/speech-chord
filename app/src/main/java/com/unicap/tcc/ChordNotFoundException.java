package com.unicap.tcc;


public class ChordNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String msgError = "Acorde nao encontrado!!";

	public ChordNotFoundException() {
		super(msgError);
	}

}
