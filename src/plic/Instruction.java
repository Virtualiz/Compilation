package plic;

public abstract class Instruction {
	
	/**
	 * Vérifie que l'instruction est correcte - Analyse Sémantique
	 * @throws NonDeclareeException
	 * @throws TypeException 
	 */
	public abstract void verifier() throws NonDeclareeException, TypeException;
	
	/**
	 * Retourne le code Mips correspondant à l'instruction
	 * @return code Mips
	 * @throws NonDeclareeException 
	 */
	public abstract String toMips() throws NonDeclareeException;
	
}
