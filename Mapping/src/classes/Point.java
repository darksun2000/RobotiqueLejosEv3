package classes;
/**
 * classe simple utilisé pour les coordonnées cartésienne à deux dimension, soient les valeurs x et y
 * @author Khalid Naimi
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
