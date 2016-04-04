package plic;

public class Booleen extends Operande{
	
	/**
	 * valeur du booleen
	 */
	private boolean val;
	
	/**
	 * constructeur
	 * @param b valeur booléenne
	 */
	public Booleen(boolean b){
		val = b;
	}
	
	/**
	 * getter de la valeur
	 * @return valeur du booleen
	 */
	public boolean getVal(){
		return val;
	}

	/**
	 * toString, description de l'objet
	 */
	@Override
	public String toString() {
		return "Booleen [val=" + val + "]";
	}

	@Override
	public String toMips() throws NonDeclareeException {
		String res = "li $v0,";
		res += val? "0 #Charge 0 car vrai==0\n":"1 #Charge 1 car faux==1\n" ;
		return res;
	}

	@Override
	public String verifier() {
		return "booleen";
	}
	
	
}
