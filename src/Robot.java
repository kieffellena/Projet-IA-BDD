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
        
        //robot avance
        Motor.B.forward(); 
		Motor.D.forward(); 

		float[] tabDistances = new float[30]; // Tableau pour enregistrer les distances avec le palet captées en avançant

		// avancer et vérifier distance du palet qui diminue
		for (int i=0; i < tabDistances.length || i<(int) distancePalet; i++) {
			float nouvelleDistancePalet = distance();
			tabDistances[i] = nouvelleDistancePalet;
            if(i>0 && tabDistances[i]<tabDistances[i-1]) {
                stopRobot();
                rechercheEtTourner(220);
            }
			Delay.msDelay(111); // vérifie distance du palet tous les 5cm
		}

        stopRobot();
        //gérer l'ouverture des pinces qd il s'approche du palet
	}
}
