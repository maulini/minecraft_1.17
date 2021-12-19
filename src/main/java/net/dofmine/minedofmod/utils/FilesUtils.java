package net.dofmine.minedofmod.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FilesUtils {

	private FilesUtils() {
	}
	
	/**
	 * Méthode renvoyant une List de String avec un File passé en paramètre.
	 * @param file : Fichier source.
	 * @return : Une list de String.
	 */
	public static List<String> lireFichier(File file)  {
		List<String> retour = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file));){
			String ligne;
			
			while((ligne = br.readLine()) != null) {
				retour.add(ligne);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return retour;
	}
	
	/**
	 * Méthode renvoyant une List de String avec le chemin passé en paramètre.
	 * @param chemin : chemin vers la destination du fichier source.
	 * @return : Une list de String.
	 */
	public static List<String> lireFichier(String chemin) {
		List<String> retour = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(chemin)))){
			String ligne;
			
			while((ligne = br.readLine()) != null) {
				retour.add(ligne);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return retour;
	}
	

	/**
	 * Méthode renvoyant une List de String avec un fichier dans le dossier ressource.
	 * @param chemin : chemin du fichier dans le dossier ressource.
	 * @return : Une List de String.
	 */
	public static List<String> lireFichierDansRessource(String chemin) {
		List<String> retour = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(FilesUtils.class.getResource(chemin).getPath()))){
			String ligne;
			while((ligne = br.readLine()) != null) {
				retour.add(ligne);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return retour;
	}
	
	/**
	 * Méthode qui copie le fichier source dans un autre fichier.
	 * Et supprime le fichier source ou pas en fonction du parametre passer.
	 * @param source : Chemin du fichier source
	 * @param destination : Chemin ou va atterir l'autre fichier
	 * @param suppression : Boolean pour supprimier ou pas le fichier source.
	 */
	public static void copierUnFichier(String source, String destination, boolean suppression) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(source)))){
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(destination)));
			String ligne;
			while((ligne = br.readLine()) != null) {
				bw.write(String.format("%s%n", ligne));
			}
			bw.close();
			br.close();
			if(suppression) {
				Path p = Paths.get(source);
				Files.delete(p);
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Méthode permettant d'écrire toute une liste de String dans un fichier.
	 * @param ligneAEcrire : List<String>.
	 * @param destination : Destination du fichier.
	 */
	public static void ecrireUnfichier(List<String> ligneAEcrire, String destination) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(destination)));){
			for (String string : ligneAEcrire) {
				bw.write(String.format("%s%n", string));
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
