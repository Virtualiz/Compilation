package plic;

import java.util.ArrayList;
import java.util.List;

public class Affichage extends Instruction{
	/**
	 * Liste des noms des variables � afficher
	 */
	List<Idf> vars;
	
	/**
	 * garde en m�moire le nombre d'�criture de booleen, car il faut avoir le nombre d'�tiquette
	 */
	private static int ecrireBool=0;

	/**
	 * construction de la liste des variables � partir d'un tableau
	 * @param variables tableau du nom des variables � afficher
	 * @throws NonDeclareeException cas o� la variable n'existe pas
	 */
	public Affichage(String[] variables) throws NonDeclareeException{
		vars = new ArrayList<Idf>();
		for(int i = 0; i<variables.length;i++){
			vars.add(new Idf(variables[i]));
		}
	}

	/**
	 * construction de la liste des variables � partir d'une liste
	 * @param variables liste du nom des variables � afficher
	 */
	public Affichage(List<String> variables){
		vars = new ArrayList<Idf>();
		for(int i = 0; i<variables.size();i++){
			vars.add(new Idf(variables.get(i)));
		}
	}

	/**
	 * cr�er une liste de variable vide
	 */
	public Affichage(){
		vars = new ArrayList<Idf>();
	}

	/**
	 * ajoute une variable � afficher � la liste des variables
	 * @param idf identifiant
	 */
	public void ajouterVariables(Idf idf){
		vars.add(idf);
	}
	
	/**
	 * V�rifie que les variables � afficher existe bien
	 */
	public void verifier() throws NonDeclareeException{
		Tds tds = Tds.getInstance();
		for(Idf idf : vars){
			//if(!tds.identifier(idf.getNom()).getType().equals("entier")) throw new NonDeclareeException("la variable "+idf.getNom()+" n'existe pas");
			if(!tds.identifier(idf.getNom()).getType().equals("entier") && !tds.identifier(idf.getNom()).getType().equals("booleen")) throw new NonDeclareeException("la variable "+idf.getNom()+" n'existe pas");
		}
	}

	@Override
	public String toString() {
		return "Affichage [vars=" + vars + "]";
	}
	
	public String toMips() throws NonDeclareeException{
		String res = "# ecrire ";
		String operation = "";
		int dist, numVar = 1;
		Tds tds = Tds.getInstance();
		for(Idf idf : vars){
			res+= idf.getNom()+" ";
			
			dist = tds.identifier(idf.getNom()).getDistance();
			if(dist>0) dist *=-1;
			operation += "# ecriture de "+idf.getNom()+"\n";
			
			if(tds.identifier(idf.getNom()).getType().equals("entier")){
			// cas entier
				operation +="li $v0 , 1 #code de print entier\n";
				operation += "lw $a0 , "+dist+"($s7) #recuperation de la valeur de "+idf.getNom()+"\nsyscall #affichage\n\n";
			}else{
			// cas booleen
			 operation +="li $v0 , 4 #code de print caract�re\n";// deux cas : vrai et faux ==> 0 et 1
			 operation +="lw $a0,"+dist+"($s7) #r�cup�ration de la valeur du booleen\n";
			 operation +="beq $a0,0,vrai"+ecrireBool+" #si vrai alors aller � �tiquette vrai\n";
			 operation +="la $a0,strfaux #on charge faux\n";
			 operation +="j fin"+ecrireBool+" #jump � l'�tiquette d'�criture\n";
			 operation +="vrai"+ecrireBool+": la $a0,strvrai #on charge vrai\n";
			 operation +="fin"+ecrireBool+": syscall #affichage\n\n";
			 ecrireBool ++;
			}
			if(numVar != vars.size()){
				operation+="# ecrire un espace\n";
				operation+="li $v0,4 #code de print caractere\n";
				operation+="la $a0, str #affecte en valeur a ecrire la chaine correspondant a espace\n";
				operation+="syscall #afficher\n\n";
			}
			numVar ++;			
		}
		res += "\n";
		res += operation;
		res += "#fin ecrire : saut de ligne\nli $v0,4 #code de print caractere\nla $a0, str2 #affecte en valeur a ecrire la chaine correspondant a retour a la ligne\n";
		res += "syscall #afficher\n\n";
		
		return res;
		
	}
}
