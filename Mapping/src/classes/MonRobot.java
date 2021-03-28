package classes;

import Interfaces.MonRobotInterface;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.Navigator;
import lejos.utility.Delay;

/**
 * implementation de l'interface {@code MonRobotInterface}
 * qui prend en consid�ration le robot ayant deux roues, une main, deux capteurs couleurs;
 * la premi�re pour d�tecter la couleur de l'object dans la positionl du but,
 * et l'autre pour d�tecter la couleur de la surface de la maison.
 * @author Khalid Naimi
 * @see MonRobotInterface
 * @see MoveController
 * @see LineMap
 * @see EV3UltrasonicSensor
 * @see EV3ColorSensor
 * @see Ev3MediumRegulatedMotor
 * @see Navigator 
 */
public class MonRobot implements MonRobotInterface {

	// objet qui controlle le mvt du robot
	private MoveController pilot;
	// carte du robot
	private LineMap carte;
	// le senseur de la distance
	private EV3UltrasonicSensor us;
	// le senseur des couleurs
	private EV3ColorSensor cs;
	private EV3ColorSensor csMaison;
	// les moteurs du bras
	private EV3MediumRegulatedMotor bMoteur;
	private EV3MediumRegulatedMotor cMoteur;
	private Navigator navigateur;

	public MonRobot(MoveController pilot, LineMap carte, EV3UltrasonicSensor us, EV3ColorSensor cs,
			EV3MediumRegulatedMotor bMoteur, EV3MediumRegulatedMotor cMoteur, EV3ColorSensor csMaison) {
		this.pilot = pilot;
		this.carte = carte;
		this.us = us;
		this.cs = cs;
		this.csMaison = csMaison;
		this.bMoteur = bMoteur;
		this.cMoteur = cMoteur;
		this.navigateur = new Navigator(this.pilot);
		
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


	/**
	 * methode pour aller vers un but
	 * @return
	 * @param but
	 */
	@Override
	public void allerVers(But but) {
		Point point = but.getPoint();
		
		// cree un objet Navigator a partir de notre pilote
		float y = navigateur.getPoseProvider().getPose().getY();
		
		// aller vers le point (x;y) ou se trouve le but
		navigateur.goTo(point.getX(), point.getY() - 15 );
		Delay.msDelay(2000);
		while(pilot.isMoving());
		Delay.msDelay(2000);
		while(pilot.isMoving());
		// on verifie en meme temps que la distance est sup a 3 cm
		float[] sample = new float[1];
		SampleProvider sp = us.getDistanceMode();
		pilot.forward();
		pilot.setLinearSpeed(7);
		while (pilot.isMoving()) {
			LCD.drawString(sample[0]+"", 0, 3);
			y = navigateur.getPoseProvider().getPose().getY();
			sp.fetchSample(sample, 0);
			if (sample[0] <= 0.06 || y > point.getY() + 2) {
				pilot.stop();
				navigateur.stop();
			}
		}
		pilot.setLinearSpeed(15);
	}
	
	
	
	/**
	 * methode pour detection de la couleur du but
	 */
	@Override
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

		
			but.setCouleur(color);
			LCD.drawString(mes + "" , 0 ,0);
		

		Delay.msDelay(1000);
		LCD.clear();

	}


	/**
	 * methode pour attraper un but avec la main
	 */
	@Override
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

	/**
	 * methode pour poser un object provenant d'un but
	 */
	@Override
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

	/**
	 * methode pour remettre le but vers la maison appropri�e
	 * @return
	 * @param maison
	 * @param but
	 */
	@Override
	public void retourner(Maison maison,But but){
		Point point = maison.getPoint();

		// aller vers le point (x;y) ou se trouve le but
		navigateur.goTo(point.getX(), point.getY());
		Delay.msDelay(2000);
		while (pilot.isMoving());
		Delay.msDelay(2000);
		while (pilot.isMoving());
		
	}

