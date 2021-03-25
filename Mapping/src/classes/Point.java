package classes;
/**
 * classe simple utilisé pour les coordonnées cartésienne à deux dimension, soient les valeurs x et y
 * @author Khalid Naimi
 * @author Salah El Mesbahi
 * @author Ayoub Lamrani
 * @author Oussama Bekbichi
 * @author Safouan El-Ryfy
 * @author Aymane Berrhoze
 * @author Mohammed Amine Jebli
 * @author Hamza Ait Abdelouahab
 * @author Maha El Garce
 *
 */
public class Point {
	
	private float x,y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
