package plic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnalyseurLexical {
	/**
	 * attribut correspondant au Scanner
	 */
	private Scanner sc;

	private boolean lecture;

	/**
	 * Constructeur
	 * @param f nom du fichier � lire
	 */
	public AnalyseurLexical(String fichier){
		try {
			this.sc = new Scanner(new File(fichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.lecture = true;
	}

	/**
	 * 
	 * @return la prochaine cha�ne de caract�re s'il y en a une, hors commentaire.
	 */
	public String next(){
		String res = "EOF";
		if(sc.hasNext()){ // si pas fin de fichier
			res = sc.next(); // unit� lexicale dans res
			if(res.equals("/*")) lecture=false;
			if(!lecture){ // si l'unit� contient un d�but de commentaire

				while( !sc.hasNext() || !lecture){ // tant qu'on ne lit pas (commentaire)

					res = sc.next(); // on r�cup�re l'unit� lexical suivante
					if(res.equals("*/")) lecture=true; //si fin de commentaire, on repasse en mode lecture
				}
				if(sc.hasNext()){
					res = sc.next();// on r�cup�re l'unit� lexical suivante
					if(res.equals("/*")){ //si d�but de commentaire
						lecture=false;  //on sort de mode lecture
						res = this.next(); // on cherche la prochaine unit� de fa�on r�curssive.
					}
				}else{ 
					res = "EOF"; // si fin fichier : EOF
				}

			}
		}
	return res;
}

/**
 * main permettant de lire un fichier
 * @param args arguments : nom du fichier ou vide
 */
public static void main(String[] args) {
	String fichier = "D:/Cours/Compilation/PLIC/PLIC_0/erreur_lexicale_01.plic";
	if(args.length==1) fichier = args[0];
	AnalyseurLexical a = new AnalyseurLexical(fichier);
	String s = "";
	while(s!="EOF"){
		s=a.next();
		System.out.println(s);
	}
}

}
