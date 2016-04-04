package plic;

public abstract class Operande {
	public abstract String toMips() throws NonDeclareeException;
	public abstract String verifier() throws NonDeclareeException;
}
