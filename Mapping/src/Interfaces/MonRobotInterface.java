package Interfaces;

import java.util.List;

import classes.But;
import classes.Maison;
/**
 * Interface qui sera utilis� pour donner les methodes n�cessaires pour que le robot
 * se d�place, d�tecte son environement comme l'utilisation des capteurs couleurs,
 * attrapper ou poser un object.
 * <br>
 * L'implementation de cet interface sera d�termin� en fonction du nombre et les distance entres le roues,
 * la position du capteur, etc.
 * 
 * @author Khalid Naimi
 * @author Salah El Mesbahi
 * @author Ayoub Lamrani
 * @author Oussama Bekbichi
 * @author Safouan El-Ryfy
 * @author Aymane Berrhoze
 * @author Mohammed Amine Jebli
 * @author Hamza Ait Abdelouahab
 * @author Maha El Garce
 * @see But
 * @see Maison
 *
 */
public interface MonRobotInterface {

	public void allerVers(But but);
	public void detecterCouleur(But but);
	public void attraper();
	public void poser();
	public void retourner(Maison maison,But but);
	public void enchainement(But but,List<Maison> maisons);
	public void decouvrir(List<Maison> maisons);
	public void afficherCouleur(int idCouleur);
	public But ButProche(List<But> buts);
	public void insisterDetectionCouleur(But but);
	public Maison chercherMaisonCorrespondante(But but,List<Maison> maisons);
	
}
