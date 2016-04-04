package plic;

public class Idf extends Operande{
	
	/**
	 * Nom de la variable - clé dans Tds
	 */
	private String nom;
	
	/**
	 * Constructeur
	 * @param n nom de la variable
	 */
	public Idf(String n){
		nom = n;
	}
	
	/**
	 * Récupère le nom de l'Idf
	 * @return nom
	 */
	public String getNom(){
		return nom;
	}

	@Override
	public String toString() {
		return "Idf [nom=" + nom + "]";
	}

	@Override
	public String toMips() throws NonDeclareeException {
		Tds tds = Tds.getInstance();
		int dist = tds.identifier(nom).getDistance();
		String res = "lw $v0,"+dist+"($s7) #Chargement de "+nom+"\n";
		return res;
	}

	@Override
	public String verifier() throws NonDeclareeException {
		Tds tds = Tds.getInstance();
		return tds.identifier(nom).getType();
	}
	
	
}
