import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.BrickFinder;

public class Perception extends Mouvements{
	float distance; //en cm
	
	
	public float distance() {
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2); //port 2 ultrason
	    SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode(); 
		float[] sample = new float[distanceProvider.sampleSize()];
		distanceProvider.fetchSample(sample, 0); // Récupérer l'échantillon
        float distance = sample[0]; // Distance en mètres
        return distance;
	}
	
	public float rechercheDistPalet() {
		float[] tabdistance = new float[100]; // combien de donnees ?
		// faire en meme temps
		//tourner sur lui meme;
		tourner(360);
		// scanner son environnement et lenregistrer
		for (int i=0;i<tabdistance.length;i++) {
			tabdistance[i]=distance();
		}      
		
		//return la distance la plus petite
		float minDist=tabdistance[0];                                                                                                    
		for (int k=0; k<tabdistance.length;k++) {
		        if (tabdistance[k] < minDist)                                     
		            minDist = tabdistance[k];                                     
		}
		
		return minDist;
	}
	
    public static void main(String[] args) {
    	// Initialiser le capteur
        /*EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2); //port 2 ultrason
        SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode();
        
        // Tableau pour stocker les échantillons
        float[] sample = new float[distanceProvider.sampleSize()];

        while (true) {
            distanceProvider.fetchSample(sample, 0); // Récupérer l'échantillon
            float distance = sample[0]; // Distance en mètres
            
            // Afficher la distance
            System.out.println("Distance: " + distance + " m");

            // Si la distance est inférieure ou égale à 0,3 m, faire tourner le robot
            if (distance <= 0.3) {
                System.out.println("Obstacle");
                Motor.D.stop();
                Motor.B.stop();
               
                // Faire tourner à gauche
                Motor.D.setSpeed(200); 
                Motor.B.setSpeed(200); 
                Motor.D.forward(); 
                Motor.B.backward(); 
            
                try {
                    Thread.sleep(1000); // Tourner pendant 1 seconde
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            } else {
                // Continuer d'avancer si pas d'obstacle
                Motor.D.setSpeed(200);
                Motor.B.setSpeed(200);
                Motor.D.forward();
                Motor.B.forward();
            }

            // Pause de 100 ms avant la prochaine mesure
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    	
    }
}