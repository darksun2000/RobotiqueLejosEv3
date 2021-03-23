package Interfaces;

import classes.But;
import classes.Maison;

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
