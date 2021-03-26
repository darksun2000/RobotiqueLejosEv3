
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import classes.But;
import classes.Maison;
import classes.MonRobot;
import classes.Point;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.mapping.SVGMapLoader;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MoveController;

@SuppressWarnings("deprecation")
public class Robotique {
	
	MoveController pilot;
	static SVGMapLoader mapLoader;
	PoseProvider poseProvider;
	static EV3LargeRegulatedMotor leftMotor;
	static EV3LargeRegulatedMotor rightMotor;
	float[] sample = new float[1];

	public static void main(String[] args) throws FileNotFoundException {
		
		Robotique traveler = new Robotique();

		// creer les maisons
		Maison[] maisons = new Maison[3];
		maisons[0] = new Maison(new Point(105, 7));
		maisons[1] = new Maison(new Point(105, 63));
		maisons[2] = new Maison(new Point(10, 62));
//		maisons[3] = new Maison(new Point(100, 80));

		// creer les buts
		But[] buts = new But[3];
		buts[0] = new But(new Point(50, 50));
		buts[1] = new But(new Point(75, 55));
		buts[2] = new But(new Point(10, 35));
//		buts[3] = new But(new Point(90, 40));
//		buts[4] = new But(new Point(60, 80));
//		buts[5] = new But(new Point(80, 50));

		// charger la carte
		LineMap lm = traveler.getMaCarte("draw.svg");

		// intialise les senseurs et moteurs
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S2);
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S1);
		EV3ColorSensor csMaison = new EV3ColorSensor(SensorPort.S3);

		leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3MediumRegulatedMotor bMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		EV3MediumRegulatedMotor cMotor = new EV3MediumRegulatedMotor(MotorPort.C);
		traveler.pilot = new DifferentialPilot(5.6f, 10.9f, leftMotor, rightMotor);
		traveler.pilot.setLinearSpeed(15);

		// creer le robot
		MonRobot robot = new MonRobot(traveler.pilot, lm, us, cs, bMotor, cMotor, csMaison);
		traveler.pilot.setLinearAcceleration(30);
		//declenchement de la mission du robot
		traveler.go(lm, maisons, buts, robot);
	}

	
	// methode declenche la mission du robot
	public void go(LineMap lm, Maison maisons[], But buts[], MonRobot robot) {
		robot.decouvrir(maisons);
		
		for (int i = 0; i < buts.length; i++) {
			
			robot.enchainement(buts[i], maisons);
		}
		
	}

	
	
	// methode pour charger le carte
	public LineMap getMaCarte(String nom) throws FileNotFoundException {
		File f = new File(nom);
		InputStream inputStream = new DataInputStream(new FileInputStream(f));
		mapLoader = new SVGMapLoader(inputStream);
		LineMap lm = null;
		try {
			lm = mapLoader.readLineMap();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return lm;
	}
}
