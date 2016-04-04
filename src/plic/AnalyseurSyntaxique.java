package plic;

import java.util.ArrayList;
import java.util.List;

public class AnalyseurSyntaxique {
	
	/**
	 * Sert de parseur et de traitement pour les erreurs lexicales
	 */
	private AnalyseurLexical al;
	
	/**
	 * unite courante
	 */
	private String uc;
	
	/**
	 * Construit l'analyseur lexical.
	 * @param nf name of file
	 */
	public AnalyseurSyntaxique(String nf){
		al = new AnalyseurLexical(nf);
	}
	
	/**
	 * On donne l'analyseur lexical.
	 * @param a analyseur lexical
	 */
	public AnalyseurSyntaxique(AnalyseurLexical a){
		al = a;
	}
	
	/**
	 * analyse le fichier sur les erreurs syntaxiques
	 * @throws SyntaxiqueException
	 * @throws TypeException 
	 * @throws DoubleDeclarationException 
	 * @throws NonDeclareeException 
	 */
	public Bloc analyse() throws SyntaxiqueException, DoubleDeclarationException, TypeException, NonDeclareeException {
		//verifier que l'unite courante est bien 'programme' + lire prochain uc
		uc = al.next();
		if( ! uc.equals("programme") ) throw new SyntaxiqueException("mot clé 'programme' absent"); 		
		uc = al.next();
		//verifier que l'unite courante est bien idf + lire prochain
		analyseIdf();
		//verifier que reste du texte conforme a la regle bloc
		Bloc b = analyseBloc();
		if(!uc.equals("EOF")) throw new SyntaxiqueException("fin du programme attendue");
		
		return b;
	}
	
	/**
	 * analyse syntaxique d'un bloc ( de { à } )
	 * @throws SyntaxiqueException 
	 * @throws TypeException 
	 * @throws DoubleDeclarationException 
	 * @throws NonDeclareeException 
	 */
	private Bloc analyseBloc() throws SyntaxiqueException, DoubleDeclarationException, TypeException, NonDeclareeException{
		
		if(! uc.equals("{")) throw new SyntaxiqueException("un bloc est attendu ('{ ... }')");
		//un nouveau bloc débute correctement
		Bloc b = new Bloc();
		
		uc = al.next();
		boolean blocNonVide = false;
		while( !uc.equals("}") && !uc.equals("EOF")){
			analyseInterieurBloc(b);
			blocNonVide = true;
		}
		if(!blocNonVide) throw new SyntaxiqueException("un bloc doit contenir au moins une instruction");
		if(uc.equals("EOF")) throw new SyntaxiqueException("une fin de bloc est attendue '}'");
		uc = al.next();
		return b;
	}
	
	/**
	 * 
	 * @param b Bloc auquel on ajoute les instructions
	 * @throws SyntaxiqueException
	 * @throws DoubleDeclarationException
	 * @throws TypeException
	 * @throws NonDeclareeException
	 */
	private void analyseInterieurBloc(Bloc b) throws SyntaxiqueException, DoubleDeclarationException, TypeException, NonDeclareeException{
		//int nbDeclaration = 0;
		//début du contenu par une ou plusieurs declaration
		while((uc.equals("entier") || uc.equals("booleen")) && !uc.equals("EOF")){
			analyseDeclaration();
			//nbDeclaration ++;
		}
		//if(nbDeclaration==0) throw new SyntaxiqueException("une ou plusieurs déclaration sont attendues");
		if(uc.equals("EOF")) throw new SyntaxiqueException("le fichier ne se termine pas correctement");
		
		analyseInstructions(b);
	}
	
