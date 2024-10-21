import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class Robot extends Perception {

    private Perception perception;
    private Mouvements mouvements;

    public Robot() {
        perception = new Perception();
        mouvements = new Mouvements();
    }

    public void avancerVers(float distancePalet) {
        // avance en s'assurant d'aller dans la direction du palet
        // ouvre les pinces lorsque le palet est à moins de 0,9m 
        // distancePalet correspond à la distance minimale trouvée dans la méthode rechercherEtTourner()
        float nouvelleDistancePalet;
        avancer(distancePalet);
		for(float i=0; i<distancePalet; i+=0.05) {
            nouvelleDistancePalet=distance();
            if (nouvelleDistancePalet>distancePalet) {
                stopRobot();
                recherche(220);
            }
        }

	}
}
