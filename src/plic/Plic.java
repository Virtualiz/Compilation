package plic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Plic {

	private AnalyseurLexical al;
	private AnalyseurSyntaxique as;

	public static void main(String[] args) {
		String fichier = "D:/Cours/Compilation/PLIC/PLIC_2/correct_02.plic"; // à changer si besoin
		if(args.length==1) fichier = args[0];
		Plic p = new Plic(fichier);
		Bloc b;
		try {

			b=p.as.analyse();


			//System.out.println("Fin de l'analyse syntaxique !");

			b.verifier();
			/*
			System.out.println("\nDans le bloc :\n");
			// // // // // \\ \\ \\ \\ \\ 
			for(Instruction i: b.getListeInstructions()){
				System.out.println(i);
			}
			*/
			//System.out.println("\nGénération du code Mips :\n");
			String name = fichier.split("\\.")[0];
			createMips(name+".mips",b.toMips());

		} catch (SyntaxiqueException e) {
			System.out.println("Erreur : "+e.getMessage()+".");
		} catch (DoubleDeclarationException e) {
			System.out.println("Erreur : "+e.getMessage()+".");
		} catch (TypeException e) {
			System.out.println("Erreur : "+e.getMessage()+".");
		} catch (NonDeclareeException e) {
			System.out.println("Erreur : "+e.getMessage()+".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Plic(String nf){
		// créer un analyseur lexical
		this.al = new AnalyseurLexical(nf);

		// créer un analyseur syntaxique
		this.as = new AnalyseurSyntaxique(al);
	}
	
	private static void createMips(String file,String content) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
		bw.write(content);
		bw.close();
	}

}
