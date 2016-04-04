package plic;

import java.util.HashMap;

public class Tds {
	
	/**
	 * Distance de la variable la plus haute +4
	 */
	private int dist;
	
	/**
	 * Table des symboles
	 */
	private HashMap<String,Symbole> map;
	
	/**
	 * instance du singleton
	 */
	private static Tds tds;
	
	/**
	 * constructeur de la table des symboles
	 */
	private Tds(){
		map = new HashMap<String,Symbole>();
		dist = 0;
	}
	
	/**
	 * retourne l'instance de la table des symbole, ou la cr�er si elle n'existe pas
	 * @return
	 */
	public static Tds getInstance(){
		if( tds == null){
			tds = new Tds();
		}
		
		return tds;
	}
	
	/**
	 * ajoute un symbole
	 * @param var nom de la variable
	 * @param type type de la variable
	 * @throws DoubleDeclarationException s'il y a d�j� le symbole
	 * @throws TypeException 
	 */
	public void ajouter(String var, String type) throws DoubleDeclarationException, TypeException{
		if(map.containsKey(var)) throw new DoubleDeclarationException("la variable "+var+" a d�j� �t� d�clar�e");
		if(!type.equals("entier")&&!type.equals("booleen")) throw new TypeException("le type "+type+" n'existe pas");
		// par la suite le type pourra �tre booleen en plus d'entier
		Symbole infos = new Symbole(type,dist);
		dist += 4;
		map.put(var, infos);
	}
	
	/**
	 * V�rifie si une variable a d�j� �t� d�clar�e
	 * @param nom nom de la variable
	 * @return vrai si la variable existe d�j�
	 * @throws NonDeclareeException 
	 */
	public Symbole identifier(String nom) throws NonDeclareeException {
		if(!map.containsKey(nom)) throw new NonDeclareeException("la variable "+nom+" n'a pas �t� d�clar�e");
		return  map.get(nom);
	}
	
	/**
	 * Nombre d'entr�es
	 * @return nombre d'entr�es = nombre de variables
	 */
	public int nbSymboles(){
		return map.size();
	}
	
}
