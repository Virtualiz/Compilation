package plic;

import java.util.ArrayList;
import java.util.List;

public class Bloc {
	
	/**
	 * Liste des instructions
	 */
	private List<Instruction> listeInstructions;
	
	/**
	 * Constructeur d'un bloc, initialise la liste des instructions
	 */
	public Bloc(){
		this.listeInstructions = new ArrayList<Instruction>();
	}
	
	/**
	 * Ajoute une instruction au bloc
	 * @param ins instruction à ajouter
	 */
	public void ajouterInstruction(Instruction ins){
		listeInstructions.add(ins);
	}
	
	/**
	 * Méthode qui vérifie toutes les instructions - Analyse Sémantique
	 * @throws NonDeclareeException
	 * @throws TypeException 
	 */
	public void verifier() throws NonDeclareeException, TypeException{
		for(Instruction ins : listeInstructions){
			ins.verifier();
		}
	}
	
	// // // // // \\ \\ \\ \\ \\
	public List<Instruction> getListeInstructions() {
		return listeInstructions;
	}
	
	/**
	 * Générateur de code Mips
	 * @return
	 * @throws NonDeclareeException 
	 */
	public String toMips() throws NonDeclareeException{
		//Ecriture du début du code Mips
		String res= ".data\nstr: .asciiz \" \"\nstr2: .asciiz \"\\n\"\nstrvrai: .asciiz \"vrai\"\nstrfaux: .asciiz \"faux\"\n.text\nmain:\n\n";
		res +="# Initialisation de $s7 avec $sp (base des variables)\n";
		res +="move $s7, $sp\n\n";
		int nbVar = Tds.getInstance().nbSymboles();
		res +="# Réservation de la place pour les "+nbVar+" variables\n";
		res +="addi $sp, $sp, -"+(nbVar*4)+"\n\n";
		
		for(Instruction ins : listeInstructions){
			res += ins.toMips();
		}
		
		res+="end : \n#Fin du programme\nli $v0,10 #code retour systeme\nsyscall #retour systeme";
		return res;
	}
}
