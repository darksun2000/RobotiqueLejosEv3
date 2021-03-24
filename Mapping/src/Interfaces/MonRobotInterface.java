package Interfaces;

import classes.But;
import classes.Maison;
/**
 * Interface qui sera utilisé pour donner les methodes nécessaires pour que le robot
 * se déplace, détecte son environement comme l'utilisation des capteurs couleurs,
 * attrapper ou poser un object.
 * <br>
 * L'implementation de cet interface sera déterminé en fonction du nombre et les distance entres le roues,
 * la position du capteur, etc.
 * 
 * @author Khalid Naimi
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
	public void enchainement(But but,Maison maisons[]);
	public void decouvrir(Maison[] maisons);
	public But ButProche(But[] buts);
	
}
