import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class ObjetDet {
	public static void main(String[] args) {
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
        SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode();

        float[] sample = new float[distanceProvider.sampleSize()];

        // Distance cible pour atteindre l'objet
        float targetDistance = 0.1f; // 10 cm pour atteindre l'objet

        while (true) {
            distanceProvider.fetchSample(sample, 0); 
            float distance = sample[0]; 

            System.out.println("Distance: " + distance + " m");

            // Ajustement de la position
            if (distance > targetDistance + 0.05) { 
                // trop loin donc Avancer
                Motor.A.setSpeed(200);
                Motor.B.setSpeed(200);
                Motor.A.forward();
                Motor.B.forward();
            } 
            else if (distance < targetDistance - 0.05) { 
                // trop proche donc Reculer
                Motor.A.setSpeed(200);
                Motor.B.setSpeed(200);
                Motor.A.backward();
                Motor.B.backward();
            } 
            else {
                // arreter si proche
                Motor.A.stop();
                Motor.B.stop();
                
                // ferme pince
                System.out.println("Objet atteint, fermeture des pinces !");
                Sound.beep();
                Motor.C.setSpeed(200);
                Motor.C.forward();
                try {
                    Thread.sleep(500); // Fermer les pinces pendant 0.5 seconde
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Motor.C.stop(); // Arrêter le moteur des pinces

            
                break; // Sortir de boucle après avoir atteint objet
            }

        }
    }

}
