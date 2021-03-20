package classes;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.Navigator;
import lejos.utility.Delay;

public class MonRobot {

	// objet qui controlle le mvt du robot
	private MoveController pilot;
	// carte du robot
	private LineMap carte;
	// le senseur de la distance
	private EV3UltrasonicSensor us;
	// le senseur des couleurs
	private EV3ColorSensor cs;
	// les moteurs du bras
	private EV3MediumRegulatedMotor bMoteur;
	private EV3MediumRegulatedMotor cMoteur;

	public MonRobot(MoveController pilot, LineMap carte, EV3UltrasonicSensor us, EV3ColorSensor cs,
			EV3MediumRegulatedMotor bMoteur, EV3MediumRegulatedMotor cMoteur) {
		this.pilot = pilot;
		this.carte = carte;
		this.us = us;
		this.cs = cs;
		this.bMoteur = bMoteur;
		this.cMoteur = cMoteur;
	}

	public MoveController getPilot() {
		return pilot;
	}

	public void setPilot(MoveController pilot) {
		this.pilot = pilot;
	}

	public LineMap getCarte() {
		return carte;
	}

	public void setCarte(LineMap carte) {
		this.carte = carte;
	}

	public EV3UltrasonicSensor getUs() {
		return us;
	}

	public void setUs(EV3UltrasonicSensor us) {
		this.us = us;
	}

	public EV3ColorSensor getCs() {
		return cs;
	}

	public void setCs(EV3ColorSensor cs) {
		this.cs = cs;
	}

	public EV3MediumRegulatedMotor getbMoteur() {
		return bMoteur;
	}

	public void setbMoteur(EV3MediumRegulatedMotor bMoteur) {
		this.bMoteur = bMoteur;
	}

	public EV3MediumRegulatedMotor getcMoteur() {
		return cMoteur;
	}

	public void setcMoteur(EV3MediumRegulatedMotor cMoteur) {
		this.cMoteur = cMoteur;
	}

	
	// methode pour aller vers un but
	public void allerVers(But but) {
		Point point = but.getPoint();
		// cree un objet Navigator a partir de notre pilote
		Navigator navigateur = new Navigator(pilot);
		// aller vers le point (x;0)
		navigateur.goTo(point.getX(), 0);
		while (pilot.isMoving());
		// aller vers le point (x;y) ou se trouve le but
		navigateur.goTo(point.getX(), point.getY());
		Delay.msDelay(2000);
		// on verifie en meme temps que la distance est sup a 3 cm
		float[] sample = new float[1];
		SampleProvider sp = us.getDistanceMode();
		while (pilot.isMoving()) {
			sp.fetchSample(sample, 0);
			if (sample[0] <= 0.03) {
				pilot.stop();
				navigateur.stop();
			}
		}
	}
	
	//methode pour detection de la couleur du but
	public void detecterCouleur(But but){
		String mes = null;
		int color = cs.getColorID();
		switch (color){
		case 0:
			mes="RED";
			break;

		case 1:
			mes="GREEN";
			break;
			
		case 2:
			mes="BLUE";
			break;
			
		case 3:
			mes="YELLOW";
			break;
			
		case 6:
			mes="WHITE";
			break;
			
		case 7:
			mes="BLACK";
			break;
			
		default:
			LCD.drawString("UC ID = " + color, 0, 0);
			Delay.msDelay(6000);
			LCD.clear();
		}//switch
		
		if(mes != null){
			but.setCouleur(color);
			LCD.drawString(mes , 0 ,0);
		}
		
		Delay.msDelay(1000);
		LCD.clear();

	}
	

	//attraper un but
	public void attraper() {
		bMoteur.setSpeed(2500);
		bMoteur.rotate(1000);

		while (bMoteur.isMoving()) {
		}

		cMoteur.setSpeed(-150);
		cMoteur.rotate(-200);
		//
		while (cMoteur.isMoving()) {
		}

		//
		bMoteur.rotate(-1000);

		while (bMoteur.isMoving()) {
		}

		cMoteur.rotate(200);

	}

	//poser un but
	public void poser() {
		
		cMoteur.rotate(-200);

		while (cMoteur.isMoving()) {

		}

		bMoteur.rotate(1000);

		while (bMoteur.isMoving()) {

		}

		cMoteur.rotate(200);

		while (cMoteur.isMoving()) {

		}

		bMoteur.rotate(-1000);

		while (bMoteur.isMoving()) {

		}
	}
	
	//remettre le but vers la maison appropriee
	void retourner(Maison maison){
		Point point = maison.getPoint();
		// cree un objet Navigator a partir de notre pilote
		Navigator navigateur = new Navigator(pilot);
		// aller vers le point (x;0)
		navigateur.goTo(point.getX(), 0);
		while (pilot.isMoving());
		// aller vers le point (x;y) ou se trouve le but
		navigateur.goTo(point.getX(), point.getY());
		Delay.msDelay(2000);
		while (pilot.isMoving());
	}
	
	//cette methode rassemble toutes les methodes
	//elle sert a aller chercher le but donne et le remettre a sa masion appropriee
	public void enchainement(But but,Maison maisons[]){
		allerVers(but);
		attraper();
		detecterCouleur(but);
		//cherhcer la masion appropier a la couleur du but
		Maison maison = null;
		for (int i = 0; i < maisons.length; i++) {
			if(maisons[i].getCouleur() == but.getCouleur()){
				maison=maisons[i];
			}
		}
		if(maison != null){
		retourner(maison);
		}
		poser();
		//aller à la position initiale
		Navigator n = new Navigator(pilot);
		n.goTo(0,0);
	}
}
