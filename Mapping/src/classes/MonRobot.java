package classes;

import java.util.List;

import Interfaces.MonRobotInterface;
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
 * qui prend en considération le robot ayant deux roues, une main, deux capteurs couleurs;
 * la première pour détecter la couleur de l'object dans la positionl du but,
 * et l'autre pour détecter la couleur de la surface de la maison.
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
        float x = navigateur.getPoseProvider().getPose().getX();
        float y = navigateur.getPoseProvider().getPose().getY();
        Point pointRobot = new Point(x,y);
        double teta = getAngleEntreDeuxPoints(point, pointRobot);

        // aller vers le point (x;y) ou se trouve le but
        navigateur.goTo(point.getX() + 10*(float)Math.cos(teta + Math.PI), point.getY() - 10*(float)Math.sin(teta + Math.PI));
        
        Delay.msDelay(2000);
        while (pilot.isMoving());
        Delay.msDelay(2000);
        while (pilot.isMoving());
        // on verifie en meme temps que la distance est sup a 3 cm
        float[] sample = new float[1];
        SampleProvider sp = us.getDistanceMode();
        pilot.setLinearSpeed(7);
        pilot.forward();
        while (pilot.isMoving()) {
            LCD.drawString(sample[0] + "", 0, 3);
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
    public void detecterCouleur(But but) {
        int color = cs.getColorID();
        afficherCouleur(color);
        but.setCouleur(color);
    }

    /**
     * methode pour attraper un but avec la main
     */
    @Override
    public void attraper() {
        bMoteur.setSpeed(2500);
        bMoteur.rotate(1000);

        while (bMoteur.isMoving()) {}

        cMoteur.setSpeed(-150);
        cMoteur.rotate(-200);
        
        while (cMoteur.isMoving()) {}

        bMoteur.rotate(-1000);

        while (bMoteur.isMoving()) {}

        cMoteur.rotate(200);
    }

    /**
     * methode pour poser un object provenant d'un but
     */
    @Override
    public void poser() {

        cMoteur.rotate(-200);

        while (cMoteur.isMoving()) {}

        bMoteur.rotate(1000);

        while (bMoteur.isMoving()) {}

        cMoteur.rotate(200);

        while (cMoteur.isMoving()) {}

        bMoteur.rotate(-1000);

        while (bMoteur.isMoving()) {}
    }

    /**
     * methode pour remettre le but vers la maison appropriée
     * @return
     * @param maison
     * @param but
     */
    @Override
    public void retourner(Maison maison, But but) {
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
     * elle sert à aller chercher le but donné et le remettre à sa maison appropriée
     */
    @Override
    public void enchainement(But but,List<Maison> maisons) {

        allerVers(but);

        insisterDetectionCouleur(but);

        afficherCouleur(but.getCouleur());

      //cherhcer la masion appropier a la couleur du but
        Maison maison = chercherMaisonCorrespondante(but, maisons);

        if (maison != null && but.getCouleur() != -1) {
            
            retourner(maison, but);
//            navigateur.rotateTo(maison.getAngle());
            insisterPoserObjet(maison);
            poser();
            pilot.backward();
            Delay.msDelay(750);
            pilot.stop();
        }

       
        
    }
    /**
     * méthode pour détecter la couleur de la surface de chaque maison
     */
    @Override
    public void decouvrir(List<Maison> maisons) {
        int couleur = -1;
        float x, y;
        for (int i = 0; i < maisons.size(); i++) {

            navigateur.goTo(maisons.get(i).getPoint().getX(), maisons.get(i).getPoint().getY());
            Delay.msDelay(2000);
            while (pilot.isMoving());
            Delay.msDelay(2000);
            while (pilot.isMoving());
            navigateur.getPoseProvider().getPose().setLocation(maisons.get(i).getPoint().getX(), maisons.get(i).getPoint().getY());
            LCD.drawString("Maison numéros : " + i, 0, 0);
            x = navigateur.getPoseProvider().getPose().getX();
            y = navigateur.getPoseProvider().getPose().getY();
            LCD.drawString("Position: (" + x + ", " + y + ")", 0, 1);
            LCD.drawString("Appuiez sur un bouton", 0, 2);
            LCD.clear();
            couleur = csMaison.getColorID();
            if(couleur == -1 || couleur == 6)
            	insisterCouleurMaison();
            couleur = csMaison.getColorID();
            afficherCouleur(couleur);

            //modification des coordonnées de la maison
            maisons.get(i).setCouleur(couleur);
            Point pointDecale = maisons.get(i).getPoint();
            pointDecale.setX((float)(pointDecale.getX() + 6.5));
            maisons.get(i).setPoint(pointDecale);

            maisons.get(i).setAngle(navigateur.getPoseProvider().getPose().getHeading());
        }
    }

    @Override
    public void afficherCouleur(int idCouleur) {
        String mes = null;
        int color = idCouleur;
        switch (color) {
        case 0:
            mes = "RED";
            break;

        case 1:
            mes = "GREEN";
            break;

        case 2:
            mes = "BLUE";
            break;

        case 3:
            mes = "YELLOW";
            break;

        case 6:
            mes = "WHITE";
            break;

        case 7:
            mes = "BLACK";
            break;

        default:
        } //switch

        LCD.drawString(mes + "", 0, 0);

        Delay.msDelay(1000);
        LCD.clear();
    }

    /**
     * méthode utilisé pour la prise de décision en fonction du chemein le plus cours entre le robot
     * et les buts donnés en parametre
     */
    @Override
    public But ButProche(List<But> buts) {
        double x = navigateur.getPoseProvider().getPose().getX();
        double y = navigateur.getPoseProvider().getPose().getY();
        double xb = buts.get(0).getPoint().getX();
        double yb = buts.get(0).getPoint().getY();
        double min = Math.sqrt(Math.pow((x - xb), 2) + Math.pow((y - yb), 2));
        double distance = 0;
        int indiceBut = 0;

        for (int i = 1; i < buts.size(); i++) {
            xb = buts.get(i).getPoint().getX();
            yb = buts.get(i).getPoint().getY();
            distance = Math.sqrt(Math.pow((x - xb), 2) + Math.pow((y - yb), 2));
            if (distance < min) {
                min = distance;
                indiceBut = i;
            }
        }
        But butProche = buts.get(indiceBut);
        buts.remove(indiceBut);
        return butProche;
    }

    @Override
    public void insisterDetectionCouleur(But but) {

        pilot.setLinearSpeed(5);
        float[] sample = new float[1];
        SampleProvider sp = us.getDistanceMode();
        sp.fetchSample(sample, 0);
        pilot.forward();
        while(sample[0] < .06)
        	sp.fetchSample(sample, 0);
        pilot.stop();
        do {
        	
            attraper();
            detecterCouleur(but);
            if (but.getCouleur() == 13 || but.getCouleur() == -1)
                poser();
            else
                break;

            sp.fetchSample(sample, 0);

            if (sample[0] < 0.06 || sample[0] > 1000 || sample[0] < -1000) {
                pilot.backward();
                while (pilot.isMoving()) {
                    LCD.drawString(sample[0] + "", 0, 3);
                    sp.fetchSample(sample, 0);
                    if (sample[0] >= 0.06) {
                        pilot.stop();
                        navigateur.stop();
                    }
                }
            } else if (sample[0] > 0.06 || sample[0] == Float.POSITIVE_INFINITY) {
                pilot.forward();
                while (pilot.isMoving()) {
                    LCD.drawString(sample[0] + "", 0, 3);
                    sp.fetchSample(sample, 0);
                    if (sample[0] <= 0.06) {
                        pilot.stop();
                        navigateur.stop();
                    }
                }
            }
        } while (but.getCouleur() == 13 || but.getCouleur() == -1);
        pilot.setLinearSpeed(15);
    }

    @Override
    public Maison chercherMaisonCorrespondante(But but, List<Maison> maisons) {
        Maison maison = null;
        for (int i = 0; i < maisons.size(); i++) {
            if (maisons.get(i).getCouleur() == but.getCouleur()) {
                maison = maisons.get(i);
                break;
            }
        }
        return maison;
    }
    /**
     * angle en degree
     * @param pBut
     * @param pRobot
     * @return
     */
    public double getAngleEntreDeuxPoints(Point pBut, Point pRobot){
    	double a = Math.abs(pBut.getY() - pRobot.getY());
    	double aprime = pRobot.getY() - pBut.getY();
    	double b = Math.abs(pBut.getX() - pRobot.getX());
    	double bprime = pRobot.getX() - pBut.getX();
    	double c = Math.sqrt(a*a + b*b);
    	double cos_teta = b/c;
    	double teta = Math.acos(cos_teta);
    	if(bprime > 0 && aprime > 0){
    		teta = Math.abs(teta);
    	}else if (bprime < 0 && aprime > 0){
    		teta = Math.abs(teta);
    	}else if (bprime > 0 && aprime < 0){
    		teta = -Math.abs(teta);
    	}else{
    		teta = -Math.abs(teta);
    	}
    	
    	return teta;
    }
    
    public void insisterPoserObjet(Maison maison){
    	int couleur = maison.getCouleur();
    	for(int i = 0; i<360; i+=10){
    		if(csMaison.getColorID() == couleur)break;
    		else navigateur.rotateTo(i);
    		}
    		
    	}
    	
    	public void insisterCouleurMaison(){
        	for(int i = 0; i<360; i+=10){
        		if(csMaison.getColorID() != -1 && csMaison.getColorID() != 6)break;
        		else navigateur.rotateTo(i);
        	}
        	navigateur.rotateTo(18);
    }
    
}