	/**
	 * analyse syntaxique d'un identifiant
	 * @throws SyntaxiqueException 
	 */
	private Idf analyseIdf() throws SyntaxiqueException{
		char c1 = uc.charAt(0);
		if((c1>='0' && c1<='9')) throw new SyntaxiqueException(uc+" n'est pas un idf correct, il ne peut pas commencer par "+c1);
		for(int i=0; i < uc.length(); i++){
			char c = uc.charAt(i);
			if( !( (c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') ) ) throw new SyntaxiqueException(uc+" n'est pas un idf correct");
		}
		if(uc.equals("vrai")||uc.equals("faux")) throw new SyntaxiqueException(uc+" est un mot clé et ne peut être un Idf");
		Idf idf = new Idf(uc);
		uc = al.next();
		return idf;
	}
	
	/**
	 * analyse une suite d'instructions (affichage ou affectation)
	 * @throws SyntaxiqueException 
	 * @throws NonDeclareeException 
	 */
	private void analyseInstructions(Bloc b) throws SyntaxiqueException, NonDeclareeException{
		int nbInstruction = 0;
		while(!uc.equals("}") && !uc.equals("EOF")){
			switch(uc){
			case "ecrire" : b.ajouterInstruction(analyseAffiche());
							break;
			case "entier" : throw new SyntaxiqueException("les déclarations sont à faire au début du programme");
			case "booleen" : throw new SyntaxiqueException("les déclarations sont à faire au début du programme");
			default : b.ajouterInstruction(analyseAffectation());
					  break;
			}
			nbInstruction ++;
		}
		if(uc.equals("EOF")) throw new SyntaxiqueException("le fichier ne se termine pas correctement");
		if(nbInstruction==0) throw new SyntaxiqueException("une instruction ou plus sont attendues");
	}
	
	/**
	 * analyse syntaxique de l'instruction Affiche
	 * @throws SyntaxiqueException
	 * @throws NonDeclareeException 
	 */
	private Affichage analyseAffiche() throws SyntaxiqueException, NonDeclareeException{
		if(!uc.equals("ecrire")) throw new SyntaxiqueException("un affichage doit commencer par 'ecrire'");
		Affichage af = new Affichage();
		uc = al.next();
		int nbVar = 0;
		while(!uc.equals(";") && !uc.equals("EOF")){
			af.ajouterVariables(analyseIdf());
			nbVar ++;
		}
		if(nbVar ==0) throw new SyntaxiqueException("l'affichage se fait sur une ou plusieurs variables, ici il n'y en a aucune");
		if(uc.equals("EOF")) throw new SyntaxiqueException("le fichier ne se termine pas correctement");
		// On récupère le dernier bloc (par la suite on instaurera un système de bloc courant)
		uc = al.next();
		return af;
	}
	
	/**
	 * analyse syntaxique d'une declaration
	 * @throws SyntaxiqueException
	 * @throws TypeException 
	 * @throws DoubleDeclarationException 
	 */
	private void analyseDeclaration() throws SyntaxiqueException, DoubleDeclarationException, TypeException{
		if(!uc.equals("entier") && !uc.equals("booleen")) throw new SyntaxiqueException(" type 'entier' ou 'booleen' attendu, "+uc+" trouvé");
		//un type est attendu
		String type = uc;
		String var;
		Tds tds = Tds.getInstance();
		uc = al.next();
		int i = 0;
		while(!uc.equals(";") && !uc.equals("EOF")){
			//les idfs sont déclarés
			var = uc;
			tds.ajouter(var, type);
			analyseIdf();
			i++;
		}
		if(i==0) throw new SyntaxiqueException("un Idf est attendu dans la déclaration");
		if(uc.equals("EOF")) throw new SyntaxiqueException("le fichier ne se termine pas correctement");
		uc = al.next();
	}
	
	/**
	 * analyse syntaxique de l'instruction affectation
	 * @throws SyntaxiqueException
	 * @throws NonDeclareeException 
	 */
	private Affectation analyseAffectation() throws SyntaxiqueException, NonDeclareeException{
		Affectation af;
		String var = uc;
		Idf idf = analyseIdf();
		if(!uc.equals(":=")) throw new SyntaxiqueException("le signe d'affectation n'est pas ':=' , "+uc+" trouvé");
		else uc = al.next();
		
		Tds tds = Tds.getInstance();
		if(idf.verifier().equals("entier")){
			//cas entier
			if(uc.contains(".")||uc.contains(",")) throw new SyntaxiqueException("seuls les entiers sont pris en compte, pas les nombres réels");
			//test
			
			try{
				Idf val;
				val=analyseIdf();
				tds.identifier(val.getNom()).equals("entier");//vérifier que la valeur est bien un idf d'une variable entière
				af = new Affectation(idf,val);
			}catch(SyntaxiqueException e){// si exception alors ce n'est pas un idf alors on traite comme constante entière
				Nombre nb = analyseNombre();
				af = new Affectation(idf,nb);
			}
			/*
			Nombre nb = analyseNombre();
			af = new Affectation(idf,nb);
			*/
		}else{
			//cas booleen
			try{
				Idf val;
				val=analyseIdf();
				tds.identifier(val.getNom()).equals("booleen");//vérifier que la valeur est bien un idf d'une variable booléenne
				af = new Affectation(idf,val);
			}catch(SyntaxiqueException e){// si exception alors ce n'est pas un idf alors on traite comme constante booléenne
				Booleen b = analyseBooleen();
				af = new Affectation(idf,b);
			}
		}
		
		if(!uc.equals(";")) throw new SyntaxiqueException("un ';' est attendu");
		else uc = al.next();
		
		return af;
	}
	
	/**
	 * analyse la valeur de l'affectation à une variable booleenne
	 * @return Booleen correspondant au booleen
	 * @throws SyntaxiqueException
	 */
	private Booleen analyseBooleen() throws SyntaxiqueException{
		boolean b;
		if(!uc.equals("vrai")&&!uc.equals("faux")) throw new SyntaxiqueException("la valeur n'est pas booléenne, "+uc+" trouvé");
		b = uc.equals("vrai");
		uc = al.next();
		return new Booleen(b);
	}

	/**
	 * analyse la valeur de l'affectation à une variable entière
	 * @return Nombre correspondant à l'entier
	 * @throws SyntaxiqueException
	 */
	private Nombre analyseNombre() throws SyntaxiqueException{
		int i;
		try{
			i=Integer.parseInt(uc);
		}catch(NumberFormatException e){
			throw new SyntaxiqueException("une constante entière est attendue, "+uc+" trouvé");
		}
		if(!uc.equals(""+i)) throw new SyntaxiqueException("une constante entière est attendue, "+uc+" trouvé");
		else uc = al.next();
		
		return new Nombre(i);
	}

	public static void main(String[] args) {
		String fichier = "D:/Cours/Compilation/PLIC/PLIC_0/correct_02.plic";
		if(args.length==1) fichier = args[0];
		AnalyseurLexical a = new AnalyseurLexical(fichier);
		AnalyseurSyntaxique as = new AnalyseurSyntaxique(a);
		try {
			as.analyse();
		} catch (SyntaxiqueException e) {
			System.out.println("Erreur : "+e.getMessage()+".");
		} catch ( Exception e ){
			System.out.println("Erreur : "+e.getMessage()+".");
		}
		System.out.println("Fin de l'analyse syntaxique !");
	}

}
