package plic;

public abstract class Instruction {
	
	/**
	 * V�rifie que l'instruction est correcte - Analyse S�mantique
	 * @throws NonDeclareeException
	 * @throws TypeException 
	 */
	public abstract void verifier() throws NonDeclareeException, TypeException;
	
	/**
	 * Retourne le code Mips correspondant � l'instruction
	 * @return code Mips
	 * @throws NonDeclareeException 
	 */
	public abstract String toMips() throws NonDeclareeException;
	
}
