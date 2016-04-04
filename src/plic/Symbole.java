package plic;

import java.util.Comparator;

/**
 * Classe impl�mentant la partie symbole de la table des symbole (type, et distance de la variable pour le Mips
 *
 */
public class Symbole{
	
	/**
	 * type de la variable
	 */
	private String type;
	
	/**
	 * distance depuis le sommet de la m�moire
	 */
	private int distance;
	
	/**
	 * Constructeur
	 * @param t type
	 * @param d distance depuis le sommet
	 */
	public Symbole(String t, int d){
		type = t;
		distance = d;
	}
	
	/**
	 * getter de type
	 * @return le type du symbole
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * getter de distance
	 * @return distance de la variable depuis le sommet de la m�moire
	 */
	public int getDistance() {
		return distance;
	}

	
}
