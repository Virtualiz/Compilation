package plic;

public class Nombre extends Operande{
	
	/**
	 * valeur du nombre
	 */
	private int val;
	
	/**
	 * Constructeur
	 * @param n valeur � donner au Nombre
	 */
	public Nombre(int n){
		val = n;
	}
	
	/**
	 * retourne la valeur du Nombre
	 * @return val
	 */
	public int getVal(){
		return val;
	}

	@Override
	public String toString() {
		return "Nombre [val=" + val + "]";
	}

	@Override
	public String toMips() throws NonDeclareeException {
		String res = "li $v0,"+val+" #Charge la valeur enti�re"+val+"\n";
		return res;
	}

	@Override
	public String verifier() {
		return "entier";
	}
	
	
}
