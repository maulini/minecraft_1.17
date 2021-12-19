package net.dofmine.minedofmod.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pour utiliser la classe votre Class type doit implémenter Serialisable.
 * @author valentin.maulini
 *
 */
public class SerializationUtils {

    private SerializationUtils() {
    }

    /**
     * Méthode permettant de récuperer un type sauvegarder dans un fichier.
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T load(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))){
            return (T)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Transtipage impossible");
    }

    /**
     * Méthode permettant de récupere un type sauvegarder dans un fichier dans une liste.
     * @param path
     * @return
     */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> List<T> loadList(String path) {
        List<T> retour = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))){
			retour.add((T)ois.readObject());
			return retour;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return retour;
	}

    /**
     * Méthode qui permet de sauvegarder une instance d'un certain type dans un fichier.
     * @param type
     */
    public static <T extends Serializable> void save(T type, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)))){
            oos.writeObject(type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode qui permet de sauvegarder une liste d'un certain type dans un fichier.
     * @param type
     */
    public static <T extends Serializable> void save(List<T> type, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)))){
            oos.writeObject(type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

