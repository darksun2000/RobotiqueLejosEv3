package classes;


/**
 * Classe determinant la position d'un objet
 * à le porter et l'emener vers la {@code maison}
 * en fonction de sa couleur. 
 * <br>
 * Il y a deux attributs:
 * <br>
 * l'une de type {@code Point} et la deuxième nommé Couleur de type {@code int}
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
 * @see Maison
 * @see Point
 */
public class But {
	
	private Point point;
	private int Couleur = -1;
	
	/**
	 * Constructeur pour l'instantiation de la classe But
	 * @param point
	 */
	public But(Point point) {
		this.point = point;
	}
	/**
	 * accesseur de l'attribut point
	 * @return
	 */
	public Point getPoint() {
		return point;
	}
	/**
	 * mutatteur de l'attribut point
	 * @param point
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	/**
	 * acesseur de l'attribut couleur
	 * @return
	 */
	public int getCouleur() {
		return Couleur;
	}
	/**
	 * mutatteur de l'attribut couleur, qui sera nécessaire, puisque dans le constructeur de
	 * la classe But, on ne définit pas l'attribut couleur
	 * @param Couleur
	 */
	public void setCouleur(int Couleur) {
		this.Couleur = Couleur;
	}
	
	
}
