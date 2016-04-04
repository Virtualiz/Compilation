package plic;

public class Affectation extends Instruction{

	/**
	 * Nom de la variable
	 */
	public Idf var;

	public Operande operande;

	public Affectation(Idf idf, Operande op){
		operande = op;
		var = idf;
	}

	/**
	 * Vérifie que la variable existe et qu'elle est bien entière ou booleenne en fonction du type
	 * @throws TypeException 
	 */
	public void verifier() throws NonDeclareeException, TypeException{
		if(!var.verifier().equals(operande.verifier())) throw new TypeException("Le type de l'opérande n'est pas correcte: "+var.verifier()+" := "+operande.verifier());
	}

	@Override
	public String toString() {
		String s = "Affectation [var=" + var + ", operande ="+operande+"]";
		return s;
	}

	@Override
	public String toMips() throws NonDeclareeException {
		String res = "";
		int dist;
		Tds tds = Tds.getInstance();
		dist = tds.identifier(var.getNom()).getDistance();
		if(dist>0) dist *= -1;

		res += "# affectation : "+var.getNom()+" := "+operande+"\n";
		res+=operande.toMips();
		res+="sw $v0,"+dist+"($s7) #on affecte la valeur à la variable\n\n";

		return res;
	}



}
