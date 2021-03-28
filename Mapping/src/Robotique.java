import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        List < Maison > maisons = new ArrayList < Maison > ();
        maisons.add(new Maison(new Point(98, 23)));
        maisons.add(new Maison(new Point(104, 64)));
        maisons.add(new Maison(new Point(12, 67)));
        //maisons.add(new Maison(new Point(100, 80)));

        // creer les buts
        List < But > buts = new ArrayList < But > ();
        buts.add(new But(new Point(50, 65)));
        //buts.add(new But(new Point(50, 50)));
        //buts.add(new But(new Point(40, 40)));
        //buts.add(new But(new Point(90, 40)));
        //buts.add(new But(new Point(60, 80)));
        //buts.add(new But(new Point(80, 50)));

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
    public void go(LineMap lm, List < Maison > maisons, List < But > buts, MonRobot robot) {
        robot.decouvrir(maisons);

        But but = robot.ButProche(buts);
        do {
            robot.enchainement(but, maisons);
            if (!buts.isEmpty())
                but = robot.ButProche(buts);
        } while (!buts.isEmpty());

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