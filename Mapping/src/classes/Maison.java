package classes;

/**
 * C'est la classe d�terminante de l'objectif du robot ou aller pour pose
 * l'objet provenant de la position indiqu�e dans l'object de type {@code But}
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
 * @see Point
 * @see But
 */
public class Maison {

	private Point point;
	private int couleur;
	private float angle;
	
	/**
	 * Constructeur de la classe Maison, qui d�finira uniquement l'object point
	 * @param point
	 */
	public Maison(Point point) {
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
	 * accesseur de l'attribut couleur
	 * @return
	 */
	public int getCouleur() {
		return couleur;
	}
	/**
	 * mutatteur de l'attribut couleur, qui sera n�cessaire comme moyen de d�finition sans aboutir �
	 * donner une d�finition depuis le constructeur de la classe Maison.
	 * @param couleur
	 */
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	/**
	 * accesseur de l'attribut angle
	 * @return
	 */
	public float getAngle(){
		return angle;
	}
	/**
	 * mutatteur de l'attribut angle, utilis� pour pr�ciser la bonne direction ou poser l'object pris
	 * @param angle
	 */
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	
}