	/**
	 * cette methode rassemble toutes le methodes
	 * elle sert � aller chercher le but donn� et le remettre � sa maison appropri�e
	 */
	@Override
	public void enchainement(But but,Maison maisons[]){
		allerVers(but);
		do{			
			attraper();
			detecterCouleur(but);
			if(but.getCouleur() != 13 && but.getCouleur() != -1)break;
			poser();
			pilot.backward();
			pilot.setLinearSpeed(7);
			Delay.msDelay(300);
			pilot.stop();
			attraper();
			detecterCouleur(but);
			if(but.getCouleur() != 13 && but.getCouleur() != -1){
				pilot.setLinearSpeed(15);
				break;
			}
			poser();
			pilot.forward();
			pilot.setLinearSpeed(7);
			Delay.msDelay(600);
			pilot.stop();
			attraper();
			detecterCouleur(but);
			if(but.getCouleur() != 13 && but.getCouleur() != -1){
				pilot.setLinearSpeed(15);
				break;
			}
			poser();
			pilot.backward();
			pilot.setLinearSpeed(7);
			Delay.msDelay(300);
			pilot.stop();
			attraper();
			detecterCouleur(but);
			if(but.getCouleur() != 13 && but.getCouleur() != -1){
				pilot.setLinearSpeed(15);
				break;
			}
		}while(but.getCouleur() == 13 || but.getCouleur() == -1);
		LCD.drawString("Couleur du but = " + but.getCouleur(), 0, 1);

		Maison maison = null;
		for (int i = 0; i < maisons.length; i++) {
			if(maisons[i].getCouleur() == but.getCouleur()){
				maison=maisons[i];
				break;
			}
		}
		if(maison != null && but.getCouleur() != -1){
			//cherhcer la masion appropier a la couleur du but
			retourner(maison,but);
			//navigateur.rotateTo(maison.getAngle());
		}
		poser();		
		pilot.backward();
		Delay.msDelay(750);
		pilot.stop();
	}
	/**
	 * m�thode pour d�tecter la couleur de la surface de chaque maison
	 */
	@Override
	public void decouvrir(Maison[] maisons){
		int couleur = -1;
		float x, y;
		for (int i = 0; i < maisons.length; i++) {
			
			navigateur.goTo(maisons[i].getPoint().getX(), maisons[i].getPoint().getY());
			Delay.msDelay(2000);
			while (pilot.isMoving());
			Delay.msDelay(2000);
			while (pilot.isMoving());
			navigateur.getPoseProvider().getPose().setLocation(maisons[i].getPoint().getX(), maisons[i].getPoint().getY());
			LCD.drawString("Maison num�ros : " + i, 0, 0);
			x = navigateur.getPoseProvider().getPose().getX();
			y = navigateur.getPoseProvider().getPose().getY();
			LCD.drawString("Position: (" + x + ", " + y + ")", 0, 1);
			LCD.drawString("Appuiez sur un bouton", 0, 2);
			LCD.clear();
			couleur = csMaison.getColorID();
			LCD.drawString("Maison num�ros : " + i, 0, 0);
			String mes = null;
			switch (couleur){
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
				LCD.drawString("UC ID = " + couleur, 0, 1);
			}//switch
			if(mes != null)
				LCD.drawString(mes, 0, 1);
			
			LCD.drawString("Appuiez sur un bouton", 0, 2);
			
			//modification des coordonn�es de la maison
			maisons[i].setCouleur(couleur);
			Point pointDecale = maisons[i].getPoint();
			pointDecale.setX((float) (pointDecale.getX() + 6.5));
			maisons[i].setPoint(pointDecale);
			
			maisons[i].setAngle(navigateur.getPoseProvider().getPose().getHeading());
		}			
	}

	/**
	 * m�thode utilis� pour la prise de d�cision en fonction du chemein le plus cours entre le robot
	 * et les buts donn�s en parametre
	 */
	@Override
	public But ButProche(But[] buts) {
		double x = navigateur.getPoseProvider().getPose().getX();
		double y = navigateur.getPoseProvider().getPose().getX();
		double xb = buts[0].getPoint().getX();
		double yb = buts[0].getPoint().getY();
		double min = Math.sqrt(Math.pow((x-xb),2)+Math.pow((y-yb),2));
		double distance = 0;
		int indiceBut = 0;
		
		for (int i = 1; i < buts.length; i++) {
			xb = buts[i].getPoint().getX();
			yb = buts[i].getPoint().getY();
			distance = Math.sqrt(Math.pow((x-xb),2)+Math.pow((y-yb),2));
			if (distance<min){
				min = distance;
				indiceBut = i;
			}
		}
		return buts[indiceBut];
	}
}
